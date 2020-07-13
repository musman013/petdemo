package com.fastcode.demopet.domain.authorization.user;

import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserManager {
    // CRUD Operations
    UserEntity create(UserEntity user);

    void delete(UserEntity user);

    UserEntity update(UserEntity user);

    UserEntity findById(Long id);
    
    UserEntity findByUserName(String userName);
    
    UserEntity findByEmailAddress(String emailAddress);
    
    Page<UserEntity> findAll(Predicate predicate, Pageable pageable);
  
}


