package com.fastcode.demopet.reporting.application.report;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.reporting.application.report.dto.*;

@Service
public interface IReportAppService {

	CreateReportOutput create(CreateReportInput report);

    void delete(Long id, Long userId);

    UpdateReportOutput update(Long id, UpdateReportInput input);

    FindReportByIdOutput findById(Long id);

    List<FindReportByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    
    //User
    GetUserOutput getUser(Long reportid);
}
