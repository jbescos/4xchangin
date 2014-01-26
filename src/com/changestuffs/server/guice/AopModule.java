package com.changestuffs.server.guice;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import org.aopalliance.intercept.MethodInterceptor;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.guice.aspect.LoguedInterceptor;
import com.google.inject.AbstractModule;

public class AopModule extends AbstractModule{

	private MethodInterceptor loguedInterceptor = new LoguedInterceptor(); 
	
	@Override
	protected void configure() {
		bindInterceptor(any(), annotatedWith(Logued.class), loguedInterceptor);
	}
	
	public void setLoguedInterceptor(LoguedInterceptor loguedInterceptor){
		this.loguedInterceptor=loguedInterceptor;
	}

}
