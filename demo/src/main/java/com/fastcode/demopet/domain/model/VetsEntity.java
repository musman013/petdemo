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
@Table(name = "vets", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class VetsEntity extends AbstractEntity {

  	@Basic
  	@Column(name = "firstName", nullable = true, length =30)
  	private String firstName;

  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
	
  	@Basic
  	@Column(name = "lastName", nullable = true, length =30)
  	private String lastName;

  	@OneToMany(mappedBy = "vets", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<VetSpecialtiesEntity> vetspecialtiesSet = new HashSet<VetSpecialtiesEntity>(); 
  
    public void addVetSpecialties(VetSpecialtiesEntity vetspecialties) {
		vetspecialtiesSet.add(vetspecialties);
		vetspecialties.setVets(this);
	}

	public void removeVetSpecialties(VetSpecialtiesEntity vetspecialties) {
		vetspecialtiesSet.remove(vetspecialties);
		vetspecialties.setVets(null);
	}
	
	@OneToOne
    @MapsId
    private UserEntity user;


}

  
      


