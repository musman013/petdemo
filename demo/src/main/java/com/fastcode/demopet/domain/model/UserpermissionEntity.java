package com.fastcode.demopet.domain.model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Userpermission", schema = "sample")
@IdClass(UserpermissionId.class)
@Getter @Setter
@NoArgsConstructor
public class UserpermissionEntity extends AbstractEntity {

	@Basic
  	@Column(name = "revoked", nullable = true)
	private Boolean revoked;
	
	@Id
  	@Column(name = "permissionId", nullable = false)
    private Long permissionId;
    
  	@Id
  	@Column(name = "userId", nullable = false)
  	private Long userId;

  	@ManyToOne
  	@JoinColumn(name = "permissionId", insertable=false, updatable=false)
  	private PermissionEntity permission;
  
  	@ManyToOne
  	@JoinColumn(name = "userId", insertable=false, updatable=false)
  	private UserEntity user;

}
