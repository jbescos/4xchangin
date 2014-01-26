package com.changestuffs.server.actionhandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.changestuffs.shared.actions.LoginsAction;
import com.changestuffs.shared.actions.LoginsResult;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class LoginsActionHandler implements ActionHandler<LoginsAction, LoginsResult>{

	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Override
	public LoginsResult execute(LoginsAction arg0, ExecutionContext arg1)
			throws ActionException {
		Set<String> attributes = new HashSet<String>();
		UserService userService = UserServiceFactory.getUserService();
		String loginUrl = userService.createLoginURL(arg0.getRedirectUrl(), null, arg0.getProviderUrl(), attributes);
		log.info("Login URL is: "+loginUrl+" and will redirect to "+arg0.getRedirectUrl());
		return new LoginsResult(loginUrl);
	}

	@Override
	public Class<LoginsAction> getActionType() {
		return LoginsAction.class;
	}

	@Override
	public void undo(LoginsAction arg0, LoginsResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
