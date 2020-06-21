package com.fastcode.demopet.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qrtzJobDetails")
@Getter @Setter
@NoArgsConstructor
public class JobDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	 
	@Column(name = "Id", nullable = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Id
	@Column(name = "jobName", nullable = false, length = 256)
	private String jobName;
	
	@Basic
	@Column(name = "jobGroup", nullable = false, length = 256)
	private String jobGroup;
	
	@Basic
	@Column(name = "description", nullable = true, length = 256)
	private String description;
	
	@Basic
	@Column(name = "schedName", nullable = false, length = 256)
	private String schedName;
	
		
	@Basic
	@Column(name = "isUpdateData", nullable = false, length = 256)
	private Boolean isUpdateData;
	
	@Basic
	@Column(name = "jobClassName", nullable = false, length = 256)
	private String jobClassName;
	
	@Basic
	@Column(name = "isDurable", nullable = false, length = 256)
	private Boolean isDurable;
	
	@Basic
	@Column(name = "isNonconcurrent", nullable = false, length = 256)
	private Boolean isNonconcurrent;
	
	@Basic
	@Column(name = "jobData", nullable = true, length = 256)
	private byte[] jobData;
	
	@Basic
	@Column(name = "requestsRecovery", nullable = false, length = 256)
	private Boolean requestsRecovery;
	
}

