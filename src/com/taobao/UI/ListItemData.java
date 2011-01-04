package com.taobao.UI;

public class ListItemData {
	private String country, comment, flagFile;

	public ListItemData(String country, String comment, String flagFile) {
		setCountry(country);
		setComment(comment);
		setFlagFile(flagFile);
	}

	/** String representation used in printouts and in JLists */

	public String toString() {
		return ("Java, " + getCountry() + " (" + getComment() + ").");
	}

	/** Return country containing city or province named "Java". */

	public String getCountry() {
		return (country);
	}

	/** Specify country containing city or province named "Java". */

	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Return comment about city or province named "Java". Usually of the form
	 * "near such and such a city".
	 */

	public String getComment() {
		return (comment);
	}

	/** Specify comment about city or province named "Java". */

	public void setComment(String comment) {
		this.comment = comment;
	}

	/** Return path to image file of country flag. */

	public String getFlagFile() {
		return (flagFile);
	}

	/** Specify path to image file of country flag. */

	public void setFlagFile(String flagFile) {
		this.flagFile = flagFile;
	}
}
