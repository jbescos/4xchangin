package com.changestuffs.client.core.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.shared.actions.CreateToken;
import com.changestuffs.shared.actions.CreateTokenResult;
import com.changestuffs.shared.actions.GetUserInfo;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.changestuffs.shared.actions.IsOnline;
import com.changestuffs.shared.actions.IsOnlineResult;
import com.changestuffs.shared.actions.LoadMessages;
import com.changestuffs.shared.actions.LoadMessagesResult;
import com.changestuffs.shared.actions.SendMessage;
import com.changestuffs.shared.actions.SendMessageResult;
import com.changestuffs.shared.constants.MessageType;
import com.changestuffs.shared.dto.IMessageResponse;
import com.changestuffs.shared.dto.MessageResponse;
import com.changestuffs.shared.factory.MyAutoBeanFactory;
import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ChatPresenter extends
		Presenter<ChatPresenter.MyView, ChatPresenter.MyProxy> implements ChatUiHandler{

	private final MyAutoBeanFactory beanFactory = GWT.create(MyAutoBeanFactory.class);
	private final DispatchAsync dispatcher;
	private final ChannelFactory channelFactory;
	private Socket socket;
	private final CurrentUser user;
	
	public interface MyView extends View, HasUiHandlers<ChatUiHandler> {
		void appendMessage(String talkingWith, IMessageResponse response);
		void updateContact(ContactInfo contact);
		void addContact(ContactInfo friend);
		void removeContact(String email);
		void loadMessagesDBMessages(String talkingWith, IMessageResponse response);
	}

	@ProxyCodeSplit
	@UseGatekeeper(NeedsLoginKeeper.class)
	public interface MyProxy extends Proxy<ChatPresenter> {
	}

	@Inject
	public ChatPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher, ChannelFactory channelFactory, CurrentUser user) {
		super(eventBus, view, proxy, MainPresenter.TYPE_SetChatContent);
		this.dispatcher=dispatcher;
		this.channelFactory=channelFactory;
		this.user=user;
		getView().setUiHandlers(this);
	}
	
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		if(user.getEmail() != null){
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
							setContacts(toContactInfo(arg0.getFriends(), user.getOnline()));
							LoadMessages action = new LoadMessages(arg0.getFriends());
							dispatcher.execute(action, new AsyncCallback<LoadMessagesResult>() {
								@Override
								public void onFailure(Throwable caught) {
									GWT.log("Error doing LoadMessages", caught);
								}
								@Override
								public void onSuccess(LoadMessagesResult result) {
									for(String friend : arg0.getFriends()){
										user.getConversations().put(friend, result.getConversations().get(friend));
										for(IMessageResponse response : result.getConversations().get(friend)){
											getView().loadMessagesDBMessages(friend, response);
										}
									}
									setSocket();
								}
							});
						}
					});
		}
	}

	public void closeSocket(){
		if(socket != null)
			socket.close();
	}
	
	private void setSocket() {
		dispatcher.execute(new CreateToken(),
				new AsyncCallback<CreateTokenResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error doing CreateToken", caught);
					}

					@Override
					public void onSuccess(CreateTokenResult result) {
						Channel channel = channelFactory.createChannel(result
								.getToken());
						socket = channel.open(new SocketListener() {

							@Override
							public void onOpen() {
								GWT.log("Socket opened");
								// Notify im online
							}

							@Override
							public void onMessage(String message) {
								GWT.log("Socket received: " + message);
								IMessageResponse response = getResponse(message);
								if(MessageType.login == response.getMessageType()){
									GWT.log(response.getFrom()+" is online");
									user.getOnline().add(response.getFrom());
									getView().updateContact(new ContactInfo(response.getFrom(), true));
									
								}else if(MessageType.logout == response.getMessageType()){
									GWT.log(response.getFrom()+" is offline");
									user.getOnline().remove(response.getFrom());
									getView().updateContact(new ContactInfo(response.getFrom(), false));
								}else if(MessageType.removeContact == response.getMessageType()){
									removeContact(response.getFrom());
								}else if(MessageType.addContact == response.getMessageType()){
									appendFriend(response.getFrom());
								}else{
									addConversation(response.getFrom(), response);
									getView().appendMessage(response.getFrom(), response);
								}
							}

							@Override
							public void onError(ChannelError error) {
								GWT.log("Error in socket: "
										+ error.getDescription());
								socket.close();
							}

							@Override
							public void onClose() {
								GWT.log("Socket closed");
								// Notify i'm closed
							}
						});
					}
				});
	}
	
	public void removeContact(String from){
		user.getConversations().remove(from);
		getView().removeContact(from);
		user.getOnline().remove(from);
	}
	
	public void appendFriend(final String contact){
		dispatcher.execute(new IsOnline(contact), new AsyncCallback<IsOnlineResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error doing IsOnline", caught);
			}
			@Override
			public void onSuccess(IsOnlineResult result) {
				if(result.isOnline())
					user.getOnline().add(contact);
				user.getConversations().put(contact, new ArrayList<IMessageResponse>());
				setContacts(toContactInfo(user.getConversations().keySet(), user.getOnline()));
			}
		});
	}
	
	@Override
	public void submitMessage(final String contact, final String message) {
		dispatcher.execute(new SendMessage(contact, message),
				new AsyncCallback<SendMessageResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error doing SendMessage", caught);
					}

					@Override
					public void onSuccess(SendMessageResult result) {
						MessageResponse response = new MessageResponse();
						response.setFrom(user.getEmail());
						response.setMessage(message);
						addConversation(contact, response);
						getView().appendMessage(contact, response);
					}
				});
	}
	
	private void addConversation(String from, IMessageResponse response){
		if(user.getConversations().containsKey(from)){
			List<IMessageResponse> conversation = user.getConversations().get(from);
			conversation.add(response);
		}else{
			List<IMessageResponse> conversation = new ArrayList<IMessageResponse>();
			conversation.add(response);
			user.getConversations().put(from, conversation);
		}
	}
	
	private IMessageResponse getResponse(String json){
		AutoBean<IMessageResponse> autoBeanClone = AutoBeanCodex.decode(beanFactory, IMessageResponse.class, json);
		IMessageResponse bean = autoBeanClone.as();
		return bean;
	}
	
	private void setContacts(Set<ContactInfo> contacts){
		List<ContactInfo> orderedList = new ArrayList<ContactInfo>(contacts);
		Collections.sort(orderedList, new Comparator<ContactInfo>() {
			@Override
			public int compare(ContactInfo o1, ContactInfo o2) {
				if(o1.isOnline())
					return -1;
				else
					return 1;
			}
		});
		for(ContactInfo contact:orderedList){
			getView().addContact(contact);
		}
	}
	
	private Set<ContactInfo> toContactInfo(Set<String> contacts, Set<String> online){
		Set<ContactInfo> contactInfo = new HashSet<ContactInfo>();
		for(String contact : contacts){
			contactInfo.add(new ContactInfo(contact, online.contains(contact)));
		}
		return contactInfo;
	}
	
	class ContactInfo{
		private String email;
		private boolean online;
		public ContactInfo(String email, boolean online) {
			super();
			this.email = email;
			this.online = online;
		}
		public String getEmail() {
			return email;
		}
		public boolean isOnline() {
			return online;
		}
	}
	
}
