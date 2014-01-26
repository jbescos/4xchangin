package com.changestuffs.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface ChangestuffsMessages extends Messages {

	@DefaultMessage("Enter a link URL:")
	String enterLink();

	@DefaultMessage("This article doesn''t exist. Maybe it was removed.")
	String articleDoesntExist();

	@DefaultMessage("Do you want to add him to your social panel?. If you accept, you could contact with him, and he with you.")
	String addUser();

	@DefaultMessage("Do you want to remove {0}?")
	String removeArticle(String article);

	@DefaultMessage("Do you want to remove the offer?")
	String removeOffer();

	@DefaultMessage("Invitation sent to {0}")
	String invitationSent(String email);

	@DefaultMessage("Invitation couldn''t be sent to {0}. Make sure it exists.")
	String invitationNotSent(String email);

	@DefaultMessage("Do you want to remove {0} from your contacts?")
	String removeContact(String email);

	@DefaultMessage("{0} has invited you to 4xchangin")
	String inviteContact(String email);
	
	@DefaultMessage("You have receibed an offer for {0}")
	String offerReceibed(String productName);

}
