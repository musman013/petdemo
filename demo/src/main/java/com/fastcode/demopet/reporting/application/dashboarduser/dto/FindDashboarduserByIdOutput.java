package com.fastcode.demopet.reporting.application.dashboarduser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindDashboarduserByIdOutput {

	private Long userId;
	private Long dashboardId;
	private Boolean editable;
	private Boolean isResetted;
	private Boolean isRefreshed;
	private Boolean ownerSharingStatus;
	private Boolean recipientSharingStatus;
	private Boolean isAssignedByRole; 
	private String userDescriptiveField;
	private Long version;
}
