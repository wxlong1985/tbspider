package com.taobao.collectbuyinfo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

import com.businessalliance.hibernate.Buyinfo;
import com.businessalliance.hibernate.Mission;
import com.businessalliance.hibernate.Product;
import com.businessalliance.hibernate.Userinfo;
import com.jimenghu.DBDAO;
import com.jimenghu.Sys;
import com.jimenghu.UserProxyRequestUrl;

public class CollectBuyinfoThread implements Runnable {
	
	private String currentIndex = "currentIndex";
	
	private String configfile = "collectbuyinfo.ini";

	private static final Log log = LogFactory
			.getLog(CollectBuyinfoThread.class);

	private Long id;
	
	/**
	 * 用户购买的最后日期
	 */
	private String lastbuyday ;
	

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"[yyyy.MM.dd HH:mm:ss]");	
	
	private final static SimpleDateFormat sdfdb = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	private void saveCurrentIndex() throws IOException{
		File file = new File(this.configfile);
		FileOutputStream fops = new FileOutputStream(file);
		Properties pro = new Properties();
		pro.put(this.currentIndex, id + "");
		pro.store(fops, "comments");
		fops.close();
	}
	
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
	
	private final static String endUrl = "--buyerOrSeller%7C1--receivedOrPosted%7C0--goodNeutralOrBad%7C--timeLine%7C-210--detailed%7C1--ismore%7C1--showContent%7C--currentPage%7C1.htm#RateType";
		
	/** 读取当前值，如果存在返回，如果不存在存储最小值为当前值，并保存配置文件。
	 * @return
	 * @throws IOException
	 */
	private long readCurrentIndex() throws IOException{
		File file = new File(this.configfile);
		if(file.exists()){
			FileInputStream fips = new FileInputStream(file);
			Properties pro = new Properties();
			pro.load(fips);
			fips.close();
			Object currrent = pro.get(this.currentIndex);
			if(currrent != null){
				long a = Long.parseLong((String)currrent);
				if(a == -1){
					this.id = DBDAO.getMinMissionId();
					saveCurrentIndex();
					return this.id;
				}else{
					this.id = Long.parseLong((String)currrent); 
					return this.id;
				}
				
			}else{
				this.id = DBDAO.getMinMissionId();
				saveCurrentIndex();
				return this.id;
			}
		}else{
			this.id = DBDAO.getMinMissionId();
			saveCurrentIndex();
			return this.id;
		}
		
	}

	public Mission getNextUser() throws IOException {		
		this.id ++;
		saveCurrentIndex();
		return DBDAO.findMission(this.id);
	}
	
	

	public void parseUserBuyinfo(Mission mission)
			throws FileNotFoundException, DocumentException {
		
		String lastchecktime = mission.getLastchecktime();
		Calendar lastcheck = null;
		if(lastchecktime != null){
			lastcheck = Calendar.getInstance();
			try {
				lastcheck.setTime(sdfdb.parse(mission.getLastchecktime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e);
				e.printStackTrace();
				
			}
		}
		String nextHref = mission.getBuyrate().replace(".htm", "") + endUrl;
		while (nextHref != null && nextHref.trim().length()>0) {
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
						if (DBDAO.checkExistBuyinfo(tradeId)) {
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
				Buyinfo buyinfo = new Buyinfo();
				buyinfo.setSalername(userName);
				buyinfo.setPersonspace(personSpace);
				buyinfo.setProducturl(productUrl);
				buyinfo.setPrice(Double.parseDouble(price));
				//buyinfo.setSellerid(mission.getId());
				buyinfo.setBuyerid(mission.getId());
				buyinfo.setTradeid(tradeId);
				buyinfo.setTitle(title);
				buyinfo.setAuctionid(auctionId);
				try {
					buyinfo.setBuyday(sdfdb.format(sdf.parse(day)));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				

				if (!DBDAO.checkUserNameExist(userName)) {
					Userinfo newUserinfo = new Userinfo();
					newUserinfo.setUsername(userName);				
					if(buyRate!=null && buyRate.trim().length()!=0){
						buyRate = buyRate.replace("|", "%7C");
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
						DBDAO.saveBuyInfo(buyinfo);
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
							buyinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond1.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond1);
							if(funsecondId != null){
								buyinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird1.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird1);
							if(funthirdId != null){
								buyinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour1.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour1);
							if(funfourId != null){
								buyinfo.setFunfour((Integer)funfourId);
							}
						}
						DBDAO.saveBuyInfo(buyinfo);						
						detailUrl = null;
					} else if (funFirst2.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst2);
						if(funfirstId != null){
							buyinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond2.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond2);
							if(funsecondId != null){
								buyinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird2.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird2);
							if(funthirdId != null){
								buyinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour2.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour2);
							if(funfourId != null){
								buyinfo.setFunfour((Integer)funfourId);
							}
						}						
						DBDAO.saveBuyInfo(buyinfo);
						detailUrl = null;
					} else if (nextUrl.trim().length() > 0) {
						detailUrl = nextUrl;
					} else if (errorInfoUrl.trim().length() > 0) {
						detailUrl = errorInfoUrl;
					}else{						
						DBDAO.saveBuyInfo(buyinfo);			
						break;
					}
				}

			

			}

		}
	}

	
	

