package com.fastcode.demo.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.fastcode.demo.domain.model.VisitsEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IVisitsRepository extends JpaRepository<VisitsEntity, Integer>,QuerydslPredicateExecutor<VisitsEntity> {

}
