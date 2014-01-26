package com.changestuffs.server.persistence.beans;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@NamedQueries({
	@NamedQuery(name = User.GET_USER_BY_USER_ID, query = "select x from User x where x.userId = :userId")})
public class User {

	@Transient
	public static final String GET_USER_BY_USER_ID = "getUserByUserId";
	@Id
	private String email;
	@Column
	private String authDomain;
	@Column
	private String federatedIdentity;
	@Column
	private String nickName;
	@Column
	private String userId;
	@Column
	private String cell;
	@Column
	private String country;
	@Column
	private String city;
	@Column
	private boolean receiveEmails;
	@Column
	private Set<String> friends;
	@Column
	private Set<String> pendingFriends;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Login> logins;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Login> getLogins() {
		return logins;
	}
	public void setLogins(List<Login> logins) {
		this.logins = logins;
	}
	public void addLogin(Login login){
		logins.add(login);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAuthDomain() {
		return authDomain;
	}
	public void setAuthDomain(String authDomain) {
		this.authDomain = authDomain;
	}
	public String getFederatedIdentity() {
		return federatedIdentity;
	}
	public void setFederatedIdentity(String federatedIdentity) {
		this.federatedIdentity = federatedIdentity;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isReceiveEmails() {
		return receiveEmails;
	}
	public void setReceiveEmails(boolean receiveEmails) {
		this.receiveEmails = receiveEmails;
	}
	public Set<String> getFriends() {
		return friends;
	}
	public void setFriends(Set<String> friends) {
		this.friends = friends;
	}
	public Set<String> getPendingFriends() {
		return pendingFriends;
	}
	public void setPendingFriends(Set<String> pendingFriends) {
		this.pendingFriends = pendingFriends;
	}
	
}
