package com.changestuffs.server.actionhandlers;

import javax.validation.Valid;

import org.apache.bval.guice.Validate;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.MessageOAM;
import com.changestuffs.shared.actions.SendMessage;
import com.changestuffs.shared.actions.SendMessageResult;
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
public class SendMessageActionHandler implements ActionHandler<SendMessage, SendMessageResult> {

	private final Provider<MessageOAM> provider;
	private final DtoToJson toJson;
	
	@Inject
	public SendMessageActionHandler(DtoToJson toJson, Provider<MessageOAM> provider){
		this.toJson = toJson;
		this.provider = provider;
	}
	
	@Logued
	@Validate
	@Override
	public SendMessageResult execute(@Valid SendMessage action,
			ExecutionContext context) throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String json = toJson.getJson(action.getMessage(), user.getEmail(), MessageType.message);
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		channelService.sendMessage(new ChannelMessage(action.getEmail(), json));
		MessageOAM oam = provider.get();
		oam.addMessage(action, user.getEmail());
		return new SendMessageResult();
	}

	@Override
	public void undo(SendMessage action, SendMessageResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<SendMessage> getActionType() {
		return SendMessage.class;
	}
}
