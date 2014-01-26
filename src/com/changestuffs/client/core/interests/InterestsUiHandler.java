package com.changestuffs.client.core.interests;

import java.util.Map;

import com.gwtplatform.mvp.client.UiHandlers;

public interface InterestsUiHandler extends UiHandlers{
	void handleProduct(String productId);
	void handleContact(String email);
	void handleAddOffer(String offerId, Map<String,String> productIdNames);
	void handleRemoveOffer(String offerId);
	void handleWatchArticles(String offerId);
	void handleFriend(String email);
}
