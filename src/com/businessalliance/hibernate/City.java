package com.businessalliance.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * City generated by MyEclipse Persistence Tools
 */

public class City implements java.io.Serializable {

	// Fields

	private Integer id;

	private Province province;

	private String city;


	// Constructors

	/** default constructor */
	public City() {
	}

	/** minimal constructor */
	public City(Province province) {
		this.province = province;
	}

	/** full constructor */
	public City(Province province, String city) {
		this.province = province;
		this.city = city;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Province getProvince() {
		return this.province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}