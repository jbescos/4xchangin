package com.changestuffs.shared.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.bval.guice.ValidationModule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.changestuffs.server.actionhandlers.ArticlesAddActionHandler;
import com.changestuffs.server.guice.AopModule;
import com.changestuffs.server.guice.ServerModule;
import com.changestuffs.server.guice.aspect.LoguedInterceptor;
import com.changestuffs.shared.constants.Tags;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class BeanValidatorTest {

	private Injector injector;
	private Validator validator;
	private final static Logger log = Logger.getLogger(BeanValidatorTest.class.getName());
	@Mock
	private LoguedInterceptor loguedInterceptor;
	
	@BeforeClass
	public static void beforeClass(){
		for(Field field: ArticlesAddAction.class.getDeclaredFields()){
			log.info("Field "+field+" with annotations: ");
			for(Annotation annotation : field.getAnnotations()){
				log.info(annotation.toString());
			}
		}
	}
	
	@Before
	public void before() throws Throwable{
		MockitoAnnotations.initMocks(this);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				MethodInvocation methodInvocation = (MethodInvocation) invocation.getArguments()[0];
				return methodInvocation.proceed();
			}
		}).when(loguedInterceptor).invoke(any(MethodInvocation.class));
		AopModule aop = new AopModule();
		aop.setLoguedInterceptor(loguedInterceptor);
		injector = Guice.createInjector(new ValidationModule(), new ServerModule(), new Module(), aop, new JpaPersistModule("appEngine"));
		validator = injector.getInstance(Validator.class);
	}
	
	@Test
	public void articlesAddActionPattern(){
		log.info("Validator: "+validator);
		ArticlesAddAction action = new ArticlesAddAction(Tags.electronic, "", "description", new Date(), "interestedIn", "es");
		Set<ConstraintViolation<ArticlesAddAction>> violations = validator.validate(action);
		assertEquals("Violations: "+violations, 1, violations.size());
	}
	
	@Test
	public void articlesAddActionNull(){
		ArticlesAddAction action = new ArticlesAddAction(null, null, "description", new Date(), "interestedIn", "es");
		 Set<ConstraintViolation<ArticlesAddAction>> violations = validator.validate(action);
		 assertEquals("Violations: "+violations, 1, violations.size());
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void articlesActionHandler() throws ActionException{
		ArticlesAddActionHandler handler = injector.getInstance(ArticlesAddActionHandler.class);
		handler.execute(new ArticlesAddAction(null, null, "description", new Date(), "interestedIn", "es"), mock(ExecutionContext.class));
	}
	
	private static class Module extends AbstractModule{

		@Override
		protected void configure() {
			bind(HttpServletRequest.class).toInstance(mock(HttpServletRequest.class));
		}
		
	}
	
}
