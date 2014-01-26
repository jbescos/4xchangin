package com.changestuffs.client.core.lookfor;

import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.client.widget.products.ProductsPresenter;
import com.changestuffs.client.widget.social.SocialPresenter;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.PlaceRequest.Builder;

public class LookforPresenter extends
		Presenter<LookforPresenter.MyView, LookforPresenter.MyProxy> {

	private final CurrentUser user;
	private final PlaceManager placeManager;
	private final SocialPresenter socialPresenter;
	private final OffersPresenter offersPresenter;
	private final ProductsPresenter productsPresenter;
	private final DispatchAsync dispatcher;
	private final GoogleAnalytics analytics;

	public interface MyView extends View {
		void viewArticle(IArticlesDto article);
		void noArticle();
		
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SocialContent = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_OffersContent = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_ProductsContent = new Type<RevealContentHandler<?>>();

	@ProxyCodeSplit
	@NoGatekeeper
	@NameToken(NameTokens.lookfor)
	public interface MyProxy extends ProxyPlace<LookforPresenter> {
	}

	@Inject
	public LookforPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher,
			final ProductsPresenter productsPresenter,
			final SocialPresenter socialPresenter,
			final OffersPresenter offersPresenter, GoogleAnalytics analytics, CurrentUser user, PlaceManager placeManager) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		this.dispatcher = dispatcher;
		this.socialPresenter = socialPresenter;
		this.offersPresenter = offersPresenter;
		this.productsPresenter = productsPresenter;
		this.analytics = analytics;
		this.user = user;
		this.placeManager = placeManager;
	}

	@Override
	protected void onBind() {
		super.onBind();
		setInSlot(TYPE_SocialContent, socialPresenter);
		setInSlot(TYPE_ProductsContent, productsPresenter);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		trackWithGoogleAnalytics(request);
		String id = request.getParameter(Parameters.id.name(), null);
		String tag = request.getParameter(Parameters.tag.name(), null);
		if(id != null){
			retireveFromServer(id);
		}else if(tag != null){
			productsPresenter.retireveFromServer(Tags.valueOf(tag));
		}else if(tag == null && id == null){
			Tags[] tags = Tags.values();
			Tags randomTag = tags[Random.nextInt(tags.length)];
			Builder newRequest = new PlaceRequest.Builder().nameToken(NameTokens
					.getLookfor()).with(Parameters.tag.name(), randomTag.name());
			placeManager.revealPlace(newRequest.build());
		}
	}
	
	private void trackWithGoogleAnalytics(PlaceRequest request){
		// FIXME $wnd._gaq is undefined in hosted mode
		if(!Window.Location.getHost().contains("127.0.0.1")){
			String path = "/#"+NameTokens.lookfor;
			for(String paramName : request.getParameterNames()){
				path=path+";"+paramName+"="+request.getParameter(paramName, null);
			}
			GWT.log("Tracking "+path);
			if(user.getEmail() != null)
				analytics.trackPageview(user.getEmail(), path);
			else
				analytics.trackPageview(path);
		}
	}

	private void watchArticle(IArticlesDto dto){
		GWT.log("Watching article: "+dto.getKeyHash());
		getView().viewArticle(dto);
		String url = Window.Location.getHref();
		socialPresenter.updateUrl(url);
		offersPresenter.setOffers(dto);
		setInSlot(TYPE_OffersContent, offersPresenter);
	}

	private void retireveFromServer(final String idHash) {
		dispatcher.execute(new LookForAction(null, idHash),
				new AsyncCallback<LookForResult>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Some error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						IArticlesDto article = result.getArticles().get(idHash);
						
						// FIXME think in something to keep facebook url
						if(article != null){
							// Then create social panel
							watchArticle(article);
						}else{
							getView().noArticle();
						}
					}
				});
	}

	public static enum Parameters {
		tag, id;
	}

}
