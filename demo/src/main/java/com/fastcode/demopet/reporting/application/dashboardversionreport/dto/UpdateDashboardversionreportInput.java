package com.fastcode.demopet.reporting.application.dashboardversionreport.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateDashboardversionreportInput {

	@NotNull(message = "dashboardId Should not be null")
	private Long dashboardId;
	@NotNull(message = "reportId Should not be null")
	private Long reportId;
	private Long userId;
	private String dashboardVersion;
	private String reportWidth;
	private Long orderId;
    private Long version;
    
}
