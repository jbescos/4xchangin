package com.changestuffs.server.actionhandlers;

import javax.validation.Valid;

import org.apache.bval.Validate;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.AddFriend;
import com.changestuffs.shared.actions.AddFriendResult;
import com.changestuffs.shared.constants.MessageType;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class AddFriendActionHandler implements
		ActionHandler<AddFriend, AddFriendResult> {
	
	private final Provider<UserBeanOAM> provider;
	private final DtoToJson toJson;
	
	@Inject
	public AddFriendActionHandler(Provider<UserBeanOAM> provider, DtoToJson toJson){
		this.provider = provider;
		this.toJson = toJson;
	}

	@Logued
	@Validate
	@Override
	public AddFriendResult execute(@Valid AddFriend action,
			ExecutionContext context) throws ActionException {
		boolean result = false;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserBeanOAM oam = provider.get();
		result = oam.addFriend(user.getEmail(), action.getEmail());
		// And the other has the same friend
		oam.addFriend(action.getEmail(), user.getEmail());
		
		String json = toJson.getJson(null, user.getEmail(), MessageType.addContact);
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		channelService.sendMessage(new ChannelMessage(action.getEmail(), json));
		return new AddFriendResult(result);
	}

	@Override
	public void undo(AddFriend action, AddFriendResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<AddFriend> getActionType() {
		return AddFriend.class;
	}
}
