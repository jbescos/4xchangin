package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.shared.actions.CreateToken;
import com.changestuffs.shared.actions.CreateTokenResult;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class CreateTokenActionHandler implements
		ActionHandler<CreateToken, CreateTokenResult> {

	private final int MINUTES = 120;
	
	@Logued
	@Override
	public CreateTokenResult execute(CreateToken action, ExecutionContext context)
			throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
	    String userId = userService.getCurrentUser().getEmail();
	    ChannelService channelService = ChannelServiceFactory.getChannelService();
	    String token = channelService.createChannel(userId, MINUTES);
		return new CreateTokenResult(token);
	}

	@Override
	public void undo(CreateToken action, CreateTokenResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CreateToken> getActionType() {
		return CreateToken.class;
	}
}
