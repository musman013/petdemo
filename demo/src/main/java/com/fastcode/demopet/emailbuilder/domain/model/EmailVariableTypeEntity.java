package com.fastcode.demopet.emailbuilder.domain.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EmailVariableType")
public class EmailVariableTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String variableType;

	public EmailVariableTypeEntity() {
	}

	public EmailVariableTypeEntity(String variableType) {
		this.variableType = variableType;
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
	@Column(name = "variableType", nullable = false, length = 50, unique = true)
	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

}