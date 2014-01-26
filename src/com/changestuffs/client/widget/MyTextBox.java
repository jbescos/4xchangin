package com.changestuffs.client.widget;

import com.changestuffs.client.i18n.Translator;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextBox;

public class MyTextBox extends TextBox {
	
	private static final ConstantsWithLookup translator = GWT.create(Translator.class);
	
	@UiConstructor
	public MyTextBox(String placeholderValue){
		getElement().setAttribute("placeholder", translator.getString(placeholderValue));
	}
	
}
