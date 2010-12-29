package com.businessalliance.hibernate;

import java.util.Date;

public class Shops {
	private Long id;

	private String province;
	
	private String city;

	private String taobaohref;

	private String shopname;

	private Boolean iscompaney;

	private String salerate;

	private String buyrate;

	private String username;

	private String companeyname;

	private String personspace;

	private Integer salelevel;
	
	private Integer buylevel;
	
	private Boolean mission;
	
	private String lastchecktime;

	public Integer getBuylevel() {
		return buylevel;
	}

	public void setBuylevel(Integer buylevel) {
		this.buylevel = buylevel;
	}

	public String getBuyrate() {
		return buyrate;
	}

	public void setBuyrate(String buyrate) {
		this.buyrate = buyrate;
	}


	public String getCompaneyname() {
		return companeyname;
	}

	public void setCompaneyname(String companeyname) {
		this.companeyname = companeyname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIscompaney() {
		return iscompaney;
	}

	public void setIscompaney(Boolean iscompaney) {
		this.iscompaney = iscompaney;
	}

	public String getLastchecktime() {
		return lastchecktime;
	}

	public void setLastchecktime(String lastchecktime) {
		this.lastchecktime = lastchecktime;
	}

	public Boolean getMission() {
		return mission;
	}

	public void setMission(Boolean mission) {
		this.mission = mission;
	}

	public String getPersonspace() {
		return personspace;
	}

	public void setPersonspace(String personspace) {
		this.personspace = personspace;
	}


	public Integer getSalelevel() {
		return salelevel;
	}

	public void setSalelevel(Integer salelevel) {
		this.salelevel = salelevel;
	}

	public String getSalerate() {
		return salerate;
	}

	public void setSalerate(String salerate) {
		this.salerate = salerate;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getTaobaohref() {
		return taobaohref;
	}

	public void setTaobaohref(String taobaohref) {
		this.taobaohref = taobaohref;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	
}
