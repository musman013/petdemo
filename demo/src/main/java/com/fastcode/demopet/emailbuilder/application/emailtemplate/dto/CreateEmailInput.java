package com.fastcode.demopet.emailbuilder.application.emailtemplate.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fastcode.demopet.emailbuilder.domain.model.File;

public class CreateEmailInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Not a good idea to validate email addresses because there are too many
	// variations for an email address
	@NotNull(message = "Id: should not be null")
	private Long id;
	@NotNull(message = "To: should not be null")
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String emailBody;
	private String contentJson;
	private Set<File> inlineImages = new HashSet<File>();
	private Set<File> attachments = new HashSet<File>();

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Set<File> getInlineImages() {
		return inlineImages;
	}

	public void setInlineImages(Set<File> inlineImages) {
		this.inlineImages = inlineImages;
	}

	public Set<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<File> attachments) {
		this.attachments = attachments;
	}

	public String getContentJson() {
		return contentJson;
	}

	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
