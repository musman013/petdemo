package com.fastcode.demo.domain.model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rolepermission", schema = "sample")
@IdClass(RolepermissionId.class)
@Getter @Setter
@NoArgsConstructor
public class RolepermissionEntity extends AbstractEntity {

	@Id
  	@Column(name = "permissionId", nullable = false)
  	private Long permissionId;
  	
  	@Id
  	@Column(name = "roleId", nullable = false)
  	private Long roleId;
  
  	public RolepermissionEntity(Long permissionId, Long roleId) {
	  	this.roleId = roleId;
	  	this.permissionId = permissionId;
  	}

  	@ManyToOne
  	@JoinColumn(name = "permissionId", insertable=false, updatable=false)
  	private PermissionEntity permission;
 
  	@ManyToOne
  	@JoinColumn(name = "roleId", insertable=false, updatable=false)
  	private RoleEntity role;
 
}

  
      


