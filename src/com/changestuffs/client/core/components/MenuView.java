package com.changestuffs.client.core.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuView extends Composite  {

	private final static Binder uiBinder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, MenuView> {
	}

	public MenuView() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log(getClass()+" instanced");
	}
}
