package com.fastcode.demopet.application.invoices.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateInvoicesOutput {

    private Long amount;
    private Long id;
    private String processInstanceId;
    private InvoiceStatus status;
	private Long version;
	
	private Long visitId;
	private Date visitsDescriptiveField;
	private Long vetId;
	private String vetsDescriptiveField;
	private Long ownerId;
	private String ownersDescriptiveField;

}
