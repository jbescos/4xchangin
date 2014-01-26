package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class LogoutAction implements Action<LogoutResult> { 

  private java.lang.String urlToRedirect;

  public LogoutAction(java.lang.String urlToRedirect) {
    this.urlToRedirect = urlToRedirect;
  }

  protected LogoutAction() {
    // Possibly for serialization.
  }

  public java.lang.String getUrlToRedirect() {
    return urlToRedirect;
  }

  @Override
  public String getServiceName() {
    return Action.DEFAULT_SERVICE_NAME + "Logout";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    LogoutAction other = (LogoutAction) obj;
    if (urlToRedirect == null) {
      if (other.urlToRedirect != null)
        return false;
    } else if (!urlToRedirect.equals(other.urlToRedirect))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (urlToRedirect == null ? 1 : urlToRedirect.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "LogoutAction["
                 + urlToRedirect
    + "]";
  }
}
