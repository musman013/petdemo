package com.fastcode.demopet.domain.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import com.fastcode.demopet.JSONObjectConverter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "reportversion", schema = "reporting")
@IdClass(ReportversionId.class)
public class ReportversionEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
  	@Column(name = "userId", nullable = false)
	private Long userId;
	
	@Id
  	@Column(name = "reportId", nullable = false)
	private Long reportId;
	
	@Basic
  	@Column(name = "ctype", nullable = true, length =255)
  	private String ctype;
	
	@Basic
  	@Column(name = "description", nullable = true, length =255)
  	private String description;
	
	@Basic
  	@Column(columnDefinition = "TEXT",name = "query", nullable = true, length =255)
  	@Convert(converter= JSONObjectConverter.class)
  	private JSONObject query;
	
	@Basic
  	@Column(name = "reportType", nullable = true, length =255)
  	private String reportType;
	
	@Basic
  	@Column(name = "title", nullable = false, length =255)
  	private String title;
  	
  	@Id
  	@Column(name = "reportVersion", nullable = false, length =255)
  	private String reportVersion;
  	
  	@Basic
	@Column(name = "isAssignedByDashboard" , nullable= false)
  	private Boolean isAssignedByDashboard;

  	@ManyToOne
  	@JoinColumn(name = "reportId", referencedColumnName = "id", insertable = false, updatable = false)
  	private ReportEntity report;
  	
  	@ManyToOne
  	@JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
  	private UserEntity user;
  	
  	@PreRemove
    private void dismissParent() {
  		//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
  		if(this.user != null) {
  			this.user.removeReportversion(this);
  	        this.user = null;	
  		}
  		
  		if(this.report != null) {
  			this.report.removeReportversion(this);
  	        this.report = null;	
  		}
        
    }

}
