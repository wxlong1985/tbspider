package com.jimenghu;

import java.util.Calendar;

public class TestThreadJoin implements Runnable{
	
	
	
	
	private boolean run = false;
	
	private static Object lock = new Object();

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		TestThreadJoin testThreadJoin =  new TestThreadJoin();
		Thread thread = new Thread(testThreadJoin);
		thread.start();
		int count = 0;
		while(true){
			count++;
			System.out.println(Thread.currentThread().getName() + "   "+count);			
		} 

	}

	public void run() {
		// TODO Auto-generated method stub
		run = true;
		 
			long i = 0;
			while(true){
				i++;
				System.out.println(Thread.currentThread().getName() + "   "+i);
			} 
	}

}
