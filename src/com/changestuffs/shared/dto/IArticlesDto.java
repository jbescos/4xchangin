package com.changestuffs.shared.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.changestuffs.shared.constants.Locales;
import com.changestuffs.shared.constants.Tags;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface IArticlesDto{

	public String getName();
	public String getDescription();
	public String getInterestedIn();
	public Locales getLocale();
	public Date getDate();
	public Tags getTag();
	public String getKeyHash();
	public List<Offers> getOffers();
	
	public static class Offers implements IsSerializable{
		private String userId;
		private boolean friend;
		private Map<String,String> idNameProducts;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Map<String, String> getIdNameProducts() {
			return idNameProducts;
		}
		public void setIdNameProducts(Map<String, String> idNameProducts) {
			this.idNameProducts = idNameProducts;
		}
		public boolean isFriend() {
			return friend;
		}
		public void setFriend(boolean friend) {
			this.friend = friend;
		}
		
	}
	
}
