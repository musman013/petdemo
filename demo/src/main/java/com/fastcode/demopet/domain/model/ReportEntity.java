package com.fastcode.demopet.domain.model;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "report")
public class ReportEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
  	
  	@Basic
	@Column(name = "isPublished", nullable = false)
  	private Boolean isPublished;
  	
  	@ManyToOne
  	@JoinColumn(name = "ownerId")
  	private UserEntity user;
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<ReportversionEntity> reportversionSet = new HashSet<ReportversionEntity>(); 
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<DashboardversionreportEntity> reportdashboardSet = new HashSet<DashboardversionreportEntity>(); 
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<ReportuserEntity> reportuserSet = new HashSet<ReportuserEntity>(); 
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<ReportroleEntity> reportroleSet = new HashSet<ReportroleEntity>(); 
  	
  	public void addReportuser(ReportuserEntity reportUser) {
  		reportuserSet.add(reportUser);
  		reportUser.setReport(this);
	}
  	
  	public void removeReportuser(ReportuserEntity reportUser) {
        reportuserSet.remove(reportUser);
        reportUser.setReport(null);
    }
  
  	public void addReportrole(ReportroleEntity reportRole) {
  		reportroleSet.add(reportRole);
  		reportRole.setReport(this);
	}
  	
  	public void removeReportrole(ReportroleEntity reportRole) {
       reportroleSet.remove(reportRole);
       reportRole.setReport(null);
    }
  	
  	public void addReportversion(ReportversionEntity reportVersion) {
  		reportversionSet.add(reportVersion);
  		reportVersion.setReport(this);
	}
  	
  	public void removeReportversion(ReportversionEntity reportVersion) {
        reportversionSet.remove(reportVersion);
        reportVersion.setReport(null);
    }

  	public void addDashboardversionreport(DashboardversionreportEntity dashboardversionreport) {
  		reportdashboardSet.add(dashboardversionreport);
  		dashboardversionreport.setReport(this);
	}

	public void removeDashboardversionreport(DashboardversionreportEntity dashboardversionreport) {
		reportdashboardSet.remove(dashboardversionreport);
		dashboardversionreport.setReport(null);
	}
  	
  	@PreRemove
  	private void dismissParent() {
  	//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  	if(this.user != null) {
  	this.user.removeReport(this);
  	this.user = null;
  	}
  	}

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof ReportEntity)) return false;
//        ReportEntity report = (ReportEntity) o;
//        return id != null && id.equals(report.id);
//  }

}

  
      


