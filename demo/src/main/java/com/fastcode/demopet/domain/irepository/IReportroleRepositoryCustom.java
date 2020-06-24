package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.domain.model.RoleEntity;

public interface IReportroleRepositoryCustom {

	Page<RoleEntity> getAvailableReportrolesList(Long reportId, String search, Pageable pageable);

	Page<RoleEntity> getReportrolesList(Long reportId, String search, Pageable pageable);


}
