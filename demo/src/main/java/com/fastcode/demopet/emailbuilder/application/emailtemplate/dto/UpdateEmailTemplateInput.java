package com.fastcode.demopet.emailbuilder.application.emailtemplate.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.emailbuilder.domain.model.File;

public class UpdateEmailTemplateInput {

	private Long id;
	@NotNull(message = "Template_Name Should not be null")
	@Length(max = 256, message = "Template_Name must be less than 256 characters")
	private String templateName;

	@Length(max = 256, message = "Email_Category must be less than 256 characters")
	private String category;

	@Length(max = 4096, message = "Content_Html must be less than 32768 characters")
	private String contentHtml;

	@Length(max = 4096, message = "Content_Json must be less than 32768 characters")
	private String contentJson;

	@NotNull(message = "To Should not be null")
	@Length(max = 256, message = "To must be less than 256 characters")
	private String to;

	@Length(max = 256, message = "CC must be less than 256 characters")
	private String cc;

	@Length(max = 256, message = "Bcc must be less than 256 characters")
	private String bcc;

	@Length(max = 256, message = "Subject must be less than 256 characters")
	private String subject;
	private Boolean active;
	private String attachmentpath;
	@Length(max = 512, message = "Description Name must be less than 512 characters")
	private String description;
	List<File> inlineImages;
	List<File> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContentHtml() {
		return contentHtml;
	}

	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

	public String getContentJson() {
		return contentJson;
	}

	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getAttachmentpath() {
		return attachmentpath;
	}

	public void setAttachmentpath(String attachmentpath) {
		this.attachmentpath = attachmentpath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<File> getInlineImages() {
		return inlineImages;
	}

	public void setInlineImages(List<File> inlineImages) {
		this.inlineImages = inlineImages;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

}
