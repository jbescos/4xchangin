package com.changestuffs.client.widget;

import com.changestuffs.client.i18n.EnumRenderer;
import com.changestuffs.client.i18n.Translator;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ListBox;

public class MyEnumListBox<T extends Enum<T>> extends ListBox{

	private T[] enumValues;
	private static final ConstantsWithLookup translator = GWT.create(Translator.class);
	private final EnumRenderer<T> render = new EnumRenderer<T>(translator);
	
	@UiConstructor
	public MyEnumListBox(){
		setStylePrimaryName("picker");
	}
	
	public void setEnum(T[] enumValues){
		this.enumValues=enumValues;
		for(T value:enumValues){
			// TODO set in arg0 with i18n
			this.addItem(render.render(value), value.name());
		}
	}
	
	public void setSelectedItem(T value){
		for(int i=0;i<enumValues.length;i++){
			if(enumValues[i]==value){
				setSelectedIndex(i);
				break;
			}
		}
	}
	
}
