package com.jimenghu;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class TestDoc {

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		String docStr = "<buyInfo> "
				+ "<all>"
				+ "   <buyLevel/>"
				+ "  <msg/>"
				+ " <address>辽宁"
				+ "      ? "
				+ "      营口</address>"
				+ "<allsalePreSixUrl>http://ratehis.taobao.com/user-rate-e9dcd5495a6302378c3f8d319ec3b625--isarchive|true--detailed|1--timeLine|-211--receivedOrPosted|0--isarchive|true--buyerOrSeller|1.htm#RateType</allsalePreSixUrl>"
				+ "<allsaleLastSixUrl>http://rate.taobao.com/user-rate-e9dcd5495a6302378c3f8d319ec3b625--detailed|1--timeLine|-210--receivedOrPosted|0--buyerOrSeller|1.htm#RateType</allsaleLastSixUrl>"
				+ "<allbuyPreSixUrl/>" + "<allbuyLastSixUrl/>" + "</all>"
				+ "</buyInfo>";
		SAXReader reader = new SAXReader();
		StringReader sr = new StringReader(docStr);
		Document doc = reader.read(sr);
		String address = doc.getRootElement().selectSingleNode(
		"//address").getStringValue();
		String a = "";
	}

}
