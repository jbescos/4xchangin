package com.changestuffs.server.persistence.beans;

import java.util.Date;
import java.util.List;

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
import com.google.appengine.api.datastore.Text;

@Entity
@NamedQueries({
		@NamedQuery(name = Product.GET_BY_USER_ID, query = "select x from Product x where x.userId = :userId")})
public class Product {

	@Transient
	public static final String GET_BY_USER_ID = "getProductByUserId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	@Column
	@Index
	private String userId;

	@ManyToOne
	private Tag tag;

	@Column
	private String name;

	@Column
	private Text description;
	
	@Column
	private Text interestedIn;

	@Column
	private Date date;
	
	@Column
	private String locale;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Offer> offer;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Text getInterestedIn() {
		return interestedIn;
	}

	public void setInterestedIn(Text interestedIn) {
		this.interestedIn = interestedIn;
	}

	public List<Offer> getOffer() {
		return offer;
	}

	public void setOffer(List<Offer> offer) {
		this.offer = offer;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
