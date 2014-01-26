package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.MessageOAM;
import com.changestuffs.shared.actions.LoadMessages;
import com.changestuffs.shared.actions.LoadMessagesResult;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoadMessagesActionHandler implements
		ActionHandler<LoadMessages, LoadMessagesResult> {

	@Inject
	private Provider<MessageOAM> provider;

	@Logued
	@Override
	public LoadMessagesResult execute(LoadMessages action, ExecutionContext context)
			throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		MessageOAM oam = provider.get();
		return new LoadMessagesResult(oam.getConversations(user.getEmail(), action.getContacts()));
	}

	@Override
	public void undo(LoadMessages action, LoadMessagesResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoadMessages> getActionType() {
		return LoadMessages.class;
	}
}
