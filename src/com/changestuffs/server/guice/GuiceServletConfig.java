package com.changestuffs.server.guice;

import org.apache.bval.guice.ValidationModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	public Injector getInjector() {
		return Guice.createInjector(new ServerModule(), new DispatchServletModule(), new ValidationModule(), new AopModule(), new MyModule());
	}
	
}
