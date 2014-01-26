package com.changestuffs.client.core.dontNotify;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DontNotifyView extends ViewImpl implements DontNotifyPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DontNotifyView> {
	}

	@Inject
	public DontNotifyView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		GWT.log(getClass()+" instanced");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
