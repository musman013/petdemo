package com.fastcode.demopet.emailbuilder.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcode.demopet.emailbuilder.application.emailvariable.IMasterEntityService;

@RestController
@RequestMapping("/master")
public class MasterController {

	@Autowired
	private IMasterEntityService masterEntityService;
	
	@GetMapping("/getMastersByMasterName")
	ResponseEntity<String> getMastersByMasterName(@RequestParam(value="name") String name) throws Exception {
		return new ResponseEntity(masterEntityService.getMastersByMasterName(name), HttpStatus.OK);
	}
}
