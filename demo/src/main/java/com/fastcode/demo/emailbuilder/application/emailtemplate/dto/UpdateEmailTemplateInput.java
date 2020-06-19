package com.fastcode.demo.emailbuilder.application.emailtemplate.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
	@Email(message= "Invalid Email")
    private String to;
	
	@Length(max = 256, message = "CC must be less than 256 characters")
	@Email(message= "Invalid Email")
    private String cc;
	
	@Length(max = 256, message = "Bcc must be less than 256 characters")
	@Email(message= "Invalid Email")
    private String bcc;
	
	@Length(max = 256, message = "Subject must be less than 256 characters")
    private String subject;
	private Boolean active;
	private String attachmentpath;
    private Long version;
    
}
