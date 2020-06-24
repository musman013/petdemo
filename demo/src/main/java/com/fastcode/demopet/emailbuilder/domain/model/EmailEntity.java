package com.fastcode.demopet.emailbuilder.domain.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Email")
public class EmailEntity implements Serializable {

	private Long id;
	private String emailBody;
	// Not a good idea to validate email addresses because there are too many
	// variations for an email address
	@NotNull(message = "To: should not be null")
	private String to;
	private String cc;
	private String bcc;
	private String subject;

	public EmailEntity() {
	}

	public EmailEntity(Long id, String emailBody, @NotNull(message = "To: should not be null") String to, String cc, String bcc, String subject) {
		this.id = id;
		this.emailBody = emailBody;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
	}

	@Id
	@Column(name = "Id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "EmailBody", nullable = true)
	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	@Basic
	@Column(name = "To_", nullable = false)
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Basic
	@Column(name = "Cc", nullable = true)
	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	@Basic
	@Column(name = "Bcc", nullable = true)
	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	@Basic
	@Column(name = "Subject", nullable = true)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof EmailEntity))
			return false;
		EmailEntity email = (EmailEntity) o;
		return id != null && id.equals(email.id);
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
