package com.taobao.UI;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class ListItemRender extends DefaultListCellRenderer {
	
	private ImageIcon defaultIcon = new ImageIcon("head.gif");
	private Hashtable iconTable = new Hashtable();
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		if (value instanceof ListItemData) {
			ListItemData location = (ListItemData) value;
			ImageIcon icon = (ImageIcon) iconTable.get(value);
			if (icon == null) {
				try {
					icon = new ImageIcon(new URL("http://aplcenmp.apl.jhu.edu/~hall/java/Swing-Tutorial/ComplexExamples/JLists/flags"+location.getFlagFile()));
					icon.setImageObserver(new ImageObserverImpl(label));
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iconTable.put(value, icon);
			}
			label.setIcon(defaultIcon);
		} else {
			// Clear old icon; needed in 1st release of JDK 1.2
			label.setIcon(null);
		}
		return (label);
	}
}

class ImageObserverImpl implements ImageObserver{
	
	private JLabel label;
	
	public ImageObserverImpl(JLabel label){
		this.label = label;
	}
	
	@Override
	public boolean imageUpdate(Image img, int flags, int x, int y,
			int width, int height) {
		if ( (flags & HEIGHT) !=0 ) 
            System.out.println("Image height = " + height ); 
 
        if ( (flags & WIDTH ) !=0 ) 
            System.out.println("Image width = " + width ); 
         
        if ( (flags & FRAMEBITS) != 0 ) 
            System.out.println("Another frame finished."); 
 
        if ( (flags & SOMEBITS) != 0 ) 
            System.out.println("Image section :" 
                         + new Rectangle( x, y, width, height ) ); 
         
 
        if ( (flags & ABORT) != 0 )  { 
            System.out.println("Image load aborted..."); 
            return false;  
        } 
		if ( (flags & ALLBITS) != 0 ) { 
            System.out.println("Image finished!"); 
            label.setIcon(new ImageIcon(img));
            return false;  
        } 
		// TODO Auto-generated method stub
		return true;
	}
}