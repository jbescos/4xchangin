package com.changestuffs.client.widget.texteditor;

import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.gwtplatform.mvp.client.UiHandlers;

public interface TextEditorUiHandler extends UiHandlers{

	void handleFormResponse(SubmitCompleteEvent event);
	
}
