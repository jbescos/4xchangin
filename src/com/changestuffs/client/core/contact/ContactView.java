package com.changestuffs.client.core.contact;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ContactView extends ViewImpl implements ContactPresenter.MyView{

	private final Widget widget;
	
	interface Binder extends UiBinder<Widget, ContactView>{
		
	}
	
	@Inject
	public ContactView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
}
