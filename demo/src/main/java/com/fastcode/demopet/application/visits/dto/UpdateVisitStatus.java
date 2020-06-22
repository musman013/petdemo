package com.fastcode.demopet.application.visits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVisitStatus {

	private Status status;
	private String visitNotes;
	private Long invoiceAmount;
	
}
