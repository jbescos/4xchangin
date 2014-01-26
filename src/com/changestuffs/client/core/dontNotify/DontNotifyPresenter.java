package com.changestuffs.client.core.dontNotify;

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

public class DontNotifyPresenter extends
		Presenter<DontNotifyPresenter.MyView, DontNotifyPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NoGatekeeper
	@NameToken(NameTokens.dontNotify)
	public interface MyProxy extends ProxyPlace<DontNotifyPresenter> {
	}

	@Inject
	public DontNotifyPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
	}
	
}
