package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.GetUserInfo;
import com.changestuffs.shared.actions.GetUserInfoResult;
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
public class GetUserInfoActionHandler implements
		ActionHandler<GetUserInfo, GetUserInfoResult> {

	@Inject
	private Provider<UserBeanOAM> provider;
	
	@Logued
	@Override
	public GetUserInfoResult execute(GetUserInfo action, ExecutionContext context)
			throws ActionException {
		GetUserInfoResult result = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserBeanOAM oam = provider.get();
		GetUserInfoResult myInfo = oam.getGetUserInfoResult(user.getEmail());
		if(action.getEmail() != null){
			if(myInfo.getFriends().contains(action.getEmail())){
				result = oam.getGetUserInfoResult(action.getEmail());
			}else{
				throw new IllegalArgumentException(action.getEmail()+" isn't friend of "+user.getEmail());
			}
		}else{
			result = myInfo;
		}
		return result;
	}

	@Override
	public void undo(GetUserInfo action, GetUserInfoResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetUserInfo> getActionType() {
		return GetUserInfo.class;
	}
}
