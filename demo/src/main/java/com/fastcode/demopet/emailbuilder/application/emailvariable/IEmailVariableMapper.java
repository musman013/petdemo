package com.fastcode.demopet.emailbuilder.application.emailvariable;

import org.mapstruct.Mapper;

import com.fastcode.demopet.emailbuilder.application.emailvariable.dto.*;
import com.fastcode.demopet.emailbuilder.domain.model.EmailVariableEntity;

@Mapper(componentModel = "spring")
public interface IEmailVariableMapper {

    EmailVariableEntity createEmailVariableInputToEmailVariableEntity(CreateEmailVariableInput emailDto);

    CreateEmailVariableOutput emailVariableEntityToCreateEmailVariableOutput(EmailVariableEntity entity);

    EmailVariableEntity updateEmailVariableInputToEmailVariableEntity(UpdateEmailVariableInput emailDto);

    UpdateEmailVariableOutput emailVariableEntityToUpdateEmailVariableOutput(EmailVariableEntity entity);

    FindEmailVariableByIdOutput emailVariableEntityToFindEmailVariableByIdOutput(EmailVariableEntity entity);

    FindEmailVariableByNameOutput emailVariableEntityToFindEmailVariableByNameOutput(EmailVariableEntity entity);
}