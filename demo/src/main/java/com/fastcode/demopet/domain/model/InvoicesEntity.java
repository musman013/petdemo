package com.fastcode.demopet.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
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
	
  	@ManyToOne
  	@JoinColumn(name = "visitId")
  	private VisitsEntity visits;
  	


}

  
      


