package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.UserpreferenceEntity;

@Repository
public interface IUserpreferenceRepository extends JpaRepository<UserpreferenceEntity, Long>,QuerydslPredicateExecutor<UserpreferenceEntity> {

}
