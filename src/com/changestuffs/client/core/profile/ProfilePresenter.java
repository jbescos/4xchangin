package com.changestuffs.client.core.profile;

import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.shared.actions.GetUserInfo;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.changestuffs.shared.actions.UpdateUserInfo;
import com.changestuffs.shared.actions.UpdateUserInfoResult;
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
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class ProfilePresenter extends
		Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy> implements ProfileUiHandler {

	private final DispatchAsync dispatcher;
	
	public interface MyView extends View, HasUiHandlers<ProfileUiHandler>{
		void setFields(GetUserInfoResult result);
		void successUpdated(boolean done);
	}

	@ProxyCodeSplit
	@UseGatekeeper(NeedsLoginKeeper.class)
	@NameToken(NameTokens.profile)
	public interface MyProxy extends ProxyPlace<ProfilePresenter> {
	}

	@Inject
	public ProfilePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		getView().setUiHandlers(this);
		this.dispatcher=dispatcher;
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		dispatcher.execute(new GetUserInfo(null), new AsyncCallback<GetUserInfoResult>() {
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Unexpected error", arg0);
			}
			@Override
			public void onSuccess(GetUserInfoResult arg0) {
				getView().setFields(arg0);
			}
		});
	}

	@Override
	public void handleSubmit(String cell, String city, String country,
			boolean receiveEmails) {
		getView().successUpdated(false);
		dispatcher.execute(new UpdateUserInfo(cell, city, country, receiveEmails), new AsyncCallback<UpdateUserInfoResult>() {
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Unexpected error", arg0);
			}
			@Override
			public void onSuccess(UpdateUserInfoResult arg0) {
				getView().successUpdated(true);
			}
		});
	}
}
