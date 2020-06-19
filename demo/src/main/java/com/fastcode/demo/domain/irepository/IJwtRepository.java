package com.fastcode.demo.domain.irepository; 
 
import com.fastcode.demo.domain.model.JwtEntity; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

@Repository
public interface IJwtRepository extends JpaRepository<JwtEntity, Long> { 

    JwtEntity findByToken(String token); 
    
} 