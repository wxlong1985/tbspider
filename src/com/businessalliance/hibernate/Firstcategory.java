package com.businessalliance.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Firstcategory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Firstcategory implements java.io.Serializable {

	// Fields

	private Long id;
	private String title;
	private Set secondcategories = new HashSet(0);

	// Constructors

	/** default constructor */
	public Firstcategory() {
	}

	/** minimal constructor */
	public Firstcategory(String title) {
		this.title = title;
	}

	/** full constructor */
	public Firstcategory(String title, Set secondcategories) {
		this.title = title;
		this.secondcategories = secondcategories;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set getSecondcategories() {
		return this.secondcategories;
	}

	public void setSecondcategories(Set secondcategories) {
		this.secondcategories = secondcategories;
	}

}