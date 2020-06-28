package com.fastcode.demopet.application.invoices;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.application.invoices.dto.*;

@Service
public interface IInvoicesAppService {

	CreateInvoicesOutput create(CreateInvoicesInput invoices);

    void delete(Long id);

    UpdateInvoicesOutput update(Long id, UpdateInvoicesInput input);

    FindInvoicesByIdOutput findById(Long id);

    List<FindInvoicesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    
    //Visits
    GetVisitsOutput getVisits(Long invoicesid);

	InvoicesEntity updateStatus(String processInstanceId, InvoiceStatus status);
}
