package com.fastcode.demopet.domain.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tokenverification", schema = "sample")
@IdClass(TokenverificationId.class)
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class TokenverificationEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Basic
	@Column(name = "Token", nullable = true, length = 512)
	private String token; 
	
	@Basic
	@Column(name = "ExpirationTime", nullable = true, length = 512)
	private Date expirationTime;

	@Id
	@EqualsAndHashCode.Include()
	@Column(name = "TokenType", nullable = true, length = 256)
	private String tokenType;
	
	@Id
	@EqualsAndHashCode.Include()
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "id", nullable = false, insertable=false, updatable=false)
    private UserEntity user;

}
