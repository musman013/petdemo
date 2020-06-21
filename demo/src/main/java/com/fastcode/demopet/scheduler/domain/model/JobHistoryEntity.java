package com.fastcode.demopet.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JobHistory")
@Getter @Setter
@NoArgsConstructor
public class JobHistoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "JobName", nullable = false, length = 256)
    private String jobName;
    
    @Basic
    @Column(name = "JobDescription", nullable = true, length = 1024)
    private String jobDescription;

    @Basic
    @Column(name = "JobGroup", nullable = false, length = 256)
    private String jobGroup;
    
    @Basic
    @Column(name = "JobClass", nullable = false, length = 256)
    private String jobClass;

    @Basic
    @Column(name = "FiredTime", nullable = false)
    private Date firedTime;

    @Basic
    @Column(name = "FinishedTime", nullable = true)
    private Date finishedTime;
    
    @Basic
    @Column(name = "TriggerName", nullable = false, length = 256)
    private String triggerName;

    @Basic
    @Column(name = "TriggerGroup", nullable = false, length = 256)
    private String triggerGroup;

    @Basic
    @Column(name = "Duration", nullable = false, length = 256)
    private String duration;
    
    @Basic
    @Column(name = "JobStatus", nullable = false, length = 256)
    private String jobStatus;
    
    @Basic
    @Column(name = "JobMapData", nullable = true, length = 256)
    private String jobMapData;

    @Override
    public boolean equals(Object o) {
    	 if (this == o) return true;
    	 if (!(o instanceof JobHistoryEntity)) return false;
         JobHistoryEntity that = (JobHistoryEntity) o;
         return id != null && id.equals(that.id);
       
    }

    @Override
    public int hashCode() {
    	 return 31;
    	 
    }
}