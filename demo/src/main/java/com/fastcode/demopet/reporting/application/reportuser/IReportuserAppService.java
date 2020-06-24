package com.fastcode.demopet.reporting.application.reportuser;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.reporting.application.reportuser.dto.*;

@Service
public interface IReportuserAppService {

	CreateReportuserOutput create(CreateReportuserInput reportuser);

    void delete(ReportuserId reportuserId);

    UpdateReportuserOutput update(ReportuserId reportuserId, UpdateReportuserInput input);

    FindReportuserByIdOutput findById(ReportuserId reportuserId);

    List<FindReportuserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public ReportuserId parseReportuserKey(String keysString);
    
    
    //Report
    GetReportOutput getReport(ReportuserId reportuserId);
}
