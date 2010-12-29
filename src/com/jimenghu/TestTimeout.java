package com.jimenghu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.dom4j.DocumentException;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

import com.sun.corba.se.impl.orbutil.closure.Future;

public class TestTimeout implements Runnable{
	
	private boolean out = false;
	
	public TestTimeout() {
		// TODO Auto-generated constructor stub
	}
	 
	
	public static void executeTimeControlMethod() {
        // 创建一个使用单个 worker 线程的 Executor
        ExecutorService service = Executors.newSingleThreadExecutor();
        //提交一个 Runnable 任务用于执行，跟踪一个或多个异步任务执行状况而生成 Future 的方法。
        //submit(Runnable task, T result) task - 要提交的任务 result - 返回的结果
        TestTimeout testTimeout = new TestTimeout();
        Thread thread = new Thread(testTimeout);
        
        try {
        	service.execute(thread);
        	
        	int count = 0;
          while(!service.isTerminated()){
        	  service.awaitTermination(3, TimeUnit.SECONDS);
        	  /*count++;
        	  if(count == 2){
        		  testTimeout.shutDown();
        		  break;
        	  }*/
          }
          System.out.println("   out  finish");
          service.shutdown();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		executeTimeControlMethod();
	}

	public void run() {
		// TODO Auto-generated method stub
		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration(
					"D:/EWorkSpace/taobao/src/com/jimenghu/ParseSecondCategoryShops.xml");
			String firstPage = "http://list.taobao.com/browse/shop-15.htm?page=2799";
			while (firstPage != null) {
				Scraper scraper = new Scraper(config, ".");
				scraper.addVariableToContext("firstUrl", firstPage);				
				//scraper.setDebug(true);
				//long starttime = System.currentTimeMillis();
				UserProxyRequestUrl.ParsingUrl(scraper);
				//long endtime = System.currentTimeMillis();
				//log.fatal("Spent time:" + (endtime - starttime));
				Variable userInfos = scraper.getContext().getVar("result");
				System.out.println(userInfos.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void shutDown()
	{
		System.out.println("shutdown");
		out = true;
	}

}
