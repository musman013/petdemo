package com.fastcode.demopet.application.invoices.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateInvoicesOutput {

  	private Long amount;
  	private Long id;
  	private Long visitId;
	private Date visitsDescriptiveField;
	private InvoiceStatus status;
	private Long vetId;
	private String vetsDescriptiveField;
	private Long ownerId;
	private String ownersDescriptiveField;

}