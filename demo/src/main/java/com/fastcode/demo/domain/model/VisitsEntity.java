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
  	private Integer id;
  	
  	@ManyToOne
  	@JoinColumn(name = "petId")
  	private PetsEntity pets;
  	
  	@ManyToOne
  	@JoinColumn(name = "vetId")
  	private VetsEntity vets;
  	
 	@Basic
  	@Column(name = "visitDate", nullable = true)
  	private Date visitDate;
  	


}

  
      


