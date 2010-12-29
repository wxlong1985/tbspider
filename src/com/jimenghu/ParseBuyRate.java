package com.jimenghu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htmlcleaner.HtmlCleaner;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.businessalliance.hibernate.Buyinfo;
import com.businessalliance.hibernate.Product;
import com.businessalliance.hibernate.SaleInfo;
import com.businessalliance.hibernate.Userinfo;

public class ParseBuyRate {

	private final static String Reg = "http://space.taobao.com/(.*)/portal/personal_portal.htm";

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"[yyyy.MM.dd HH:mm:ss]");

	private final static Log log = LogFactory.getLog(ParseBuyRate.class);

	
	private static String[] parseAddress(String addr) throws IOException{
		if(addr == null)
			return null;
		String[] address = new String[2];
		StringReader sr = new StringReader(addr);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while((line = br.readLine())!= null){
			if(line.trim().length()>0){
				if(address[0] ==null){
					address[0] = line.trim();
				}else{
					address[1] = line.trim();
				}
			}
		}
		return address;
	}
	
	public static void parseBuyRate(Userinfo userinfo)
			throws DocumentException, MalformedURLException, IOException { 
		ScraperConfiguration config = new ScraperConfiguration("parseSaleRate.xml");
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("rateUrl", userinfo.getSalerate());
		Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
		StringReader sr = new StringReader(variable.toString());
		log.info(variable.toString());
		SAXReader reader = new SAXReader();
		Document doc = reader.read(sr);		
		String buyLevel = doc.getRootElement().selectSingleNode(
		"//buyLevel").getText();
		String msg = doc.getRootElement().selectSingleNode(
		"//msg").getText();
		String[] address = null;
		try {
			address = parseAddress(doc.getRootElement().selectSingleNode(
			"//address").getStringValue());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(msg.indexOf("店铺盘点")!=-1 || msg.indexOf("查封")!=-1){
			//log.error(userinfo.getSalerate() + " is being checking or has been stopped!:"+msg);
			DBDAO.saveImmediately(userinfo);
			return;
		}else if(buyLevel.trim().length()>0){
			userinfo.setBuylevel(Integer.parseInt(buyLevel));
			if(address != null){
				try {
					userinfo.setProvince(address[0]);
					userinfo.setCity(address[1]);
				} catch (NullPointerException e) {
					// TODO: handle exception
					if(DBDAO.provinces.get(address[0]) == null){
						userinfo.setProvince(address[0]);
						userinfo.setCity(address[1]);						
					}
				}
				
			}
			DBDAO.saveImmediately(userinfo);
		}		
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException,
			DocumentException, IOException {
		String address = " ";
		String province = address.substring(0, address.indexOf(":"));
		String city = address.substring(address.lastIndexOf(":")+1).trim();
		// TODO Auto-generated method stub
		
		/* Userinfo userinfo = DBDAO.findUser("jimenghu");
		 userinfo.setRate("http://rate.taobao.com/user-rate-a77f0ff8696a48f364571821b6768609.htm");
		 parseBuyRate(userinfo);*/
		 
		/*
		 * URL url=new
		 * URL("http://rate.taobao.com/user-rate-fb696595cf642041db25bdd369f97e7c.htm");
		 * HtmlCleaner clearner=new HtmlCleaner(url); clearner.clean();
		 * System.out.println(clearner.getXmlAsString());
		 */
		// System.out.println(URLEncoder.encode("框架", "utf-8"));
		/*String personSpace = "http://auction1.taobao.com/auction/snap_detail.htm?trade_id=1317032826&auction_id=2433d6f7cf2ee16be960640f59e8e479";
		String auctionId = personSpace
				.substring(personSpace.lastIndexOf('=') + 1);
		String tradeId = personSpace.substring(personSpace.indexOf('=') + 1,
				personSpace.indexOf("&"));
		System.out.println(auctionId + "\n" + tradeId);*/

	}

}
