package com.changestuffs.client.core.messages;

import java.util.Map.Entry;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.client.styles.Styles;
import com.changestuffs.client.widget.MyItemsPanel;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.changestuffs.shared.utils.StringUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MessagesView extends ViewImpl implements MessagesPresenter.MyView {

	private final ChangestuffsMessages messages = GWT.create(ChangestuffsMessages.class);
	private final Widget widget;
	private MessagesUiHandler uiHandlers;
	@UiField
	MyItemsPanel friends;
	@UiField
	FlowPanel articles;
	@UiField
	Label cell;
	@UiField
	Label city;
	@UiField
	Label country;
	@UiField
	TextBox friendEmail;
	@UiField
	SubmitButton invite;
	@UiField
	MyItemsPanel pendingFriends;

	public interface Binder extends UiBinder<Widget, MessagesView> {
	}

	@Inject
	public MessagesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(MessagesUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}

	@Override
	public void lookUserInfo(GetUserInfoResult result) {
		cell.setText(result.getCell());
		city.setText(result.getCity());
		country.setText(result.getCountry());
	}
	
	@UiHandler("invite")
	public void invite(ClickEvent event) {
		uiHandlers.sendInvitation(friendEmail.getText());
	}

	@Override
	public void addArticles(LookForResult result) {
		articles.clear();
		for(Entry<String, IArticlesDto> article:result.getArticles().entrySet()){
			Label link = new Label(article.getValue().getName());
			link.setStylePrimaryName(Styles.CLICKABLE_OVAL_COLUMNS);
			handleClickName(link, article.getKey(), article.getValue().getTag());
			articles.add(link);
		}
	}
	
	private void handleClickName(HasClickHandlers handler, final String idHash, final Tags tag){
		handler.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.handleClickName(tag, idHash);
			}
		});
	}

	@Override
	public void invitationSent(String email, boolean isSent) {
		if(isSent)
			Window.alert(messages.invitationSent(email));
		else
			Window.alert(messages.invitationNotSent(email));
	}

	@Override
	public void addContact(final String contact, final boolean pending) {
		Label label = new Label(StringUtils.subString(25, contact));
		label.setTitle(contact);
		label.setStylePrimaryName(Styles.DEFAULT);
		Label removeContact = new Label();
		removeContact.getElement().setInnerHTML("<i class='icon-cancel-squared'/>");
		removeContact.setStylePrimaryName(Styles.CLICKABLE_COLUMNS);
		removeContact.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(messages.removeContact(contact))){
					uiHandlers.removeContact(contact, pending);
				}
			}
		});
		if(pending) {
			Label addContact = new Label();
			addContact.getElement().setInnerHTML("<i class='icon-user-add'/>");
			addContact.setStylePrimaryName(Styles.CLICKABLE_COLUMNS);
			pendingFriends.addItem(contact, addContact, removeContact, label);
			addContact.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					uiHandlers.addPendingFriend(contact);
				}
			});
			
		} else {
			Label infoContact = new Label();
			infoContact.getElement().setInnerHTML("<i class='icon-info'/>");
			infoContact.setStylePrimaryName(Styles.CLICKABLE_COLUMNS);
			friends.addItem(contact, infoContact, removeContact, label);
			infoContact.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					uiHandlers.loadFriendInfo(contact);
				}
			});
		}
	}

	@Override
	public void removeContact(String email, boolean pending) {
		if(pending){
			pendingFriends.removeItem(email);
		}else{
			friends.removeItem(email);
		}
	}
	
}
