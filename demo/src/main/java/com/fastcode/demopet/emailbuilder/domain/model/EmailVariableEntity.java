package com.fastcode.demopet.emailbuilder.domain.model;

import java.io.Serializable;
import com.fastcode.demopet.domain.model.AbstractEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EmailVariable")
@Getter @Setter
@NoArgsConstructor
public class EmailVariableEntity extends AbstractEntity {
	
	@Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
	@Column(name = "PropertyName", nullable = false ,length = 50)
    private String propertyName;
    
    @Basic
	@Column(name = "PropertyType", nullable = false ,length = 50)
    private String propertyType;
    
    @Basic
	@Column(name = "DefaultValue", nullable = true ,length = 100)
    private String defaultValue;

	public EmailVariableEntity(String propertyName, String propertyType, String defaultValue) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.defaultValue = defaultValue;
	}

}