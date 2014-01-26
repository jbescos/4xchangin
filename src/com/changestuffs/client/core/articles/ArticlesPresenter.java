package com.changestuffs.client.core.articles;

import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.event.AddArticleEvent;
import com.changestuffs.client.event.AddArticleEvent.GlobalDataHandler;
import com.changestuffs.client.event.PresenterSlotEvent;
import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.shared.actions.ArticleRemoveAction;
import com.changestuffs.shared.actions.ArticleRemoveResult;
import com.changestuffs.shared.actions.ArticlesGetAction;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
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

public class ArticlesPresenter extends
		Presenter<ArticlesPresenter.MyView, ArticlesPresenter.MyProxy>
		implements ArticlesUiHandler, UpdateSuccessArticleUiHandler, GlobalDataHandler{

	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;
	private final Provider<UpdateArticlePresenter> updateArticleProvider;
	private final Provider<AddArticlesPresenter> addArticleProvider;

	public interface MyView extends View, HasUiHandlers<ArticlesUiHandler> {
		void addArticle(IArticlesDto articlesDto, String index);
		void removeArticle(String index);
	}

	@ProxyCodeSplit
	@UseGatekeeper(NeedsLoginKeeper.class)
	@NameToken(NameTokens.articles)
	public interface MyProxy extends ProxyPlace<ArticlesPresenter> {
	}

	@Inject
	public ArticlesPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, DispatchAsync dispatcher,
			final PlaceManager placeManager, final UpdateArticlePresenter updateArticlePresenter, Provider<AddArticlesPresenter> addArticleProvider, Provider<UpdateArticlePresenter> updateArticleProvider) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.updateArticleProvider=updateArticleProvider;
		this.addArticleProvider=addArticleProvider;
		getView().setUiHandlers(this);
		updateArticlePresenter.setUiHandlers(this);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AddArticleEvent.getType(), this);
		dispatcher.execute(new ArticlesGetAction(false, null),
				new AsyncCallback<LookForResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Unexpected error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						for (IArticlesDto article : result.getArticles().values()) {
							getView().addArticle(article, article.getKeyHash());
						}
					}

				});
	}

	@Override
	public void handleRemoveArticle(String keyHash) {
		dispatcher.execute(new ArticleRemoveAction(keyHash),
				new AsyncCallback<ArticleRemoveResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error removing article", caught);
					}

					@Override
					public void onSuccess(ArticleRemoveResult result) {
						getView().removeArticle(result.getKeyHashOut());
					}
				});
	}

	@Override
	public void handleClickName(Tags tag, String idHash) {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens.getLookfor()).
				with(LookforPresenter.Parameters.id.name(), idHash);
		
		placeManager.revealPlace(request.build());
	}

	@Override
	public void handleEditArticle(IArticlesDto article) {
		UpdateArticlePresenter presenter = updateArticleProvider.get();
		presenter.setFields(article);
		presenter.setUiHandlers(this);
		GWT.log("Trying to reveal "+presenter);
		PresenterSlotEvent.fire(this, presenter);
	}

	@Override
	public void updateSuccessProduct(IArticlesDto articlesDto) {
		getView().removeArticle(articlesDto.getKeyHash());
		getView().addArticle(articlesDto, articlesDto.getKeyHash());
	}

	@Override
	public void handleRevealAddArticle() {
		AddArticlesPresenter presenter = addArticleProvider.get();
		GWT.log("Trying to reveal "+presenter);
		PresenterSlotEvent.fire(this, presenter);
	}

	@Override
	public void onGlobalEvent(AddArticleEvent event) {
		IArticlesDto article = event.getArticle();
		getView().addArticle(article, article.getKeyHash());
	}
	
}
