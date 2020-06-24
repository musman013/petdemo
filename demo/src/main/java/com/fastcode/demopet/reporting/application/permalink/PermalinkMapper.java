package com.fastcode.demopet.reporting.application.permalink;

import org.mapstruct.Mapper;
import com.fastcode.demopet.domain.model.PermalinkEntity;
import com.fastcode.demopet.reporting.application.permalink.dto.*;

@Mapper(componentModel = "spring")
public interface PermalinkMapper {

	PermalinkEntity createPermalinkInputToPermalinkEntity(CreatePermalinkInput permalinkDto);

	CreatePermalinkOutput permalinkEntityToCreatePermalinkOutput(PermalinkEntity entity);


	PermalinkEntity updatePermalinkInputToPermalinkEntity(UpdatePermalinkInput permalinkDto);

	UpdatePermalinkOutput permalinkEntityToUpdatePermalinkOutput(PermalinkEntity entity);

	FindPermalinkByIdOutput permalinkEntityToFindPermalinkByIdOutput(PermalinkEntity entity);


}
