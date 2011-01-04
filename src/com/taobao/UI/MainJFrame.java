package com.taobao.UI;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import javax.swing.WindowConstants;
import org.jdesktop.application.Application;
import javax.swing.SwingUtilities;

public class MainJFrame extends javax.swing.JFrame {
	private JScrollPane scrollPanel;
	private JLabel jlabel;
	private JList jList;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainJFrame inst = new MainJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public MainJFrame() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setLayout(new BorderLayout());
			{
				scrollPanel = new JScrollPane();
				getContentPane().add(getScrollPanel(), BorderLayout.CENTER);
				{
					/*
					 * ListModel jListModel = new DefaultComboBoxModel( new
					 * String[] { "Item One", "Item Two" });
					 */
					
					
					// jList.setModel(jListModel);
					ListItemDataCollection collection = new ListItemDataCollection();
					ListItemModel listModel = new ListItemModel(collection);
					jList = new JList(listModel);
					jList.setCellRenderer(new ListItemRender());
					scrollPanel.setViewportView(getJList());
					ListItemData itemdata = (ListItemData) jList.getModel().getElementAt(0);
					itemdata.setFlagFile("");
				}
			}
			{
				jlabel = new JLabel();
				jlabel.setLayout(new BoxLayout(jlabel,BoxLayout.Y_AXIS));
				ImageIcon defaultIcon = new ImageIcon("head.gif");
				jlabel.setSize(200, 200);
				jlabel.setIcon(defaultIcon);
				getContentPane().add(getJlabel(), BorderLayout.NORTH);
				jlabel.setName("jlabel");
				jlabel.setPreferredSize(new java.awt.Dimension(384, 38));
			}
			pack();
			setSize(400, 300);
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}

	public JList getJList() {
		return jList;
	}
	
	public JLabel getJlabel() {
		return jlabel;
	}

}
