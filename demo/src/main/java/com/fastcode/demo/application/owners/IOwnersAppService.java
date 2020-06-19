package com.fastcode.demo.application.owners;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.owners.dto.*;

@Service
public interface IOwnersAppService {

	CreateOwnersOutput create(CreateOwnersInput owners);

    void delete(Integer id);

    UpdateOwnersOutput update(Integer id, UpdateOwnersInput input);

    FindOwnersByIdOutput findById(Integer id);

    List<FindOwnersByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
