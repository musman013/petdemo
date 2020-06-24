package com.fastcode.demopet.reporting.application.dashboardversionreport.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateDashboardversionreportOutput {

	private Long dashboardId;
	private Long reportId;
	private Long userId;
	private String dashboardVersion;
	private String reportWidth;
	private Long orderId;

}