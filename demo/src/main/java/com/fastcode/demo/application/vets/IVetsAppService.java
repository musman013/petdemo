package com.fastcode.demo.application.vets;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.vets.dto.*;

@Service
public interface IVetsAppService {

	CreateVetsOutput create(CreateVetsInput vets);

    void delete(Integer id);

    UpdateVetsOutput update(Integer id, UpdateVetsInput input);

    FindVetsByIdOutput findById(Integer id);

    List<FindVetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
