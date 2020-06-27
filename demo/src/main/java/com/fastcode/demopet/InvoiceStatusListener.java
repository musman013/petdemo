package com.fastcode.demopet;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "invoiceStatusListener")
public class InvoiceStatusListener implements TaskListener {

    @Autowired
    StartProcessService petClinicProcessService;

    /**
     *  when Loan request is reviewed by  Review Loan Request user task loan_request table will be updated by setting is_review_success to true/false
     */
    private static final long serialVersionUID = 1L;


    @Override
    public void notify(DelegateTask delegateTask) {

        petClinicProcessService.userTaskListener(delegateTask);

    }

}
