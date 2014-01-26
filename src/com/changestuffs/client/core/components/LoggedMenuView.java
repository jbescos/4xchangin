package com.changestuffs.client.core.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class LoggedMenuView extends ViewImpl implements
		LoggedMenuPresenter.MyView {

	private final Widget widget;
	private LoggedMenuUiHandler uiHandlers;
	@UiField
	InlineHyperlink logout;

	public interface Binder extends UiBinder<Widget, LoggedMenuView> {
	}

	@Inject
	public LoggedMenuView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiHandler(value = { "logout" })
	public void handleLogout(ClickEvent e){
		uiHandlers.handleLogout();
	}
	
	@Override
	public void setUiHandlers(LoggedMenuUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}
}
