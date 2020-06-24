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
@Table(name = "dashboarduser", schema = "reporting")
@IdClass(DashboarduserId.class)
public class DashboarduserEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
  	@Column(name = "dashboardId", nullable = false)
	private Long dashboardId;
	
  	@Id
  	@Column(name = "userId", nullable = false)
  	private Long userId; 
  	
  	@Basic
	@Column(name = "editable", nullable = false)
  	private Boolean editable;
	
	@Basic
	@Column(name = "isResetted", nullable = false)
	private Boolean isResetted;

	@Basic
	@Column(name = "isRefreshed", nullable = false)
	private Boolean isRefreshed;

	@Basic
	@Column(name = "ownerSharingStatus", nullable = false)
	private Boolean ownerSharingStatus;

	@Basic
	@Column(name = "recipientSharingStatus", nullable = false)
	private Boolean recipientSharingStatus;

	@Basic
	@Column(name = "isAssignedByRole", nullable = false)
	private Boolean isAssignedByRole;
	
	@ManyToOne
  	@JoinColumn(name = "dashboardId", insertable=false, updatable=false)
  	private DashboardEntity dashboard;
  	
  	@ManyToOne
  	@JoinColumn(name = "userId", insertable=false, updatable=false)
  	private UserEntity user;
  	
  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.user != null) {
  	this.user.removeDashboarduser(this);
  	this.user = null;
  	}

  	if(this.dashboard != null) {
  	this.dashboard.removeDashboarduser(this);
  	this.dashboard = null;
  	}

  	}
	
}
