package com.changestuffs.client.core.components;

import java.util.HashMap;
import java.util.Map;

import com.changestuffs.client.core.components.ChatPresenter.ContactInfo;
import com.changestuffs.client.widget.chat.ChatPanel;
import com.changestuffs.shared.dto.IMessageResponse;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ChatView extends ViewImpl implements ChatPresenter.MyView {

	private final Widget widget;
	private ChatUiHandler uiHandlers;
	@UiField
	FlowPanel friends;
	private final Map<String, ChatPanel> chats = new HashMap<String, ChatPanel>();

	public interface Binder extends UiBinder<Widget, ChatView> {
	}
	public interface BinderChat extends ChatPanel.Binder {
	}

	@Inject
	public ChatView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(ChatUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}

	@Override
	public void appendMessage(String talkingWith, IMessageResponse response) {
		chats.get(talkingWith).addText(response.getFrom(), response.getMessage(), true);
	}

	@Override
	public void updateContact(ContactInfo contact) {
		ChatPanel chat = chats.get(contact.getEmail());
		chat.setOnline(contact.isOnline());
		int index = friends.getWidgetIndex(chat);
		friends.remove(index);
		friends.insert(chat, 0);
	}

	@Override
	public void addContact(final ContactInfo contact) {
		if(!chats.containsKey(contact.getEmail())){
			final ChatPanel chatWindow = new ChatPanel(contact.getEmail());
			chatWindow.setUiHandlers(uiHandlers);
			chatWindow.setOnline(contact.isOnline());
			chats.put(contact.getEmail(), chatWindow);
			friends.insert(chatWindow, 0);
		}
	}

	@Override
	public void removeContact(String email) {
		friends.remove(chats.remove(email));
	}

	@Override
	public void loadMessagesDBMessages(String talkingWith,
			IMessageResponse response) {
		chats.get(talkingWith).addText(response.getFrom(), response.getMessage(), false);
	}

}
