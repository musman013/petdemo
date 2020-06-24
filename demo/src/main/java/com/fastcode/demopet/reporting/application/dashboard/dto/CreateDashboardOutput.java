package com.fastcode.demopet.reporting.application.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateDashboardOutput {

	private String description;
	private Long id;
	private String title;
	private Boolean isPublished;
	private Long ownerId;
	private String ownerDescriptiveField;
	private Boolean isShareable;
	
}
