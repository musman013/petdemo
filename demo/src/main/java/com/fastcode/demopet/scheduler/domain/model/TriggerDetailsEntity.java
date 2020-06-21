package com.fastcode.demopet.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qrtzTriggers")
@Getter @Setter
@NoArgsConstructor
public class TriggerDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "Id", nullable = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	    
	@Basic
	@Column(name = "schedName", nullable = false, length = 256)
	private String schedName;
		
	@Basic
	@Column(name = "jobName", nullable = false, length = 256)
	private String jobName;
	    
	@Basic
	@Column(name = "jobGroup", nullable = false, length = 256)
	private String jobGroup;
	    
	@Basic
	@Column(name = "description", nullable = true, length = 256)
	private String description;
		
	@Id
	@Column(name = "triggerName", nullable = false, length = 256)
	private String triggerName;
	    
	@Basic
	@Column(name = "triggerGroup", nullable = false, length = 256)
	private String triggerGroup;
	    
	@Basic
	@Column(name = "nextFireTime", nullable = true, length = 256)
	private Long nextFireTime;
	    
	@Basic
	@Column(name = "prevFireTime", nullable = true, length = 256)
	private Long prevFireTime;
	    
	@Basic
	@Column(name = "priority", nullable = true, length = 256)
	private String priority;
		
	@Basic
	@Column(name = "triggerState", nullable = false, length = 256)
	private String triggerState;
	    
	@Basic
	@Column(name = "triggerType", nullable = false, length = 256)
	private String triggerType;
	    	
	@Basic
	@Column(name = "startTime", nullable = false, length = 256)
	private Long startTime;
	    
	@Basic
	@Column(name = "endTime", nullable = true, length = 256)
	private Long endTime;
	    
	@Basic
	@Column(name = "calendarName", nullable = true, length = 256)
	private String calendarName;	   
	    
	@Basic
	@Column(name = "misfireInstr", nullable = true, length = 256)
	private String misfireInstr;
	    
	@Basic
	@Column(name = "jobData", nullable = true, length = 256)
	private byte[] jobData;

}
