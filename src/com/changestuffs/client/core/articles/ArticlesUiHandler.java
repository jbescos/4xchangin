package com.changestuffs.client.core.articles;

import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ArticlesUiHandler extends UiHandlers{

	void handleRemoveArticle(String keyHash);
	void handleEditArticle(IArticlesDto article);
	void handleClickName(Tags tag, String idHash);
	void handleRevealAddArticle();
	
}
