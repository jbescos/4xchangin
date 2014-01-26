package com.changestuffs.server.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.changestuffs.server.guice.MyModule;
import com.changestuffs.server.persistence.beans.User;
import com.changestuffs.shared.actions.InfoLoginResult;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class UserBeanOAMTest {

	private final Logger log = Logger.getLogger(getClass().getName());
	private LocalServiceTestHelper helper;
	private final com.google.appengine.api.users.User user=new com.google.appengine.api.users.User("test@test.com", "domain");
	private final String ip="192.1.1.1";
	private UserBeanOAM oam;
	private EntityManager entity;
	private Injector injector;
	private PersistService service;
	
	@Before
	public void before(){
		log.info("starting test");
		
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
		
		injector = Guice.createInjector(new MyModule(), new JpaPersistModule("appEngine"));
		service = injector.getInstance(PersistService.class);
		assertNotNull(service);
		service.start();
		entity = injector.getInstance(EntityManager.class);
		oam = injector.getInstance(UserBeanOAM.class);
	}
	
	@After
	public void after(){
		log.info("finish test");
		entity.close();
		service.stop();
		
		helper.tearDown();
	}
	
	@Test
	public void ok(){
		Date firstDate = new Date(0);
		Date secondDate = new Date(1000);
		Date thirdDate = new Date(5000);
		
		log.info("First insert");
		InfoLoginResult result1 = oam.persistUser(firstDate, user, ip, null);
		assertEquals(true, result1.isFirstTime());
		assertEquals(firstDate, result1.getDate());
		User user = getUser();
		log.info("User(1): "+user);
		assertEquals(user.getEmail(), user.getEmail());
		assertEquals(1, user.getLogins().size());
		assertEquals(firstDate, user.getLogins().get(0).getLoginDate());
		
		
		log.info("Second insert");
		InfoLoginResult result2 = oam.persistUser(secondDate, this.user, ip, null);
		assertEquals(false, result2.isFirstTime());
		assertEquals(firstDate, result2.getDate());
		user = getUser();
		log.info("User(2): "+user);
		assertEquals(user.getEmail(), user.getEmail());
		assertEquals(2, user.getLogins().size());
		assertEquals(firstDate, user.getLogins().get(0).getLoginDate());
		assertEquals(secondDate, user.getLogins().get(1).getLoginDate());
		
		log.info("Third insert");
		InfoLoginResult result3 = oam.persistUser(thirdDate, this.user, ip, null);
		assertEquals(false, result3.isFirstTime());
		assertEquals(secondDate, result3.getDate());
		user = getUser();
		log.info("User(3): "+user);
		assertEquals(user.getEmail(), user.getEmail());
		assertEquals(3, user.getLogins().size());
		assertEquals(firstDate, user.getLogins().get(0).getLoginDate());
		assertEquals(secondDate, user.getLogins().get(1).getLoginDate());
		assertEquals(thirdDate, user.getLogins().get(2).getLoginDate());
	}
	
	private User getUser(){
		entity.getTransaction().begin();
		User user = entity.find(User.class, this.user.getEmail());
		assertNotNull(user);
		assertNotNull(user.getLogins());
		log.info("Number of logins "+user.getLogins().size());
		entity.getTransaction().commit();
		return user;
	}
	
}
