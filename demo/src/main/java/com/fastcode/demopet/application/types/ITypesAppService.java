package com.fastcode.demopet.application.types;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.types.dto.*;

@Service
public interface ITypesAppService {

	CreateTypesOutput create(CreateTypesInput types);

    void delete(Long id);

    UpdateTypesOutput update(Long id, UpdateTypesInput input);

    FindTypesByIdOutput findById(Long id);

    List<FindTypesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
