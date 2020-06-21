package com.fastcode.demopet.application.specialties;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.specialties.dto.*;

@Service
public interface ISpecialtiesAppService {

	CreateSpecialtiesOutput create(CreateSpecialtiesInput specialties);

    void delete(Long id);

    UpdateSpecialtiesOutput update(Long id, UpdateSpecialtiesInput input);

    FindSpecialtiesByIdOutput findById(Long id);

    List<FindSpecialtiesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
