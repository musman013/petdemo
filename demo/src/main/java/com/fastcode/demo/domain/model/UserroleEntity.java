package com.fastcode.demo.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Userrole", schema = "sample")
@IdClass(UserroleId.class)
@Getter @Setter
@NoArgsConstructor
public class UserroleEntity extends AbstractEntity {

	@Id
  	@Column(name = "roleId", nullable = false)
	private Long roleId;
	
  	@Id
  	@Column(name = "userId", nullable = false)
  	private Long userId;
  	
  	@ManyToOne
  	@JoinColumn(name = "roleId", insertable=false, updatable=false)
  	private RoleEntity role;
  
  	@ManyToOne
  	@JoinColumn(name = "userId", insertable=false, updatable=false)
  	private UserEntity user;
  
}

  
      


