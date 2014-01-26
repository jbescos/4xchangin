package com.changestuffs.shared.actions;

import java.util.Date;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class InfoLoginResult implements Result {

	private String email;
	private Date date;
	private boolean firstTime;
	private boolean sendEmail;
	private String token;

	public InfoLoginResult(String email, Date date, boolean firstTime,
			boolean sendEmail, String token) {
		this.email = email;
		this.date = date;
		this.firstTime = firstTime;
		this.sendEmail = sendEmail;
		this.token = token;
	}

	protected InfoLoginResult() {
		// Possibly for serialization.
	}

	public String getEmail() {
		return email;
	}

	public Date getDate() {
		return date;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public String getToken() {
		return token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (firstTime ? 1231 : 1237);
		result = prime * result + (sendEmail ? 1231 : 1237);
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		InfoLoginResult other = (InfoLoginResult) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstTime != other.firstTime)
			return false;
		if (sendEmail != other.sendEmail)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InfoLoginResult [email=" + email + ", date=" + date
				+ ", firstTime=" + firstTime + ", sendEmail=" + sendEmail
				+ ", token=" + token + "]";
	}

}
