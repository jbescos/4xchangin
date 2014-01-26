package com.changestuffs.client.core.components;

import com.changestuffs.shared.actions.LogoutAction;
import com.changestuffs.shared.actions.LogoutResult;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class LoggedMenuPresenter extends
		Presenter<LoggedMenuPresenter.MyView, LoggedMenuPresenter.MyProxy> implements LoggedMenuUiHandler{

	private final DispatchAsync dispatcher;
	private final ChatPresenter chatPresenter;
	
	public interface MyView extends View, HasUiHandlers<LoggedMenuUiHandler> {

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<LoggedMenuPresenter> {
	}

	@Inject
	public LoggedMenuPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher, ChatPresenter chatPresenter) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetHeadContent);
		this.dispatcher=dispatcher;
		this.chatPresenter=chatPresenter;
		getView().setUiHandlers(this);
	}

	@Override
	public void handleLogout() {
		GWT.log("Revealing NotLogged");
		dispatcher.execute(new LogoutAction(Window.Location.getHref()), new AsyncCallback<LogoutResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error doing logout", caught);
			}

			@Override
			public void onSuccess(LogoutResult result) {
				Window.open(result.getUrl(), "_self", "");
				chatPresenter.closeSocket();
			}
		});
	}
	
	
}
