package com.changestuffs.server.servlets.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.UrlUtils;
import com.changestuffs.server.utils.UserBeanOAM;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class RedirectImpl implements IServletManager{
	
	private final Logger log = Logger.getLogger(getClass().getName());
	@Inject
	private Provider<UserBeanOAM> provider;
	
	@Logued
	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user!=null){
			log.info("User is "+user);
	        UserBeanOAM oam = provider.get();
	        oam.persistUser(new Date(), user, req.getRemoteAddr(), null);
		}
		StringBuilder builder = new StringBuilder(UrlUtils.getBaseURL(req.getRequestURL().toString(), req.getRequestURI())).append("?");
		@SuppressWarnings("unchecked")
		Map<String,String[]> params = req.getParameterMap();
		boolean first=true;
		for(Entry<String,String[]> entry : params.entrySet()){
			if(!first)
				builder.append("&");
			first = false;
			builder.append(entry.getKey()).append("=").append(entry.getValue()[0]);
		}
		log.info("Redirecting to: "+builder.toString());
		resp.sendRedirect(builder.toString());
	}

}
