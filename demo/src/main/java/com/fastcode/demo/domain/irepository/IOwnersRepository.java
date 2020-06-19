package com.fastcode.demo.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demo.domain.model.PetsEntity; 
import java.util.List;
import com.fastcode.demo.domain.model.OwnersEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IOwnersRepository extends JpaRepository<OwnersEntity, Integer>,QuerydslPredicateExecutor<OwnersEntity> {

}
