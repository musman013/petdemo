package com.fastcode.demo.application.specialties;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.specialties.dto.*;

@Service
public interface ISpecialtiesAppService {

	CreateSpecialtiesOutput create(CreateSpecialtiesInput specialties);

    void delete(Integer id);

    UpdateSpecialtiesOutput update(Integer id, UpdateSpecialtiesInput input);

    FindSpecialtiesByIdOutput findById(Integer id);

    List<FindSpecialtiesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
