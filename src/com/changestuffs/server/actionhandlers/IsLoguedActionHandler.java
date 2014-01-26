package com.changestuffs.server.actionhandlers;

import com.changestuffs.shared.actions.IsLoguedAction;
import com.changestuffs.shared.actions.IsLoguedResult;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class IsLoguedActionHandler implements
		ActionHandler<IsLoguedAction, IsLoguedResult> {

	@Override
	public IsLoguedResult execute(IsLoguedAction action, ExecutionContext context)
			throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user!=null){
			return new IsLoguedResult(user.getEmail());
		}else{
			return new IsLoguedResult(null);
		}
	}

	@Override
	public void undo(IsLoguedAction action, IsLoguedResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<IsLoguedAction> getActionType() {
		return IsLoguedAction.class;
	}
}
