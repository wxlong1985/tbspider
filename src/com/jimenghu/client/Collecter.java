package com.jimenghu.client;

import com.jimenghu.ParseSecondCategoryShops;
import com.taobao.collectbuyinfo.CollectBuyinfoThread;
import com.taobao.collectfavinfo.CollectFavInfoThread;
import com.taobao.collectsaleinfo.CollectSaleinfoThread;

public class Collecter implements Runnable{
	
	private static Thread parseShopThread;
	
	private static Thread collectbuyinfoThread;
	
	private static Thread collectfavThread;
	
	private static Thread collectsaleinfoThread;
	
	public Collecter() {
		// TODO Auto-generated constructor stub
		Collecter.parseShopThread = new Thread(new ParseSecondCategoryShops());
		Collecter.collectbuyinfoThread = new Thread(new CollectBuyinfoThread());
		Collecter.collectfavThread = new Thread(new CollectFavInfoThread());
		Collecter.collectsaleinfoThread = new Thread(new CollectSaleinfoThread());
		Collecter.parseShopThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.parseShopThread.setDaemon(true);
		Collecter.collectbuyinfoThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.collectbuyinfoThread.setDaemon(true);
		Collecter.collectfavThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.collectfavThread.setDaemon(true);
		Collecter.collectsaleinfoThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.collectsaleinfoThread.setDaemon(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collecter collecter = new Collecter();
		Thread thread = new Thread(collecter);
		thread.start();
	}

	public void run() {
		// TODO Auto-generated method stub
		Collecter.parseShopThread.start();
		Collecter.collectbuyinfoThread.start();
		Collecter.collectfavThread.start();
		Collecter.collectsaleinfoThread.start();
	}

}
