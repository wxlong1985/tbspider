package com.taobao.spider;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class Test {

	/**
	 * @param args
	 * @throws JDOMException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		/*SAXBuilder builder = new SAXBuilder();
		
		String xml = "<a stat='lf_aclog=1-30996aa919cee0d6611262dd7850a621-42-sale_desc-0&amp;lf_acfrom=0&amp;at_alitrackid=' href='http://item.taobao.com/item.htm?id=3821778856' target='_blank'><span><img data-lazyload-src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg' class='hesper:small2big' /></span></a>";
		StringReader sr = new StringReader(xml);
		Element ele = builder.build(sr).getRootElement();
		 
		System.out.println(((Element)XPath.selectSingleNode(ele, "//a")).getAttributeValue("href"));
		System.out.println(XPath.selectSingleNode(ele, "//img/@data-lazyload-src"));*/
		
		JEditorPane editor = new JEditorPane("text/html",
        "<H1>A!</H1><P><FONT COLOR=blue>要在在</FONT>在在</P>");
	    editor.setEditable(false);
	    JScrollPane pane = new JScrollPane(editor);
	    JFrame f = new JFrame("HTML Demo");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.getContentPane().add(pane);
	    f.setSize(800, 600);
	    f.setVisible(true);
		
	}

}
