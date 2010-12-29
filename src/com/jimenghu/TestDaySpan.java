package com.jimenghu;

import java.util.Calendar;
import java.util.Date;

public class TestDaySpan {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date start = new Date(2009,10,10);
		Date end = new Date(2009,10,15);
		Calendar calendar = Calendar.getInstance();
		//System.out.println(calendar.DAY_OF_YEAR);
		//calendar.setTime(new Date(end.getTime() - start.getTime()));
		//System.out.println(calendar.DAY_OF_YEAR);
		calendar.setTime(start);
		System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
		calendar.add(Calendar.DAY_OF_MONTH, 10);
		System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
		
	}

}
