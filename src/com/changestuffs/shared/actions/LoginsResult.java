package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class LoginsResult implements Result {

	private String loginUrl;

	public LoginsResult(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	protected LoginsResult() {
		// Possibly for serialization.
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginUrl == null) ? 0 : loginUrl.hashCode());
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
		LoginsResult other = (LoginsResult) obj;
		if (loginUrl == null) {
			if (other.loginUrl != null)
				return false;
		} else if (!loginUrl.equals(other.loginUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginsResult [loginUrl=" + loginUrl + "]";
	}

}
