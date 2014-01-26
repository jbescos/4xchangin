package com.changestuffs.server.actionhandlers;

import javax.validation.Valid;

import org.apache.bval.guice.Validate;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.RemoveContact;
import com.changestuffs.shared.actions.RemoveContactResult;
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
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class RemoveContactActionHandler implements
		ActionHandler<RemoveContact, RemoveContactResult> {

	private final Provider<UserBeanOAM> provider;
	private final DtoToJson toJson;
	
	@Inject
	public RemoveContactActionHandler(Provider<UserBeanOAM> provider, DtoToJson toJson) {
		this.provider = provider;
		this.toJson = toJson;
	}

	@Logued
	@Validate
	@Override
	public RemoveContactResult execute(@Valid RemoveContact action,
			ExecutionContext context) throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserBeanOAM oam = provider.get();
		oam.removeContact(user.getEmail(), action.getEmail(), action.isPending());
		// Pending contacts exists only in one user, but contacts in two
		if(!action.isPending()){
			oam.removeContact(action.getEmail(), user.getEmail(), false);
			String json = toJson.getJson(null, user.getEmail(), MessageType.removeContact);
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			channelService.sendMessage(new ChannelMessage(action.getEmail(), json));
		}
		return new RemoveContactResult();
	}

	@Override
	public void undo(RemoveContact action, RemoveContactResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<RemoveContact> getActionType() {
		return RemoveContact.class;
	}
}
