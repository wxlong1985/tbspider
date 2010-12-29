package com.jimenghu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.businessalliance.hibernate.Buyinfo;
import com.businessalliance.hibernate.Product;
import com.businessalliance.hibernate.Userinfo;

/**
 * @author Star
 * 
 */
public class ParseProduct {

	private final static Log log = LogFactory.getLog(ParseProduct.class);

	private final static String endFlag = "?is_start=true";

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * �û���Ϣ����ڡ����������
	 * 
	 * @param productUrl
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	public static boolean parseProduct(String productUrl)
			throws DocumentException, MalformedURLException,
			UnsupportedEncodingException, IOException {
		ScraperConfiguration detailConfig = new ScraperConfiguration("parseProductDetail.xml");
		Scraper detailScraper = new Scraper(detailConfig, ".");
		String funFirst1 = null;
		String funFirst2 = null;
		String nextUrl = null;
		String errorInfoUrl = null;
		String detailUrl = null;
		String productDetailUrl = null;
		log.info("productDetailUrl : '" + productUrl + "'");
		detailUrl = productUrl;
		while (detailUrl != null) {
			detailScraper.addVariableToContext("productDetailUrl", detailUrl);
			detailScraper.execute();
			Variable variable = detailScraper.getContext().getVar("result");
			StringReader sr = new StringReader(variable.toString());
			log.info(variable.toString());
			SAXReader reader = new SAXReader();
			Document detailDoc = reader.read(sr);
			Element productInfo = detailDoc.getRootElement().element(
					"productInfo");
			funFirst1 = productInfo.elementTextTrim("funFirst");
			String funSecond1 = productInfo.elementTextTrim("funSecond");
			String funThird1 = productInfo.elementTextTrim("funThird");
			String funFour1 = productInfo.elementTextTrim("funFour");
			funFirst2 = productInfo.elementTextTrim("funFirst1");
			String funSecond2 = productInfo.elementTextTrim("funSecond1");
			String funThird2 = productInfo.elementTextTrim("funThird1");
			String funFour2 = productInfo.elementTextTrim("funFour1");
			nextUrl = productInfo.elementTextTrim("nextUrl");
			productDetailUrl = productInfo.elementTextTrim("detailUrl");
			errorInfoUrl = productInfo.elementTextTrim("errorInfoUrl");
			if (funFirst1.length() > 0) {
				return true;
			} else if (funFirst2.length() > 0) {
				return true;
			} else if (nextUrl.trim().length() > 0) {
				detailUrl = nextUrl;
			} else if (errorInfoUrl.trim().length() > 0) {
				detailUrl = errorInfoUrl;
			}  else if (productDetailUrl.trim().length() > 0) {
				detailUrl = productDetailUrl;
			}else {
				return true;
			}
		}
		return false;

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws MalformedURLException,
			UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		String productUrl = "http://auction1.taobao.com/auction/trade_detail.htm?item_id=&seller_id=&trade_id=2243918603&post_fee=&post_fee_type=&alipay_no=&is_archive=&page=&page_size=&x_id=";
		try {

			ParseProduct.parseProduct( productUrl);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.info(e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.info(e);
		}
		/*
		 * Product product=new Product(); product.setTitle("title");
		 * product.setPrice(new Float(33)); product.setProductUrl("productUrl");
		 * String productTableName=GetTableName.getTableName();
		 * 
		 * DBDAO.saveProduct(product, productTableName);
		 */
		/*
		 * String
		 * url="http://auction1.taobao.com/auction/trade_detail.htm?trade_id=1571010966";
		 * System.out.println();
		 */
	}
}
