package com.changestuffs.server.actionhandlers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.shared.actions.LogoutAction;
import com.changestuffs.shared.actions.LogoutResult;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class LogoutActionHandler implements ActionHandler<LogoutAction, LogoutResult> {

	private final Logger log = Logger.getLogger(getClass().getName());
	@Inject
	private Provider<HttpServletRequest> requestProvider;

	@Override
	@Logued
	public LogoutResult execute(LogoutAction arg0, ExecutionContext arg1) throws ActionException {
		log.info("Request " + requestProvider.get());
		UserService userService = UserServiceFactory.getUserService();
		log.info("Doing logout for "+userService.getCurrentUser().getNickname());
        return new LogoutResult(userService.createLogoutURL(arg0.getUrlToRedirect()));
	}

	@Override
	public Class<LogoutAction> getActionType() {
		return LogoutAction.class;
	}

	@Override
	public void undo(LogoutAction arg0, LogoutResult arg1, ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
