package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class LoginsAction implements Action<LoginsResult> {

	private String providerUrl;
	private String redirectUrl;

	public LoginsAction(String providerUrl, String redirectUrl) {
		this.providerUrl = providerUrl;
		this.redirectUrl = redirectUrl;
	}

	protected LoginsAction() {
		// Possibly for serialization.
	}

	@Override
	public String getServiceName() {
		return Action.DEFAULT_SERVICE_NAME + "Logins";
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((providerUrl == null) ? 0 : providerUrl.hashCode());
		result = prime * result
				+ ((redirectUrl == null) ? 0 : redirectUrl.hashCode());
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
		LoginsAction other = (LoginsAction) obj;
		if (providerUrl == null) {
			if (other.providerUrl != null)
				return false;
		} else if (!providerUrl.equals(other.providerUrl))
			return false;
		if (redirectUrl == null) {
			if (other.redirectUrl != null)
				return false;
		} else if (!redirectUrl.equals(other.redirectUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginsAction [providerUrl=" + providerUrl + ", redirectUrl="
				+ redirectUrl + "]";
	}

}
