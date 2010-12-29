package temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;

import com.businessalliance.hibernate.Favproduct;
import com.businessalliance.hibernate.Favshop;
import com.businessalliance.hibernate.Mission;
import com.jimenghu.DBDAO;
import com.jimenghu.Sys;
import com.jimenghu.UserProxyRequestUrl;
import com.jimenghu.client.Config;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class CollectFavInfoThread implements Runnable {

	public final static String configfile = "collectfav.ini";

	public final static String currentIndex = "currentIndex";

	private static final Log log = LogFactory
			.getLog(CollectFavInfoThread.class);

	private Long id;

	private void saveCurrentIndex() throws IOException {
		File file = new File(this.configfile);
		FileOutputStream fops = new FileOutputStream(file);
		Properties pro = new Properties();
		pro.put(this.currentIndex, id + "");
		pro.store(fops, "comments");
		fops.close();
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
				long a = Long.parseLong((String) currrent);
				if (a == -1) {
					this.id = DBDAO.getMinMissionId();
					saveCurrentIndex();
					return this.id;
				} else {
					this.id = Long.parseLong((String) currrent);
					return this.id;
				}

			} else {
				this.id = DBDAO.getMinMissionId();
				saveCurrentIndex();
				return this.id;
			}
		} else {
			this.id = DBDAO.getMinMissionId();
			saveCurrentIndex();
			return this.id;
		}

	}

	public Mission getNextUser() throws IOException {
		this.id++;
		saveCurrentIndex();
		return DBDAO.findMission(this.id);
	}

	public Mission getMinMissionUser() throws IOException {
		this.id = DBDAO.getMinMissionId();
		saveCurrentIndex();
		return DBDAO.findMission(this.id);
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			this.id = readCurrentIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error(e);
			Sys.out(e, "读取当前位置异常");
		}
		if (this.id == -1) {
			return;
		}
		Mission mission = DBDAO.findMission(this.id);
		if (mission == null) {
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
				parseUserFavorite(mission);
				mission = getNextUser();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
				Sys.out(e, e.getMessage());
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
				Sys.out(e, e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
				Sys.out(e, e.getMessage());
			}

		}

	}

	public static void parseUserFavorite(Mission mission)
			throws DocumentException, MalformedURLException,
			UnsupportedEncodingException, IOException {
		String userName = mission.getUsername();
		if(userName == null || userName.trim().length() == 0){
			return;
		}
		String favoriteProductUrl = null;
		String favoriteShopUrl = null;
		URL url = new URL(Config.readProperty(Sys.KEY_favheader,Sys.KEY_config)+ URLEncoder.encode(userName, "gbk"));
		String favPreUrl = Config.readProperty(Sys.KEY_favPreUrl,Sys.KEY_config);
		Source source = new Source(url);
		for (Iterator iter = source.findAllStartTags("a").iterator(); iter
				.hasNext();) {
			Element element = ((StartTag) iter.next()).getElement();
			// System.out.println(element.toString()+"
			// "+element.getTextExtractor()+"
			// "+element.getAttributeValue("href"));
			if (element.getTextExtractor().toString().equals("宝贝")) {

				favoriteProductUrl = element.getAttributeValue("href");
			} else if (element.getTextExtractor().toString().equals("店铺")) {
				favoriteShopUrl = element.getAttributeValue("href");
			}
		}
		if (!Sys.checkNotNullOrEmpty(new String[] { favoriteProductUrl,
				favoriteShopUrl })) {
			Sys.out(new NullPointerException("favoriteProductUrl\t"
					+ favoriteProductUrl + "\tfavoriteShopUrl\t"
					+ "favoriteProductUrl\t"), CollectFavInfoThread.class
					.getName()
					+ "appear null element(s)!");
		}

		// DBDAO.delUserFavProducts(mission.getId());
		String nextHref = favPreUrl + favoriteProductUrl;
		log.info("favoriteProductUrl: " + nextHref);
		while (nextHref != null) {
			ScraperConfiguration config = new ScraperConfiguration("ParseUserFavoriteProduct.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("fav", nextHref);
			Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
			log.info(variable.toString());
			StringReader sr = new StringReader(variable.toString());
			SAXReader reader = new SAXReader();
			Document doc = reader.read(sr);

			for (Iterator<org.dom4j.Element> favoriteProductLinkIte = doc
					.getRootElement().selectNodes("//product").iterator(); favoriteProductLinkIte
					.hasNext();) {
				org.dom4j.Element productLink = favoriteProductLinkIte.next();
				String productUrl = productLink.elementTextTrim("productUrl");
				String auctionId = productUrl.substring(productUrl
						.lastIndexOf("-") + 1, productUrl.lastIndexOf("."));
				String title = productLink.elementTextTrim("title");
				String sellerName = productLink.elementTextTrim("sellerName");
				String price = productLink.elementTextTrim("price");
				
				/**
				 * 开始验证
				 */
				Boolean checkResult=check(productUrl, price, title, sellerName);
				Check.printRes(checkResult);
				
				if (!Sys.checkNotNullOrEmpty(new String[] { productUrl, title,
						sellerName, price })) {
					Sys.out(new NullPointerException(doc.asXML()),
							CollectFavInfoThread.class.getName()
									+ "appear null element(s)!");
				}
				config = new ScraperConfiguration("parseFavProductCategory.xml");
				scraper = new Scraper(config, ".");
				scraper.addVariableToContext("productDetailUrl", productUrl);
				variable = UserProxyRequestUrl.ParsingUrl(scraper);
				log.info(variable.toString());
				StringReader srr = new StringReader(variable.toString());

				SAXReader readerr = new SAXReader();
				Document docc = readerr.read(srr);
				org.dom4j.Element productInfo = docc.getRootElement().element(
						"productInfo");
				String funFirst1 = productInfo.elementTextTrim("funFirst");

				String funFirst2 = productInfo.elementTextTrim("funFirst1");
				Favproduct favproduct = new Favproduct();
	 			if (funFirst1.trim().length() != 0) {
					String funSecond1 = productInfo
							.elementTextTrim("funSecond");
					String funThird1 = productInfo.elementTextTrim("funThird");
					String funFour1 = productInfo.elementTextTrim("funFour");

					Object funfirstId = DBDAO.funfirsts.get(funFirst1);
					if(funfirstId != null){
						favproduct.setFunfirst((Integer)funfirstId);
					}
					if(funSecond1.trim().length()>0){
						Object funsecondId = DBDAO.funseconds.get(funSecond1);
						if(funsecondId != null){
							favproduct.setFunsecond((Integer)funsecondId);
						}
					}
					
					if(funThird1.trim().length()>0){
						Object funthirdId = DBDAO.funthirds.get(funThird1);
						if(funthirdId != null){
							favproduct.setFunthird((Integer)funthirdId);
						}
					}
					
					if(funFour1.trim().length()>0){
						Object funfourId = DBDAO.funfours.get(funFour1);
						if(funfourId != null){
							favproduct.setFunfour((Integer)funfourId);
						}
					}

				} else if (funFirst2.trim().length() != 0) {
					String funSecond2 = productInfo
							.elementTextTrim("funSecond1");
					String funThird2 = productInfo.elementTextTrim("funThird1");
					String funFour2 = productInfo.elementTextTrim("funFour1");
					Object funfirstId = DBDAO.funfirsts.get(funFirst2);
					if(funfirstId != null){
						favproduct.setFunfirst((Integer)funfirstId);
					}
					if(funSecond2.trim().length()>0){
						Object funsecondId = DBDAO.funseconds.get(funSecond2);
						if(funsecondId != null){
							favproduct.setFunsecond((Integer)funsecondId);
						}
					}
					
					if(funThird2.trim().length()>0){
						Object funthirdId = DBDAO.funthirds.get(funThird2);
						if(funthirdId != null){
							favproduct.setFunthird((Integer)funthirdId);
						}
					}
					
					if(funFour2.trim().length()>0){
						Object funfourId = DBDAO.funfours.get(funFour2);
						if(funfourId != null){
							favproduct.setFunfour((Integer)funfourId);
						}
					}

				}
				favproduct.setAuctionid(auctionId);
				favproduct.setPrice(Double.parseDouble(price));
				favproduct.setProducturl(productUrl);
				favproduct.setTitle(title);
				favproduct.setSellername(sellerName);
				// favproduct.setUsername(userName);
				favproduct.setUserid(mission.getId());
				DBDAO.saveFavProduct(favproduct,mission.getUsername());
				log.info("favoriteProductUrl " + url);
			}
			nextHref = doc.getRootElement().elementTextTrim("nextHref");
			if (nextHref.trim().length() == 0) {
				nextHref = null;
			} else {
				nextHref = favPreUrl + nextHref;
				log.info("nextHref favProductUrl :" + nextHref);
			}

		}
		log.info("favproduct collect finish!");
		// DBDAO.delUserFavShops(mission.getId());
		nextHref = favPreUrl + favoriteShopUrl;
		while (nextHref != null) {
			log.info("favoriteShopUrl: " + nextHref);
			ScraperConfiguration config = new ScraperConfiguration("ParseUserFavoriteShop.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("fav", nextHref);
			Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);
			log.info(variable.toString());
			StringReader sr = new StringReader(variable.toString());

			SAXReader reader = new SAXReader();
			Document doc = reader.read(sr);

			for (Iterator<org.dom4j.Element> favoriteProductLinkIte = doc
					.getRootElement().selectNodes("//sellerName").iterator(); favoriteProductLinkIte
					.hasNext();) {
				org.dom4j.Element productLink = favoriteProductLinkIte.next();
				String sellerName = productLink.getTextTrim();
				if (!Sys.checkNotNullOrEmpty(new String[] { sellerName })) {
					continue;
				}
				Favshop favshop = new Favshop();
				// favshop.setSaveusername(userName);
				favshop.setSaveuserId(mission.getId());
				favshop.setSellername(sellerName);
				DBDAO.saveFavShops(favshop);
				log.info("favoriteShopUrl " + url);
			}
			nextHref = doc.getRootElement().elementTextTrim("nextHref");
			if (nextHref.trim().length() == 0) {
				nextHref = null;
			} else {
				nextHref = favPreUrl + nextHref;
				log.info("nextHref favShop :" + nextHref);
			}

		}
	}

	private static boolean check(String productUrl,String price,String title,String sellerName){
//		String productUrl2="",sellerName2="",title2="",price2="";
		/**
		 * 验证productUrl是否以http开头
		 */
		if(!Check.isUrl(productUrl))
			return true;
		
		/**
		 * 验证price是否为float类型
		 */
		if(!Check.isFloat(price))
			return true;
		return false;
		
		/**
		 * 下面两个属性属于字符串类型
		 */
//		sellerName2="String";
//		title2="String";
//		
//		try{
//			final String checkInfo = "checkCollectFavInfo.ini";
//			File file = new File(checkInfo);
//			Boolean flag=file.exists();
////			FileOutputStream fops = new FileOutputStream(file);
//			
//   			if(flag==true){
//   				final String productUrl3 = Config.readProperty("productUrl2", checkInfo);
//   				final String title3=Config.readProperty("title2", checkInfo);
//   				final String sellerName3=Config.readProperty("sellerName2", checkInfo);
//   				final String price3=Config.readProperty("price2", checkInfo);
//   				if(!(productUrl3.equals(productUrl2))){
//					Config.storeProperty("productUrl2", productUrl2, checkInfo);
//	   				return true;
//				}
//   				else if(!(title3.equals(title2))){
//					Config.storeProperty("title2", title2, checkInfo);
//	   				return true;
//				}
//				else if(!(sellerName3.equals(sellerName2))){
//					Config.storeProperty("sellerName2", sellerName2, checkInfo);
//					return true;
//				}
//				else if(!(price3.equals(price2))){
//					Config.storeProperty("price2", price2, checkInfo);
//					return true;
//				}
//   				return false;
//			}else{
//				file.createNewFile();
//				Config.storeProperty("productUrl2", productUrl2, checkInfo);
//				Config.storeProperty("title2", title2, checkInfo);
//				Config.storeProperty("sellerName2", sellerName2, checkInfo);
//				Config.storeProperty("price2", price2, checkInfo);
//				return false;
//			}
//				
//		}catch(Exception e){
//			e.printStackTrace();
//			return true;
//		}
	}
	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		/*
		 * Properties pro = new Properties();
		 * pro.put(CollectFavInfoThread.conti, "false");
		 * pro.put(CollectFavInfoThread.currentIndex, String.valueOf(10));
		 * pro.put(CollectFavInfoThread.rate, String.valueOf(86400));
		 * pro.put(CollectFavInfoThread.timespan, "1"); pro.store(new
		 * FileOutputStream(CollectFavInfoThread.config), "taobao comments");
		 */
		new Thread(new CollectFavInfoThread()).start();
		// log.info(Config.readProperty(Sys.KEY_favheader,Sys.KEY_config));

	}

}
