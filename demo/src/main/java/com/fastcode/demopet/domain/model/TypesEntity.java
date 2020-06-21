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
@Table(name = "types", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class TypesEntity extends AbstractEntity {

  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
	
  	@Basic
  	@Column(name = "name", nullable = true, length =80)
  	private String name;

  	@OneToMany(mappedBy = "types", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<PetsEntity> petsSet = new HashSet<PetsEntity>(); 
  
    public void addPets(PetsEntity pets) {
		petsSet.add(pets);
		pets.setTypes(this);
	}

	public void removePets(PetsEntity pets) {
		petsSet.remove(pets);
		pets.setTypes(null);
	}


}

  
      


