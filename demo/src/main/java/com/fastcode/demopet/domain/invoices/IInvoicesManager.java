package com.fastcode.demopet.domain.invoices;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;

public interface IInvoicesManager {
    // CRUD Operations
    InvoicesEntity create(InvoicesEntity invoices);

    void delete(InvoicesEntity invoices);

    InvoicesEntity update(InvoicesEntity invoices);

    InvoicesEntity findById(Long id);
	
    Page<InvoicesEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Visits
    public VisitsEntity getVisits(Long invoicesId);
    
    InvoicesEntity findByProcessInstanceId(String value);
}
