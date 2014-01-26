package com.changestuffs.client.core.components;

import com.changestuffs.client.event.PresenterSlotEvent;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.shared.actions.IsLoguedAction;
import com.changestuffs.shared.actions.IsLoguedResult;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> implements PresenterSlotEvent.GlobalDataHandler {

	public interface MyView extends View {
	}

	private final LoggedMenuPresenter loggedMenu;
	private final NotLoggedMenuPresenter notLoggedMenu;
	private final DispatchAsync dispatcher;
	private final CurrentUser user;
	private final ChatPresenter chat;

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPresenter> {
	}

	/**
	 * Child presenters can fire a RevealContentEvent with TYPE_SetMainContent
	 * to set themselves as children of this presenter.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetHeadContent = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetChatContent = new Type<RevealContentHandler<?>>();

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher,
			final LoggedMenuPresenter loggedMenu,
			final NotLoggedMenuPresenter notLoggedMenu, CurrentUser user, ChatPresenter chat) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.loggedMenu = loggedMenu;
		this.notLoggedMenu = notLoggedMenu;
		this.user=user;
		this.chat=chat;
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(PresenterSlotEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		dispatcher.execute(new IsLoguedAction(), new AsyncCallback<IsLoguedResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error doing IsLogin", caught);
			}

			@Override
			public void onSuccess(IsLoguedResult result) {
				GWT.log("Result: " + result);
				user.setEmail(result.getEmail());
				if (result.getEmail() != null) {
					setInSlot(TYPE_SetHeadContent, loggedMenu);
					setInSlot(TYPE_SetChatContent, chat);
				} else {
					setInSlot(TYPE_SetHeadContent, notLoggedMenu);
				}
			}
		});
	}

	@Override
	public void onGlobalEvent(PresenterSlotEvent event) {
		GWT.log("Setting in slot "+event.getPresenter());
		setInSlot(TYPE_SetMainContent, event.getPresenter());
	}

}
