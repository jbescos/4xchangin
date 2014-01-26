package com.changestuffs.server.servlets.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.client.place.NameTokens;
import com.changestuffs.server.utils.UrlUtils;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.constants.RequestParams;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DontNotify implements IServletManager{

	private final Provider<UserBeanOAM> provider;
	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Inject
	public DontNotify(Provider<UserBeanOAM> provider){
		this.provider=provider;
	}
	
	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String token = req.getParameter(RequestParams.token.name());
		log.info("Cancelling userId = "+token);
		UserBeanOAM oam = provider.get();
		String email = oam.updateNotify(token, false);
		log.info("Cancelling userId = "+token+". He is "+email);
		StringBuilder builder = new StringBuilder(UrlUtils.getBaseURL(req.getRequestURL().toString(), req.getRequestURI())).append("?#").append(NameTokens.dontNotify);
		log.info("Redirecting to "+builder.toString());
		resp.sendRedirect(builder.toString());
	}

}
