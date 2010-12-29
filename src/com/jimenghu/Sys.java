package com.jimenghu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jimenghu.client.Config;

public class Sys {

	public Thread buyinfoCollecterThread;

	public final static String KEY_mailfrom = "mailfrom";

	public String VALUE_mailfrom;

	public final static String KEY_mailsmtp = "smtp";

	public String VALUE_mailsmtp;

	public final static String KEY_mailusername = "mailusername";

	public String VALUE_mailusername;

	public final static String KEY_mailpwd = "mailpwd";

	public String VALUE_mailpwd;

	public final static String KEY_mailto = "mailto";

	public String VALUE_mailto;

	public final static String KEY_config = "config.ini";

	public String VALUE_config;

	public final static String KEY_lastUrl = "lasturl";

	public String VALUE_lastUrl;

	public final static String KEY_conti = "continue";

	public String VALUE_conti;

	public final static String KEY_repeat = "repeat";

	public String VALUE_repeat;

	public final static String KEY_firstUrl = "firstUrl";

	public String VALUE_firstUrl;

	public final static String KEY_firstrepeat = "firstrepeat";

	public String VALUE_firstrepeat;

	public final static String KEY_shopcollecttime = "shopcollecttime";

	public String VALUE_shopcollecttime;

	public final static String KEY_shopcollectdayspan = "shopcollectdayspan";

	public String VALUE_shopcollectdayspan;

	public final static String KEY_buyinfocollecttime = "buyinfocollecttime";

	public String VALUE_buyinfocollecttime;

	public final static String KEY_buyinfocollectdayspan = "buyinfocollectdayspan";

	public String VALUE_buyinfocollectdayspan;

	public final static String KEY_favcollectdayspan = "favcollectdayspan";

	public String VALUE_favcollectdayspan;

	public final static String KEY_favcollecttime = "favcollecttime";

	public String VALUE_favcollecttime;

	public final static String KEY_favheader = "favheader";

	public String VALUE_favheader;
	
	public final static String KEY_XMLPATH = "xmlpath";

	public String VALUE_XMLPATH;
	
	public final static String KEY_favPreUrl = "favpreurl";

	public String VALUE_favPreUrl;
	public Properties pros = new Properties();

	public final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	public final static SimpleDateFormat sdfdayspan = new SimpleDateFormat(
	"dd HH:mm:ss");

	private static final Log log = LogFactory.getLog(Sys.class);

	private static Sys sys;

	/*public static void saveProperties() {
		try {
			Sys.getInstance().pros.store(new FileOutputStream(Sys.KEY_config), "taobao config properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		}
	}*/
	
	public static String getTimeString(long between){
		  long day=between/(24*3600);
		   long hour=between%(24*3600)/3600;
		   long minute=between%3600/60;
		   long second=between%60;
		return day+" "+hour+":"+minute+":"+second;
	}
	
	/*public static void saveProperties(Properties pro,String file) {
		try {
			pro.store(new FileOutputStream(file), "taobao config properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
			Sys.out(e, "save property error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
			Sys.out(e, "save property error");
		}
	}*/
	

	/*public static Sys getInstance() {
		try {
			if (sys == null) {
				sys = new Sys();
				sys.pros.load(new FileInputStream(Sys.KEY_config));
			}
			return sys;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		}
		return null;
	}*/

	public final static void sendMail(String title, String msg) {
		try {
			log.info(title + " : " + msg);
			//Properties pro = Sys.getInstance().pros;
			//System.out.println(pro.get(Sys.KEY_lastUrl));
			Mail.send(Config.readProperty(Sys.KEY_mailfrom, Sys.KEY_config), Config.readProperty(Sys.KEY_mailsmtp,Sys.KEY_config), 
			Config.readProperty(Sys.KEY_mailusername, Sys.KEY_config),
			Config.readProperty(Sys.KEY_mailpwd, Sys.KEY_config), Config.readProperty(Sys.KEY_mailto, Sys.KEY_config)
			, title, msg);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			log.error(e1);
		}

	}
	
	public final static void sendMail(Exception e, String title) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SERVER ERROR  ERROR CLASS  "+e.getClass().getName()+"\r\n");
			StackTraceElement[] elements = e.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				sb.append(elements[i]+"\r\n");
				sb.append("\r\n");
			}
			log.error(sb.toString());
			Mail.send(Config.readProperty(Sys.KEY_mailfrom, Sys.KEY_config), Config.readProperty(Sys.KEY_mailsmtp,Sys.KEY_config), 
			Config.readProperty(Sys.KEY_mailusername, Sys.KEY_config),
			Config.readProperty(Sys.KEY_mailpwd, Sys.KEY_config), Config.readProperty(Sys.KEY_mailto, Sys.KEY_config)
			, title, sb.toString());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			log.error(e1);
		}

	}
	
	public final static void out(Exception e) {

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SERVER ERROR  ERROR CLASS  "+e.getClass().getName()+"\r\n");
			StackTraceElement[] elements = e.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				sb.append(elements[i]+"\r\n");
				sb.append("\r\n");
			}
			log.error(sb.toString());
			Mail.send("jimenghu@163.com", "smtp.163.com", "jimenghu", "lovefamily", "jimenghu@163.com", e.getMessage(), e.toString());

			System.exit(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			log.error(e1);
			System.exit(0);
		} 

	}

	

	public final static void out(Exception e, String title) {

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(title+"\r\nERROR CLASS :"+e.getClass().getName()+"\r\n");			
			StackTraceElement[] elements = e.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				sb.append(elements[i]);
				sb.append("\r\n");
			}
			log.error(title + " : " + sb);			
			//Properties pro = Sys.getInstance().pros;
			//pro.store(new FileOutputStream(Sys.KEY_config), "taobao system config");
			Mail.send(Config.readProperty(Sys.KEY_mailfrom, Sys.KEY_config), Config.readProperty(Sys.KEY_mailsmtp,Sys.KEY_config), 
					Config.readProperty(Sys.KEY_mailusername, Sys.KEY_config),
					Config.readProperty(Sys.KEY_mailpwd, Sys.KEY_config), Config.readProperty(Sys.KEY_mailto, Sys.KEY_config)
					, title, sb.toString() + "\nlastUrl : "
					+ Config.readProperty(Sys.KEY_lastUrl, Sys.KEY_config));
			System.exit(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			log.error(e1);
			System.exit(0);
		}

	}

	public final static boolean checkNotNullOrEmpty(String[] obj) {
		if (obj == null) {
			return false;
		} else {
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] == null || obj[i].trim().length() == 0)
					return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		//Sys.out(new Exception("null"), "title");
		Sys.sendMail("sendmail", "sendmail");
	}

}
