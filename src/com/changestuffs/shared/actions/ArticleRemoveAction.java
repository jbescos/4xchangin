package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class ArticleRemoveAction implements Action<ArticleRemoveResult> { 

  private java.lang.String keyHash;

  public ArticleRemoveAction(java.lang.String keyHash) {
    this.keyHash = keyHash;
  }

  protected ArticleRemoveAction() {
    // Possibly for serialization.
  }

  public java.lang.String getKeyHash() {
    return keyHash;
  }

  @Override
  public String getServiceName() {
    return Action.DEFAULT_SERVICE_NAME + "ArticleRemove";
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
    ArticleRemoveAction other = (ArticleRemoveAction) obj;
    if (keyHash == null) {
      if (other.keyHash != null)
        return false;
    } else if (!keyHash.equals(other.keyHash))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (keyHash == null ? 1 : keyHash.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "ArticleRemoveAction["
                 + keyHash
    + "]";
  }
}
