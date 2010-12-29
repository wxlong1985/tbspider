package com.jimenghu;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Priority;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;
import org.webharvest.exception.ScraperXQueryException;
import org.webharvest.exception.ScriptException;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

public class UserProxyRequestUrl implements Runnable {

	private final static Log log = LogFactory.getLog(UserProxyRequestUrl.class);

	private Scraper scraper;

	public boolean running = false;

	public boolean once = false;

	public UserProxyRequestUrl(Scraper scraper) {
		this.scraper = scraper;
	}

	public final static synchronized Variable ParsingUrl(Scraper scraper) {

		while (true) {
			
			try {
				UserProxyRequestUrl userProxyRequestUrl = new UserProxyRequestUrl(scraper);
				Thread thread = new Thread(userProxyRequestUrl);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.start();
				thread.join(10000);
				while (!userProxyRequestUrl.once) {
					Thread.sleep(1);
				}
				if (userProxyRequestUrl.running) {
					scraper.stopExecution();

					userProxyRequestUrl.running = false;
					thread.interrupt();
					Scraper copyScraper = new Scraper(scraper
							.getConfiguration(), ".");
					
					for (Iterator iter = scraper.getContext().keySet()
							.iterator(); iter.hasNext();) {
						String element = (String) iter.next();
						if (!element.equalsIgnoreCase("sys")
								&& !element.equalsIgnoreCase("content")
								&& !element.equalsIgnoreCase("http")) {
							copyScraper.addVariableToContext(element, scraper
									.getContext().get(element).toString());
						}
					}
					scraper.dispose();
					scraper = copyScraper;
					continue;
				}
				return scraper.getContext().getVar("result");
			} catch (Exception e) {
				log.error(Thread.currentThread().getName() + "    " + e);
				System.exit(0);
				return null;
			}
			
		}
		// log.info("userProxyRequestUrl.running"+userProxyRequestUrl.running+"\tuserProxyRequestUrl.once"+userProxyRequestUrl.once+"\tthread.isAlive()"+thread.isAlive());

	}

	public void run() {
		// TODO Auto-generated method stub
		if (scraper == null)
			return;
		running = true;
		while (running) {
			try {
				// proxyCatcher.setProxy(scraper);
				once = true;
				scraper.execute();
				running = false;
				break;
			}/*
				 * catch(org.webharvest.exception.HttpException e){ try {
				 * Thread.sleep(5); } catch (InterruptedException e1) {
				 * //e1.printStackTrace(); log.error("sleep
				 * InterruptedException"+Thread.currentThread().getName()+"
				 * "+e1); } continue; }
				 */catch (Exception e) {
				if (e instanceof InterruptedException
						|| e instanceof IllegalStateException
						|| e instanceof HttpException
						|| e instanceof ScraperXQueryException
						) {
					
					//log.error(Thread.currentThread().getName() + e);
					break;
				} else {
					log.error(scraper.getConfiguration().getSourceFile().getName()+"   (" + e.getClass().getName()+")" + e);
					Sys.out(e, e.getMessage());
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			throw new NullPointerException();
		} catch (Exception e) {
			// TODO: handle exception
			if (e instanceof NullPointerException) {
				System.out.println("null point exception");

			}
		}

	}
}
