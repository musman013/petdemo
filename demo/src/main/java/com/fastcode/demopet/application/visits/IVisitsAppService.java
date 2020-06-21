package com.fastcode.demopet.application.visits;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.visits.dto.*;

@Service
public interface IVisitsAppService {

	CreateVisitsOutput create(CreateVisitsInput visits);

    void delete(Long id);

    UpdateVisitsOutput update(Long id, UpdateVisitsInput input);

    FindVisitsByIdOutput findById(Long id);

    List<FindVisitsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    
    //Pets
    GetPetsOutput getPets(Long visitsid);
}
