package com.changestuffs.server.guice;

import java.util.logging.Logger;

import com.changestuffs.server.servlets.FrontServlet;
import com.changestuffs.shared.constants.ServletPaths;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;

public class DispatchServletModule extends ServletModule {

	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Override
	public void configureServlets() {
		serve(ServletPaths.dispatch.getPath()).with(DispatchServiceImpl.class);
		
		for(ServletPaths frontServletPath: ServletPaths.values()){
			if(frontServletPath != ServletPaths.dispatch)
				serve(frontServletPath.getPath()).with(FrontServlet.class);
		}
		
		install(new JpaPersistModule("appEngine"));
	    filter("/*").through(PersistFilter.class);
	    
		log.info("DispatchServletModule is configured");
	}
}
