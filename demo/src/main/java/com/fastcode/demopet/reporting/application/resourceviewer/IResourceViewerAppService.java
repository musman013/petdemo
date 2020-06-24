package com.fastcode.demopet.reporting.application.resourceviewer;

import org.springframework.stereotype.Service;

import com.fastcode.demopet.reporting.application.permalink.dto.FindPermalinkByIdOutput;
import com.fastcode.demopet.reporting.application.resourceviewer.dto.ResourceOutput;

@Service
public interface IResourceViewerAppService {

	public ResourceOutput getData(FindPermalinkByIdOutput output);
	boolean isAuthorized(FindPermalinkByIdOutput output, String password);

}

