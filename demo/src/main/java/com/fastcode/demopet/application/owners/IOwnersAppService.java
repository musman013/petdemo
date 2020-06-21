package com.fastcode.demopet.application.owners;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.owners.dto.*;

@Service
public interface IOwnersAppService {

	CreateOwnersOutput create(CreateOwnersInput owners);

    void delete(Long id);

    UpdateOwnersOutput update(Long id, UpdateOwnersInput input);

    FindOwnersByIdOutput findById(Long id);

    List<FindOwnersByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
