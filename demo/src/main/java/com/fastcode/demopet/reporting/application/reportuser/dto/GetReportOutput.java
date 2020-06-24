package com.fastcode.demopet.reporting.application.reportuser.dto;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetReportOutput {

	private String ctype;
	private String description;
	private Long id;
	private JSONObject query;
	private String reportType;
	private String title;
	private String reportWidth;
	private Long reportuserUserId;
	private Long reportuserReportId;

}
