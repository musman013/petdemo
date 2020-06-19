package com.fastcode.demo.domain.apihistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demo.domain.model.ApiHistoryEntity;
import com.querydsl.core.types.Predicate;

public interface IApiHistoryManager {
	// CRUD Operations
	ApiHistoryEntity create(ApiHistoryEntity apiHistory);

	ApiHistoryEntity update(ApiHistoryEntity apiHistory);

	ApiHistoryEntity findById(Integer id);

	Page<ApiHistoryEntity> findAll(Predicate predicate, Pageable pageable);
}
