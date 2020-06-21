package com.fastcode.demopet.emailbuilder.application.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public interface IEmailService {

	void sendSimpleMessage(String to,String subject,String text);
	
	void sendSimpleMessageUsingTemplate(String to,String subject,
			SimpleMailMessage template,
			String ...templateArgs);
	void sendMessageWithAttachment(String to,String subject,
			String text,String pathToAttachment);
	
	public void sendEmail(SimpleMailMessage email);
	public SimpleMailMessage buildEmail(String email, String appUrl, String resetCode);
}
