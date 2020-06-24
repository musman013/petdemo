package com.fastcode.demopet.emailbuilder.application.emailtemplate.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.emailbuilder.domain.model.File;

public class UpdateEmailTemplateOutput {

	private Long id;
	private String templateName;
	private String category;
	private String contentHtml;
	private String contentJson;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private Boolean active;
	private String attachmentpath;
	List<File> inlineImages;
	List<File> attachments;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String description;

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
