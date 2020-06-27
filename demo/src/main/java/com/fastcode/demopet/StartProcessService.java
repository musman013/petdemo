package com.fastcode.demopet;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.demopet.application.invoices.InvoicesAppService;
import com.fastcode.demopet.application.invoices.dto.CreateInvoicesInput;
import com.fastcode.demopet.application.invoices.dto.InvoiceStatus;
import com.fastcode.demopet.application.owners.dto.FindOwnersByIdOutput;
import com.fastcode.demopet.domain.irepository.IInvoicesRepository;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.commons.logging.LoggingHelper;

import java.util.HashMap;
import java.util.Map;

@Service
public class StartProcessService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private InvoicesAppService _invoicesAppService;

	@Autowired
	LoggingHelper logger;


	public void startProcess(String instanceKey,CreateInvoicesInput invoice, FindOwnersByIdOutput owner) {

		// First create an invoice object

		// InvoicesEntity invoice = new InvoicesEntity();

		// We need to copy the Invoice amount from the Visit object's amount
		// invoice.setAmount(250L);
		// invoice.setStatus(InvoiceStatus.Unpaid);


		// Start the process and pass the invoice status as a variable

		Map<String, Object> variables = new HashMap<>();
		variables.put("invoiceAmount", invoice.getAmount());
		variables.put("invoiceStatus", invoice.getStatus().toString());
		variables.put("petOwner", owner.getFirstName());
		variables.put("ownerEmail", owner.getEmailAddress());

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(instanceKey, variables);
		invoice.setProcessInstanceId(processInstance.getProcessInstanceId());
		_invoicesAppService.create(invoice);

		// System.out.println(" invoice status " + runtimeService.getVariable(processInstance.getProcessInstanceId(), "invoiceStatus"));
		// System.out.println(" invoice variables " + runtimeService.getVariableInstances(instanceKey));
		//

	}

	public void updateInvoiceStatus(String processInstanceId, String variableName, String variableValue) {
		runtimeService.setVariable(processInstanceId, variableName, variableValue);
	}

	public void userTaskListener (DelegateTask delegateTask) {

		logger.getLogger().info("Flowable FlowableTaskListener notify called, event name {}", delegateTask.getEventName());
		logger.getLogger().info("task name : "+delegateTask.getName());

		String processInstanceId = (String) delegateTask.getProcessInstanceId();

		logger.getLogger().info("processInstanceId {}", processInstanceId);

		//String invoicePaid = (String) delegateTask.getVariable("form_invoicepayment_outcome");

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();
		// Task task = taskService.createTaskQuery().singleResult();

		// Map<String, Object> localVariables = taskService.getVariables(task.getId());

		String invoicePaid = null;
		InvoiceStatus status;

		Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
		invoicePaid = variables.get("form_invoicepayment_outcome").toString();

		if (invoicePaid.equalsIgnoreCase("Paid"))
			status = InvoiceStatus.Paid;
		else
			status = InvoiceStatus.Unpaid;

		InvoicesEntity invoice = _invoicesAppService.updateStatus(processInstanceId, status);



		// _invoiceRepository.save(invoice);

		// runtimeService.setVariable(processInstanceId, "invoiceStatus", invoice.getStatus());

	}


}