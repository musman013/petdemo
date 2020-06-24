package com.fastcode.demopet.emailbuilder.application.mail;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.emailbuilder.domain.model.File;

@Service
public interface IEmailService {

	 void sendMessage(String to, String cc, String bcc, String subject, String htmlContent, List<File> inlineImages, List<File> attachments);
	
	 public void sendEmail(SimpleMailMessage email);
		
	 public SimpleMailMessage buildEmail(String email, String appUrl, String resetCode);
	 
}
