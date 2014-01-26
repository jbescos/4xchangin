package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class LookForAction implements Action<LookForResult> { 

  private com.changestuffs.shared.constants.Tags tag;
  private java.lang.String idKey;

  public LookForAction(com.changestuffs.shared.constants.Tags tag, java.lang.String idKey) {
    this.tag = tag;
    this.idKey = idKey;
  }

  protected LookForAction() {
    // Possibly for serialization.
  }

  public com.changestuffs.shared.constants.Tags getTag() {
    return tag;
  }

  public java.lang.String getIdKey() {
    return idKey;
  }

  @Override
  public String getServiceName() {
    return Action.DEFAULT_SERVICE_NAME + "LookFor";
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
    LookForAction other = (LookForAction) obj;
    if (tag == null) {
      if (other.tag != null)
        return false;
    } else if (!tag.equals(other.tag))
      return false;
    if (idKey == null) {
      if (other.idKey != null)
        return false;
    } else if (!idKey.equals(other.idKey))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (tag == null ? 1 : tag.hashCode());
    hashCode = (hashCode * 37) + (idKey == null ? 1 : idKey.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "LookForAction["
                 + tag
                 + ","
                 + idKey
    + "]";
  }
}
