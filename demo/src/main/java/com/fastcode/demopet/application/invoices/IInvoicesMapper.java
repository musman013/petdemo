package com.fastcode.demopet.application.invoices;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.application.invoices.dto.*;
import com.fastcode.demopet.domain.model.InvoicesEntity;

@Mapper(componentModel = "spring")
public interface IInvoicesMapper {

   InvoicesEntity createInvoicesInputToInvoicesEntity(CreateInvoicesInput invoicesDto);
   
   @Mappings({ 
   @Mapping(source = "visits.id", target = "visitId"),                   
   @Mapping(source = "visits.id", target = "visitsDescriptiveField"),                    
   }) 
   CreateInvoicesOutput invoicesEntityToCreateInvoicesOutput(InvoicesEntity entity);

	
    InvoicesEntity updateInvoicesInputToInvoicesEntity(UpdateInvoicesInput invoicesDto);

    @Mappings({ 
    @Mapping(source = "visits.id", target = "visitId"),                   
    @Mapping(source = "visits.id", target = "visitsDescriptiveField"),                    
   }) 
   UpdateInvoicesOutput invoicesEntityToUpdateInvoicesOutput(InvoicesEntity entity);

   @Mappings({ 
   @Mapping(source = "visits.id", target = "visitId"),                   
   @Mapping(source = "visits.id", target = "visitsDescriptiveField"),                    
   }) 
   FindInvoicesByIdOutput invoicesEntityToFindInvoicesByIdOutput(InvoicesEntity entity);


   @Mappings({
   @Mapping(source = "visits.id", target = "id"),                  
   @Mapping(source = "invoices.id", target = "invoicesId"),
   })
   GetVisitsOutput visitsEntityToGetVisitsOutput(VisitsEntity visits, InvoicesEntity invoices);

}
