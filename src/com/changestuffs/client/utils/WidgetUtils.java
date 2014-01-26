package com.changestuffs.client.utils;

import com.google.gwt.user.client.ui.ListBox;

public class WidgetUtils {

	public static void selectItemListBox(ListBox listBox, String selectValue){
		if(selectValue != null){
			for(int i=0;i<listBox.getItemCount();i++){
				if(selectValue.contains(listBox.getValue(i))){
					listBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}
	
}
