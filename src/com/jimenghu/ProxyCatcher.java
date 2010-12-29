package com.jimenghu;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.net.SocketAddress;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;


class Proxy{
	String ip;
	int port;
	public Proxy(String ip,String port) {
		// TODO Auto-generated constructor stub
		this.ip=ip;
		this.port=Integer.parseInt(port);
	}
	
}

public class ProxyCatcher implements Runnable{
	
	private ArrayList<Proxy> proxys=new ArrayList<Proxy>();
	
	private final static int maxProxyNum=100;
	
	private final static String proxyUrl="http://www.proxycn.com/html_proxy/30fastproxy-1.html";
	
	private Log log=LogFactory.getLog(ProxyCatcher.class);
	
	private int index=0;
	
	public void setProxy(Scraper scraper){
		if(proxys.size()>0){
			Proxy proxy=proxys.get(index);
			if(index>proxys.size()-1){
				index=0;
			}
			log.fatal("Proxy :"+proxy.ip+" "+proxy.port);
			scraper.getHttpClientManager().setHttpProxy(proxy.ip, proxy.port);
			index++;
		}
	}
	
	
	
	public void run(){
		log.fatal("runing.............................");
		while(true){
			ScraperConfiguration config;
			try {
				config = new ScraperConfiguration(
				"D:/EWorkSpace/taobao/src/com/jimenghu/parseProxys.xml");
				Scraper scraper = new Scraper(config, ".");
				scraper.addVariableToContext("proxyUrl", proxyUrl);
				int i=0;
				while(i<2){
					log.fatal("parsing........................");
					try {
						scraper.execute();
						Variable variable=scraper.getContext().getVar("result");
						
						StringReader sr=new StringReader(variable.toString());
						SAXReader reader=new SAXReader();
						Document doc=reader.read(sr);						
						for(Iterator<Element> ite=doc.getRootElement().selectNodes("//proxy").iterator();ite.hasNext();){
							Element element=ite.next();
							String ip=element.elementTextTrim("ip");
							String regex="clip\\('(.*)'\\);";
							Pattern pattern=Pattern.compile(regex);
							Matcher matcher=pattern.matcher(ip);
							String tempIp=null;
							String port=null;
							if(matcher.find()){
								String[] proxySp=matcher.group(1).split(":");
								tempIp=proxySp[0];
								port=proxySp[1];
							}
							log.fatal(tempIp);
							log.fatal(port);
							if(proxys.size()>maxProxyNum){
								proxys.remove(0);
								proxys.add(new Proxy(tempIp,port));
							}else{
								proxys.add(new Proxy(tempIp,port));
							}								
						}						
						break;
					} catch (Exception e) {
						// TODO: handle exception
						log.fatal(e);
					}
					i++;
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				log.fatal(e);
			}			
			try {
				Thread.sleep(1000*60*60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				log.fatal(e);
			}
		}
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tempip="clip('193.37.152.216:3128');alert('脪脩驴陆卤麓碌陆录么脤霉掳氓!')";
		String regex="clip\\('(.*)'\\);";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(tempip);
		if(matcher.find()){
			System.out.println(matcher.group(1));
		}
	}

}
