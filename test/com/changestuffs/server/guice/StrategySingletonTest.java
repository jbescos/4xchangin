package com.changestuffs.server.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.changestuffs.server.guice.aspect.LoguedTest;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;

public class StrategySingletonTest {

	private static final Logger log = Logger.getLogger(StrategySingletonTest.class.getName());
	private final Injector injector = Guice.createInjector(new MyModule());
	private final static String PARAM_1 = "paramDto1";
	private final static String PARAM_2 = "paramDto2";
	
	@Test
	public void test(){
		Logic<?> logicDto1 = injector.getInstance(LogicDto1.class);
		logicDto1.watchDto();
		
		// Comprobamos que aunque son singletons, los DTO's que instancian son diferentes
		assertNotSame(logicDto1.getInstance(), logicDto1.getInstance());
		
		Logic<?> logicDto1SameInstance = injector.getInstance(LogicDto1.class);
		
		// Comprobamos que son la misma instancia
		assertEquals(logicDto1, logicDto1SameInstance);
		
		
		Logic<?> logicDto2 = injector.getInstance(LogicDto2.class);
		logicDto2.watchDto();
		
	}
	
	/*
	 * Dto que manejara la implementacion LogicDto1
	 */
	private static class Dto1{
		private final String field;
		public Dto1(String field) {
			this.field = field;
		}
	}
	
	/*
	 * Dto que manejara la implementacion LogicDto2
	 */
	private static class Dto2{
		private final int integer;
		public Dto2(int integer) {
			this.integer = integer;
		}
	}
	
	/*
	 * Definimos la forma de instanciarse Dto1
	 */
	private static class Dto1Provider implements Provider<Dto1>{

		private final HttpServletRequest request;
		
		@Inject
		public Dto1Provider(HttpServletRequest request){
			this.request=request;
		}
		
		@Override
		public Dto1 get() {
			return new Dto1(request.getParameter(PARAM_1));
		}
		
	}
	
	/*
	 * Definimos la forma de instanciarse Dto2
	 */
	private static class Dto2Provider implements Provider<Dto2>{

		private final HttpServletRequest request;
		
		@Inject
		public Dto2Provider(HttpServletRequest request){
			this.request=request;
		}
		
		@Override
		public Dto2 get() {
			return new Dto2(Integer.parseInt(request.getParameter(PARAM_2)));
		}
		
	}
	
	/*
	 * Decimos que los DTOs se consiguen a traves de los providers y
	 * introducimos las implementaciones de las interfaces
	 * para que quice sepa que existen
	 */
	private static class MyModule extends AbstractModule{

		@Override
		protected void configure() {
			bind(Dto1.class).toProvider(Dto1Provider.class);
			bind(Dto2.class).toProvider(Dto2Provider.class);
			bind(LogicDto1.class);
			bind(LogicDto2.class);
		}
		
		/*
		 * Devuelve un mock de HttpServletRequest, que es con lo que
		 * formaremos los DTOs
		 */
		@Provides
		public HttpServletRequest getRequest(){
			HttpServletRequest request = mock(HttpServletRequest.class);
			when(request.getParameter(PARAM_1)).thenReturn("Jorge");
			when(request.getParameter(PARAM_2)).thenReturn("28");
			return request;
		}
		
	}
	
	private static interface Logic<DTO> {
		void watchDto();
		DTO getInstance();
	}
	
	@Singleton
	private static class LogicDto1 implements Logic<Dto1>{

		@Inject
		private Provider<Dto1> provider;
		
		@Override
		public void watchDto() {
			Dto1 dto = getInstance();
			log.info("I'm "+dto+" and my name is: "+dto.field);
		}

		@Override
		public Dto1 getInstance() {
			return provider.get();
		}
		
	}
	
	@Singleton
	private static class LogicDto2 implements Logic<Dto2>{

		@Inject
		private Provider<Dto2> provider;
		
		@Override
		public void watchDto() {
			Dto2 dto = getInstance();
			log.info("I'm "+dto+" and my age is: "+dto.integer);
		}

		@Override
		public Dto2 getInstance() {
			return provider.get();
		}
		
	}
	
}
