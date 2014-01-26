package com.changestuffs.client.core.components;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainView extends ViewImpl implements MainPresenter.MyView {

	private final Widget widget;
	@UiField
	FlowPanel mainContentPanel;
	@UiField
	FlowPanel headContentPanel;
	@UiField
	FlowPanel chatContentPanel;

	public interface Binder extends UiBinder<Widget, MainView> {
	}

	@Inject
	public MainView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		GWT.log(getClass() + " instanced");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == MainPresenter.TYPE_SetMainContent) {
			setContent(mainContentPanel, content);
		} else if (slot == MainPresenter.TYPE_SetHeadContent) {
			setContent(headContentPanel, content);
		} else if (slot == MainPresenter.TYPE_SetChatContent) {
			setContent(chatContentPanel, content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void setContent(FlowPanel base, IsWidget content) {
		base.clear();

		if (content != null) {
			base.add(content);
		}
	}

}
