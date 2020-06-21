package com.fastcode.demopet.application.vets;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.vets.dto.*;

@Service
public interface IVetsAppService {

	CreateVetsOutput create(CreateVetsInput vets);

    void delete(Long id);

    UpdateVetsOutput update(Long id, UpdateVetsInput input);

    FindVetsByIdOutput findById(Long id);

    List<FindVetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
