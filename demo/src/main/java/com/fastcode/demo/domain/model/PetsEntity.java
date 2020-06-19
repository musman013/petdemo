package com.fastcode.demo.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class PetsEntity extends AbstractEntity {

 	@Basic
  	@Column(name = "birthDate", nullable = true)
  	private Date birthDate;
  	
  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
  	private Integer id;
  	
  	@Basic
  	@Column(name = "name", nullable = true, length =30)
  	private String name;

  	@ManyToOne
  	@JoinColumn(name = "ownerId")
  	private OwnersEntity owners;
  	
  	@ManyToOne
  	@JoinColumn(name = "typeId")
  	private TypesEntity types;
  	
  	@OneToMany(mappedBy = "pets", cascade = CascadeType.ALL, orphanRemoval = true) 
  	private Set<VisitsEntity> visitsSet = new HashSet<VisitsEntity>(); 
  
    public void addVisits(VisitsEntity visits) {
		visitsSet.add(visits);
		visits.setPets(this);
	}

	public void removeVisits(VisitsEntity visits) {
		visitsSet.remove(visits);
		visits.setPets(null);
	}


}

  
      


