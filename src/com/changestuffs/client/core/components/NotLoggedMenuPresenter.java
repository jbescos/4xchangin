package com.changestuffs.client.core.components;

import com.changestuffs.client.place.NameTokens;
import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class NotLoggedMenuPresenter
		extends
		Presenter<NotLoggedMenuPresenter.MyView, NotLoggedMenuPresenter.MyProxy> implements NotLoggedMenuUiHandler{

	private final PlaceManager placeManager;
	
	public interface MyView extends View, HasUiHandlers<NotLoggedMenuUiHandler> {
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<NotLoggedMenuPresenter> {
	}

	@Inject
	public NotLoggedMenuPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetHeadContent);
		this.placeManager=placeManager;
		getView().setUiHandlers(this);
	}

	@Override
	public void handleLogin() {
		PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.getLogin()).build();
		GWT.log("Revealing "+request.getNameToken());
		placeManager.revealPlace(request);
	}
	
}
