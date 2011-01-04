package com.taobao.spider.util;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import com.taobao.UI.TaokeMaster;

public class ProductHandle implements Handle{
	private JPanel frame;
	
	private String key;
	public ProductHandle(String key) {
		// TODO Auto-generated constructor stub
		this.key = key;
	}
	
	
	public ProductHandle(String key,JPanel frame) {
		// TODO Auto-generated constructor stub
		this.key = key;
		this.frame = frame;
	}
	@Override
	public void execute(List<Object> results) {
		// TODO Auto-generated method stub
		int i = 1;
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Element ele = (Element) iterator.next();
			XMLOutputter out = new XMLOutputter();
			//System.out.println(out.outputString(ele));
			Attribute eleA = null;
			Attribute eleTitle = null;
			try {
				eleA = (Attribute) XPath.selectSingleNode(ele, ".//a/@href");
				eleTitle = (Attribute) XPath.selectSingleNode(ele, ".//a/@title");
				//System.out.println(out.outputString((Element)XPath.selectSingleNode(ele.getDocument(), "//a")));
				//System.out.println(out.outputString(ele));
			} catch (JDOMException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Attribute eleImg = null;
			try {
				eleImg = (Attribute) XPath.selectSingleNode(ele, ".//img/@data-lazyload-src");
			} catch (JDOMException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if(eleA == null || eleImg == null || eleTitle == null)
				continue;
			String href = eleA.getValue();
			String src = eleImg.getValue();
			String title = eleTitle.getValue().replace(key, "<FONT COLOR=red>"+key+"</FONT>");
			String[] keys = key.split(" ");
			/*for (int j = 0; j < keys.length; j++) {
				title = title.replace(keys[j].trim(), "<FONT COLOR=red>"+keys[j].trim()+"</FONT>");
			}*/
			
			
			/*if(eleA != null){
				href = eleA.getAttributeValue("href");		
			} 
			if(eleImg != null){
				src = eleImg.getAttributeValue("data-lazyload-src");		
			}*/
			//System.out.println("href:"+href+"\nimg:"+src);
			
			String a = "<html><img src='"+src+"'/><br/>"+(i++)+title+"</html>";
			
			JLabel product = new JLabel(a);
			product.setBorder(BorderFactory.createLineBorder(Color.blue,1));
			product.setPreferredSize(new Dimension(220,270));
			product.setSize(220, 270);			 
			frame.add(product);
			
		}
		frame.validate();
		/*SAXBuilder builder = new SAXBuilder();
		
		String xml = "<a stat='lf_aclog=1-30996aa919cee0d6611262dd7850a621-42-sale_desc-0&amp;lf_acfrom=0&amp;at_alitrackid=' href='http://item.taobao.com/item.htm?id=3821778856' target='_blank'><span><img data-lazyload-src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg' class='hesper:small2big' /></span></a>";
		StringReader sr = new StringReader(xml);
		Element ele = null;
		try {
			ele = builder.build(sr).getRootElement();
		} catch (JDOMException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Element eleA = null;
		try {
			eleA = (Element) XPath.selectSingleNode(ele, "//a");
		} catch (JDOMException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Element eleImg = null;
		try {
			eleImg = (Element) XPath.selectSingleNode(ele, "//img");
		} catch (JDOMException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String href = null;
		String src = null;
		if(eleA != null){
			href = eleA.getAttributeValue("href");		
		} 
		if(eleImg != null){
			src = eleImg.getAttributeValue("data-lazyload-src");		
		}
		
		String a = "<html><img src='"+src+"'/><br/><a href='"+href+"'>搜索的关<FONT COLOR=red>要在在</FONT>搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字</a></html>";
		JLabel product = new JLabel(a);
		product.setBorder(BorderFactory.createLineBorder(Color.blue,1));
		product.setPreferredSize(new Dimension(220,280));
		product.setSize(220, 280);			 
		frame.add(product);
		frame.validate();*/
	}


	public JPanel getFrame() {
		return frame;
	}


	public void setFrame(JPanel frame) {
		this.frame = frame;
	}
	
	 
}
