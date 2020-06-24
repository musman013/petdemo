package com.fastcode.demopet.reporting.domain.reportversion;

import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReportversionManager {
    // CRUD Operations
    ReportversionEntity create(ReportversionEntity reportversion);

    void delete(ReportversionEntity reportversion);

    ReportversionEntity update(ReportversionEntity reportversion);

    ReportversionEntity findById(ReportversionId reportversionId);
	
    Page<ReportversionEntity> findAll(Predicate predicate, Pageable pageable);
   
  //  ReportversionEntity findByReportIdAndVersionAndUserId(ReportversionId id);
    
    List<ReportversionEntity> findByUserId(Long userId);
    //User
    public UserEntity getUser(ReportversionId reportversionId);
}
