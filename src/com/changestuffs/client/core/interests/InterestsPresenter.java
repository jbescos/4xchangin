package com.changestuffs.client.core.interests;

import java.util.HashMap;
import java.util.Map;

import com.changestuffs.client.core.components.ChatPresenter;
import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.core.lookfor.LookforPresenter.Parameters;
import com.changestuffs.client.core.messages.MessagesPresenter;
import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.shared.actions.AddFriend;
import com.changestuffs.shared.actions.AddFriendResult;
import com.changestuffs.shared.actions.ArticlesGetAction;
import com.changestuffs.shared.actions.GetOffersAction;
import com.changestuffs.shared.actions.GetOffersResult;
import com.changestuffs.shared.actions.GetOffersResult.OffersPerProduct;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.actions.OfferCreateAction;
import com.changestuffs.shared.actions.OfferRemove;
import com.changestuffs.shared.actions.OfferRemoveResult;
import com.changestuffs.shared.actions.UpdateOfferAction;
import com.changestuffs.shared.actions.UpdateOfferResult;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.PlaceRequest.Builder;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class InterestsPresenter extends
		Presenter<InterestsPresenter.MyView, InterestsPresenter.MyProxy>
		implements InterestsUiHandler {

	private LookForResult result;
	private final ChatPresenter chatPresenter;
	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;
	private final Map<String, IArticlesDto> myArticles = new HashMap<String, IArticlesDto>();
	private final Map<String, OffersPerProduct> offersPerProducts = new HashMap<String, OffersPerProduct>();

	public interface MyView extends View, HasUiHandlers<InterestsUiHandler> {
		void setReceivedOffers(Map<String, IArticlesDto> articles);
		void setSentOffers(Map<String, OffersPerProduct> offersPerProducts);
		void watchArticles(String offerId, LookForResult result);
		void updateSentOffer(String offerId, OffersPerProduct offersPerProduct);
		void removeSentOffer(String offerId);
		void userIsYourFriend(String email, boolean alreadyAdded);
	}

	@ProxyCodeSplit
	@UseGatekeeper(NeedsLoginKeeper.class)
	@NameToken(NameTokens.interests)
	public interface MyProxy extends ProxyPlace<InterestsPresenter> {
	}

	@Inject
	public InterestsPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher,
			final PlaceManager placeManager, ChatPresenter chatPresenter) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.chatPresenter = chatPresenter;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setUiHandlers(this);
		dispatcher.execute(new GetOffersAction(),
				new AsyncCallback<GetOffersResult>() {
					@Override
					public void onFailure(Throwable arg0) {
						GWT.log("Unexpected error", arg0);
					}

					@Override
					public void onSuccess(GetOffersResult arg0) {
						offersPerProducts.putAll(arg0.getOffers());
						getView().setSentOffers(offersPerProducts);
					}
				});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		myArticles.clear();
		dispatcher.execute(new ArticlesGetAction(true, null),
				new AsyncCallback<LookForResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Unexpected error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						myArticles.putAll(result.getArticles());
						getView().setReceivedOffers(result.getArticles());
					}

				});
		dispatcher.execute(new ArticlesGetAction(false, null),
				new AsyncCallback<LookForResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Unexpected error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						InterestsPresenter.this.result = result;
					}

				});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		String id = request.getParameter(Parameters.id.name(), null);
		if (id != null) {
			dispatcher.execute(new OfferCreateAction(id),
					new AsyncCallback<GetOffersResult>() {
						@Override
						public void onFailure(Throwable arg0) {
							GWT.log("Error creating offer", arg0);
						}

						@Override
						public void onSuccess(GetOffersResult arg0) {
							offersPerProducts.putAll(arg0.getOffers());
							getView().setSentOffers(arg0.getOffers());
						}
					});
		}
	}

	@Override
	public void handleProduct(String key) {
		Builder request = new PlaceRequest.Builder().nameToken(
				NameTokens.getLookfor()).with(
				LookforPresenter.Parameters.id.name(), key);

		placeManager.revealPlace(request.build());
	}

	@Override
	public void handleContact(final String email) {
		dispatcher.execute(new AddFriend(email), new AsyncCallback<AddFriendResult>() {
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Unexpected error", arg0);
			}
			@Override
			public void onSuccess(AddFriendResult arg0) {
				getView().userIsYourFriend(email, arg0.isAlreadyAdded());
				chatPresenter.appendFriend(email);
			}
		});

	}

	@Override
	public void handleAddOffer(String offerId,
			Map<String, String> productIdNames) {
		dispatcher.execute(new UpdateOfferAction(offerId, productIdNames),
				new AsyncCallback<UpdateOfferResult>() {
					@Override
					public void onFailure(Throwable paramThrowable) {
						GWT.log("Unexpected error", paramThrowable);
					}

					@Override
					public void onSuccess(UpdateOfferResult paramT) {
						getView().updateSentOffer(
								paramT.getOffersPerProduct().getOfferKey(),
								paramT.getOffersPerProduct());
					}
				});
	}

	@Override
	public void handleRemoveOffer(final String offerId) {
		dispatcher.execute(new OfferRemove(offerId), new AsyncCallback<OfferRemoveResult>() {
			@Override
			public void onFailure(Throwable paramThrowable) {
				GWT.log("Unexpected error", paramThrowable);
			}
			@Override
			public void onSuccess(OfferRemoveResult paramT) {
				getView().removeSentOffer(offerId);
			}
		});
	}

	@Override
	public void handleWatchArticles(final String offerId) {
		getView().watchArticles(offerId, result);
	}

	@Override
	public void handleFriend(String email) {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens.getSocial()).
				with(MessagesPresenter.Parameters.user.name(), email);
		
		placeManager.revealPlace(request.build());
	}

}
