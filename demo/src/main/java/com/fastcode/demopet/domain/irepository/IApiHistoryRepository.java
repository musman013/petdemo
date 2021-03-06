package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.ApiHistoryEntity;

@Repository
public interface IApiHistoryRepository
		extends JpaRepository<ApiHistoryEntity, Integer>, QuerydslPredicateExecutor<ApiHistoryEntity> {

	ApiHistoryEntity getByCorrelation(String correlation);
}
