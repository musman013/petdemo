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
@Table(name = "reportuser", schema = "reporting")
@IdClass(ReportuserId.class)
public class ReportuserEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
  	@Column(name = "reportId", nullable = false)
	private Long reportId;
	
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
  	@JoinColumn(name = "reportId", insertable=false, updatable=false)
  	private ReportEntity report;
  	
  	@ManyToOne
  	@JoinColumn(name = "userId", insertable=false, updatable=false)
  	private UserEntity user;
  	
  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.user != null) {
  	this.user.removeReportuser(this);
  	this.user = null;
  	}

  	if(this.report != null) {
  	this.report.removeReportuser(this);
  	this.report = null;
  	}

  	}
	
}
