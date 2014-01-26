package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class ArticlesAddResult implements Result { 

  private java.lang.String idHash;
  private com.changestuffs.shared.constants.Tags tagOut;

  public ArticlesAddResult(java.lang.String idHash, com.changestuffs.shared.constants.Tags tagOut) {
    this.idHash = idHash;
    this.tagOut = tagOut;
  }

  protected ArticlesAddResult() {
    // Possibly for serialization.
  }

  public java.lang.String getIdHash() {
    return idHash;
  }

  public com.changestuffs.shared.constants.Tags getTagOut() {
    return tagOut;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    ArticlesAddResult other = (ArticlesAddResult) obj;
    if (idHash == null) {
      if (other.idHash != null)
        return false;
    } else if (!idHash.equals(other.idHash))
      return false;
    if (tagOut == null) {
      if (other.tagOut != null)
        return false;
    } else if (!tagOut.equals(other.tagOut))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (idHash == null ? 1 : idHash.hashCode());
    hashCode = (hashCode * 37) + (tagOut == null ? 1 : tagOut.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "ArticlesAddResult["
                 + idHash
                 + ","
                 + tagOut
    + "]";
  }
}
