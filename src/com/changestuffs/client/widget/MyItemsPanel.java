package com.changestuffs.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MyItemsPanel extends SimplePanel{

	private final Map<String, Element> items = new HashMap<String, Element>();
	
	@UiConstructor
	public MyItemsPanel(){
		
	}
	
	public void addItem(String id, Widget ... childs){
		if(!items.containsKey(id)){
			Element element = DOM.createSpan();
			for(Widget child : childs){
				DOM.setEventListener(child.getElement(), child);
				element.appendChild(child.getElement());
				Event.sinkEvents(child.getElement(), Event.ONCLICK);
			}
			DOM.insertChild(getElement(), element, 0);
			items.put(id, element);
		}
	}
	
	public void removeItem(String id){
		items.remove(id).removeFromParent();
	}
	
	public void changeStyle(String id, String style){
		if(style == null)
			items.get(id).removeAttribute("style");
		else
			items.get(id).setAttribute("style", style);
	}
	
}
