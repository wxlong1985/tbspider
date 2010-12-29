package com.businessalliance.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Firstshopcategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Firstshopcategory implements java.io.Serializable {

	// Fields

	private Long id;
	private Userinfo userinfo;
	private String title;
	private String href;
	private Set secondshopcategories = new HashSet(0);

	// Constructors

	/** default constructor */
	public Firstshopcategory() {
	}

	/** minimal constructor */
	public Firstshopcategory(Userinfo userinfo, String title, String href) {
		this.userinfo = userinfo;
		this.title = title;
		this.href = href;
	}

	/** full constructor */
	public Firstshopcategory(Userinfo userinfo, String title, String href,
			Set secondshopcategories) {
		this.userinfo = userinfo;
		this.title = title;
		this.href = href;
		this.secondshopcategories = secondshopcategories;
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

	public Set getSecondshopcategories() {
		return this.secondshopcategories;
	}

	public void setSecondshopcategories(Set secondshopcategories) {
		this.secondshopcategories = secondshopcategories;
	}

}