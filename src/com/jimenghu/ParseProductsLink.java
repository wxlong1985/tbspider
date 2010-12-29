package com.jimenghu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

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

public class ParseProductsLink {
	
	private final static Log log=LogFactory.getLog(ParseProductsLink.class);
	
	
	/**
	 * 返回的xml中没有a子节点就表示无宝贝.舍弃掉.
	 * @param productsLink
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void parseProductLinks(Userinfo userinfo,String productsLink) throws DocumentException, IOException{
		ScraperConfiguration config = new ScraperConfiguration(
		"D:/EWorkSpace/taobao/src/com/jimenghu/parseProductsLink.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("productsLink",productsLink);		
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr=new StringReader(variable.toString());
		log.info(sr.toString());
		BufferedReader br=new BufferedReader(sr);
		SAXReader reader=new SAXReader();
		Document doc=reader.read(br);
		for(Iterator<Element> aIte=doc.getRootElement().elementIterator("a");aIte.hasNext();){
			Element a=aIte.next();
			String productUrl=a.getTextTrim();
			log.info(productUrl);
			ParseProduct.parseProduct(productUrl);
		}
		
	}

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		Userinfo userInfo=new Userinfo();
		userInfo.setTaobaohref("http://shop33849170.taobao.com/");
		parseProductLinks(userInfo,"http://moonlightstone.mall.taobao.com/shop/xshop/wui_page-cat-57302264-96745984-MDm0utewzNjC9A==.htm");
		log.fatal("parse successful!");
	}

}
