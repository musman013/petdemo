package com.fastcode.demopet.application.visits.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateVisitsOutput {

    private String description;
    private Long id;
    private Date visitDate;
    private Status status;
    private String visitNotes;
	private Long petId;
	private Long vetId;
	private String petsDescriptiveField;
	private String vetsDescriptiveField;

}
