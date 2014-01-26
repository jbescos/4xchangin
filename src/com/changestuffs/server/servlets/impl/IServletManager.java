package com.changestuffs.server.servlets.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IServletManager {

	public void manage(HttpServletRequest req, HttpServletResponse resp)  throws IOException;
	
}
