package com.fastcode.demopet.emailbuilder.application.emailvariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fastcode.demopet.emailbuilder.domain.irepository.IEmailVariableTypeRepository;
import com.fastcode.demopet.emailbuilder.domain.model.EmailVariableTypeEntity;

@Service
@Validated
public class EmailVariableTypeAppService implements IEmailVariableTypeAppService {
	@Autowired
	IEmailVariableTypeRepository emailVariableTypeRepository;

	@Override
	public List<EmailVariableTypeEntity> getAllTypes() throws Exception {
		return emailVariableTypeRepository.findAll();
	}

}
