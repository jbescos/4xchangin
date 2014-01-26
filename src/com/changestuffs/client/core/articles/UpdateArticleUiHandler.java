package com.changestuffs.client.core.articles;

import com.changestuffs.shared.constants.Tags;
import com.gwtplatform.mvp.client.UiHandlers;

public interface UpdateArticleUiHandler extends UiHandlers{

	void updateProduct(Tags tag, String name, String interestedIn, String language);
	void handleCancel();
	
}
