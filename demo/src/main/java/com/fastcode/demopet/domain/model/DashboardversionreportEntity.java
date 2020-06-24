package com.fastcode.demopet.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "dashboardversionreport", schema = "reporting")
@IdClass(DashboardversionreportId.class)
public class DashboardversionreportEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "dashboardId", nullable = false)
	private Long dashboardId;

	@Id
	@Column(name = "userId", nullable = false)
	private Long userId;

	@Id
	@Column(name = "dashboardVersion", nullable = false)
	private String dashboardVersion;

	@Id
	@Column(name = "reportId", nullable = false)
	private Long reportId;
	
	@Basic
	@Column(name = "reportWidth", nullable = false, length = 255)
	private String reportWidth;

	@Basic
	@Column(name = "orderId", nullable = false)
	private Long orderId;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "dashboardId", referencedColumnName = "dashboardId", insertable=false, updatable=false),
		@JoinColumn(name = "dashboardVersion",referencedColumnName = "dashboardVersion", insertable=false, updatable=false),
		@JoinColumn(name = "userId", referencedColumnName = "userId", insertable=false, updatable=false)
	})
	private DashboardversionEntity dashboardversionEntity;

	@ManyToOne
	@JoinColumn(name = "reportId", insertable=false, updatable=false)
	private ReportEntity report;
	
	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.report != null) {
  	this.report.removeDashboardversionreport(this);
  	this.report = null;
  	}

  	if(this.dashboardversionEntity != null) {
  	this.dashboardversionEntity.removeDashboardversionreport(this);
  	this.dashboardversionEntity = null;
  	}

  	}

}
