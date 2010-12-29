package com.businessalliance.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Procatfirst generated by MyEclipse Persistence Tools
 */

public class Procatfirst implements java.io.Serializable {

	// Fields

	private Long id;

	private String categoryname;

	private Set procatseconds = new HashSet(0);

	// Constructors

	/** default constructor */
	public Procatfirst() {
	}

	/** full constructor */
	public Procatfirst(String categoryname, Set procatseconds) {
		this.categoryname = categoryname;
		this.procatseconds = procatseconds;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public Set getProcatseconds() {
		return this.procatseconds;
	}

	public void setProcatseconds(Set procatseconds) {
		this.procatseconds = procatseconds;
	}

}