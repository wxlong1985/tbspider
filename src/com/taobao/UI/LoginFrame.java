package com.taobao.UI;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import org.jdesktop.application.Application;
import javax.swing.SwingUtilities;

public class LoginFrame extends javax.swing.JFrame {
	private JTextField txtUsername;
	private JButton btnLogin;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton jButton1;
	private JTextField jTextField1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginFrame inst = new LoginFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public LoginFrame() {
		super();
		initGUI();
		this.setResizable(false);
		 
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				txtUsername = new JTextField();
				getContentPane().add(getTxtUsername());
				txtUsername.setBounds(180, 31, 155, 41);
				txtUsername.setName("txtUsername");
			}
			{
				jTextField1 = new JTextField();
				getContentPane().add(jTextField1);
				jTextField1.setName("jTextField1");
				jTextField1.setBounds(180, 78, 155, 41);
			}
			{
				btnLogin = new JButton();
				getContentPane().add(getBtnLogin());
				btnLogin.setBounds(54, 144, 106, 38);
				btnLogin.setName("btnLogin");
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setName("jButton1");
				jButton1.setBounds(180, 144, 106, 38);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setBounds(63, 37, 97, 41);
				jLabel1.setName("jLabel1");
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setName("jLabel2");
				jLabel2.setBounds(63, 84, 97, 41);
			}
			pack();
			this.setSize(400, 235);
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public JTextField getTxtUsername() {
		return txtUsername;
	}
	
	public JButton getBtnLogin() {
		return btnLogin;
	}

}
