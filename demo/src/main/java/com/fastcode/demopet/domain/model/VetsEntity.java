package com.fastcode.demopet.domain.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vets", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class VetsEntity extends AbstractEntity {

  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
	

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

  
      


