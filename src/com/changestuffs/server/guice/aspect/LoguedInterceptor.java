package com.changestuffs.server.guice.aspect;

import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.http.client.RequestException;

public class LoguedInterceptor implements MethodInterceptor{

	private final Logger log = Logger.getLogger(LoguedInterceptor.class.getName());
	
	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		if(!hasLoguedAnnotation(arg0)){
			log.info("It doesn't need logging");
			return arg0.proceed();
		}else{
			if(isLogued()){
				log.info("User is logged");
				return arg0.proceed();
			}else{
				log.warning("Trying to invoke "+arg0.getMethod().getName()+" without logging");
				throw new RequestException("Trying to invoke "+arg0.getMethod().getName()+" without logging");
			}
		}
	}
	
	boolean isLogued(){
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
		return user!=null;
	}
	
	private boolean hasLoguedAnnotation(MethodInvocation arg0){
		Logued annotation = arg0.getMethod().getAnnotation(Logued.class);
		
		return annotation!=null;
	}
	
}
