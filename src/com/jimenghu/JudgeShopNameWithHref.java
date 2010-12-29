package com.jimenghu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.businessalliance.hibernate.Userinfo;

public class JudgeShopNameWithHref {
	
	private final static Log log=LogFactory.getLog(JudgeShopNameWithHref.class);
	
	public static boolean JudgeShopNameWithHref(Userinfo userInfo) throws Exception{
		try {
			ScraperConfiguration config = new ScraperConfiguration(
					"D:/EWorkSpace/taobao/src/com/jimenghu/judgeShopNameWithHref.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("shopUrl",userInfo.getTaobaohref());			
			Variable variable = UserProxyRequestUrl.ParsingUrl(scraper);		
			boolean judge=Boolean.parseBoolean(variable.toString());
			log.info(judge+"   "+userInfo.getTaobaohref());
			return judge;
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("", e);
			throw(e);
		}		
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Userinfo userInfo=new Userinfo();
		userInfo.setTaobaohref("http://shop33982444.taobao.com/");
		JudgeShopNameWithHref.JudgeShopNameWithHref(userInfo);
	}

}
