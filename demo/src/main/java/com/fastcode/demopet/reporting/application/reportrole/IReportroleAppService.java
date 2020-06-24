package com.fastcode.demopet.reporting.application.reportrole;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.ReportroleId;
import com.fastcode.demopet.reporting.application.reportrole.dto.*;

@Service
public interface IReportroleAppService {

	CreateReportroleOutput create(CreateReportroleInput reportrole);

    void delete(ReportroleId reportroleId);

    UpdateReportroleOutput update(ReportroleId reportroleId, UpdateReportroleInput input);

    FindReportroleByIdOutput findById(ReportroleId reportroleId);

    List<FindReportroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public ReportroleId parseReportroleKey(String keysString);
    
    //Report
    GetReportOutput getReport(ReportroleId reportroleId);
}
