package com.changestuffs.client.widget;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class MyLabelImage extends Label{

	private final String value;
	
	@UiConstructor
	public MyLabelImage(String imageSrc, String imageAlt, String value){
		Image image = new Image(imageSrc);
		image.setAltText(imageAlt);
		getElement().appendChild(image.getElement());
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
