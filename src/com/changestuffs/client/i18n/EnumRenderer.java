package com.changestuffs.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.text.shared.AbstractRenderer;

public class EnumRenderer<T extends Enum<T>> extends AbstractRenderer<T> {

	private final static String emptyValue = "";
	private final ConstantsWithLookup translator;
	
	public EnumRenderer(ConstantsWithLookup translator){
		this.translator=translator;
	}

	@Override
	public String render(T object) {
		if (object == null)
			return emptyValue;

		return translator.getString(object.name());
	}

}