/*
	public void parseUserBuyinfo(Mission mission,String url,String posturl)
			throws FileNotFoundException, DocumentException {
		
		String lastchecktime = mission.getLastchecktime();
		Calendar lastcheck = null;
		if(lastchecktime != null){
			lastcheck = Calendar.getInstance();
			try {
				lastcheck.setTime(sdfdb.parse(mission.getLastchecktime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e);
				e.printStackTrace();
				
			}
		}
		String nextHref = url.replace("|", "%7C");
		String currentPage = "1";
		int i = 0;
        while (currentPage != null) {
        	if(++ i > 100)
        		return;
			ScraperConfiguration config = new ScraperConfiguration("parseSaleInfo.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("url", nextHref);
			scraper.addVariableToContext("currentPage", currentPage);
			scraper.addVariableToContext("posturl", posturl);
			scraper.addVariableToContext("buyerOrSeller", "1");
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
						if (DBDAO.checkExistBuyinfo(tradeId)) {
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
				Buyinfo buyinfo = new Buyinfo();
				buyinfo.setSalername(userName);
				buyinfo.setPersonspace(personSpace);
				buyinfo.setProducturl(productUrl);
				buyinfo.setPrice(Double.parseDouble(price));
				//buyinfo.setSellerid(mission.getId());
				buyinfo.setBuyerid(mission.getId());
				buyinfo.setTradeid(tradeId);
				buyinfo.setTitle(title);
				buyinfo.setAuctionid(auctionId);
				try {
					buyinfo.setBuyday(sdfdb.format(sdf.parse(day)));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				

				if (!DBDAO.checkUserNameExist(userName)) {
					Userinfo newUserinfo = new Userinfo();
					newUserinfo.setUsername(userName);				
					if(buyRate!=null && buyRate.trim().length()!=0){
						buyRate = buyRate.replace("|", "%7C");
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
						DBDAO.saveBuyInfo(buyinfo);
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
							buyinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond1.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond1);
							if(funsecondId != null){
								buyinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird1.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird1);
							if(funthirdId != null){
								buyinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour1.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour1);
							if(funfourId != null){
								buyinfo.setFunfour((Integer)funfourId);
							}
						}
						DBDAO.saveBuyInfo(buyinfo);						
						detailUrl = null;
					} else if (funFirst2.length() > 0) {
						Object funfirstId = DBDAO.funfirsts.get(funFirst2);
						if(funfirstId != null){
							buyinfo.setFunfirst((Integer)funfirstId);
						}
						if(funSecond2.trim().length()>0){
							Object funsecondId = DBDAO.funseconds.get(funSecond2);
							if(funsecondId != null){
								buyinfo.setFunsecond((Integer)funsecondId);
							}
						}
						
						if(funThird2.trim().length()>0){
							Object funthirdId = DBDAO.funthirds.get(funThird2);
							if(funthirdId != null){
								buyinfo.setFunthird((Integer)funthirdId);
							}
						}
						
						if(funFour2.trim().length()>0){
							Object funfourId = DBDAO.funfours.get(funFour2);
							if(funfourId != null){
								buyinfo.setFunfour((Integer)funfourId);
							}
						}						
						DBDAO.saveBuyInfo(buyinfo);
						detailUrl = null;
					} else if (nextUrl.trim().length() > 0) {
						detailUrl = nextUrl;
					} else if (errorInfoUrl.trim().length() > 0) {
						detailUrl = errorInfoUrl;
					}else{						
						DBDAO.saveBuyInfo(buyinfo);			
						break;
					}
				}

			

			}

		}
	}
*/
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.id = readCurrentIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
			Sys.out(e, "读取当前位置异常");
		}
		if(this.id == -1){
			return;
		}		
		Mission mission = DBDAO.findMission(this.id);
		if(mission == null){
			long minindex = DBDAO.getMinMissionId();			
			mission = DBDAO.findMission(minindex);
			this.id = minindex;
			try {
				saveCurrentIndex();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (mission != null) {				
				try {
					this.lastbuyday = null;
					String posturl = mission.getBuyrate();
					ScraperConfiguration config = new ScraperConfiguration("GetBuyUrl.xml");
					Scraper scraper = new Scraper(config, ".");
					scraper.addVariableToContext("url", posturl);
					Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
					StringReader sr = new StringReader(variable.toString());
					log.info(variable.toString());
					SAXReader reader = new SAXReader();
					Document doc = reader.read(sr);
					String allsalePreSixUrl = doc.getRootElement().elementTextTrim("allsalePreSixUrl");
					String allsaleLastSixUrl = doc.getRootElement().elementTextTrim("allsaleLastSixUrl");
					String allbuyPreSixUrl = doc.getRootElement().elementTextTrim("allbuyPreSixUrl");
					String allbuyLastSixUrl = doc.getRootElement().elementTextTrim("allbuyLastSixUrl");
					if(!Sys.checkNotNullOrEmpty(new String[]{allsalePreSixUrl,allsaleLastSixUrl})){
						Sys.out(new NullPointerException(doc.asXML()), config.getSourceFile().getName()+" appear null element(s)!");
					}
					if(allbuyLastSixUrl != null){
						//parseUserBuyinfo(mission, allbuyPreSixUrl,posturl);
						parseUserBuyinfo(mission);
						if(this.lastbuyday != null){
							mission.setLastchecktime(lastbuyday);
							DBDAO.updateMissionLastcheckDate(mission);
						}
						this.lastbuyday = null;
						//parseUserBuyinfo(mission, allbuyLastSixUrl,posturl);
						/*parseUserBuyinfo(mission);
						if(this.lastbuyday != null){
							mission.setLastchecktime(lastbuyday);
							DBDAO.updateMissionLastcheckDate(mission);
						}*/
					}else{
						//parseUserBuyinfo(mission, allsalePreSixUrl,posturl);
						parseUserBuyinfo(mission);
						if(this.lastbuyday != null){
							mission.setLastchecktime(lastbuyday);
							DBDAO.updateMissionLastcheckDate(mission);
						}
						this.lastbuyday = null;
						//parseUserBuyinfo(mission, allsaleLastSixUrl,posturl);
						/*parseUserBuyinfo(mission);
						if(this.lastbuyday != null){
							mission.setLastchecktime(lastbuyday);
							DBDAO.updateMissionLastcheckDate(mission);
						}*/
					}					
					mission = getNextUser();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					
					log.error(e);
					Sys.out(e, e.getMessage());
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					log.error(e);
					Sys.out(e, e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					log.error(e);
					Sys.out(e, e.getMessage());
				}
				
		}

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		/*Properties pro = new Properties();
		pro.put(CollectBuyinfoThread.conti, "false");
		pro.put(CollectBuyinfoThread.currentIndex, String.valueOf(-1));
		pro.put(CollectBuyinfoThread.rate, String.valueOf(86400));
		pro.put(CollectBuyinfoThread.timespan, "1");
		File file = new File(CollectBuyinfoThread.config);
		pro.store(new FileOutputStream(file), "taobao comments");
		System.out.println(file.getAbsolutePath());*/
		
		new Thread(new CollectBuyinfoThread()).start();
		
		
		
	}

}
