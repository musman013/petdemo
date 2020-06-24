package com.fastcode.demopet.reporting.application.reportversion;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.reporting.application.reportversion.dto.*;

@Service
public interface IReportversionAppService {
	
	CreateReportversionOutput create(CreateReportversionInput report);

    void delete(ReportversionId id);

    UpdateReportversionOutput update(ReportversionId id, UpdateReportversionInput input);

    FindReportversionByIdOutput findById(ReportversionId id);

    List<FindReportversionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    
    //User
    GetUserOutput getUser(ReportversionId reportid);

}
