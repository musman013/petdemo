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
@Table(name = "visits", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class VisitsEntity extends AbstractEntity {

  	@Basic
  	@Column(name = "description", nullable = true, length =255)
  	private String description;

  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
	
  	@OneToMany(mappedBy = "visits", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<InvoicesEntity> invoicesSet = new HashSet<InvoicesEntity>(); 
  
    public void addInvoices(InvoicesEntity invoices) {
		invoicesSet.add(invoices);
		invoices.setVisits(this);
	}

	public void removeInvoices(InvoicesEntity invoices) {
		invoicesSet.remove(invoices);
		invoices.setVisits(null);
	}
  	@ManyToOne
  	@JoinColumn(name = "petId")
  	private PetsEntity pets;
  	
 	@Basic
  	@Column(name = "visitDate", nullable = true)
  	private Date visitDate;
  	


}

  
      


