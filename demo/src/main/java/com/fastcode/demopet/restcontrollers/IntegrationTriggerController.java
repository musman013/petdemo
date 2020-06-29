package com.fastcode.demopet.restcontrollers;

import com.fastcode.demopet.application.pets.dto.FindPetsByIdOutput;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/integrations")
public class IntegrationTriggerController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findById(@PathVariable String id) {


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

        JSONObject invoiceJsonObject = new JSONObject();
        invoiceJsonObject.put("visitCompletionDate", "06/28/2020");
        invoiceJsonObject.put("ownerName", "Sunil");
        invoiceJsonObject.put("invoiceAmount", 9000);
        invoiceJsonObject.put("paymentDueDate", "07/11/2020");
        invoiceJsonObject.put("invoiceStatus", "Paid");

        HttpEntity<String> request =
                new HttpEntity<String>(invoiceJsonObject.toString(), headers);

        //Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");

        ResponseEntity<String> responseEntityStr  = restTemplate.postForEntity(uri, request, String.class);

        System.out.print("Exiting the integrations controller");

        return new ResponseEntity(responseEntityStr.getBody(), HttpStatus.OK);
    }
}
