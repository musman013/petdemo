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
 // 	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@OneToMany(mappedBy = "vets", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<VisitsEntity> visitsSet = new HashSet<VisitsEntity>(); 
	
	public void addVisits(VisitsEntity visits) {
		visitsSet.add(visits);
		visits.setVets(this);
	}

	public void removeVisits(VisitsEntity visits) {
		visitsSet.remove(visits);
		visits.setVets(null);
	}

	
	@OneToOne
	@JoinColumn(name = "id")
//    @MapsId
    private UserEntity user;
	
}

  
      


