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
@Table(name = "specialties", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class SpecialtiesEntity extends AbstractEntity {

  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
	
  	@Basic
  	@Column(name = "name", nullable = true, length =80)
  	private String name;

  	@OneToMany(mappedBy = "specialties", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<VetSpecialtiesEntity> vetspecialtiesSet = new HashSet<VetSpecialtiesEntity>(); 
  
    public void addVetSpecialties(VetSpecialtiesEntity vetspecialties) {
		vetspecialtiesSet.add(vetspecialties);
		vetspecialties.setSpecialties(this);
	}

	public void removeVetSpecialties(VetSpecialtiesEntity vetspecialties) {
		vetspecialtiesSet.remove(vetspecialties);
		vetspecialties.setSpecialties(null);
	}


}

  
      


