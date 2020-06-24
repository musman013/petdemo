package com.fastcode.demopet.reporting.domain.permalink;

import com.fastcode.demopet.domain.model.PermalinkEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface IPermalinkManager {
    // CRUD Operations
    PermalinkEntity create(PermalinkEntity permalink);

    void delete(PermalinkEntity permalink);

    PermalinkEntity update(PermalinkEntity permalink);

    PermalinkEntity findById(UUID id);
    
    PermalinkEntity findByResourceAndresourceId(Long resourceId, String resource);
	
    Page<PermalinkEntity> findAll(Predicate predicate, Pageable pageable);
}
