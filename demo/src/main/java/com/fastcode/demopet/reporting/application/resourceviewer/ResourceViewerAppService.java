package com.fastcode.demopet.reporting.application.resourceviewer;

import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.reporting.application.dashboard.DashboardAppService;
import com.fastcode.demopet.reporting.application.dashboard.dto.FindDashboardByIdOutput;
import com.fastcode.demopet.reporting.application.permalink.dto.FindPermalinkByIdOutput;
import com.fastcode.demopet.reporting.application.report.ReportAppService;
import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.demopet.reporting.application.resourceviewer.dto.ResourceOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Validated
public class ResourceViewerAppService implements IResourceViewerAppService {

	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private PasswordEncoder pEncoder;

	@Autowired
	private ReportAppService _reportAppService;

	@Autowired
	private DashboardAppService _dashboardAppService;

	public ResourceOutput getData(FindPermalinkByIdOutput permalink){
		ResourceOutput data = new ResourceOutput();
		data.setResourceInfo(permalink);

		if(permalink.getResource().equals("report")) {
			FindReportByIdOutput report = _reportAppService.findById(permalink.getResourceId());
			if(report == null) {
				return null;
			}
			data.setData(report);
		}
		else if(permalink.getResource().equals("dashboard")) {
			FindDashboardByIdOutput dashboard = _dashboardAppService.findById(permalink.getResourceId());
			if(dashboard == null) {
				return null;
			}
			dashboard.setReportDetails(_dashboardAppService.setReportsList(permalink.getResourceId(), permalink.getUserId()));
			data.setData(dashboard);
		}
		return data;
	}

	public boolean isAuthorized(FindPermalinkByIdOutput output, String password) {
		if(output.getAuthentication() == "login") {
			if(_userAppService.getUser() == null) {
				return false;
			}
		}

		else if (output.getAuthentication() == "password") {
			if(!pEncoder.matches(password, output.getPassword())) {
				return false;
			}
		}

		return true;
	}



}



