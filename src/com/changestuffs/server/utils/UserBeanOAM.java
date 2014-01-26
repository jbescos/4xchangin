package com.changestuffs.server.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.changestuffs.server.persistence.beans.Login;
import com.changestuffs.server.persistence.beans.Online;
import com.changestuffs.server.persistence.beans.User;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.changestuffs.shared.actions.InfoLoginResult;
import com.changestuffs.shared.actions.UpdateUserInfo;
import com.google.inject.persist.Transactional;

public class UserBeanOAM {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	private final EntityManager model;
	public final static String AUTH_DOMAIN_NOT_REGISTERED = "not registered yet";
	
	@Inject
	public UserBeanOAM(EntityManager model){
		this.model=model;
	}
	
	@Transactional
	public InfoLoginResult persistUser(Date dateIn, com.google.appengine.api.users.User user, String ip, String pendingFriend){
		InfoLoginResult result = null;
		Login login = new Login();
		login.setIp(ip);
		login.setLoginDate(dateIn);
		log.info("Looking for id: "+user.getEmail());
        User userBean = model.find(User.class, user.getEmail());
        if(userBean != null){
        	if(AUTH_DOMAIN_NOT_REGISTERED.equals(userBean.getAuthDomain()))
        		setUserOpenIdValues(userBean, user);
        	Date date = userBean.getLogins().get(userBean.getLogins().size()-1).getLoginDate();
        	if(pendingFriend != null)
        		userBean.getPendingFriends().add(pendingFriend);
        	login.setUser(userBean);
        	model.persist(login);
        	log.info("Update user: "+user.getEmail()+" with date "+dateIn);
        	result = new InfoLoginResult(user.getEmail(), date, false, userBean.isReceiveEmails(), userBean.getUserId());
        }else{
        	List<Login> logins = new ArrayList<Login>();
        	userBean = new User();
        	setUserOpenIdValues(userBean, user);
        	userBean.setReceiveEmails(true);
        	userBean.setFriends(new HashSet<String>());
        	userBean.setPendingFriends(new HashSet<String>());
        	if(pendingFriend != null)
        		userBean.getPendingFriends().add(pendingFriend);
        	logins.add(login);
        	userBean.setLogins(logins);
        	model.persist(userBean);
        	log.info("Creating user: "+user.getEmail());
        	result = new InfoLoginResult(user.getEmail(), login.getLoginDate(), true, userBean.isReceiveEmails(), userBean.getUserId());
        }
        log.info("User "+userBean);
		log.info("Returning "+result); 
		return result;
	}
	
	private void setUserOpenIdValues(User userBean, com.google.appengine.api.users.User user){
		userBean.setUserId(user.getUserId());
    	userBean.setEmail(user.getEmail());
    	userBean.setAuthDomain(user.getAuthDomain());
    	userBean.setFederatedIdentity(user.getFederatedIdentity());
    	userBean.setNickName(user.getNickname());
	}
	
	@Transactional
	public void updateUser(UpdateUserInfo action, String email){
		User userBean = model.find(User.class, email);
		userBean.setCell(action.getCell());
		userBean.setCity(action.getCity());
		userBean.setCountry(action.getCountry());
		userBean.setReceiveEmails(action.isReceiveEmails());
	}
	
	@Transactional
	public void updateOnline(boolean online, String email){
		Online bean = model.find(Online.class, email);
		if(bean != null){
			bean.setOnline(online);
		}else{
			bean = new Online();
			bean.setEmail(email);
			bean.setOnline(online);
			model.persist(bean);
		}
	}
	
	@Transactional
	public String updateNotify(String token, boolean receiveEmails){
		Query query = model.createNamedQuery(User.GET_USER_BY_USER_ID);
		query.setParameter("userId", token);
		log.info("Query: " + query.toString());
		User user = (User) query.getSingleResult();
		user.setReceiveEmails(receiveEmails);
		return user.getEmail();
	}
	
	@Transactional
	public boolean addFriend(String email, String newFriend){
		User userBean = model.find(User.class, email);
		int sizeBefore = userBean.getFriends().size();
		userBean.getFriends().add(newFriend);
		if(userBean.getPendingFriends().contains(newFriend))
			userBean.getPendingFriends().remove(newFriend);
		int sizeAfter = userBean.getFriends().size();
		
		if(sizeBefore == sizeAfter)
			return true;
		else
			return false;
	}
	
	@Transactional
	public void removeContact(String email, String contactEmail, boolean pending){
		User user = model.find(User.class, email);
		if(pending)
			user.getPendingFriends().remove(contactEmail);
		else
			user.getFriends().remove(contactEmail);
	}
	
	public GetUserInfoResult getGetUserInfoResult(String email){
		User userBean = model.find(User.class, email);
		if(userBean != null){
			Set<String> friends = getSecureSet(userBean.getFriends());
			Set<String> prending = getSecureSet(userBean.getPendingFriends());
			return new GetUserInfoResult(userBean.getCell(), userBean.getCountry(), userBean.getCity(), email, userBean.isReceiveEmails(), friends, prending, getOnline(friends));
		}else{
			// It's maybe the first time, and DB is slow so it can be null
			Set<String> empty = new HashSet<String>();
			return new GetUserInfoResult("", "", "", email, true, empty, empty, empty);
		}
	}
	
	private Set<String> getSecureSet(Set<String> set){
		Set<String> secure = new HashSet<String>();
		if(set != null)
			secure.addAll(set);
		return secure;
	}
	
	public Set<String> getOnline(Set<String> contacts){
		Set<String> onlines = new HashSet<String>();
		for(String contact:contacts){
			Online online = model.find(Online.class, contact);
			if(online != null && online.isOnline()){
				onlines.add(contact);
			}
		}
		return onlines;
	}

}
