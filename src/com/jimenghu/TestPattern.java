package com.jimenghu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String patternStr="共(\\d*)页";
		Pattern pattern=Pattern.compile(patternStr);
		Matcher matcher=pattern.matcher("   \n共34条 共3页\r\n        ");
		if(matcher.find()){
			System.out.println(matcher.group(1));
		}
	}

}
