package com.fastcode.demopet.emailbuilder.application.emailtemplate.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.emailbuilder.domain.model.File;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class FindEmailTemplateByIdOutput {

	private Boolean active;
	private String attachmentpath;

	private Long id;
	private String templateName;
	private String category;
	private String contentHtml;
	private String contentJson;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String description;
	List<File> inlineImages;
	List<File> attachments;
	private Long version;


}
