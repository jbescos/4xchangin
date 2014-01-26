package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.LookForResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class LookForSearchActionHandler implements
		ActionHandler<LookForAction, LookForResult> {

	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Override
	public LookForResult execute(LookForAction arg0, ExecutionContext arg1)
			throws ActionException {
		LookForResult result = null;
		ArticlesOAM oam = provider.get();
		result = new LookForResult(oam.getArticles(arg0, null));
		return result;
	}

	@Override
	public Class<LookForAction> getActionType() {
		return LookForAction.class;
	}

	@Override
	public void undo(LookForAction arg0, LookForResult arg1,
			ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
