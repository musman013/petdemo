package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.PermalinkEntity;
import java.util.UUID;

@Repository
public interface IPermalinkRepository extends JpaRepository<PermalinkEntity, UUID>,QuerydslPredicateExecutor<PermalinkEntity> {

}
