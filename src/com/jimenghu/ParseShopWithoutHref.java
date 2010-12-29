package com.jimenghu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.businessalliance.hibernate.Firstshopcategory;
import com.businessalliance.hibernate.Userinfo;

public class ParseShopWithoutHref {

	private final static Log log = LogFactory
			.getLog(ParseShopWithoutHref.class);
	
	private final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	public static void parseShopWithoutHref(Userinfo userInfo)
			throws DocumentException, IOException {

		ScraperConfiguration config = new ScraperConfiguration("parseShopWithoutHref.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("shopUrl", userInfo.getTaobaohref());
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr = new StringReader(variable.toString());
		log.info(variable.toString());
		SAXReader reader = new SAXReader();
		Document doc = reader.read(sr);
		
		Element basic = (Element) doc.getRootElement().selectNodes("//basic").get(0);
		String shopName = basic.elementTextTrim("shopName");
		String buyinfo = basic.elementTextTrim("buyInfo");
		String msg = basic.elementTextTrim("msg");
		if(msg != null && msg.trim().length() > 0){
			return;
		}
		if(!Sys.checkNotNullOrEmpty(new String[]{shopName,buyinfo})){
			Sys.sendMail(new NullPointerException("URL :"+userInfo.getTaobaohref() + "\r\n"+doc.asXML()), config.getSourceFile().getName()+" \nURL:"+ userInfo.getTaobaohref()+"\n"+doc.asXML());
			return;
		}
		log.info(shopName);
		userInfo.setShopname(shopName);
		userInfo.setSalerate(buyinfo);
		userInfo.setBuyrate(buyinfo);
		//DBDAO.updateUser(userInfo);
		ParseBuyRate.parseBuyRate(userInfo);


	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		Userinfo userInfo = new Userinfo();
		userInfo.setId(new Long(8684));
		userInfo.setTaobaohref("http://shop34713350.taobao.com/");
		parseShopWithoutHref(userInfo);
		log.info("parse successful!");
		
		
		
	}

}
