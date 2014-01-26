package com.changestuffs.client.core.messages;

import com.changestuffs.shared.constants.Tags;
import com.gwtplatform.mvp.client.UiHandlers;

public interface MessagesUiHandler extends UiHandlers{
	void loadFriendInfo(String email);
	void handleClickName(Tags tag, String idHash);
	void sendInvitation(String email);
	void addPendingFriend(String pending);
	void removeContact(String contact, boolean pending);
}
