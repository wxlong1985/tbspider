package com.jimenghu;

import java.util.Iterator;
import java.util.List;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

/**
 * @author Star
 *
 *Use The Proxy
 *scraper.getHttpClientManager().setHttpProxy("proxy.wh", 3128); 
  scraper.getHttpClientManager().setHttpProxyCredentials("username", "password"); 
 */
public class ParseAllShop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ScraperConfiguration config = new ScraperConfiguration(
					"D:/EWorkSpace/WebHarvestTAOBAO/src/com/jimenghu/AllShop.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.setDebug(true);
			//long starttime = System.currentTimeMillis();
			scraper.execute();
			//long endtime = System.currentTimeMillis();
			//System.out.println("Spent time:" + (endtime - starttime));
			Variable mainCategory = scraper.getContext().getVar("result2");			
			System.out.println(mainCategory.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
