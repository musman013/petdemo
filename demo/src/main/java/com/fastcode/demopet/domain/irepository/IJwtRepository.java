package com.fastcode.demopet.domain.irepository; 
 
import com.fastcode.demopet.domain.model.JwtEntity; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

@Repository
public interface IJwtRepository extends JpaRepository<JwtEntity, Long> { 

    JwtEntity findByToken(String token); 
    
} 