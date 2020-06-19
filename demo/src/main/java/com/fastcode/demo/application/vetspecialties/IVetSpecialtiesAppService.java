package com.fastcode.demo.application.vetspecialties;

import java.util.List;
import javax.validation.constraints.Positive;
import com.fastcode.demo.domain.model.VetSpecialtiesId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.vetspecialties.dto.*;

@Service
public interface IVetSpecialtiesAppService {

	CreateVetSpecialtiesOutput create(CreateVetSpecialtiesInput vetspecialties);

    void delete(VetSpecialtiesId vetSpecialtiesId);

    UpdateVetSpecialtiesOutput update(VetSpecialtiesId vetSpecialtiesId, UpdateVetSpecialtiesInput input);

    FindVetSpecialtiesByIdOutput findById(VetSpecialtiesId vetSpecialtiesId);

    List<FindVetSpecialtiesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public VetSpecialtiesId parseVetSpecialtiesKey(String keysString);
    
    //Specialties
    GetSpecialtiesOutput getSpecialties(VetSpecialtiesId vetSpecialtiesId);
    
    //Vets
    GetVetsOutput getVets(VetSpecialtiesId vetSpecialtiesId);
}
