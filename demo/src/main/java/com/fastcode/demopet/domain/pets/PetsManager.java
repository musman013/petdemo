package com.fastcode.demopet.domain.pets;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.irepository.IVisitsRepository;
import com.fastcode.demopet.domain.irepository.ITypesRepository;
import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.domain.irepository.IOwnersRepository;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.irepository.IPetsRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class PetsManager implements IPetsManager {

    @Autowired
    IPetsRepository  _petsRepository;
    
    @Autowired
	IVisitsRepository  _visitsRepository;
    
    @Autowired
	ITypesRepository  _typesRepository;
    
    @Autowired
	IOwnersRepository  _ownersRepository;
    
	public PetsEntity create(PetsEntity pets) {

		return _petsRepository.save(pets);
	}

	public void delete(PetsEntity pets) {

		_petsRepository.delete(pets);	
	}

	public PetsEntity update(PetsEntity pets) {

		return _petsRepository.save(pets);
	}

	public PetsEntity findById(Long petsId) {
    	Optional<PetsEntity> dbPets= _petsRepository.findById(petsId);
		if(dbPets.isPresent()) {
			PetsEntity existingPets = dbPets.get();
		    return existingPets;
		} else {
		    return null;
		}

	}

	public Page<PetsEntity> findAll(Predicate predicate, Pageable pageable) {

		return _petsRepository.findAll(predicate,pageable);
	}
  
   //Types
	public TypesEntity getTypes(Long petsId) {
		
		Optional<PetsEntity> dbPets= _petsRepository.findById(petsId);
		if(dbPets.isPresent()) {
			PetsEntity existingPets = dbPets.get();
		    return existingPets.getTypes();
		} else {
		    return null;
		}
	}
  
   //Owners
	public OwnersEntity getOwners(Long petsId) {
		
		Optional<PetsEntity> dbPets= _petsRepository.findById(petsId);
		if(dbPets.isPresent()) {
			PetsEntity existingPets = dbPets.get();
		    return existingPets.getOwners();
		} else {
		    return null;
		}
	}
}
