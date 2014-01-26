package com.changestuffs.server.actionhandlers;

import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.ArticleRemoveAction;
import com.changestuffs.shared.actions.ArticleRemoveResult;
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
public class ArticleRemoveActionHandler implements ActionHandler<ArticleRemoveAction, ArticleRemoveResult>{

	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Override
	public ArticleRemoveResult execute(ArticleRemoveAction arg0,
			ExecutionContext arg1) throws ActionException {
		ArticleRemoveResult result = new ArticleRemoveResult(arg0.getKeyHash());
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		ArticlesOAM oam = provider.get();
		oam.remove(arg0.getKeyHash(), user.getEmail());
		return result;
	}

	@Override
	public Class<ArticleRemoveAction> getActionType() {
		return ArticleRemoveAction.class;
	}

	@Override
	public void undo(ArticleRemoveAction arg0, ArticleRemoveResult arg1,
			ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
