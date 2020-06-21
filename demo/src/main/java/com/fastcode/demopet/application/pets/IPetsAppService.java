package com.fastcode.demopet.application.pets;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.pets.dto.*;

@Service
public interface IPetsAppService {

	CreatePetsOutput create(CreatePetsInput pets);

    void delete(Long id);

    UpdatePetsOutput update(Long id, UpdatePetsInput input);

    FindPetsByIdOutput findById(Long id);

    List<FindPetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    
    //Types
    GetTypesOutput getTypes(Long petsid);
    
    //Owners
    GetOwnersOutput getOwners(Long petsid);
}
