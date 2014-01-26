package com.changestuffs.client.core.messages;

import java.util.Set;

import com.changestuffs.client.core.components.ChatPresenter;
import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.shared.actions.AddFriend;
import com.changestuffs.shared.actions.AddFriendResult;
import com.changestuffs.shared.actions.ArticlesGetAction;
import com.changestuffs.shared.actions.GetUserInfo;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.actions.RemoveContact;
import com.changestuffs.shared.actions.RemoveContactResult;
import com.changestuffs.shared.actions.SendInvitation;
import com.changestuffs.shared.actions.SendInvitationResult;
import com.changestuffs.shared.constants.Tags;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.PlaceRequest.Builder;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class MessagesPresenter extends
		Presenter<MessagesPresenter.MyView, MessagesPresenter.MyProxy>
		implements MessagesUiHandler {

	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;
	private final CurrentUser user;
	private final ChatPresenter chatPresenter;

	public interface MyView extends View, HasUiHandlers<MessagesUiHandler> {
		void lookUserInfo(GetUserInfoResult result);
		void addArticles(LookForResult result);
		void invitationSent(String email, boolean isSent);
		void addContact(String friend, boolean pending);
		void removeContact(String friend, boolean pending);
	}

	@ProxyCodeSplit
	@UseGatekeeper(NeedsLoginKeeper.class)
	@NameToken(NameTokens.social)
	public interface MyProxy extends ProxyPlace<MessagesPresenter> {
	}

	@Inject
	public MessagesPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher,
			final PlaceManager placeManager, CurrentUser user, ChatPresenter chatPresenter) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetMainContent);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.user = user;
		this.chatPresenter = chatPresenter;
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		setContacts(user.getPendingFriends(), true);
		setContacts(user.getConversations().keySet(), false);
		
		dispatcher.execute(new GetUserInfo(null),
				new AsyncCallback<GetUserInfoResult>() {
					@Override
					public void onFailure(Throwable arg0) {
						GWT.log("Error doing GetUserInfo", arg0);
					}

					@Override
					public void onSuccess(final GetUserInfoResult arg0) {
						user.getOnline().addAll(arg0.getOnline());
						user.getPendingFriends().addAll(arg0.getPendingFriends());
						setContacts(arg0.getFriends(), false);
						setContacts(arg0.getPendingFriends(), true);
					}
				});
	}
	
	private void setContacts(Set<String> contacts, boolean pending){
		for(String contact:contacts){
			getView().addContact(contact, pending);
		}
	}

	@Override
	public void loadFriendInfo(String email) {
		Builder request = new PlaceRequest.Builder().nameToken(
				NameTokens.getSocial()).with(
				MessagesPresenter.Parameters.user.name(), email);
		placeManager.revealPlace(request.build());
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		final String user = request.getParameter(Parameters.user.name(), null);
		if (user != null && this.user.getConversations().keySet().contains(user)) {
			dispatcher.execute(new GetUserInfo(user),
					new AsyncCallback<GetUserInfoResult>() {
						@Override
						public void onFailure(Throwable arg0) {
							GWT.log("Error doing GetUserInfo", arg0);
						}

						@Override
						public void onSuccess(GetUserInfoResult arg0) {
							getView().lookUserInfo(arg0);
							dispatcher.execute(new ArticlesGetAction(false, user),
									new AsyncCallback<LookForResult>() {
										@Override
										public void onFailure(Throwable caught) {
											GWT.log("Error doing GetUserInfo", caught);
										}

										@Override
										public void onSuccess(LookForResult result) {
											getView().addArticles(result);
										}
									});
						}
					});
		}
	}

	public static enum Parameters {
		user;
	}

	@Override
	public void handleClickName(Tags tag, String idHash) {
		PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.lookfor).with(LookforPresenter.Parameters.id.name(), idHash).build();
		placeManager.revealPlace(request, true);
	}

	@Override
	public void sendInvitation(final String email) {
		dispatcher.execute(new SendInvitation(email), new AsyncCallback<SendInvitationResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error doing SendInvitation", caught);
				getView().invitationSent(email, false);
			}
			@Override
			public void onSuccess(SendInvitationResult result) {
				getView().invitationSent(email, result.isSent());
			}
		});
	}

	@Override
	public void addPendingFriend(final String contact) {
		dispatcher.execute(new AddFriend(contact), new AsyncCallback<AddFriendResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error doing AddFriend", caught);
			}
			@Override
			public void onSuccess(AddFriendResult result) {
				user.getPendingFriends().remove(contact);
				getView().removeContact(contact, true);
				getView().addContact(contact, false);
				chatPresenter.appendFriend(contact);
			}
		});
	}

	@Override
	public void removeContact(final String contact, final boolean pending) {
		dispatcher.execute(new RemoveContact(contact, pending), new AsyncCallback<RemoveContactResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error removing contact", caught);
			}
			@Override
			public void onSuccess(RemoveContactResult result) {
				if(pending){
					user.getPendingFriends().remove(contact);
				}else{
					chatPresenter.removeContact(contact);
				}
				getView().removeContact(contact, pending);
			}
		});
	}

}
