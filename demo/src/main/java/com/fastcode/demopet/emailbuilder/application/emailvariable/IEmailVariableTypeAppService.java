package com.fastcode.demopet.emailbuilder.application.emailvariable;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.emailbuilder.application.emailvariable.dto.*;
import com.fastcode.demopet.emailbuilder.domain.irepository.IEmailVariableTypeRepository;
import com.fastcode.demopet.emailbuilder.domain.model.EmailVariableTypeEntity;

@Service
public interface IEmailVariableTypeAppService {
	


	List<EmailVariableTypeEntity> getAllTypes() throws Exception;

}
