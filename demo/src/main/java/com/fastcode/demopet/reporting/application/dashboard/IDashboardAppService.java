package com.fastcode.demopet.reporting.application.dashboard;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.reporting.application.dashboard.dto.*;
import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;

@Service
public interface IDashboardAppService {

	CreateDashboardOutput create(CreateDashboardInput dashboard);

    void delete(Long id, Long userId);

    UpdateDashboardOutput update(Long id, UpdateDashboardInput input);

    FindDashboardByIdOutput findById(Long id);

    List<FindDashboardByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    
    //User
    GetUserOutput getUser(Long dashboardid);

	List<FindReportByIdOutput> setReportsList(Long resourceId, Long userId);
}
