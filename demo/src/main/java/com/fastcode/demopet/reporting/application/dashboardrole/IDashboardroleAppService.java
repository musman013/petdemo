package com.fastcode.demopet.reporting.application.dashboardrole;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.DashboardroleId;
import com.fastcode.demopet.reporting.application.dashboardrole.dto.*;

@Service
public interface IDashboardroleAppService {

	CreateDashboardroleOutput create(CreateDashboardroleInput dashboardrole);

    void delete(DashboardroleId dashboardroleId);

    UpdateDashboardroleOutput update(DashboardroleId dashboardroleId, UpdateDashboardroleInput input);

    FindDashboardroleByIdOutput findById(DashboardroleId dashboardroleId);

    List<FindDashboardroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public DashboardroleId parseDashboardroleKey(String keysString);
    
    //Dashboard
    GetDashboardOutput getDashboard(DashboardroleId dashboardroleId);
}
