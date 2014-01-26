package com.changestuffs.server.actionhandlers;

import java.util.HashSet;
import java.util.Set;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.IsOnline;
import com.changestuffs.shared.actions.IsOnlineResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class IsOnlineActionHandler implements
		ActionHandler<IsOnline, IsOnlineResult> {

	private final Provider<UserBeanOAM> provider;
	
	@Inject
	public IsOnlineActionHandler(Provider<UserBeanOAM> provider) {
		this.provider = provider;
	}

	@Override
	@Logued
	public IsOnlineResult execute(IsOnline action, ExecutionContext context)
			throws ActionException {
		UserBeanOAM oam = provider.get();
		Set<String> emails = new HashSet<String>();
		emails.add(action.getEmail());
		return new IsOnlineResult(oam.getOnline(emails).contains(action.getEmail()));
	}

	@Override
	public void undo(IsOnline action, IsOnlineResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<IsOnline> getActionType() {
		return IsOnline.class;
	}
}
