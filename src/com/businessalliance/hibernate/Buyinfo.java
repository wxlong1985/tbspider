package com.businessalliance.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Buyinfo generated by MyEclipse Persistence Tools
 */

public class Buyinfo implements java.io.Serializable {

	// Fields

	private Long id;

	private String salername;

	private String personspace;

	private String buyday;

	private String tradeid;

	private String producturl;

	private Double price;

	private Long buyerid;

	private String auctionid;

	private Date addday;

	private String title;

	private Integer funfirst, funsecond, funfour, funthird;

	private Boolean serverexist;

	// Constructors

	public Integer getFunfirst() {
		return funfirst;
	}

	public void setFunfirst(Integer funfirst) {
		this.funfirst = funfirst;
	}

	public Integer getFunfour() {
		return funfour;
	}

	public void setFunfour(Integer funfour) {
		this.funfour = funfour;
	}

	public Integer getFunsecond() {
		return funsecond;
	}

	public void setFunsecond(Integer funsecond) {
		this.funsecond = funsecond;
	}

	

	public Integer getFunthird() {
		return funthird;
	}

	public void setFunthird(Integer funthird) {
		this.funthird = funthird;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title = title.replace("'", "''");
		this.title = title;
	}

	public Boolean getServerexist() {
		return serverexist;
	}

	public void setServerexist(Boolean serverexist) {
		this.serverexist = serverexist;
	}

	/** default constructor */
	public Buyinfo() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSalername() {
		return salername;
	}

	public void setSalername(String salername) {
		this.salername = salername.replace("'", "''");;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Long buyerid) {
		this.buyerid = buyerid;
	}

	public Date getAddday() {
		return addday;
	}

	public void setAddday(Date addday) {
		this.addday = addday;
	}

	public String getBuyday() {
		return buyday;
	}

	public void setBuyday(String buyday) {
		this.buyday = buyday;
	}

	public String getPersonspace() {
		return personspace;
	}

	public void setPersonspace(String personspace) {
		this.personspace = personspace;
	}

	

	public String getAuctionid() {
		return auctionid;
	}

	public void setAuctionid(String auctionid) {
		this.auctionid = auctionid;
	}

	public String getProducturl() {
		return producturl;
	}

	public void setProducturl(String producturl) {
		this.producturl = producturl;
	}

	public String getTradeid() {
		return tradeid;
	}

	public void setTradeid(String tradeid) {
		this.tradeid = tradeid;
	}

}