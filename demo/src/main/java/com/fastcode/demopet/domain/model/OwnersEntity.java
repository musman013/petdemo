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
@Table(name = "owners", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class OwnersEntity extends AbstractEntity {

  	@Basic
  	@Column(name = "address", nullable = true, length =255)
  	private String address;

  	@Basic
  	@Column(name = "city", nullable = true, length =80)
  	private String city;

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

  	@Basic
  	@Column(name = "telephone", nullable = true, length =20)
  	private String telephone;

  	@OneToMany(mappedBy = "owners", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<PetsEntity> petsSet = new HashSet<PetsEntity>(); 
  
    public void addPets(PetsEntity pets) {
		petsSet.add(pets);
		pets.setOwners(this);
	}

	public void removePets(PetsEntity pets) {
		petsSet.remove(pets);
		pets.setOwners(null);
	}

	@OneToOne
    @MapsId
    private UserEntity user;


}

  
      


