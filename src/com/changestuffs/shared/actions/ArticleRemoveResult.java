package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class ArticleRemoveResult implements Result { 

  private java.lang.String keyHashOut;

  public ArticleRemoveResult(java.lang.String keyHashOut) {
    this.keyHashOut = keyHashOut;
  }

  protected ArticleRemoveResult() {
    // Possibly for serialization.
  }

  public java.lang.String getKeyHashOut() {
    return keyHashOut;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    ArticleRemoveResult other = (ArticleRemoveResult) obj;
    if (keyHashOut == null) {
      if (other.keyHashOut != null)
        return false;
    } else if (!keyHashOut.equals(other.keyHashOut))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (keyHashOut == null ? 1 : keyHashOut.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "ArticleRemoveResult["
                 + keyHashOut
    + "]";
  }
}
