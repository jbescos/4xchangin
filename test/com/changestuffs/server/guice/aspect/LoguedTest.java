package com.changestuffs.server.guice.aspect;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class LoguedTest {

	private static final Logger log = Logger.getLogger(LoguedTest.class.getName());

	private static boolean methodInvoked;
	@Spy
	private LoguedInterceptor interceptor = new LoguedInterceptor();

	private Injector injector;
	
	@Before
	public void before(){
		methodInvoked = false;
		MockitoAnnotations.initMocks(this);
		
		injector = Guice.createInjector(new TestModule());

	}
	
	@Test
	public void needLogingAndItIs(){
		log.info("needLogingAndItIs begin");
		SomeAction action = injector.getInstance(SomeAction.class);
		doReturn(true).when(interceptor).isLogued();
		action.needLogin();
		assertTrue(methodInvoked);
	}
	
	@Test
	public void needLogingAndItIsNot(){
		log.info("needLogingAndItIsNot begin");
		SomeAction action = injector.getInstance(SomeAction.class);
		doReturn(false).when(interceptor).isLogued();
		try{
			action.needLogin();
			fail();
		}catch(Exception e){}
		assertFalse(methodInvoked);
	}
	
	@Test
	public void dontNeedLoging(){
		log.info("dontNeedLoging begin");
		SomeAction action = injector.getInstance(SomeAction.class);
		doReturn(false).when(interceptor).isLogued();
		action.dontNeedLogin();
		assertTrue(methodInvoked);
	}
	
	private interface SomeAction{
		
		@Logued
		public void needLogin();
		
		public void dontNeedLogin();
		
	}
	
	private static class SomeActionImpl implements SomeAction{

		@SuppressWarnings("unused")
		public SomeActionImpl(){}
		
		@Override
		@Logued
		public void needLogin() {
			methodInvoked = true;
		}

		@Override
		public void dontNeedLogin() {
			methodInvoked = true;
		}
		
	}
	
	private class TestModule extends AbstractModule{
		
		@Override
		protected void configure() {
			bind(SomeAction.class).to(SomeActionImpl.class);
			bindInterceptor(any(), annotatedWith(Logued.class), interceptor);
		}

	}
	
}
