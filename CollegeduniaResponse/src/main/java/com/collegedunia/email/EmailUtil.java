package com.collegedunia.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("beekesh.singh@collegedunia.com", "Test EMail"));

	      msg.setReplyTo(InternetAddress.parse("er.beekeshsingh@gmail.com", false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public static void sendAttachMentEmail(Session session, String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/html; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("beekesh.singh@collegedunia.com","Test EMail"));
			msg.setReplyTo(InternetAddress.parse("beekesh.singh@collegedunia.com", false));
			msg.setSubject(subject, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			
			//create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body);
			//Create the multipart message for attachment 
			Multipart multipart = new MimeMultipart();
			//set text message part 
			multipart.addBodyPart(messageBodyPart);
			
			//second part attachment
			multipart = new MimeMultipart();
			String fileName = "C:\\Users\\BSINGH\\Automation\\Collegedunia\\src\\main\\java\\com\\collegedunia\\testdata\\Links.xlsx";
			DataSource source = new FileDataSource(fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			//send the complete message part
			msg.setContent(multipart);
			//Send message 
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			// TODO: handle exception
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
