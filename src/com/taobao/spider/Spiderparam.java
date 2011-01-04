package com.taobao.spider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpHost;

import com.taobao.spider.util.Handle;
import com.taobao.spider.util.ProductHandle;

public class Spiderparam extends Observable{
	
	private HttpHost httphost;
	
	private BlockingQueue<String> queue ;
	
	private ArrayList<Object> results = new ArrayList<Object>();
	
	private int type;
	
	public ArrayList<Object> getResults() {
		return results;
	}

	public void setResults(ArrayList<Object> results) {
		this.results = results;
	}

	public void resultJob(){
		this.queue.clear();
		
	}
			
	private String queueXpath;
	
	private String nextPage;
	
	private String nextXpath; 
	
	private Handle handle;
	 
	
	private String search;
	
	//private static StringBuffer sb = new StringBuffer("s.taobao.com/search?q=%D1%A5%D7%D3&sort=sale-desc&s=0");
	
	public static Spiderparam productList(String keyword) throws UnsupportedEncodingException{
		
		Spiderparam param = new Spiderparam();
		param.setType(MessageType.PRODUCT);
		param.setHttphost(new HttpHost("s.taobao.com",80));
		param.getQueue().add("/search?sort=sale-desc&s=0&q="+URLEncoder.encode(keyword, "gbk"));
		param.setQueueXpath("//li[@class='list-item'][position() >= 1]");
		//param.setQueueXpath("//li[@class='list-item']");
		return param;
		
	}
	
	public Spiderparam(){
		queue = new LinkedBlockingQueue<String>(100);
	}
	public Spiderparam(int len){
		queue = new LinkedBlockingQueue<String>(len);
	}
	public BlockingQueue<String> getQueue() {
		return queue;
	}
	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	public String getQueueXpath() {
		return queueXpath;
	}
	public void setQueueXpath(String queueXpath) {
		this.queueXpath = queueXpath;
	}
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getNextXpath() {
		return nextXpath;
	}
	public void setNextXpath(String nextXpath) {
		this.nextXpath = nextXpath;
	}
	
	public HttpHost getHttphost() {
		return httphost;
	}

	public void setHttphost(HttpHost httphost) {
		this.httphost = httphost;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Handle getHandle() {
		return handle;
	}

	public void setHandle(Handle handle) {
		this.handle = handle;
	}

	
}
