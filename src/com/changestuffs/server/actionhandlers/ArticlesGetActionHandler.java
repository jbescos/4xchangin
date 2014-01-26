package com.changestuffs.server.actionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.persistence.beans.Product;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.ArticlesGetAction;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.dto.IArticlesDto;
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
public class ArticlesGetActionHandler implements
		ActionHandler<ArticlesGetAction, LookForResult> {

	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Override
	@Logued
	public LookForResult execute(ArticlesGetAction arg0, ExecutionContext arg1)
			throws ActionException {
		Map<String, IArticlesDto> articles = new HashMap<String, IArticlesDto>();
		String email = null;
		if(arg0.getEmail() != null){
			email = arg0.getEmail();
		}else{
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			email = user.getEmail();
		}
		
		ArticlesOAM oam = provider.get();
		List<Product> products = oam.getProducts(email);
		for (Product product : products) {
			IArticlesDto dto = oam.getIArticlesDto(product,
					arg0.isFullInfo(), email);
			articles.put(dto.getKeyHash(), dto);
		}
		LookForResult result = new LookForResult(articles);
		return result;
	}

	@Override
	public Class<ArticlesGetAction> getActionType() {
		return ArticlesGetAction.class;
	}

	@Override
	public void undo(ArticlesGetAction paramA, LookForResult paramR,
			ExecutionContext paramExecutionContext) throws ActionException {
		// TODO Auto-generated method stub

	}

}
