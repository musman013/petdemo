package com.fastcode.demopet.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "dashboardversion")
@IdClass(DashboardversionId.class)
public class DashboardversionEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
  	@Column(name = "userId", nullable = false)
  	private Long userId;
  	
  	@Id
  	@Column(name = "dashboardId", nullable = false)
  	private Long dashboardId;
  	
  	@Id
  	@Column(name = "dashboardVersion", nullable = false, length =255)
  	private String dashboardVersion;
	
	@Basic
  	@Column(name = "description", nullable = true, length =255)
	private String description;
	
  	@Basic
  	@Column(name = "title", nullable = false, length =255)
  	private String title;
  	
  	@ManyToOne
  	@JoinColumn(name = "dashboardId", insertable = false, updatable = false)
  	private DashboardEntity dashboard;
  	
  	@ManyToOne
  	@JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
  	private UserEntity user;
  	
  	@OneToMany(mappedBy = "dashboardversionEntity", cascade = CascadeType.ALL) 
  	private Set<DashboardversionreportEntity> dashboardversionreportSet = new HashSet<DashboardversionreportEntity>(); 

  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.user != null) {
  	this.user.removeDashboardversion(this);
  	this.user = null;
  	}

  	if(this.dashboard != null) {
  	this.dashboard.removeDashboardversion(this);
  	this.dashboard = null;
  	}

  	}

  	public void addDashboardversionreport(DashboardversionreportEntity dashboardversionreport) {
  		dashboardversionreportSet.add(dashboardversionreport);
  		dashboardversionreport.setDashboardversionEntity(this);
	}

	public void removeDashboardversionreport(DashboardversionreportEntity dashboardversionreport) {
		dashboardversionreportSet.remove(dashboardversionreport);
		dashboardversionreport.setDashboardversionEntity(null);
	}
}
