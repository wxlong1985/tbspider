package com.businessalliance.hibernate;

/**
 * Secondshopcategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Secondshopcategory implements java.io.Serializable {

	// Fields

	private Long id;
	private Userinfo userinfo;
	private Firstshopcategory firstshopcategory;
	private String title;
	private String href;

	// Constructors

	/** default constructor */
	public Secondshopcategory() {
	}

	/** full constructor */
	public Secondshopcategory(Userinfo userinfo,
			Firstshopcategory firstshopcategory, String title, String href) {
		this.userinfo = userinfo;
		this.firstshopcategory = firstshopcategory;
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

	public Userinfo getUserinfo() {
		return this.userinfo;
	}

	public void setUserinfo(Userinfo userinfo) {
		this.userinfo = userinfo;
	}

	public Firstshopcategory getFirstshopcategory() {
		return this.firstshopcategory;
	}

	public void setFirstshopcategory(Firstshopcategory firstshopcategory) {
		this.firstshopcategory = firstshopcategory;
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