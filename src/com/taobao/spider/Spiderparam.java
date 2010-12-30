package com.taobao.spider;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Spiderparam {
	
	private String url;
	
	private BlockingQueue<String> queue ;
	
	private String queueXpath;
	
	private String nextPage;
	
	private String nextXpath;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
