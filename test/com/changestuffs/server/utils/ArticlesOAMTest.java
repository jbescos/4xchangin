package com.changestuffs.server.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.changestuffs.server.guice.MyModule;
import com.changestuffs.server.persistence.beans.Product;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ArticlesOAMTest {

	private final Logger log = Logger.getLogger(getClass().getName());
	private LocalServiceTestHelper helper;
	private final static Injector injector = Guice.createInjector(new MyModule(), new JpaPersistModule("appEngine"));
	private ArticlesOAM oam;
	private EntityManager appEntity;
	
	private final Date date = new Date();
	private final String name = "name";
	private final String interestedIn = "interestedIn";
	private final String description = "description";
	private final String userId = "user@dsad.com";
	private final Tags tag = Tags.electronic;
	
	@BeforeClass
	public static void beforeClass(){
		PersistService service = injector.getInstance(PersistService.class);
		service.start();
	}
	
	@Before
	public void before(){
		log.info("starting test");
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
		
		oam = injector.getInstance(ArticlesOAM.class);
		appEntity = injector.getInstance(EntityManager.class);
	}
	
	@After
	public void after(){
		log.info("finish test");
		helper.tearDown();
	}
	
	@Test
	public void testAll(){
		
		ArticlesAddAction dto = createArticlesDtoIn(tag);
		checksInsert(dto);
		checksGetProductAndRemove();
		
	}
	
	@Test
	public void testGetArticles(){	
		ArticlesAddAction dto1 = createArticlesDtoIn(tag);
		Product product1 = oam.insertArticle(dto1, userId);
		
		ArticlesAddAction dto2 = createArticlesDtoIn(Tags.entertainment);
		Product product2 = oam.insertArticle(dto2, userId);
		
		LookForAction action1 = new LookForAction(dto1.getTag(), null);
		Map<String, IArticlesDto> articles1 = oam.getArticles(action1, null);
		assertEquals(1, articles1.size());
		String key1 = KeyFactory.keyToString(product1.getKey());
		assertTrue(articles1.containsKey(key1));
		assertEquals(dto1.getTag(), articles1.get(key1).getTag());
		
		LookForAction action2 = new LookForAction(null, KeyFactory.keyToString(product2.getKey()));
		Map<String, IArticlesDto> articles2 = oam.getArticles(action2, null);
		assertEquals(1, articles2.size());
		String key2 = KeyFactory.keyToString(product2.getKey());
		assertTrue(articles2.containsKey(key2));
		assertEquals(dto2.getTag(), articles2.get(key2).getTag());
	}
	
	@Test
	public void addImages(){
		Map<String, Blob> images = new HashMap<String, Blob>();
		Blob blob1 = new Blob(new byte[30]);
		Blob blob2 = new Blob(new byte[80]);
		images.put("blob1", blob1);
		images.put("blob2", blob2);
		Map<String, String> keys = oam.addImages(userId, images);
		assertEquals(2, keys.size());
	}
	
	private ArticlesAddAction createArticlesDtoIn(Tags tag){
		return new ArticlesAddAction(tag, name, description, date, interestedIn, null);
	}
	
	private void checksGetProductAndRemove(){
		List<Product> products = oam.getProducts(userId);
		assertNotNull(products);
		assertEquals(1, products.size());
		String keyHash = KeyFactory.keyToString(products.get(0).getKey());
		log.info("Key test hash: "+keyHash);
		oam.remove(keyHash, userId);
		products = oam.getProducts(userId);
		assertNotNull(products);
		assertEquals(0, products.size());
	}
	
	private void checksInsert(ArticlesAddAction dto){
		Product productNotDb = oam.insertArticle(dto, userId);
		assertNotNull(productNotDb.getKey());
		Product product = lookUpProduct(productNotDb.getKey());
		assertEquals(tag.name(), product.getTag().getTagId());
		assertEquals(description, product.getDescription().getValue());
		assertEquals(name, product.getName());
		assertEquals(userId, product.getUserId());
		assertEquals(date, product.getDate());
		
		ArticlesAddAction forUpdate = new ArticlesAddAction(Tags.clothes, "anyName", "anyDescription", new Date(), KeyFactory.keyToString(product.getKey()), "interest", null);
		Product productUpdatedNoDb = oam.updateArticle(forUpdate, userId);
		Product productUpdated = lookUpProduct(productUpdatedNoDb.getKey());
		assertNotNull(productUpdated);
		assertEquals(forUpdate.getName(), productUpdated.getName());
		assertEquals(forUpdate.getDate(), productUpdated.getDate());
		assertEquals(forUpdate.getDescription(), productUpdated.getDescription().getValue());
	}
	
	private Product lookUpProduct(Key key){
		return appEntity.find(Product.class, key);
	}
	
}
