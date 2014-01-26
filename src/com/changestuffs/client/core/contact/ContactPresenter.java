package com.changestuffs.client.core.contact;

import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class ContactPresenter extends Presenter<ContactPresenter.MyView, ContactPresenter.MyProxy> {

	public interface MyView extends View {

	}

	@ProxyCodeSplit
	@NoGatekeeper
	@NameToken(NameTokens.contact)
	public interface MyProxy extends ProxyPlace<ContactPresenter> {
	}

	@Inject
	public ContactPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
	}

}
