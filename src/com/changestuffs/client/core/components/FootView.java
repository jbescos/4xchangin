package com.changestuffs.client.core.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FootView extends Composite  {

	private final static Binder uiBinder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, FootView> {
	}

	public FootView() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log(getClass()+" instanced");
	}
}
