package com.fastcode.demopet.domain.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "permalink")
public class PermalinkEntity extends AbstractEntity {

//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.PostgresUUIDType")
//    @Column(name = "id", columnDefinition = "VARCHAR(255)")
//    private UUID id;

	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "id", nullable = false, length =256)
  	private UUID id;
    
    @Basic
  	@Column(name = "authentication", nullable = true, length =30)
  	private String authentication;
  	
    @Basic
  	@Column(name = "description", nullable = true)
  	private Boolean description;
	
    @Basic
  	@Column(name = "refreshRate", nullable = true)
    private Long refreshRate;
  	
    @Basic
  	@Column(name = "rendering", nullable = false, length =30)
  	private String rendering;
  	
    @Basic
  	@Column(name = "resource", nullable = false, length =30)
  	private String resource;
  	
    @Basic
  	@Column(name = "password", nullable = true, length =256)
    private String password;
    
    @Basic
  	@Column(name = "resourceId", nullable = true)
	private Long resourceId;

    @Basic
  	@Column(name = "toolbar", nullable = true)
  	private Boolean toolbar;

	@Column(name = "userId", nullable = false)
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "userId", insertable=false, updatable=false)
	private UserEntity user;

}

  
      


