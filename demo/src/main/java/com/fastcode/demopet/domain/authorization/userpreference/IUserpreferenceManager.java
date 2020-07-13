package com.fastcode.demopet.domain.authorization.userpreference;

import com.fastcode.demopet.domain.model.UserpreferenceEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserpreferenceManager {
    // CRUD Operations
    UserpreferenceEntity create(UserpreferenceEntity userpreference);

    void delete(UserpreferenceEntity userpreference);

    UserpreferenceEntity update(UserpreferenceEntity userpreference);

    UserpreferenceEntity findById(Long id);
 
    Page<UserpreferenceEntity> findAll(Predicate predicate, Pageable pageable);
  
}


