package com.jimenghu;

import java.util.Calendar;

public class TestTimeSpan {
	
	public static String getTimeString(long between){
		  long day=between/(24*3600);
		   long hour=between%(24*3600)/3600;
		   long minute=between%3600/60;
		   long second=between%60;
		return day+" "+hour+":"+minute+":"+second;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		Thread.sleep(2000);
		long end = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start);
		calendar.add(Calendar.DAY_OF_YEAR, 10);
		long between = 86400;
		System.out.println(getTimeString(between));
	}
}
