package com.jimenghu;

public class TestThreadIntterurpt implements Runnable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestThreadIntterurpt test = new TestThreadIntterurpt();
		Thread thread = new Thread(test);
		thread.start();
		while(true)
		{
			System.out.println(Thread.currentThread().getName());
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		int index = 0;
		while(true)
		{
			if(index ++ > 100){
				//Thread.currentThread().
				
			}
			System.out.println(Thread.currentThread().getName()  + index);
		}
	}

}
