package com.businessalliance.hibernate;

/**
 * Secondcategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Secondcategory implements java.io.Serializable {

	// Fields

	private Long id;
	private Firstcategory firstcategory;
	private String title;
	private String href;

	// Constructors

	/** default constructor */
	public Secondcategory() {
	}

	/** full constructor */
	public Secondcategory(Firstcategory firstcategory, String title, String href) {
		this.firstcategory = firstcategory;
		this.title = title;
		this.href = href;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Firstcategory getFirstcategory() {
		return this.firstcategory;
	}

	public void setFirstcategory(Firstcategory firstcategory) {
		this.firstcategory = firstcategory;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return this.href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}