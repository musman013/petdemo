package com.fastcode.demopet.reporting.application.dashboard.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fastcode.demopet.reporting.application.report.dto.UpdateReportInput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateDashboardInput {

	private String description;
	@NotNull(message = "id Should not be null")
	private Long id;
	@NotNull
	private String title;
	private Long ownerId;
	private Long userId;
	private Boolean isPublished;
	List<UpdateReportInput> reportDetails= new ArrayList<>();
	private Boolean isShareable;
	private Long version;
	
}
