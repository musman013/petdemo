package com.fastcode.demopet;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.demopet.application.invoices.InvoicesAppService;
import com.fastcode.demopet.application.invoices.dto.CreateInvoicesInput;
import com.fastcode.demopet.application.invoices.dto.InvoiceStatus;
import com.fastcode.demopet.domain.irepository.IInvoicesRepository;
import com.fastcode.demopet.domain.model.InvoicesEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class StartProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
	private InvoicesAppService  _invoicesAppService;


    public void startProcess(String instanceKey,CreateInvoicesInput invoice) {

        // First create an invoice object

    //    InvoicesEntity invoice = new InvoicesEntity();

        // We need to copy the Invoice amount from the Visit object's amount
     //   invoice.setAmount(250L);
      //  invoice.setStatus(InvoiceStatus.Unpaid);

        
        // Start the process and pass the invoice status as a variable

        Map<String, Object> variables = new HashMap<>();
        variables.put("invoiceAmount", invoice.getAmount());
        variables.put("invoiceStatus", invoice.getStatus());

       ProcessInstance processInstance =  runtimeService.startProcessInstanceByKey(instanceKey, variables);
       invoice.setProcessInstanceId(processInstance.getProcessInstanceId());
       _invoicesAppService.create(invoice);

//      System.out.println(" invoice status " + runtimeService.getVariable(processInstance.getProcessInstanceId(), "invoiceStatus"));
//        System.out.println(" invoice variables " + runtimeService.getVariableInstances(instanceKey));
//     
      
    }
    
    public void updateInvoiceStatus(String processInstanceId, String variableName, String variableValue) {
    	runtimeService.setVariable(processInstanceId, variableName, variableValue);
    }


}
