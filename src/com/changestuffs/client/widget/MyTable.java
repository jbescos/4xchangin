package com.changestuffs.client.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MyTable extends SimplePanel{;
	
	private final int colPerRows;
	private int actualCol = 0;
	private final Element table = DOM.createTable();
	private final Element thead = DOM.createTHead();
	private final Element tbody = DOM.createTBody();
	private final Element trHead = DOM.createTR();
	private final Map<String, Element> idexesRow = new HashMap<String, Element>();

	@UiConstructor
	public MyTable(String colPerRows){
		this.colPerRows=Integer.parseInt(colPerRows);
		DOM.insertChild(this.getElement(), table, 0);
		DOM.appendChild(table, thead);
		DOM.appendChild(table, tbody);
        DOM.appendChild(thead, trHead);
	}
	
	/**
	 * For head only
	 * @param child
	 */
	@Override
	public void add(Widget child) {
		Element th = DOM.createTH();
        th.appendChild(child.getElement());
        DOM.appendChild(trHead, th);
        actualCol++;
	}
	
	/**
	 * Auto adjust rows
	 * @param child -> CellWidget
	 * @param indexValue -> Value of the Collection
	 */
	public void addCell(String indexValue, Widget ... childs){
		Element trBody = null;
		if(actualCol<colPerRows-1){
			actualCol++;
			trBody = idexesRow.get(indexValue);
		}else{
			actualCol=0;
			trBody = DOM.createTR();
			idexesRow.put(indexValue, trBody);
		}
		DOM.insertChild(tbody, trBody, 0);
//		DOM.appendChild(tbody, trBody);
		Element td = DOM.createTD();
		for(Widget child : childs){
			if(child!=null){
				DOM.setEventListener(child.getElement(), child);
				Event.sinkEvents(child.getElement(), Event.ONCLICK);
			}
			td.appendChild(child.getElement());
		}
		DOM.appendChild(trBody, td);
	}
	
	public void removeBody(){
		for(Entry<String, Element> entry: idexesRow.entrySet()){
			entry.getValue().removeFromParent();
		}
		idexesRow.clear();
	}
	
	public void removeRow(String indexValue){
		idexesRow.remove(indexValue).removeFromParent();
	}

}
