package com.changestuffs.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.changestuffs.server.persistence.beans.Image;
import com.changestuffs.server.persistence.beans.Offer;
import com.changestuffs.server.persistence.beans.Product;
import com.changestuffs.server.persistence.beans.ProductOffered;
import com.changestuffs.server.persistence.beans.Tag;
import com.changestuffs.server.persistence.beans.User;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.GetOffersResult;
import com.changestuffs.shared.actions.GetOffersResult.OffersPerProduct;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.OfferRemoveResult;
import com.changestuffs.shared.actions.UpdateOfferResult;
import com.changestuffs.shared.constants.Locales;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.ArticlesDtoOut;
import com.changestuffs.shared.dto.IArticlesDto;
import com.changestuffs.shared.dto.IArticlesDto.Offers;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ArticlesOAM {

	private final Logger log = Logger.getLogger(getClass().getName());
	private final EntityManager model;

	@Inject 
	public ArticlesOAM(EntityManager model) {
		this.model = model;
	}

	@Transactional
	public Product insertArticle(ArticlesAddAction dto, String userId) {
		Product product = new Product();
		product.setDate(dto.getDate());
		product.setDescription(new Text(dto.getDescription()));
		product.setName(dto.getName());
		product.setTag(getTagBean(dto.getTag()));
		product.setUserId(userId);
		product.setInterestedIn(new Text(dto.getInterestedIn()));
		product.setOffer(new ArrayList<Offer>());
		product.setLocale(dto.getLanguage());
		model.persist(product);
		log.info("Product saved");
		return product;
	}

	@Transactional
	public Product updateArticle(ArticlesAddAction dto, String userId) {
		Product product = model.find(Product.class,
				KeyFactory.stringToKey(dto.getKeyHash()));
		if (userId.equals(product.getUserId())) {
			product.setDate(dto.getDate());
			product.setDescription(new Text(dto.getDescription()));
			product.setName(dto.getName());
			product.setInterestedIn(new Text(dto.getInterestedIn()));
			product.setLocale(dto.getLanguage());
			log.info("Updating product");
		} else {
			log.warning("User " + userId + " is not allowed to update product");
		}
		return product;
	}

	public Map<String, String> addImages(String userId, Map<String, Blob> images) {
		Map<String, String> keys = new HashMap<String, String>();
		for (Entry<String, Blob> entry : images.entrySet()) {
			Image image = new Image();
			image.setName(entry.getKey());
			image.setImage(entry.getValue());
			image.setUserId(userId);
			model.getTransaction().begin();
			model.persist(image);
			model.getTransaction().commit();
			keys.put(KeyFactory.keyToString(image.getId()), entry.getKey());
		}
		return keys;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProducts(String userId) {
		Query query = model.createNamedQuery(Product.GET_BY_USER_ID);
		query.setParameter("userId", userId);
		log.info("Query: " + query.toString());
		List<Product> products = new ArrayList<Product>((List<Product>) query.getResultList());
		log.info(userId + " has " + products.size() + " products");
		return products;
	}

	@Transactional
	public void remove(String keyHash, String userId) {
		Key key = KeyFactory.stringToKey(keyHash);
		log.info("Key: " + KeyFactory.keyToString(key) + " Kind: "
				+ key.getKind() + " Id: " + key.getId() + " Name: "
				+ key.getName() + " Namespace: " + key.getNamespace());
		Product product = model.find(Product.class, key);
		log.info("Product: " + product);
		if (product != null && userId.equals(product.getUserId())) {
			model.remove(product);
		} else {
			throw new IllegalAccessError("Illegal user");
		}
	}

	private Tag getTagBean(Tags tagEnum) {
		Tag tag = model.find(Tag.class, tagEnum.name());

		if (tag == null) {
			log.info("Creating new tag");
			tag = new Tag();
			tag.setTagId(tagEnum.name());
			model.persist(tag);
		}

		return tag;
	}

	public Map<String, IArticlesDto> getArticles(LookForAction input, String email) {
		Map<String, IArticlesDto> articles = new HashMap<String, IArticlesDto>();
		if (input.getTag() != null) {
			Tag tag = model.find(Tag.class, input.getTag().name());
			if (tag != null) {
				for (Product product : tag.getProducts()) {
					IArticlesDto dto = getIArticlesDto(product, false, email);
					articles.put(dto.getKeyHash(), dto);
				}
				log.info("Getting " + articles.size() + " articles for tag "
						+ input.getTag());
			}
		}
		if (input.getIdKey() != null) {
			Product product = model.find(Product.class, input.getIdKey());
			if (product != null) {
				IArticlesDto dto = getIArticlesDto(product, true, email);
				articles.put(dto.getKeyHash(), dto);
			}
		}
		return articles;
	}

	public IArticlesDto getIArticlesDto(Product product, boolean completeInfo, String email) {
		ArticlesDtoOut dto = new ArticlesDtoOut();
		dto.setName(product.getName());
		dto.setDate(product.getDate());
		dto.setTag(Tags.valueOf(product.getTag().getTagId()));
		dto.setKeyHash(KeyFactory.keyToString(product.getKey()));
		dto.setLocale(Locales.getLocale(product.getLocale()));
		if (completeInfo) {
			dto.setDescription(product.getDescription().getValue());
			dto.setInterestedIn(product.getInterestedIn().getValue());
			List<Offers> allOffers = new ArrayList<Offers>();
			if (product.getOffer() != null) {
				for (Offer offer : product.getOffer()) {
					Map<String, String> offerPerUser = new HashMap<String, String>();
					for (ProductOffered productOffered : offer.getProductOffered()) {
						String productId = productOffered.getProductId();
						Product offered = model.find(Product.class,
								KeyFactory.stringToKey(productId));
						// Maybe it was removed
						if(offered != null)
							offerPerUser.put(productId, offered.getName());
					}
					if(offerPerUser.size()>0){
						Offers offers = new Offers();
						offers.setIdNameProducts(offerPerUser);
						offers.setUserId(offer.getUserId());
						if(email != null){
							User user = model.find(User.class, email);
							offers.setFriend(user.getFriends().contains(offer.getUserId()));
						}
						allOffers.add(offers);
					}
				}
			}
			dto.setOffers(allOffers);
		}
		return dto;
	}

	public List<Offer> getOffers(String userId) {
		Query query = model.createNamedQuery(Offer.GET_BY_USER_ID);
		query.setParameter("userId", userId);
		log.info("Query: " + query.toString());
		@SuppressWarnings("unchecked")
		List<Offer> offers = new ArrayList<Offer>(
				(List<Offer>) query.getResultList());
		log.info(userId + " has " + offers.size() + " products");
		return offers;
	}

	public GetOffersResult createOffer(String productId, String userId) {
		GetOffersResult result = null;
		Product product = model.find(Product.class,
				KeyFactory.stringToKey(productId));
		Offer offer = new Offer();
		offer.setAcceptIt(false);
		offer.setProduct(product);
		offer.setProductOffered(new HashSet<ProductOffered>());
		offer.setUserId(userId);
		model.getTransaction().begin();
		model.persist(offer);
		model.getTransaction().commit();
		OffersPerProduct offerPerProduct = new OffersPerProduct(KeyFactory.keyToString(offer.getKey()), productId, product.getName(), new HashMap<String, String>());
		Map<String, OffersPerProduct> offers = new HashMap<String, OffersPerProduct>();
		offers.put(offerPerProduct.getOfferKey(), offerPerProduct);
		result = new GetOffersResult(offers);
		log.info("Offer created");
		return result;
	}
	
	@Transactional
	public UpdateOfferResult updateOffer(String userId, String offerId, Map<String,String> productIdNames){
		UpdateOfferResult result = null;
		Offer offer = model.find(Offer.class, KeyFactory.stringToKey(offerId));
		if(!userId.equals(offer.getUserId())){
			log.log(Level.WARNING, "User "+userId+" is trying to update an offer of "+offer.getUserId());
			return null;
		}
		Set<ProductOffered> offers = new HashSet<ProductOffered>();
		for(Entry<String,String> entry : productIdNames.entrySet()){
			ProductOffered productOffered = new ProductOffered();
			productOffered.setOffer(offer);
			productOffered.setProductId(entry.getKey());
			productOffered.setProductName(entry.getValue());
			offers.add(productOffered);
			model.persist(productOffered);
		}
		offer.setProductOffered(offers);
		result = new UpdateOfferResult(new OffersPerProduct(offerId, KeyFactory.keyToString(offer.getProduct().getKey()), offer.getProduct().getName(), productIdNames));
		log.info("Offer updated");
		return result;
	}
	
	public OfferInfo getOwner(String offerId){
		Offer offer = model.find(Offer.class, KeyFactory.stringToKey(offerId));
		User user = model.find(User.class, offer.getProduct().getUserId());
		return new OfferInfo(user, offer.getProduct().getName(), KeyFactory.keyToString(offer.getProduct().getKey()));
	}
	
	@Transactional
	public OfferRemoveResult removeOffer(String userId, String offerId){
		OfferRemoveResult result = null;
		Offer offer = model.find(Offer.class, offerId);
		if(!userId.equals(offer.getUserId())){
			log.log(Level.WARNING, "User "+userId+" is trying to remove an offer of "+offer.getUserId());
			return null;
		}
		model.remove(offer);
		result = new OfferRemoveResult();
		return result;
	}
	
	public class OfferInfo{
		private final User user;
		private final String productName;
		private final String productId;
		public OfferInfo(User user, String productName, String productId) {
			super();
			this.user = user;
			this.productName = productName;
			this.productId = productId;
		}
		public User getUser() {
			return user;
		}
		public String getProductName() {
			return productName;
		}
		public String getProductId() {
			return productId;
		}
	}

}
