package com.changestuffs.server.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.server.servlets.impl.IServletManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

@SuppressWarnings("serial")
@Singleton
public class FrontServlet extends HttpServlet {

	private final Logger log = Logger.getLogger(getClass().getName());
	@Inject
	private Injector injector;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		IServletManager manager = getManager(req);
		log.info("Instance: " + manager);
		manager.manage(req, resp);
	}

	IServletManager getManager(HttpServletRequest req) {
		IServletManager manager = injector.getInstance(Key.get(IServletManager.class, Names.named(req.getServletPath())));

		return manager;
	}

}
