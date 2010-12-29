package com.jimenghu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.businessalliance.hibernate.Firstshopcategory;
import com.businessalliance.hibernate.Secondcategory;
import com.businessalliance.hibernate.Userinfo;


public class ParseShopWithHref {

	private final static String result = "result";

	private final static Log log = LogFactory.getLog(ParseShopWithHref.class);

	private String shopUrl = "http://shop33982444.taobao.com/";

	private final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	private Userinfo userInfo;
	
	private static String searchEnd = "?search=y";

	private void updateUserInfo(Variable variable) throws DocumentException, IOException {
		StringReader sr = new StringReader(variable.toString());
		log.fatal(sr);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(sr);
		Element basic = (Element) doc.getRootElement().selectNodes("//basic")
				.get(0);
		String shopName = basic.elementTextTrim("shopName"); 
		log.info(shopName); 
		userInfo.setShopname(shopName);
		String cats = doc.getRootElement().elementTextTrim("cats");
		log.info("全部产品 ： "+cats);
		ParseProductsLink.parseProductLinks(userInfo,cats);
	}

	public static void parseShopWithHref(Userinfo userInfo)
			throws DocumentException, IOException {

		ScraperConfiguration config = new ScraperConfiguration("parseShopWithHref.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("shopUrl", userInfo.getTaobaohref());
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr = new StringReader(variable.toString());
		log.info(variable.toString());
		SAXReader reader = new SAXReader();
		Document doc = reader.read(sr);
		Element basic = (Element) doc.getRootElement().selectNodes("//basic")
				.get(0);
		String shopName = basic.elementTextTrim("shopName");
		String buyInfo = basic.elementTextTrim("buyInfo");
		String msg = basic.elementTextTrim("msg");
		if(msg != null && msg.trim().length() > 0){
			return;
		}
		if(!Sys.checkNotNullOrEmpty(new String[]{shopName,buyInfo})){
			Sys.sendMail(new NullPointerException("URL :"+userInfo.getTaobaohref() + "\r\n"+doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
			return;
		}
		log.info("shopName" + shopName);
		log.info("buyInfo "+buyInfo);
		userInfo.setShopname(shopName);	
		userInfo.setSalerate(buyInfo);
		userInfo.setBuyrate(buyInfo);
		//DBDAO.updateUser(userInfo);
		ParseBuyRate.parseBuyRate(userInfo);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			Userinfo userinfo = new Userinfo();
			userinfo.setTaobaohref("http://shop33412575.taobao.com/");
			try {
				parseShopWithHref(userinfo);

			} catch (Exception e) {
				// TODO: handle exception
				log.error("Taobao URL:" + userinfo.getTaobaohref(), e);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
