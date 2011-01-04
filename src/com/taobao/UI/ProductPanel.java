package com.taobao.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.WindowConstants;
import org.jdesktop.application.Application;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTML.Tag;

public class ProductPanel extends javax.swing.JScrollPane implements
		HyperlinkListener, MouseListener {
	private JEditorPane content;
	private Border border = BorderFactory.createLineBorder(Color.BLUE, 3);
	private Cursor onCursor = new Cursor(Cursor.HAND_CURSOR);
	private Cursor outCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private JPanel panel;

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		// Element ele = new Element()
		frame.getContentPane().add(new ProductPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public ProductPanel() {
		super();
		initGUI();
		try {
			insertImg(null);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initGUI() {
		try {
			{
				panel = new JPanel();
				//this.add(getJPanel());
				this.setViewportView(panel);
			}
			MyFlowLayout thisLayout = new MyFlowLayout();
			panel.setLayout(thisLayout);
			
			
			{
				//String html = "<img src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg'/><br/><a href='http://item.taobao.com/item.htm?id=3821778856'>搜索的关<FONT COLOR=red>要在在</FONT>搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字</a>";
				content = new JEditorPane();
				content.setEditable(false);
				content.addHyperlinkListener(this);
				// content.getDocument().insertString(content.getDocument().getLength(),
				// "ttttttttttt", null);
				content.setContentType("text/html;charset=gbk");
				
				content.addMouseListener(this);
				content.setName("content");
			//	content.setPreferredSize(new java.awt.Dimension(220, 280));
				content.setSize(220, 280);
				panel.add(content, FlowLayout.LEFT);
				for(int i = 0 ;i<100;i++){
					JEditorPane pane = new JTextPane();
					pane.setPreferredSize(new java.awt.Dimension(220, 280) );
					//pane.setSize(220, 280);
					panel.add(pane, FlowLayout.LEFT);
				}
				
				
			}
			//Application.getInstance().getContext().getResourceMap(getClass())
					//.injectComponents(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JEditorPane getContent() {
		return content;
	}

	public void parse() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();

		String xml = "<a stat='lf_aclog=1-30996aa919cee0d6611262dd7850a621-42-sale_desc-0&amp;lf_acfrom=0&amp;at_alitrackid=' href='http://item.taobao.com/item.htm?id=3821778856' target='_blank'><span><img data-lazyload-src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg' class='hesper:small2big' /></span></a>";
		StringReader sr = new StringReader(xml);
		Element ele = builder.build(sr).getRootElement();
		Element eleA = (Element) XPath.selectSingleNode(ele, "//a");
		if(eleA == null){
			String href = eleA.getAttributeValue("href");
		
		}
		Element eleImg = (Element) XPath.selectSingleNode(ele, "//img");
		if(eleA == null){
			String src = eleA.getAttributeValue("data-lazyload-src");
		
		}
		
	}
	
	private void insertImg(Element srcEle) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		
		String xml = "<a stat='lf_aclog=1-30996aa919cee0d6611262dd7850a621-42-sale_desc-0&amp;lf_acfrom=0&amp;at_alitrackid=' href='http://item.taobao.com/item.htm?id=3821778856' target='_blank'><span><img data-lazyload-src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg' class='hesper:small2big' /></span></a>";
		StringReader sr = new StringReader(xml);
		Element ele = builder.build(sr).getRootElement();
		
		Element eleA = (Element) XPath.selectSingleNode(ele, "//a");
		Element eleImg = (Element) XPath.selectSingleNode(ele, "//img");
		String href = null;
		String src = null;
		if(eleA != null){
			href = eleA.getAttributeValue("href");		
		} 
		if(eleImg != null){
			src = eleImg.getAttributeValue("data-lazyload-src");		
		}
		HTMLEditorKit htmlEditor = (HTMLEditorKit) content.getEditorKit();
		// content.setEditorKit(htmlEditor);
		HTMLDocument doc = (HTMLDocument) content.getDocument();
		
		try {
			htmlEditor
					.insertHTML(
							doc,
							content.getCaretPosition(),
							"<img src='"+src+"'/><br/><a href='"+href+"'>搜索的关<FONT COLOR=red>要在在</FONT>搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字</a>",
							0, 0, Tag.IMG);
			 
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void btnInsertActionPerformed(ActionEvent evt) {
		// content.setText("<a href='http://www.baidu.com'>xxxxxxxx</a>");
		HTMLEditorKit htmlEditor = (HTMLEditorKit) content.getEditorKit();
		// content.setEditorKit(htmlEditor);
		HTMLDocument doc = (HTMLDocument) content.getDocument();
		try {
			htmlEditor
					.insertHTML(
							doc,
							content.getCaretPosition(),
							"<img src='http://avatar.profile.csdn.net/8/1/2/2_xeon_pan.jpg'/>",
							0, 0, Tag.IMG);
			htmlEditor
			.insertHTML(
					doc,
					content.getCaretPosition(),
					"<br/>",
					0, 0, Tag.BR);
			htmlEditor
			.insertHTML(
					doc,
					content.getCaretPosition(),
					"<a target='_blank' href='http://www.baidu.com'>大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦大厦在</a>",
					0, 0, Tag.A);
			
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("btnInsert.actionPerformed, event=" + evt);
		// TODO add your code for btnInsert.actionPerformed
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		// TODO Auto-generated method stub
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) pane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			} else {
				try {
					//pane.setPage(e.getURL());					
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
					desktop.browse(new URI(e.getURL().toString()));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 2) {
			System.out.println("双击");
		}
		// System.out.println("xxxx");
		if (this.getBorder() == null) {
			this.setBorder(border);
		} else {
			this.setBorder(null);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("entered");
		this.setCursor(onCursor);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("exit");
		this.setCursor(outCursor);
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	public JPanel getJPanel() {
		return panel;
	}

}
