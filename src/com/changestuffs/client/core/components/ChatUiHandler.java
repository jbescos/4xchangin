package com.changestuffs.client.core.components;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ChatUiHandler extends UiHandlers {

	void submitMessage(String friend, String message);
	
}
