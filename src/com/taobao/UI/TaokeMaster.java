package com.taobao.UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;

import org.apache.http.nio.reactor.IOReactorException;
import org.jdesktop.application.Application;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.taobao.spider.Spider;
import com.taobao.spider.Spiderparam;
import com.taobao.spider.util.ProductHandle;

public class TaokeMaster extends JFrame implements ActionListener{
	private JPanel searchBar;
	private JTextField key;
	private JPanel jPanel;
	private JScrollPane jScrollPane;
	private JButton btnSend;
	private JButton btnSearch;
	private Spider spider = new Spider();
	private JLabel statebar = new JLabel("提示：");
	public TaokeMaster() {
		{
			searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
		//	searchBar.setPreferredSize(new java.awt.Dimension(747, 43));
			
			searchBar.setName("searchBar");
			{
				key = new JTextField();
				searchBar.add(key);
				key.setName("key");
				key.setText("靴子");
				key.setPreferredSize(new java.awt.Dimension(389, 24));
			}
			{
				btnSearch = new JButton();
				btnSearch.setPreferredSize(new Dimension(60,24));
				btnSearch.setText("搜索");
				searchBar.add(getBtnSearch());
				btnSearch.setName("btnSearch");
				btnSearch.addActionListener(this);
			}
			{
				btnSend = new JButton();
				btnSend.setPreferredSize(new Dimension(60,24));
				btnSend.setText("群发");
				searchBar.add(btnSend);
				btnSend.setName("btnSend");
				btnSend.addActionListener(this);
			}
		}
		{
			jPanel = new JPanel(new MyFlowLayout(FlowLayout.LEFT, 10, 10));
			jScrollPane = new JScrollPane(jPanel);
			//jPanel.setPreferredSize(new Dimension(700,400));
		//	jScrollPane.setPreferredSize(new Dimension(700,400));
			//jScrollPane.setViewportView(jPanel);	
			jScrollPane.getVerticalScrollBar().setUnitIncrement(60);
			add(searchBar, BorderLayout.NORTH);
			add(jScrollPane, BorderLayout.CENTER);
			
		}
		{
			this.setSize(700, 400);
			add(statebar,BorderLayout.SOUTH);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					try {
						TaokeMaster.this.spider.getClient().destroy();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					super.windowClosing(e);
				}
			});
			/*addComponentListener(new ComponentAdapter(){
				@Override
				public void componentResized(ComponentEvent e) {
					// TODO Auto-generated method stub
					TaokeMaster parent = (TaokeMaster) e.getSource();
					parent.getKey().setSize(new Dimension(parent.getSearchBar().getWidth() - parent.getBtnSearch().getWidth() - parent.getBtnSend().getWidth() - 100, parent.getKey().getHeight()));
					parent.getSearchBar().repaint();
					super.componentResized(e);
				}
			});*/
		}
		// TODO Auto-generated constructor stub
		
		
		//Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaokeMaster inst = new TaokeMaster();
		//inst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inst.setVisible(true);
	}
	
	public JButton getBtnSend() {
		return btnSend;
	}
	
	public JPanel getSearchBar() {
		return searchBar;
	}
	
	public JTextField getKey() {
		return key;
	}
	
	public JButton getBtnSearch() {
		return btnSearch;
	}
	
	public JScrollPane getJScrollPane() {
		return jScrollPane;
	}
	
	public JPanel getJPanel() {
		return jPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btn = (JButton) e.getSource();
		if(btn.getName().equals(this.btnSearch.getName())){
			String key = this.getKey().getText().trim();
			if(key.length() == 0){
				JOptionPane.showMessageDialog(getContentPane(), "关键字不能为空","提示",JOptionPane.WARNING_MESSAGE);
			}else{
				try {
					//JPanel panel = (JPanel) getContentPane();
					ProductHandle handle = new ProductHandle(key,TaokeMaster.this.getJPanel());
					Spiderparam param = Spiderparam.productList(key);
					param.setHandle(handle);
					spider.setSpiderparam(param);
					spider.connect();
				} catch (IOReactorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), e1.getMessage(),"提示",JOptionPane.WARNING_MESSAGE);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), e1.getMessage(),"提示",JOptionPane.WARNING_MESSAGE);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), e1.getMessage(),"提示",JOptionPane.WARNING_MESSAGE);
				}
			}
		}else if(btn.getName().equals(this.btnSend.getName())){
			
		}
		System.out.println(btn.getName());
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
		jPanel.add(product);
		validate();*/
	}

}
