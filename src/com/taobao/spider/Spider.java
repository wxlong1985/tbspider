package com.taobao.spider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

public class Spider {
	
	public static Spider spider;
	
	public String  defaultSort = "&sort=sale-desc";
	
	public void parse(Spiderparam param){
		
	}
	
	public Spider(){
				
	}
	
	
	public static void main(String[] args) throws IOException {
		/*String url = "http://item.taobao.com/item.htm?id=8602891932";
		File file = new File("GetBuyUrl.xml");
		ScraperConfiguration config = new ScraperConfiguration(file);
		Scraper scraper = new Scraper(config, ".");
		scraper.addVariableToContext("url", url);
		scraper.setDebug(false);
		scraper.execute();
		Variable variable = scraper.getContext().getVar("result");
		StringReader sr = new StringReader(variable.toString());
		StringBuffer sb = new StringBuffer();
		
		char[] buf = new char[1024];
		int i = -1;
		while((i = sr.read(buf, 0, buf.length))!= -1){
			sb.append(new String(buf,0,i));
		}
		System.out.println(sb.toString());*/
		/*SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(sr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}*/
		File file = new File("d:/log.txt");
		System.setOut(new PrintStream(new FileOutputStream(file)));
		String keyword = "福建";
		System.out.println(URLEncoder.encode(keyword, "gbk"));
	}

}
