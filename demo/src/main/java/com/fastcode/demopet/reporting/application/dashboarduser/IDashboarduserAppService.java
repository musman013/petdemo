package com.fastcode.demopet.reporting.application.dashboarduser;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.reporting.application.dashboarduser.dto.*;

@Service
public interface IDashboarduserAppService {

	CreateDashboarduserOutput create(CreateDashboarduserInput dashboarduser);

    void delete(DashboarduserId dashboarduserId);

    UpdateDashboarduserOutput update(DashboarduserId dashboarduserId, UpdateDashboarduserInput input);

    FindDashboarduserByIdOutput findById(DashboarduserId dashboarduserId);

    List<FindDashboarduserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public DashboarduserId parseDashboarduserKey(String keysString);
    
    //Dashboard
    GetDashboardOutput getDashboard(DashboarduserId dashboarduserId);
}
