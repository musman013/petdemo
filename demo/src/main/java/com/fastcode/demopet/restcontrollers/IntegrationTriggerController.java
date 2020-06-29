package com.fastcode.demopet.restcontrollers;

import com.fastcode.demopet.application.invoices.IInvoicesAppService;
import com.fastcode.demopet.application.pets.dto.FindPetsByIdOutput;
import com.fastcode.demopet.domain.invoices.IInvoicesManager;
import com.fastcode.demopet.domain.model.InvoicesEntity;

import javassist.expr.NewArray;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

@RestController
@RequestMapping("/integrations")
public class IntegrationTriggerController {
	
	@Autowired
	private IInvoicesManager _invoicesManager;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findById(@PathVariable String id) {

    	InvoicesEntity invoice = _invoicesManager.findById(Long.valueOf(id));

        System.out.print("Entered the integrations controller");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "https://i-petclinicinvoicetracking-proj538029.6a63.fuse-ignite.openshiftapps.com/webhook/PX6RY8dicqvKQU5UPzlwKgvLB43Jb20VICdpPxITZ4NhHiMVna";

        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        JSONObject invoiceJsonObject = new JSONObject();
        invoiceJsonObject.put("visitCompletionDate", df.format(new Date()));
        invoiceJsonObject.put("ownerName", invoice.getVisits().getPets().getOwners().getUser().getFirstName());
        invoiceJsonObject.put("invoiceAmount", invoice.getAmount());
        invoiceJsonObject.put("paymentDueDate", df.format(DateUtils.addDays(invoice.getVisits().getVisitDate(), 15)));
        invoiceJsonObject.put("invoiceStatus", invoice.getStatus());

        HttpEntity<String> request =
                new HttpEntity<String>(invoiceJsonObject.toString(), headers);

        //Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");

        ResponseEntity<String> responseEntityStr  = restTemplate.postForEntity(uri, request, String.class);

        System.out.print("Exiting the integrations controller");

        return new ResponseEntity(responseEntityStr.getBody(), HttpStatus.OK);
    }
}
