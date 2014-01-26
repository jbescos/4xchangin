package com.changestuffs.server.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.changestuffs.server.guice.DispatchServletModule;
import com.changestuffs.server.guice.MyModule;
import com.changestuffs.server.servlets.impl.DownloadImageImpl;
import com.changestuffs.server.servlets.impl.FileUploadImpl;
import com.changestuffs.server.servlets.impl.IServletManager;
import com.changestuffs.shared.constants.ServletPaths;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FileManagerTest {

	@Mock
	private HttpServletRequest request;
	private Injector injector;
	private FrontServlet fileManager;
	
	private final String DOWNLOAD_PATH = ServletPaths.downloadImages.getPath();
	private final String UPLOAD_PATH = ServletPaths.uploadImages.getPath();
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		injector = Guice.createInjector(new MyModule(), new DispatchServletModule());
		fileManager = injector.getInstance(FrontServlet.class);
	}
	
	@Test
	public void downloadClass(){
		when(request.getServletPath()).thenReturn(DOWNLOAD_PATH);
		IServletManager manager = fileManager.getManager(request);
		assertEquals(DownloadImageImpl.class, manager.getClass());
	}
	
	@Test
	public void uploadClass(){
		when(request.getServletPath()).thenReturn(UPLOAD_PATH);
		IServletManager manager = fileManager.getManager(request);
		// Class is enhanced by Guice, because it has AOP
		assertTrue(manager.getClass().getName().contains(FileUploadImpl.class.getName()));
	}
	
}
