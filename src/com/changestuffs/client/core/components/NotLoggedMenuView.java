package com.changestuffs.client.core.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class NotLoggedMenuView extends ViewImpl implements
		NotLoggedMenuPresenter.MyView {

	private final Widget widget;
	private NotLoggedMenuUiHandler uiHandlers;
	@UiField
	Label login;

	public interface Binder extends UiBinder<Widget, NotLoggedMenuView> {
	}

	@Inject
	public NotLoggedMenuView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(NotLoggedMenuUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}
	
	@UiHandler(value = { "login" })
	public void handleLogin(ClickEvent e){
		uiHandlers.handleLogin();
	}
	
}
