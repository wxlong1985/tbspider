package com.jimenghu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
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

import com.businessalliance.hibernate.Userinfo;

public class ParseShop {
	
	private final static Log log=LogFactory.getLog(ParseShop.class);
	
	
	public static void ParseCompaney(Userinfo userinfo) throws DocumentException, IOException{
		ScraperConfiguration config = new ScraperConfiguration("parseCompaney.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("shopUrl",userinfo.getTaobaohref());		
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr=new StringReader(variable.toString());
		log.info(variable.toString());
		SAXReader reader=new SAXReader();
		Document doc=reader.read(sr);
		Element basic=(Element) doc.getRootElement().selectNodes("//basic").get(0);
		String shopName=basic.elementTextTrim("shopName");
		String companeyName=basic.elementTextTrim("companeyName");
		String address=basic.elementTextTrim("address");
		log.info("店铺名称:"+shopName);
		log.info("公司名称"+companeyName);
		log.info("地址:"+address);
		userinfo.setShopname(shopName);
		userinfo.setCompaneyname(companeyName);
		//userinfo.setAddress(address);
		DBDAO.updateUser(userinfo);
		String cats = doc.getRootElement().elementTextTrim("cats");
		log.info("全部产品 ： "+cats);
		ParseProductsLink.parseProductLinks(userinfo,cats);
		
				
	}
	
	
	
	public static void parseShop(Userinfo userinfo) throws DocumentException, IOException{
		ScraperConfiguration config = new ScraperConfiguration("JudgeShopType.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("shopUrl",userinfo.getTaobaohref());
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr=new StringReader(variable.toString());
		//log.info(sr.toString());
		log.info(variable.toString());
		SAXReader reader=new SAXReader();
		Document doc=reader.read(sr);
		String withHrefStr = doc.getRootElement().elementTextTrim("withHref");
		if(!Sys.checkNotNullOrEmpty(new String[]{withHrefStr})){
			Sys.sendMail(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
			return;
		}
		
		boolean withHref = Boolean.parseBoolean(withHrefStr);		
		if(withHref){			
			ParseShopWithHref.parseShopWithHref(userinfo);
		}else if(!withHref){			
			ParseShopWithoutHref.parseShopWithoutHref(userinfo);
		}
	}

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		
		Userinfo userinfo=new Userinfo();
		
		String shopUrl="http://shop57299952.taobao.com/";
		userinfo.setTaobaohref(shopUrl);
		parseShop(userinfo);
		
		//System.out.println(new Byte("1"));
	}

}
