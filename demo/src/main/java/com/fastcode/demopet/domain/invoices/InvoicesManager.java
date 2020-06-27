package com.fastcode.demopet.domain.invoices;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.irepository.IVisitsRepository;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.irepository.IInvoicesRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class InvoicesManager implements IInvoicesManager {

    @Autowired
    IInvoicesRepository  _invoicesRepository;
    
    @Autowired
	IVisitsRepository  _visitsRepository;
    
	public InvoicesEntity create(InvoicesEntity invoices) {

		return _invoicesRepository.save(invoices);
	}

	public void delete(InvoicesEntity invoices) {

		_invoicesRepository.delete(invoices);	
	}

	public InvoicesEntity update(InvoicesEntity invoices) {

		return _invoicesRepository.save(invoices);
	}

	public InvoicesEntity findByProcessInstanceId(String value) {
		
		return _invoicesRepository.findByProcessInstanceId(value);
	}

	public InvoicesEntity findById(Long invoicesId) {
    	Optional<InvoicesEntity> dbInvoices= _invoicesRepository.findById(invoicesId);
		if(dbInvoices.isPresent()) {
			InvoicesEntity existingInvoices = dbInvoices.get();
		    return existingInvoices;
		} else {
		    return null;
		}

	}

	public Page<InvoicesEntity> findAll(Predicate predicate, Pageable pageable) {

		return _invoicesRepository.findAll(predicate,pageable);
	}
  
   //Visits
	public VisitsEntity getVisits(Long invoicesId) {
		
		Optional<InvoicesEntity> dbInvoices= _invoicesRepository.findById(invoicesId);
		if(dbInvoices.isPresent()) {
			InvoicesEntity existingInvoices = dbInvoices.get();
		    return existingInvoices.getVisits();
		} else {
		    return null;
		}
	}
}
