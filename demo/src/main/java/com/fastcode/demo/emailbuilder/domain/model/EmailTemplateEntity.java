package com.fastcode.demo.emailbuilder.domain.model;

import java.io.Serializable;
import com.fastcode.demo.domain.model.AbstractEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Email")
@Getter @Setter
@NoArgsConstructor
public class EmailTemplateEntity extends AbstractEntity {
    
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	@Column(name = "TemplateName", nullable = false ,length = 256)
	private String templateName;
	
	@Basic
	@Column(name = "Category", nullable = true ,length = 256)
	private String category;
	
	@Basic
	@Column(name = "ContentHtml", nullable = true, length = 32768)
	private String contentHtml;
	
	@Basic
	@Column(name = "ContentJson", nullable = true, length = 32768)	
	private String contentJson;
	
	@Basic
	@Column(name = "To_", nullable = false,length = 256)	
	@Email
	private String to;
	
	@Basic
	@Column(name = "Cc", nullable = true ,length = 256)
	@Email
	private String cc;

	@Basic
	@Column(name = "Bcc", nullable = true ,length = 256)
	@Email
	private String bcc;

	@Basic
	@Column(name = "Subject", nullable = true ,length = 256)
	private String subject;

	@Basic
	@Column(name = "active", nullable = true)
	private Boolean active;

	@Basic
	@Column(name = "attachmentpath", nullable = true, length =256)
	private String attachmentpath;
	
}
