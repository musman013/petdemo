package com.fastcode.demopet.reporting.application.dashboard.dto;

import java.util.List;

import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindDashboardByIdOutput {

	private String description;
	private String title;
	private Long userId;
	private Long id;
	private Boolean editable;
	private Boolean isResetted;
	private Boolean isRefreshed;
	private Boolean ownerSharingStatus;
	private Boolean recipientSharingStatus;
	private Boolean isAssignedByRole;
	private Boolean isResetable;
	private Boolean isPublished;
	private Long ownerId;
	private String ownerDescriptiveField;
	private Boolean sharedWithMe;
	private Boolean sharedWithOthers;
	private List<FindReportByIdOutput> reportDetails;
	private Boolean isShareable;
	private Long version;
}
