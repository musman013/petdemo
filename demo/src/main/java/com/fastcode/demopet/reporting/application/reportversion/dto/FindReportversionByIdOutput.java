package com.fastcode.demopet.reporting.application.reportversion.dto;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindReportversionByIdOutput {

	private String ctype;
	private String description;
	private JSONObject query;
	private String reportType;
	private String title;
	private Long userId;
	private String userDescriptiveField;
	private Long reportId;
	private Boolean isAssignedByDashboard;

}
