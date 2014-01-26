package com.changestuffs.shared.actions;

import java.util.Set;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class GetUserInfoResult implements Result {

	private String cell;
	private String country;
	private String city;
	private String email;
	private boolean receiveEmails;
	private Set<String> friends;
	private Set<String> pendingFriends;
	private Set<String> online;

	@SuppressWarnings("unused")
	private GetUserInfoResult() {
		// For serialization only
	}

	public GetUserInfoResult(String cell, String country, String city, String email,
			boolean receiveEmails, Set<String> friends, Set<String> pendingFriends, Set<String> online) {
		this.cell = cell;
		this.country = country;
		this.city = city;
		this.receiveEmails = receiveEmails;
		this.email=email;
		this.friends=friends;
		this.pendingFriends=pendingFriends;
		this.online=online;
	}

	public String getCell() {
		return cell;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public boolean isReceiveEmails() {
		return receiveEmails;
	}

	public String getEmail() {
		return email;
	}

	public Set<String> getFriends() {
		return friends;
	}

	public Set<String> getPendingFriends() {
		return pendingFriends;
	}

	public Set<String> getOnline() {
		return online;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((friends == null) ? 0 : friends.hashCode());
		result = prime * result + ((online == null) ? 0 : online.hashCode());
		result = prime * result
				+ ((pendingFriends == null) ? 0 : pendingFriends.hashCode());
		result = prime * result + (receiveEmails ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetUserInfoResult other = (GetUserInfoResult) obj;
		if (cell == null) {
			if (other.cell != null)
				return false;
		} else if (!cell.equals(other.cell))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (friends == null) {
			if (other.friends != null)
				return false;
		} else if (!friends.equals(other.friends))
			return false;
		if (online == null) {
			if (other.online != null)
				return false;
		} else if (!online.equals(other.online))
			return false;
		if (pendingFriends == null) {
			if (other.pendingFriends != null)
				return false;
		} else if (!pendingFriends.equals(other.pendingFriends))
			return false;
		if (receiveEmails != other.receiveEmails)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GetUserInfoResult [cell=" + cell + ", country=" + country
				+ ", city=" + city + ", email=" + email + ", receiveEmails="
				+ receiveEmails + ", friends=" + friends + ", pendingFriends="
				+ pendingFriends + ", online=" + online + "]";
	}
	
}
