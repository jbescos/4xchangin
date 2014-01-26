package com.changestuffs.client.core.login;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LoginUiHandler extends UiHandlers{

	void createLogin(String providerUrl);
	
}
