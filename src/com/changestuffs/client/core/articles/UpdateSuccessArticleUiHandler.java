package com.changestuffs.client.core.articles;

import com.changestuffs.shared.dto.IArticlesDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface UpdateSuccessArticleUiHandler extends UiHandlers{

	void updateSuccessProduct(IArticlesDto articlesDto);
	
}
