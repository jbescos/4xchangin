package com.changestuffs.shared.actions;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.changestuffs.shared.constants.Tags;
import com.gwtplatform.dispatch.shared.Action;

public class ArticlesAddAction implements Action<ArticlesAddResult> {

	private Tags tag;
	@NotNull
	@Size(min = 1)
	private String name;
	@NotNull
	@Size(min = 1)
	private String description;
	@NotNull
	@Size(min = 1)
	private String interestedIn;
	@NotNull
	@Size(min = 2)
	private String language;

	private Date date;
	private String keyHash;

	public ArticlesAddAction(Tags tag, String name, String description,
			Date date, String keyHash, String interestedIn, String language) {
		this.tag = tag;
		this.name = name;
		this.description = description;
		this.date = date;
		this.keyHash = keyHash;
		this.interestedIn = interestedIn;
		this.language = language;
	}

	public ArticlesAddAction(Tags tag, String name, String description,
			Date date, String interestedIn, String language) {
		this.tag = tag;
		this.name = name;
		this.description = description;
		this.date = date;
		this.interestedIn = interestedIn;
		this.language = language;
	}

	protected ArticlesAddAction() {
		// Possibly for serialization.
	}

	public Tags getTag() {
		return tag;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return date;
	}

	public String getKeyHash() {
		return keyHash;
	}

	public String getInterestedIn() {
		return interestedIn;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public String getServiceName() {
		return Action.DEFAULT_SERVICE_NAME + "ArticlesAdd";
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((interestedIn == null) ? 0 : interestedIn.hashCode());
		result = prime * result + ((keyHash == null) ? 0 : keyHash.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		ArticlesAddAction other = (ArticlesAddAction) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (interestedIn == null) {
			if (other.interestedIn != null)
				return false;
		} else if (!interestedIn.equals(other.interestedIn))
			return false;
		if (keyHash == null) {
			if (other.keyHash != null)
				return false;
		} else if (!keyHash.equals(other.keyHash))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tag != other.tag)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArticlesAddAction [tag=" + tag + ", name=" + name
				+ ", description=" + description + ", interestedIn="
				+ interestedIn + ", language=" + language + ", date=" + date
				+ ", keyHash=" + keyHash + "]";
	}

}
