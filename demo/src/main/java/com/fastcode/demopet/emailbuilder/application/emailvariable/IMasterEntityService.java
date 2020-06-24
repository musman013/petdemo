package com.fastcode.demopet.emailbuilder.application.emailvariable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public interface IMasterEntityService {

	List<String> getMastersByMasterName(String name);

}
