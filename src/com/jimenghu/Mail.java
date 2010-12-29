package com.jimenghu;



import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Mail {
	
	
	 
	/**
	 * @param from  emailaddress
	 * @param smtp  smtp server name
	 * @param username  username
	 * @param pwd   pwd
	 * @param to    to
	 * 
	 */
	public static void send(String from,String smtp,String username,String pwd,String to,String title,String e) {
		try {
            Properties p = new Properties(); //Properties p = System.getProperties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.transport.protocol", "smtp");
            p.put("mail.smtp.host", smtp);
            p.put("mail.smtp.port", "25");
            //建立会话
            Session session = Session.getInstance(p);
            Message msg = new MimeMessage(session); //建立信息
 
            msg.setFrom(new InternetAddress(from)); //发件人
            msg.setRecipient(Message.RecipientType.TO,
                             new InternetAddress(to)); //收件人
 
            msg.setSentDate(new Date()); // 发送日期
            if(title != null)
            	msg.setSubject(title); // 主题
            if(e != null)
            	msg.setText(e); //内容
            int i = 1;
            while(true){
            	try {
            		if(i++ > 3)
            			break;
            		Transport tran = session.getTransport("smtp");
                    tran.connect(smtp, username, pwd);
                    tran.sendMessage(msg, msg.getAllRecipients()); 
                    tran.close();
                    break;
				} catch (Exception e1) {
					// TODO: handle exception
					
				}            	
            }
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
	}

	public static void main(String[] args) {
		//Mail.send("jimenghu@163.com", "smtp.163.com", "jimenghu", "lovefamily", "jimenghu@163.com","title",new NullPointerException("null").toString());
		Mail.send("leichun.h@163.com", "smtp.163.com", "leichun.h", "070101doutrain", "leichun.h@163.com","title",new NullPointerException("null").toString());

	}

}