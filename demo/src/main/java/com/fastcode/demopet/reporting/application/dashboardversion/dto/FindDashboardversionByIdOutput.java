package com.fastcode.demopet.reporting.application.dashboardversion.dto;

import java.util.List;

import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindDashboardversionByIdOutput {

	private String description;
	private Long id;
	private String title;
	private Long userId;
	private String userDescriptiveField;
	private List<FindReportByIdOutput> reportDetails;
	private Long version;
	
}
