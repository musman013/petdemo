package com.fastcode.demopet.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Userpreference", schema = "sample")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserpreferenceEntity extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Basic
	@Column(name = "Theme", nullable = false, length = 256)
	private String theme;
	
	@Basic
	@Column(name = "Language", nullable = false, length = 256)
	private String language;
	
	@Id
	@EqualsAndHashCode.Include()
	@Column(name = "userId", nullable = false)
	private Long userId;
	
	@OneToOne
    @JoinColumn(name = "userId", nullable = false, insertable=false, updatable=false)
    private UserEntity user;

}
