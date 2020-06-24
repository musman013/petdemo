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
@Table(name = "reportrole")
@IdClass(ReportroleId.class)
public class ReportroleEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
    
	@Id
  	@Column(name = "reportId", nullable = false)
	private Long reportId;
	
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
  	@JoinColumn(name = "reportId", insertable=false, updatable=false)
  	private ReportEntity report;
  	
  	@ManyToOne
  	@JoinColumn(name = "roleId", insertable=false, updatable=false)
  	private RoleEntity role;
  	
  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.role != null) {
  	this.role.removeReportrole(this);
  	this.role = null;
  	}

  	if(this.report != null) {
  	this.report.removeReportrole(this);
  	this.report = null;
  	}

  	}
	
}
