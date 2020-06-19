package com.fastcode.demo.application.apihistory;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.domain.model.ApiHistoryEntity;

@Service
public interface IApiHistoryAppService {

	ApiHistoryEntity create(ApiHistoryEntity types);

    ApiHistoryEntity update(Integer id, ApiHistoryEntity input);

    ApiHistoryEntity findById(Integer id);

    List<ApiHistoryEntity> find(SearchCriteria search, Pageable pageable) throws Exception;

}