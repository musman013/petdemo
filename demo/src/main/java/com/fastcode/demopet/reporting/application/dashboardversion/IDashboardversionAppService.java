package com.fastcode.demopet.reporting.application.dashboardversion;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.DashboardversionId;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.*;

@Service
public interface IDashboardversionAppService {

	CreateDashboardversionOutput create(CreateDashboardversionInput dashboardversion);

    void delete(DashboardversionId id);

    UpdateDashboardversionOutput update(DashboardversionId id, UpdateDashboardversionInput input);

    FindDashboardversionByIdOutput findById(DashboardversionId id);

    List<FindDashboardversionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    //User
    GetUserOutput getUser(DashboardversionId dashboardversionid);
}
