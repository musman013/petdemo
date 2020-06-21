package com.fastcode.demopet.domain.model; 
 
import org.hibernate.validator.constraints.Length; 
import javax.persistence.*; 
import javax.validation.constraints.Email; 
import javax.validation.constraints.NotNull; 
import java.io.Serializable; 
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes =  @Index(name = "idx",columnList = "token",unique = false),
		name = "JwtEntity", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class JwtEntity implements Serializable { 

    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name = "Id", nullable = false) 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @Basic 
    @Column(name = "UserName", nullable = false, length = 32) 
    @NotNull 
    @Length(max = 32, message = "Username must be less than 32 characters")
    private String userName; 
   
    @Basic 
    @Column(name = "Token", nullable = false, length=10485760) 
    @NotNull 
    @Length(max = 10485760, message = "Token must be less than 10485760 characters")
    private String token; 
 
} 