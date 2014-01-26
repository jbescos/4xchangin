package com.changestuffs.client.core.profile;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ProfileUiHandler extends UiHandlers{
	
	void handleSubmit(String cell, String city, String country, boolean receiveEmails);
	
}
