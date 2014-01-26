package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.UpdateUserInfo;
import com.changestuffs.shared.actions.UpdateUserInfoResult;
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
public class UpdateUserInfoActionHandler implements
		ActionHandler<UpdateUserInfo, UpdateUserInfoResult> {

	@Inject
	private Provider<UserBeanOAM> provider;
	
	@Logued
	@Override
	public UpdateUserInfoResult execute(UpdateUserInfo action,
			ExecutionContext context) throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserBeanOAM oam = provider.get();
		oam.updateUser(action, user.getEmail());
		return new UpdateUserInfoResult();
	}

	@Override
	public void undo(UpdateUserInfo action, UpdateUserInfoResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<UpdateUserInfo> getActionType() {
		return UpdateUserInfo.class;
	}
}
