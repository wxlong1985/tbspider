package com.jimenghu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.businessalliance.hibernate.Secondcategory;
import com.businessalliance.hibernate.Userinfo;
import com.jimenghu.client.Config;
import com.taobao.collectbuyinfo.CollectBuyinfoThread;

public class ParseSecondCategoryShops implements Runnable{
	
	private final static Log log = LogFactory
			.getLog(ParseSecondCategoryShops.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @param args
	 * @throws IOException d
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 * @throws HibernateException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException, InterruptedException {
		/*File file = new File("b.txt");
		file.createNewFile();*/
		//Thread buyinfo= new Thread(new CollectBuyinfoThread(),"collectbuyinfothread");
		Thread collectshop = new Thread(new ParseSecondCategoryShops(),"ParseSecondCategoryShops");
		//buyinfo.start();
		collectshop.start();
		/*while(true){
			System.out.println(new Date()+"buyinfo : "+ buyinfo.isAlive()+"\ncollectshop :"+collectshop.isAlive());
			//log.error("buyinfo : "+ buyinfo.isAlive()+"\ncollectshop :"+collectshop.isAlive());
			Thread.sleep(10000);
			if(!buyinfo.isAlive()){
				buyinfo = new Thread(new CollectBuyinfoThread(),"collectbuyinfothread");
				buyinfo.start();
				Sys.sendMail("resume buyinfo thread", "resume buyinfo thread");
			}
			if(!collectshop.isAlive()){
				collectshop = new Thread(new ParseSecondCategoryShops(),"parseshops");
				collectshop.start();
				Sys.sendMail("resume collectshop thread", "resume collectshop thread");
			}
		}*/
		
	}

	public void run() {
		// TODO Auto-generated method stub
			
		try {	
			long fromIndex = -1L;
			long endIndex = -1L;
			long currendshopIndex = -1L;
			String urlHead = null;
			try {
				fromIndex = Long.parseLong(Config.readProperty(Config.fromIndex));
				endIndex = Long.parseLong(Config.readProperty(Config.endIndex));
				urlHead = Config.readProperty(Config.urlHead);
			} catch (Exception e) {
				// TODO: handle exception
				Sys.out(new NullPointerException(), "config.ini  配置失败");
			}
			
			if(Config.readProperty(Config.currendshopIndex).trim().length() > 0){
				currendshopIndex = Long.parseLong(Config.readProperty(Config.currendshopIndex));
				if(fromIndex > currendshopIndex || currendshopIndex > endIndex){
					currendshopIndex = fromIndex;
				}
			}else{
				currendshopIndex = fromIndex;
			}
			
			ScraperConfiguration config = new ScraperConfiguration("ParseSecondCategoryShops.xml");
			
			String nextUrl = null;			
			urlHead +="?page=";
			while (currendshopIndex <= endIndex) {
					nextUrl = urlHead+currendshopIndex;
					Config.storeProperty(Config.currendshopIndex, currendshopIndex+"");
					if(currendshopIndex == endIndex){
						currendshopIndex = fromIndex;
					}else{ 
						currendshopIndex++;
					}					
					Scraper scraper = new Scraper(config, ".");
					scraper.addVariableToContext("firstUrl", nextUrl);	
				 	//scraper.execute();
					Variable userInfos = UserProxyRequestUrl.ParsingUrl(scraper);
					log.info(userInfos.toString());					
					try {
						StringReader sr = new StringReader(userInfos.toString());
						SAXReader reader = new SAXReader();
						Document doc = null;
						try {
							doc = reader.read(sr);
						} catch (DocumentException e) {
							log.error(e);
							Sys.out(e, "ParseSecondCategoryShops  doc read exception" );
						}
						String nextHref = doc.getRootElement().elementTextTrim("nextHref");
						log.info("ParseSecondShopCategory   nextHref: "+ nextHref);						
						boolean setnull = false;
						int index = 0;
						for (Iterator<Element> eleIte = doc.getRootElement().selectNodes(
								"//shop").iterator(); eleIte.hasNext();) {							
							Element ele = eleIte.next();							
							/*if(++index < 39){
							continue;
						    }*/
							String seller = ele.elementTextTrim("seller");
							Userinfo findUser = DBDAO.findUser(seller);								
							if(findUser==null){								
								String companey = ele.elementTextTrim("companey");
								if("yes".equalsIgnoreCase(companey)){
									continue;
								}
								String shopUrl = ele.elementTextTrim("shopUrl");
								String level = ele.elementTextTrim("level");
								if(!Sys.checkNotNullOrEmpty(new String[]{shopUrl,seller,companey,level})){
									Sys.out(new NullPointerException(doc.asXML()), "ParseSecondCategoryShops  appear null element(s)!");
								}
								if(companey.equalsIgnoreCase("no") && level.equalsIgnoreCase("0")){
									setnull=true;
									continue;
								}
								log.info("parse shopUrl:" + shopUrl);
								log.info("parse seller:" + seller);
								Userinfo userInfo = new Userinfo();
								userInfo.setTaobaohref(shopUrl);
								userInfo.setUsername(seller);	
								userInfo.setIscompaney(true);
								userInfo.setSalelevel(Integer.parseInt(level));
								//DBDAO.saveImmediately(userInfo);
								ParseShop.parseShop(userInfo);
							}/*else{															
								String companey = ele.elementTextTrim("companey");
								if("yes".equalsIgnoreCase(companey)){
									continue;
								}
								String shopUrl = ele.elementTextTrim("shopUrl");	
								String level = ele.elementTextTrim("level");
								if(!Sys.checkNotNullOrEmpty(new String[]{shopUrl,seller,companey,level})){
									Sys.out(new NullPointerException(doc.asXML()), "ParseSecondCategoryShops  appear null element(s)!");
								}
								if(companey.equalsIgnoreCase("no") && level.equalsIgnoreCase("0")){
									setnull=true;
									continue;
								}
								log.info("parse shopUrl:" + shopUrl);
								log.info("parse seller:" + seller);
								findUser.setTaobaohref(shopUrl);
								findUser.setUsername(seller);
								findUser.setSalelevel(Integer.parseInt(level));
								DBDAO.updateUser(findUser);				
								continue;
							}*/
																	
						}
						if(setnull){							
							nextUrl = urlHead + fromIndex;							
						}else if (nextHref.trim().length() == 0)
						{
							nextHref = null;
							Sys.out(new NullPointerException(doc.asXML()), "collect second category shops next page exception!");
						}else{
							nextUrl = nextHref;
						}
							
					} catch (DocumentException e) {
						log.error(e+"\nparseSecondCategory : "+nextUrl);
					} catch (IOException e) {
						log.error(e+"\nparseSecondCategory : "+nextUrl);
					}
				}
		} catch (FileNotFoundException e) {
			log.error(e);
			//System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);			
			//System.exit(0);
		} 

	
	}
	

}
