package com.changestuffs.server.guice;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.changestuffs.server.utils.ArticlesOAM;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class GuiceTests {

	private final Injector injector = Guice.createInjector(new Module2(), new Module1());
	private final static String NAME_VALUE = "name";
	
	@Test
	public void differentFields(){
		InterfaceModule1 interface1 = injector.getInstance(InterfaceModule1.class);
		assertEquals(NAME_VALUE, interface1.getName());
		InterfaceModule2 interface2 = injector.getInstance(InterfaceModule2.class);
		assertEquals(NAME_VALUE, interface2.getName());
	}
	
	@Test
	public void differentInstances(){
		Injector injector = Guice.createInjector(new MyModule(), new JpaPersistModule("appEngine"));
		PersistService service = injector.getInstance(PersistService.class);
		service.start();
		
		ArticlesOAM oam1 = injector.getInstance(ArticlesOAM.class);
		ArticlesOAM oam2 = injector.getInstance(ArticlesOAM.class);
		assertNotSame(oam1, oam2);
		
		service.stop();
	}
	
	private class Module1 extends AbstractModule{

		@Override
		protected void configure() {
			bind(InterfaceModule1.class).to(ImplInterface1.class);
			bind(String.class).toInstance(NAME_VALUE);
		}
		
	}
	
	private class Module2 extends AbstractModule{

		@Override
		protected void configure() {
			bind(InterfaceModule2.class).to(ImplInterface2.class);
		}
		
	}
	
	private static interface InterfaceModule1{
		String getName();
	}
	private static interface InterfaceModule2{
		String getName();
	}
	
	private static class ImplInterface1 implements InterfaceModule1{
		@Inject String name;

		@Override
		public String getName() {
			return name;
		}
	}
	private static class ImplInterface2 implements InterfaceModule2{
		@Inject String name;

		@Override
		public String getName() {
			return name;
		}
	}
	
}
