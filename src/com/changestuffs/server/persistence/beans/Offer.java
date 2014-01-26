package com.changestuffs.server.persistence.beans;

import java.util.Set;

import javax.jdo.annotations.Index;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;

@Entity
@NamedQueries({
	@NamedQuery(name = Offer.GET_BY_USER_ID, query = "select x from Offer x where x.userId = :userId")})
public class Offer {

	@Transient
	public static final String GET_BY_USER_ID = "getOffersByUserId";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	@ManyToOne
	private Product product;
	
	@Index
	@Column
	private String userId;
	
	@Column
	private boolean acceptIt;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "offer")
    private Set<ProductOffered> productOffered;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isAcceptIt() {
		return acceptIt;
	}

	public void setAcceptIt(boolean acceptIt) {
		this.acceptIt = acceptIt;
	}

	public Set<ProductOffered> getProductOffered() {
		return productOffered;
	}

	public void setProductOffered(Set<ProductOffered> productOffered) {
		this.productOffered = productOffered;
	}
	
}
