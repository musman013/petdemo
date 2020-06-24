package com.fastcode.demopet.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "dashboardrole", schema = "reporting")
@IdClass(DashboardroleId.class)
public class DashboardroleEntity extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
  	@Column(name = "dashboardId", nullable = false)
	private Long dashboardId;
	
	@Id
  	@Column(name = "roleId", nullable = false)
	private Long roleId;
	
	@Basic
	@Column(name = "editable", nullable = false)
	private Boolean editable;
	
	@Basic
	@Column(name = "ownerSharingStatus", nullable = false)
	private Boolean ownerSharingStatus;
	
	@ManyToOne
  	@JoinColumn(name = "dashboardId", insertable=false, updatable=false)
  	private DashboardEntity dashboard;
  	
  	@ManyToOne
  	@JoinColumn(name = "roleId", insertable=false, updatable=false)
  	private RoleEntity role;
  	
  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.dashboard != null) {
  	this.dashboard.removeDashboardrole(this);
  	this.dashboard = null;
  	}

  	if(this.role != null) {
  	this.role.removeDashboardrole(this);
  	this.role = null;
  	}

  	}

}
