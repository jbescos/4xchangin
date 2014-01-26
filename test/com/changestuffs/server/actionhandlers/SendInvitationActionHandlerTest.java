package com.changestuffs.server.actionhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.bval.guice.ValidationModule;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.changestuffs.server.guice.AopModule;
import com.changestuffs.server.guice.MyModule;
import com.changestuffs.server.guice.ServerModule;
import com.changestuffs.server.persistence.beans.Login;
import com.changestuffs.server.persistence.beans.User;
import com.changestuffs.server.servlets.impl.RedirectImpl;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.SendInvitation;
import com.changestuffs.shared.actions.SendInvitationResult;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SendInvitationActionHandlerTest {

	private final static Injector injector = Guice.createInjector(
			new MyModule(), new JpaPersistModule("appEngine"),
			new ServerModule(), new ModuleTest(), new AopModule(),
			new ValidationModule());
	private LocalServiceTestHelper helper;
	private EntityManager appEntity;
	private final String email = "ravenskater@gmail.com";
	private final String authDomain = "anyDomain";

	@BeforeClass
	public static void beforeClass() {
		PersistService service = injector.getInstance(PersistService.class);
		service.start();
	}

	@Before
	public void before() {
		helper = new LocalServiceTestHelper(
				new LocalDatastoreServiceTestConfig(),
				new LocalMailServiceTestConfig().setLogMailBody(true),
				new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true)
				.setEnvEmail(email).setEnvAuthDomain(authDomain);
		helper.setUp();

		appEntity = injector.getInstance(EntityManager.class);
	}

	@After
	public void after() {
		helper.tearDown();
	}

	@Test(expected = ConstraintViolationException.class)
	public void notAllowedEmail() throws ActionException {
		SendInvitationActionHandler handler = injector
				.getInstance(SendInvitationActionHandler.class);
		handler.execute(new SendInvitation("dsfdsf.com@"),
				mock(ExecutionContext.class));
	}

	@Test
	public void inviteNotRegistered() throws ActionException {
		final String invite = "pablo@gmail.com";
		SendInvitationActionHandler handler = injector.getInstance(SendInvitationActionHandler.class);
		SendInvitationResult result = handler.execute(new SendInvitation(invite), mock(ExecutionContext.class));
		assertEquals(true, result.isSent());
		User pablo = appEntity.find(User.class, invite);
		assertNotNull(pablo);
		assertNotNull(pablo.getUserId());
		assertEquals(UserBeanOAM.AUTH_DOMAIN_NOT_REGISTERED, pablo.getAuthDomain());
		assertEquals(true, pablo.isReceiveEmails());
		assertEquals(1, pablo.getPendingFriends().size());
		assertTrue("Pending friends = "+pablo.getPendingFriends(), pablo.getPendingFriends().contains(email));
		assertEquals(1, pablo.getLogins().size());
	}
	
	@Test
	public void inviteAndRegister() throws ActionException, IOException{
		final String ravenWasInvitedBy = "the@owner.com";
		UserBeanOAM oam = injector.getInstance(UserBeanOAM.class);
		com.google.appengine.api.users.User user = new com.google.appengine.api.users.User(email, UserBeanOAM.AUTH_DOMAIN_NOT_REGISTERED, "anyUserId");
		oam.persistUser(new Date(0), user, ravenWasInvitedBy, ravenWasInvitedBy);
		
		
		RedirectImpl register = injector.getInstance(RedirectImpl.class);
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameterMap()).thenReturn(new HashMap<String,String[]>());
		when(req.getRequestURL()).thenReturn(new StringBuffer("changestuffs/anyuri"));
		when(req.getRequestURI()).thenReturn("anyuri");
		
		register.manage(req, mock(HttpServletResponse.class));
		User fromDb = appEntity.find(User.class, user.getEmail());
		assertEquals(authDomain, fromDb.getAuthDomain());
		assertEquals(true, fromDb.isReceiveEmails());
		assertNotSame(user.getUserId(), fromDb.getUserId());
		assertEquals(1, fromDb.getPendingFriends().size());
		assertEquals(true, fromDb.getPendingFriends().contains(ravenWasInvitedBy));
		assertEquals(2, fromDb.getLogins().size());
		assertEquals(0, fromDb.getFriends().size());
	}

	@Test
	public void inviteRegisteredNotifyActivated() throws ActionException {
		registeredUserChecks(true);
	}
	
	@Test
	public void inviteRegisteredNotifyNoActivated() throws ActionException {
		registeredUserChecks(false);
	}
	
	private void registeredUserChecks(boolean receiveEmails) throws ActionException{
		User registered = persistUser(receiveEmails);
		SendInvitationActionHandler handler = injector.getInstance(SendInvitationActionHandler.class);
		SendInvitationResult result = handler.execute(new SendInvitation(registered.getEmail()), mock(ExecutionContext.class));
		assertEquals(true, result.isSent());
		User fromDb = appEntity.find(User.class, registered.getEmail());
		assertNotNull(fromDb);
		assertEquals(registered.getUserId(), fromDb.getUserId());
		assertEquals(registered.getAuthDomain(), fromDb.getAuthDomain());
		assertEquals(receiveEmails, fromDb.isReceiveEmails());
		assertEquals(1, fromDb.getPendingFriends().size());
		assertTrue("Pending friends = "+fromDb.getPendingFriends(), fromDb.getPendingFriends().contains(email));
		assertEquals(registered.getLogins().size()+1, fromDb.getLogins().size());
	}
	
	private User persistUser(boolean receiveEmails){
		final String alreadyRegistered = "im@registered.com";
		User registeredUser = new User();
		registeredUser.setAuthDomain("registered.com");
		registeredUser.setEmail(alreadyRegistered);
		registeredUser.setUserId("userId");
		registeredUser.setReceiveEmails(receiveEmails);
		registeredUser.setPendingFriends(new HashSet<String>());
		registeredUser.setFriends(new HashSet<String>());
		registeredUser.setLogins(Arrays.asList(new Login()));
		appEntity.getTransaction().begin();
		appEntity.persist(registeredUser);
		appEntity.getTransaction().commit();
		return registeredUser;
	}

	private static class ModuleTest extends AbstractModule {

		@Override
		protected void configure() {
			HttpServletRequest request = mock(HttpServletRequest.class);
			bind(HttpServletRequest.class).toInstance(request);
			when(request.getLocale()).thenReturn(new Locale("es"));
		}

	}

}
