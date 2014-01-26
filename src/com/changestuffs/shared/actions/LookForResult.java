package com.changestuffs.shared.actions;

import java.util.Map;

import com.changestuffs.shared.dto.IArticlesDto;
import com.gwtplatform.dispatch.shared.Result;

public class LookForResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2142032577133543271L;
	private Map<String, IArticlesDto> articles;

	public LookForResult(Map<String, IArticlesDto> articles) {
		this.articles = articles;
	}

	protected LookForResult() {
		// Possibly for serialization.
	}

	public java.util.Map<String, IArticlesDto> getArticles() {
		return articles;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LookForResult other = (LookForResult) obj;
		if (articles == null) {
			if (other.articles != null)
				return false;
		} else if (!articles.equals(other.articles))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 23;
		hashCode = (hashCode * 37)
				+ (articles == null ? 1 : articles.hashCode());
		return hashCode;
	}

	@Override
	public String toString() {
		return "LookForResult[" + articles + "]";
	}
}
