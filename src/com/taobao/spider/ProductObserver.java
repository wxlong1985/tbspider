package com.taobao.spider;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.jdom.Element;

public class ProductObserver implements Observer {
	
	private JFrame frame;
	
	public ProductObserver(JFrame jframe) {
		// TODO Auto-generated constructor stub
		this.frame = jframe;
	}

	@Override
	public void update(Observable o, Object obj) {
		// TODO Auto-generated method stub
		Spiderparam param = (Spiderparam) o;
		Element ele = (Element) obj;
		
	}

}
