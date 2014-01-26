package com.changestuffs.shared.dto;

import java.util.Date;
import java.util.List;

import com.changestuffs.shared.constants.Locales;
import com.changestuffs.shared.constants.Tags;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ArticlesDtoOut implements IArticlesDto, IsSerializable{

	private String name;
	private String keyHash;
	private String description;
	private String interestedIn;
	private Tags tag;
	private Date date;
	private Locales locale;
	private List<Offers> offers;
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Tags getTag() {
		return tag;
	}
	public void setTag(Tags tag) {
		this.tag = tag;
	}
	@Override
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getKeyHash() {
		return keyHash;
	}
	public void setKeyHash(String keyHash) {
		this.keyHash = keyHash;
	}
	@Override
	public String getInterestedIn() {
		return interestedIn;
	}
	public void setInterestedIn(String interestedIn) {
		this.interestedIn = interestedIn;
	}
	@Override
	public List<Offers> getOffers() {
		return offers;
	}
	public void setOffers(List<Offers> offers) {
		this.offers = offers;
	}
	@Override
	public Locales getLocale() {
		return locale;
	}
	public void setLocale(Locales locale) {
		this.locale = locale;
	}
	
}
