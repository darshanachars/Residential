package com.residential.backend.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl{
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	public void sendSimpleMessage(
		      String to, String subject, String text) {
		        
		        SimpleMailMessage message = new SimpleMailMessage(); 
		        message.setFrom("darshanachars18@gmail.com");
		        message.setTo(to); 
		        message.setSubject(subject); 
		        message.setText(text);
		        javaMailSender.send(message);
		        System.out.println("message sent");
		    }
}
