package com.changestuffs.client.widget.products;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ProductsUiHandler extends UiHandlers{

	void handleClickName(String idHash);
	void handleKeypressTextBox(String value);
	
}
