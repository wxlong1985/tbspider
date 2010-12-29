package com.businessalliance.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Procatsecond generated by MyEclipse Persistence Tools
 */

public class Procatsecond implements java.io.Serializable {

	// Fields

	private Long id;

	private Procatfirst procatfirst;

	private String categoryname;

	private Set procatthirds = new HashSet(0);

	// Constructors

	/** default constructor */
	public Procatsecond() {
	}

	/** minimal constructor */
	public Procatsecond(Procatfirst procatfirst) {
		this.procatfirst = procatfirst;
	}

	/** full constructor */
	public Procatsecond(Procatfirst procatfirst, String categoryname,
			Set procatthirds) {
		this.procatfirst = procatfirst;
		this.categoryname = categoryname;
		this.procatthirds = procatthirds;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Procatfirst getProcatfirst() {
		return this.procatfirst;
	}

	public void setProcatfirst(Procatfirst procatfirst) {
		this.procatfirst = procatfirst;
	}

	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public Set getProcatthirds() {
		return this.procatthirds;
	}

	public void setProcatthirds(Set procatthirds) {
		this.procatthirds = procatthirds;
	}

}