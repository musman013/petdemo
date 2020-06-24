package com.fastcode.demopet.emailbuilder.application.emailtemplate;

import org.mapstruct.Mapper;

import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.*;
import com.fastcode.demopet.emailbuilder.domain.model.EmailTemplateEntity;
import com.fastcode.demopet.emailbuilder.domain.model.EmailtemplateEntityHistory;

@Mapper(componentModel = "spring")
public interface EmailTemplateMapper {

    EmailTemplateEntity createEmailTemplateInputToEmailTemplateEntity(CreateEmailTemplateInput emailDto);

    CreateEmailTemplateOutput emailTemplateEntityToCreateEmailTemplateOutput(EmailTemplateEntity entity);

    EmailTemplateEntity updateEmailTemplateInputToEmailTemplateEntity(UpdateEmailTemplateInput emailDto);

    UpdateEmailTemplateOutput emailTemplateEntityToUpdateEmailTemplateOutput(EmailTemplateEntity entity);

    FindEmailTemplateByIdOutput emailTemplateEntityToFindEmailTemplateByIdOutput(EmailTemplateEntity entity);

    FindEmailTemplateByNameOutput emailTemplateEntityToFindEmailTemplateByNameOutput(EmailTemplateEntity entity);

	FindEmailTemplateByIdOutput emailTemplateEntityToFindEmailTemplateByIdOutputforReset(
			EmailtemplateEntityHistory foundEmail);

	EmailtemplateEntityHistory createEmailTemplateInputToEmailTemplateEntityforReset(CreateEmailTemplateInput email);
}