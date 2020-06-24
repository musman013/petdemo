package com.fastcode.demopet.domain.model;

import javax.persistence.*;

import com.fastcode.demopet.application.invoices.dto.InvoiceStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class InvoicesEntity extends AbstractEntity {

  	@Basic
  	@Column(name = "amount", nullable = true)
	private Long amount;
	
  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
  	
  	@Basic
  	@Column(name = "status", nullable = false, length =20)
  	private InvoiceStatus status;
	
  	@ManyToOne
  	@JoinColumn(name = "visitId")
  	private VisitsEntity visits;
  	


}

  
      


