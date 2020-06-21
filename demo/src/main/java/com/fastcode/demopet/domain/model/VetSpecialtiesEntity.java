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
@Table(name = "vet_specialties", schema = "sample")
@IdClass(VetSpecialtiesId.class)
@Getter @Setter
@NoArgsConstructor
public class VetSpecialtiesEntity extends AbstractEntity {

  	@ManyToOne
  	@JoinColumn(name = "specialtyId", insertable=false, updatable=false)
  	private SpecialtiesEntity specialties;
  	
  	@Id
  	@Column(name = "specialtyId", nullable = false)
  	private Long specialtyId;
  	
  	@Id
  	@Column(name = "vetId", nullable = false)
  	private Long vetId;
  	
  	@ManyToOne
  	@JoinColumn(name = "vetId", insertable=false, updatable=false)
  	private VetsEntity vets;
  	


}

  
      


