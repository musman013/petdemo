package com.fastcode.demopet.reporting.application.resourceviewer.dto;

import com.fastcode.demopet.reporting.application.permalink.dto.FindPermalinkByIdOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResourceOutput {

	FindPermalinkByIdOutput resourceInfo;
	Object data;

}
