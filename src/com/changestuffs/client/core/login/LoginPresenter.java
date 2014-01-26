package com.changestuffs.client.core.login;

import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.shared.actions.LoginsAction;
import com.changestuffs.shared.actions.LoginsResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class LoginPresenter extends
		Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> implements LoginUiHandler{
	
	private final DispatchAsync dispatcher;

	public interface MyView extends View, HasUiHandlers<LoginUiHandler> {
	}

	@ProxyCodeSplit
	@NoGatekeeper
	@NameToken(NameTokens.login)
	public interface MyProxy extends ProxyPlace<LoginPresenter> {
	}
	
	@Inject
	public LoginPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher, PlaceManager placeManager) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
	}

	@Override
	public void createLogin(String providerUrl) {
		String redirectUrl = Window.Location.createUrlBuilder().setHash(NameTokens.lookfor).buildString();
		GWT.log("It will redirect to: "+redirectUrl);
		dispatcher.execute(new LoginsAction(providerUrl, redirectUrl), new AsyncCallback<LoginsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error getting login URL", caught);
			}
			@Override
			public void onSuccess(LoginsResult result) {
				Window.Location.replace(result.getLoginUrl());
			}
		});
	}
	
}
