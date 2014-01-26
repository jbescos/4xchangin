package com.changestuffs.client.core.articles;

import com.changestuffs.shared.constants.Tags;
import com.gwtplatform.mvp.client.UiHandlers;

public interface AddArticlesUiHandler extends UiHandlers{

	void handleSubmit(Tags tag, String name, String interestedIn, String language);
	void handleCancel();
	
}
