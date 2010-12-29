package com.jimenghu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Resume {
	

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Properties pro = new Properties();
		pro.put(Sys.KEY_mailfrom, "jimenghu@163.com");
		pro.put(Sys.KEY_mailpwd, "lovefamily");
		pro.put(Sys.KEY_mailsmtp, "smtp.163.com");
		pro.put(Sys.KEY_mailto, "jimenghu@163.com");
		pro.put(Sys.KEY_mailusername, "jimenghu");
		pro.put(Sys.KEY_firstUrl, "http://search1.taobao.com/browse/shop-15.htm?&page=2847");
		pro.put(Sys.KEY_lastUrl, "http://search1.taobao.com/browse/shop-15.htm?&page=2848");
		pro.put(Sys.KEY_conti, "true");
		pro.put(Sys.KEY_repeat, "true");
		pro.put(Sys.KEY_firstrepeat, "true");
		pro.put(Sys.KEY_buyinfocollectdayspan, "1");
		pro.put(Sys.KEY_buyinfocollecttime, "2009-07-23 08:08:08");
		pro.put(Sys.KEY_buyinfocollectdayspan, "1");
		pro.put(Sys.KEY_favcollecttime, "2009-07-23 08:08:08");
		pro.put(Sys.KEY_favcollectdayspan, "1");
		pro.put(Sys.KEY_shopcollecttime, "2009-07-23 08:08:08");
		pro.put(Sys.KEY_shopcollectdayspan, "1");
		pro.put(Sys.KEY_favheader, "http://favorite.taobao.com/collectList.htm?usernick=");
		pro.put(Sys.KEY_XMLPATH, "D:/EWorkSpace/taobao/src/com/jimenghu/");
		pro.put(Sys.KEY_favPreUrl, "http://favorite.taobao.com");
		File file = new File(Sys.KEY_config);
		System.out.println(file.getAbsolutePath());
		pro.store(new FileOutputStream(file), "taobao system config");
	}

}
