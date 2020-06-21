package com.fastcode.demopet.emailbuilder.application.emailtemplate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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

}
