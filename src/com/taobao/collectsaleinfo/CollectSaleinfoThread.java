package com.taobao.collectsaleinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import com.businessalliance.hibernate.SaleInfo;
import com.businessalliance.hibernate.Shops;
import com.businessalliance.hibernate.Userinfo;
import com.jimenghu.DBDAO;
import com.jimenghu.Sys;
import com.jimenghu.UserProxyRequestUrl;
import com.taobao.collectbuyinfo.CollectBuyinfoThread;

public class CollectSaleinfoThread implements Runnable {

	private String currentIndex = "currentIndex";
	private String pageIndex = "pageIndex";

	private String configfile = "collectsaleinfo.ini";

	private static final Log log = LogFactory
			.getLog(CollectBuyinfoThread.class);

	private Long id;
	
	private Long page = 1L;
	
	private String lastbuyday ;
	
	/**
	 * 收集到beforedays天前的数据
	 */
	private int beforedays = 40;

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"[yyyy.MM.dd HH:mm:ss]");
	
	private final static SimpleDateFormat sdfdb = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	private final static String endUrl = "--buyerOrSeller%7C0--receivedOrPosted%7C--goodNeutralOrBad%7C--timeLine%7C--detailed%7C--ismore%7C1--showContent%7C--currentPage%7C1.htm#RateType";
	
	
	private String[] parseAddress(String addr) throws IOException{
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

	private void saveIndex() throws IOException {
		File file = new File(this.configfile);
		FileOutputStream fops = new FileOutputStream(file);
		Properties pro = new Properties();
		pro.put(this.currentIndex, id + "");
		pro.put(this.pageIndex, page + "");
		pro.store(fops, "comments");
		fops.close();
	}
	
	/**
	 * 读取当前值，如果存在返回，如果不存在存储最小值为当前值，并保存配置文件。
	 * 
	 * @return
	 * @throws IOException
	 */
	private long readPageIndex() throws IOException {
		File file = new File(this.configfile);
		if (file.exists()) {
			FileInputStream fips = new FileInputStream(file);
			Properties pro = new Properties();
			pro.load(fips);
			fips.close();
			Object currrent = pro.get(this.pageIndex);
			if (currrent != null) {
				long a = Long.parseLong((String)currrent);
				if(a == -1){
					this.page = 1L;
					saveIndex();
					return this.id;
				}else{
					this.page = Long.parseLong((String)currrent);
					return this.page;
				}
				
			} else {
				this.page = 1L;
				saveIndex();
				return this.page;
			}
		} else {
			this.page = 1L;
			saveIndex();
			return this.id;
		}

	}

	/**
	 * 读取当前值，如果存在返回，如果不存在存储最小值为当前值，并保存配置文件。
	 * 
	 * @return
	 * @throws IOException
	 */
	private long readCurrentIndex() throws IOException {
		File file = new File(this.configfile);
		if (file.exists()) {
			FileInputStream fips = new FileInputStream(file);
			Properties pro = new Properties();
			pro.load(fips);
			fips.close();
			Object currrent = pro.get(this.currentIndex);
			if (currrent != null) {
				long a = Long.parseLong((String)currrent);
				if(a == -1){
					this.id = DBDAO.getMinShopMissionId();
					saveIndex();
					return this.id;
				}else{
					this.id = Long.parseLong((String)currrent); 
					return this.id;
				}
				
			} else {
				this.id = DBDAO.getMinShopMissionId();
				saveIndex();
				return this.id;
			}
		} else {
			this.id = DBDAO.getMinShopMissionId();
			saveIndex();
			return this.id;
		}

	}

	public Shops getNextUser() throws IOException {
		this.id++;
		saveIndex();
		return DBDAO.findShop(this.id);
	}


	public void parseUserSaleinfo(Shops shop) throws DocumentException, IOException {
		
		String lastchecktime = shop.getLastchecktime();
		Calendar lastcheck = null;
		if(lastchecktime != null){
			lastcheck = Calendar.getInstance();
			try {
				lastcheck.setTime(sdfdb.parse(shop.getLastchecktime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e);
				e.printStackTrace();
				
			}
		}
		endUrl.replace("currentPage%7C1", "currentPage%7C"+this.page);
		String nextHref = shop.getSalerate().replace(".htm", "") + endUrl;;
		while (nextHref != null && nextHref.trim().length()>0) {        	
			//new ScraperConfiguration(new InputSource(ips));        	
        	saveIndex();
			ScraperConfiguration config = new ScraperConfiguration("parseSaleInfo.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("url", nextHref);
			Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
			StringReader sr = new StringReader(variable.toString());
			log.info(variable.toString());
			SAXReader reader = new SAXReader();
			Document doc = reader.read(sr);			
			String tempNextHref = doc.getRootElement().elementTextTrim("nextHref");
			if (tempNextHref.trim().length() == 0)
				nextHref = null;
			else
				nextHref = tempNextHref.replace("|", "%7C");
			Iterator<Element> iter = doc.getRootElement()
			.elementIterator("buyInfo");
			if(!iter.hasNext()){
				nextHref = null;
			}
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				String day = element.elementTextTrim("day");				
				if(day == null || day.trim().length() == 0){
					Sys.out(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
				}
				Calendar checktime = Calendar.getInstance();
				try {
					checktime.setTime(sdf.parse(day));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					log.error(e1);
					e1.printStackTrace();
				}
				if(lastchecktime != null){
					if(lastcheck.getTimeInMillis() >= checktime.getTimeInMillis()){						
						return;
					}
				}
				if(distanceDays(Calendar.getInstance(), checktime) > beforedays){
					return;
				}
				if(this.lastbuyday == null){
					try {
						this.lastbuyday = sdfdb.format(sdf.parse(day));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String productUrl = element.elementTextTrim("productUrl");
				String auctionId = null;
				String tradeId = null;
				try {
					if(productUrl.indexOf("item_num_id")==-1 && productUrl.indexOf("item_id")==-1){
						auctionId = productUrl.substring(productUrl
								.lastIndexOf('=') + 1);
						tradeId = productUrl.substring(productUrl.indexOf('=') + 1,
								productUrl.indexOf("&"));
						//System.out.println("trade id :"+tradeId);
						if (DBDAO.checkExistSaleinfo(tradeId)) {
							//nextHref = null;
							continue;
						}
					}else if (productUrl.indexOf("item_num_id")!=-1){
						continue;
					}else{
						log.error("unhandler product Url :"+ productUrl);
						continue;
						//Sys.out(new Exception("parse buyinfo occur an uncatch exception:"+productUrl),"parse buyinfo occur an uncatch exception:"+productUrl );
					}					
				} catch (Exception e) {
					log.error("parseductUrl:  " + productUrl);
				}
				String userName = element.elementTextTrim("userName");
				String personSpace = element.elementTextTrim("personSpace");
				String buyRate = element.elementTextTrim("buyRate");
				String price = element.elementTextTrim("price");				
				String title = element.elementTextTrim("title");
				if(!Sys.checkNotNullOrEmpty(new String[]{productUrl,auctionId,tradeId,userName,price,title})){
					Sys.out(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
				}
				SaleInfo saleinfo = new SaleInfo();
				if(userName.indexOf("买家")==-1 && userName.indexOf("**")==-1){
					try {
						saleinfo.setUsername(userName);
											
					} catch (RuntimeException e) {
						log.error(e);
					}
				}else{					
					try {
						saleinfo.setUsername(userName.split("：")[1]);											
					} catch (RuntimeException e) {
						saleinfo.setUsername(userName);
						log.error(e);
					}
				}
				
				saleinfo.setPersonspace(personSpace);
				saleinfo.setProducturl(productUrl);
				saleinfo.setPrice(Double.parseDouble(price));
				//saleinfo.setSellerid(mission.getId());
				saleinfo.setSalerid(shop.getId());
				saleinfo.setTradeid(tradeId);
				saleinfo.setTitle(title);
				saleinfo.setAuctionid(auctionId);
				try {
					saleinfo.setBuyday(sdfdb.format(sdf.parse(day)));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				

				if (!DBDAO.checkUserNameExist(userName)) {
					Userinfo newUserinfo = new Userinfo();
					newUserinfo.setUsername(userName);				
					if(buyRate!=null && buyRate.trim().length()!=0){
						//buyRate = buyRate.replace("|", "%7C");
						newUserinfo.setBuyrate(buyRate);
						ScraperConfiguration buyConfig = new ScraperConfiguration("parseSaleRate.xml");
						Scraper buyScraper = new Scraper(buyConfig, ".");
						buyScraper.addVariableToContext("rateUrl",buyRate);
						Variable buyvariable = UserProxyRequestUrl.ParsingUrl(buyScraper);
						sr = new StringReader(buyvariable.toString());
						log.info(buyvariable.toString());
						reader = new SAXReader();
						Document buyDoc = reader.read(sr);							
						String buyLevell = buyDoc.getRootElement().selectSingleNode(
						"//buyLevel").getText();
						String msg = buyDoc.getRootElement().selectSingleNode(
						"//msg").getText();
						String[] address = null;
						try {
							address = parseAddress(buyDoc.getRootElement().selectSingleNode(
							"//address").getStringValue());
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}						
						if(msg.trim().length() == 0 || (msg.indexOf("盘点")==-1 && msg.indexOf("查封")==-1)){
							try {
								if(buyLevell.trim().length()>0){
									newUserinfo.setBuylevel(Integer.parseInt(buyLevell));								
									newUserinfo.setIscompaney(false);
									if(address != null){
										try {
											newUserinfo.setProvince(address[0]);
											newUserinfo.setCity(address[1]);
										} catch (NullPointerException e) {
											// TODO: handle exception
											if(DBDAO.provinces.get(address[0]) == null){
												newUserinfo.setProvince(address[0]);
												newUserinfo.setCity(address[1]);
											}
										}
										
									}
									newUserinfo.setPersonspace(personSpace);
									DBDAO.saveImmediately(newUserinfo);
								}									
							} catch (NumberFormatException e) {
								// TODO: handle exception
								if(msg.trim().length() == 0){
									Sys.out(new NumberFormatException(buyDoc.asXML()), "numformat exception");
								}
							}
						}
					}
					
					
				}					
							


				//给buyinfo添加product详细信息
				String detailUrl = productUrl;
				ScraperConfiguration detailConfig = new ScraperConfiguration("parseProductDetail.xml");
				Scraper detailScraper = new Scraper(detailConfig, ".");
				String funFirst1 = null;
				String funFirst2 = null;
				String nextUrl = null;
				String errorInfoUrl = null;
				int j = 0;
				while (detailUrl != null) {		
					if(++ j > 3){
						DBDAO.saveSaleInfo(saleinfo);
						break;
					}					
					log.info("productDetailUrl : '" + detailUrl+"'");
					detailScraper.addVariableToContext("productDetailUrl",
							detailUrl);
					variable = UserProxyRequestUrl.ParsingUrl(detailScraper);
					sr = new StringReader(variable.toString());
					log.info(variable.toString());
					reader = new SAXReader();
					Document detailDoc = reader.read(sr);
					Element productInfo = detailDoc.getRootElement().element(
							"productInfo");
					 funFirst1 = productInfo
							.elementTextTrim("funFirst");
					String funSecond1 = productInfo
							.elementTextTrim("funSecond");
					String funThird1 = productInfo
							.elementTextTrim("funThird");
					String funFour1 = productInfo
							.elementTextTrim("funFour");
					 funFirst2 = productInfo
							.elementTextTrim("funFirst1");
					String funSecond2 = productInfo
							.elementTextTrim("funSecond1");
					String funThird2 = productInfo
							.elementTextTrim("funThird1");
					String funFour2 = productInfo
							.elementTextTrim("funFour1");
					 nextUrl = productInfo.elementTextTrim("nextUrl");
					 errorInfoUrl = productInfo
							.elementTextTrim("errorInfoUrl");
					if (funFirst1.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst1);
						if(funfirstId != null){
							saleinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond1.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond1);
							if(funsecondId != null){
								saleinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird1.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird1);
							if(funthirdId != null){
								saleinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour1.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour1);
							if(funfourId != null){
								saleinfo.setFunfour((Integer)funfourId);
							}
						}
						DBDAO.saveSaleInfo(saleinfo);						
						detailUrl = null;
					} else if (funFirst2.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst2);
						if(funfirstId != null){
							saleinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond2.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond2);
							if(funsecondId != null){
								saleinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird2.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird2);
							if(funthirdId != null){
								saleinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour2.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour2);
							if(funfourId != null){
								saleinfo.setFunfour((Integer)funfourId);
							}
						}						
						DBDAO.saveSaleInfo(saleinfo);
						detailUrl = null;
					} else if (nextUrl.trim().length() > 0) {
						detailUrl = nextUrl;
					} else if (errorInfoUrl.trim().length() > 0) {
						detailUrl = errorInfoUrl;
					}else{						
						DBDAO.saveSaleInfo(saleinfo);			
						break;
					}
				}

			

			}
			this.page ++;
		}
	}

	
	
	/*
	public void parseUserSaleinfo(Shops shop) throws DocumentException, IOException {
		
		String lastchecktime = shop.getLastchecktime();
		Calendar lastcheck = null;
		if(lastchecktime != null){
			lastcheck = Calendar.getInstance();
			try {
				lastcheck.setTime(sdfdb.parse(shop.getLastchecktime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e);
				e.printStackTrace();
				
			}
		}
		
		String nextHref = shop.getSalerate();
		String currentPage = "1";
		int i = 0;
        while (currentPage != null) {
        	if(++ i > 100)
        		return;
			//new ScraperConfiguration(new InputSource(ips));
        	
        	saveIndex();
			ScraperConfiguration config = new ScraperConfiguration("parseSaleInfo.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("url", nextHref);
			scraper.addVariableToContext("posturl", nextHref);
			scraper.addVariableToContext("currentPage", currentPage);
			scraper.addVariableToContext("buyerOrSeller", "0");
			Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
			StringReader sr = new StringReader(variable.toString());
			log.info(variable.toString());
			SAXReader reader = new SAXReader();
			Document doc = reader.read(sr);
			String tempNextHref = doc.getRootElement().elementTextTrim(
					"nextHref");
			if (tempNextHref == null || tempNextHref.trim().length() == 0)
				currentPage = null;
			else
				currentPage = tempNextHref;
			Iterator<Element> iter = doc.getRootElement()
			.elementIterator("buyInfo");
			if(!iter.hasNext()){
				nextHref = null;
			}
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				String day = element.elementTextTrim("day");				
				if(day == null || day.trim().length() == 0){
					Sys.out(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
				}
				Calendar checktime = Calendar.getInstance();
				try {
					checktime.setTime(sdf.parse(day));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					log.error(e1);
					e1.printStackTrace();
				}
				if(lastchecktime != null){
					if(lastcheck.getTimeInMillis() >= checktime.getTimeInMillis()){						
						return;
					}
				}
				if(distanceDays(Calendar.getInstance(), checktime) > beforedays){
					return;
				}
				if(this.lastbuyday == null){
					try {
						this.lastbuyday = sdfdb.format(sdf.parse(day));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String productUrl = element.elementTextTrim("productUrl");
				String auctionId = null;
				String tradeId = null;
				try {
					if(productUrl.indexOf("item_num_id")==-1 && productUrl.indexOf("item_id")==-1){
						auctionId = productUrl.substring(productUrl
								.lastIndexOf('=') + 1);
						tradeId = productUrl.substring(productUrl.indexOf('=') + 1,
								productUrl.indexOf("&"));
						//System.out.println("trade id :"+tradeId);
						if (DBDAO.checkExistSaleinfo(tradeId)) {
							//nextHref = null;
							continue;
						}
					}else if (productUrl.indexOf("item_num_id")!=-1){
						continue;
					}else{
						log.error("unhandler product Url :"+ productUrl);
						continue;
						//Sys.out(new Exception("parse buyinfo occur an uncatch exception:"+productUrl),"parse buyinfo occur an uncatch exception:"+productUrl );
					}					
				} catch (Exception e) {
					log.error("parseductUrl:  " + productUrl);
				}
				String userName = element.elementTextTrim("userName");
				String personSpace = element.elementTextTrim("personSpace");
				String buyRate = element.elementTextTrim("buyRate");
				String price = element.elementTextTrim("price");				
				String title = element.elementTextTrim("title");
				if(!Sys.checkNotNullOrEmpty(new String[]{productUrl,auctionId,tradeId,userName,price,title})){
					Sys.out(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
				}
				SaleInfo saleinfo = new SaleInfo();
				if(userName.indexOf("买家")==-1 && userName.indexOf("**")==-1){
					try {
						saleinfo.setUsername(userName);
											
					} catch (RuntimeException e) {
						log.error(e);
					}
				}else{					
					try {
						saleinfo.setUsername(userName.split("：")[1]);											
					} catch (RuntimeException e) {
						saleinfo.setUsername(userName);
						log.error(e);
					}
				}
				
				saleinfo.setPersonspace(personSpace);
				saleinfo.setProducturl(productUrl);
				saleinfo.setPrice(Double.parseDouble(price));
				//saleinfo.setSellerid(mission.getId());
				saleinfo.setSalerid(shop.getId());
				saleinfo.setTradeid(tradeId);
				saleinfo.setTitle(title);
				saleinfo.setAuctionid(auctionId);
				try {
					saleinfo.setBuyday(sdfdb.format(sdf.parse(day)));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				

				if (!DBDAO.checkUserNameExist(userName)) {
					Userinfo newUserinfo = new Userinfo();
					newUserinfo.setUsername(userName);				
					if(buyRate!=null && buyRate.trim().length()!=0){
						//buyRate = buyRate.replace("|", "%7C");
						newUserinfo.setBuyrate(buyRate);
						ScraperConfiguration buyConfig = new ScraperConfiguration("parseSaleRate.xml");
						Scraper buyScraper = new Scraper(buyConfig, ".");
						buyScraper.addVariableToContext("rateUrl",buyRate);
						Variable buyvariable = UserProxyRequestUrl.ParsingUrl(buyScraper);
						sr = new StringReader(buyvariable.toString());
						log.info(buyvariable.toString());
						reader = new SAXReader();
						Document buyDoc = reader.read(sr);							
						String buyLevell = buyDoc.getRootElement().selectSingleNode(
						"//buyLevel").getText();
						String msg = buyDoc.getRootElement().selectSingleNode(
						"//msg").getText();
						String[] address = null;
						try {
							address = parseAddress(buyDoc.getRootElement().selectSingleNode(
							"//address").getStringValue());
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}						
						if(msg.trim().length() == 0 || (msg.indexOf("盘点")==-1 && msg.indexOf("查封")==-1)){
							try {
								if(buyLevell.trim().length()>0){
									newUserinfo.setBuylevel(Integer.parseInt(buyLevell));								
									newUserinfo.setIscompaney(false);
									if(address != null){
										try {
											newUserinfo.setProvince(address[0]);
											newUserinfo.setCity(address[1]);
										} catch (NullPointerException e) {
											// TODO: handle exception
											if(DBDAO.provinces.get(address[0]) == null){
												newUserinfo.setProvince(address[0]);
												newUserinfo.setCity(address[1]);
											}
										}
										
									}
									newUserinfo.setPersonspace(personSpace);
									DBDAO.saveImmediately(newUserinfo);
								}									
							} catch (NumberFormatException e) {
								// TODO: handle exception
								if(msg.trim().length() == 0){
									Sys.out(new NumberFormatException(buyDoc.asXML()), "numformat exception");
								}
							}
						}
					}
					
					
				}					
							


				//给buyinfo添加product详细信息
				String detailUrl = productUrl;
				ScraperConfiguration detailConfig = new ScraperConfiguration("parseProductDetail.xml");
				Scraper detailScraper = new Scraper(detailConfig, ".");
				String funFirst1 = null;
				String funFirst2 = null;
				String nextUrl = null;
				String errorInfoUrl = null;
				int j = 0;
				while (detailUrl != null) {		
					if(++ j > 3){
						DBDAO.saveSaleInfo(saleinfo);
						break;
					}					
					log.info("productDetailUrl : '" + detailUrl+"'");
					detailScraper.addVariableToContext("productDetailUrl",
							detailUrl);
					variable = UserProxyRequestUrl.ParsingUrl(detailScraper);
					sr = new StringReader(variable.toString());
					log.info(variable.toString());
					reader = new SAXReader();
					Document detailDoc = reader.read(sr);
					Element productInfo = detailDoc.getRootElement().element(
							"productInfo");
					 funFirst1 = productInfo
							.elementTextTrim("funFirst");
					String funSecond1 = productInfo
							.elementTextTrim("funSecond");
					String funThird1 = productInfo
							.elementTextTrim("funThird");
					String funFour1 = productInfo
							.elementTextTrim("funFour");
					 funFirst2 = productInfo
							.elementTextTrim("funFirst1");
					String funSecond2 = productInfo
							.elementTextTrim("funSecond1");
					String funThird2 = productInfo
							.elementTextTrim("funThird1");
					String funFour2 = productInfo
							.elementTextTrim("funFour1");
					 nextUrl = productInfo.elementTextTrim("nextUrl");
					 errorInfoUrl = productInfo
							.elementTextTrim("errorInfoUrl");
					if (funFirst1.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst1);
						if(funfirstId != null){
							saleinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond1.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond1);
							if(funsecondId != null){
								saleinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird1.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird1);
							if(funthirdId != null){
								saleinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour1.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour1);
							if(funfourId != null){
								saleinfo.setFunfour((Integer)funfourId);
							}
						}
						DBDAO.saveSaleInfo(saleinfo);						
						detailUrl = null;
					} else if (funFirst2.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst2);
						if(funfirstId != null){
							saleinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond2.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond2);
							if(funsecondId != null){
								saleinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird2.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird2);
							if(funthirdId != null){
								saleinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour2.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour2);
							if(funfourId != null){
								saleinfo.setFunfour((Integer)funfourId);
							}
						}						
						DBDAO.saveSaleInfo(saleinfo);
						detailUrl = null;
					} else if (nextUrl.trim().length() > 0) {
						detailUrl = nextUrl;
					} else if (errorInfoUrl.trim().length() > 0) {
						detailUrl = errorInfoUrl;
					}else{						
						DBDAO.saveSaleInfo(saleinfo);			
						break;
					}
				}

			

			}
			this.page ++;
		}
	}

	*/
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.id = readCurrentIndex();
			this.page = readPageIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error(e);
			Sys.out(e, "读取当前位置异常");
		}
		if (this.id == -1) {
			return;
		}
		Shops shop = DBDAO.findShop(this.id);
		if(shop == null){
			long minindex = DBDAO.getMinShopMissionId();			
			shop = DBDAO.findShop(minindex);
			this.id = minindex;
			try {
				saveIndex();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (shop != null) {
			try {
				
				parseUserSaleinfo(shop);
				if(this.lastbuyday != null){
					shop.setLastchecktime(lastbuyday);
					DBDAO.updateShopsLastcheckDate(shop);
				}
				this.page = 1L;		
				
				shop = getNextUser();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block

				log.error(e);
				Sys.out(e, e.getMessage());
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				log.error(e);
				Sys.out(e, e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				log.error(e);
				Sys.out(e, e.getMessage());
			}

		}

	}
	
	private long distanceDays(Calendar end,Calendar before){
		Calendar dis = Calendar.getInstance();
		dis.setTimeInMillis(end.getTimeInMillis() - before.getTimeInMillis());
		return dis.getTimeInMillis() / (1000 * 3600 * 24);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, DocumentException {
		// TODO Auto-generated method stub
		/*
		 * Properties pro = new Properties();
		 * pro.put(CollectBuyinfoThread.conti, "false");
		 * pro.put(CollectBuyinfoThread.currentIndex, String.valueOf(-1));
		 * pro.put(CollectBuyinfoThread.rate, String.valueOf(86400));
		 * pro.put(CollectBuyinfoThread.timespan, "1"); File file = new
		 * File(CollectBuyinfoThread.config); pro.store(new
		 * FileOutputStream(file), "taobao comments");
		 * System.out.println(file.getAbsolutePath());
		 */
		
		

		new Thread(new CollectSaleinfoThread()).start();
		/*CollectSaleinfoThread collectSaleinfoThread = new CollectSaleinfoThread();
		Shops shop = new Shops();
		shop.setSalerate("http://rate.taobao.com/user-rate-10986181.htm");
		collectSaleinfoThread.parseUserSaleinfo(shop);*/
		
		/*Calendar before = Calendar.getInstance();
		before.set(2009, 12, 1, 2, 2, 3);
		System.out.println(before.getTimeInMillis() / (1000 * 3600 * 24));
		Calendar end = Calendar.getInstance();
		end.set(2010, 12, 1, 2, 2, 3);
		System.out.println(end.getTimeInMillis() / (1000 * 3600 * 24));
		Calendar dis = Calendar.getInstance();
		dis.setTimeInMillis(end.getTimeInMillis() - before.getTimeInMillis());
		System.out.println(dis.getTimeInMillis() / (1000 * 3600 * 24));*/
		/*HtmlCleaner clear = new HtmlCleaner(new URL("http://www.taobao.com/"));
		//clear.
		clear.clean();
		clear.writeBrowserCompactXmlToFile("d:/a.txt");
		log.info(clear.getBrowserCompactXmlAsString());*/
	}

}
