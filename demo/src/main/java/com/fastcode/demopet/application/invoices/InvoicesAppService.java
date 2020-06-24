package com.fastcode.demopet.application.invoices;

import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.invoices.dto.*;
import com.fastcode.demopet.application.visits.dto.FindVisitsByIdOutput;
import com.fastcode.demopet.domain.invoices.IInvoicesManager;
import com.fastcode.demopet.domain.model.QInvoicesEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.visits.VisitsManager;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.owners.OwnersManager;
import com.fastcode.demopet.domain.vets.VetsManager;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class InvoicesAppService implements IInvoicesAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private IInvoicesManager _invoicesManager;

	@Autowired
	private VisitsManager _visitsManager;

	@Autowired
	private OwnersManager _ownersManager;

	@Autowired
	private VetsManager _vetsManager;

	@Autowired
	private IInvoicesMapper mapper;

	@Autowired
	private LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateInvoicesOutput create(CreateInvoicesInput input) {

		InvoicesEntity invoices = mapper.createInvoicesInputToInvoicesEntity(input);
		if(input.getVisitId()!=null) {
			VisitsEntity foundVisits = _visitsManager.findById(input.getVisitId());
			if(foundVisits!=null) {
				foundVisits.addInvoices(invoices);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}

		InvoicesEntity createdInvoices = _invoicesManager.create(invoices);
		return mapper.invoicesEntityToCreateInvoicesOutput(createdInvoices);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateInvoicesOutput update(Long  invoicesId, UpdateInvoicesInput input) {

		InvoicesEntity invoices = mapper.updateInvoicesInputToInvoicesEntity(input);
		if(input.getVisitId()!=null) {
			VisitsEntity foundVisits = _visitsManager.findById(input.getVisitId());
			if(foundVisits!=null) {
				invoices.setVisits(foundVisits);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}

		InvoicesEntity updatedInvoices = _invoicesManager.update(invoices);

		return mapper.invoicesEntityToUpdateInvoicesOutput(updatedInvoices);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateInvoicesOutput updateStatus(Long invoicesId, UserEntity user) {

		InvoicesEntity invoices = _invoicesManager.findById(invoicesId);
        VisitsEntity visits = _visitsManager.findById(invoices.getVisits().getId());
		if(_userAppService.checkIsAdmin(user) || user.getId() == visits.getPets().getOwners().getId())
		{
			invoices.setStatus(InvoiceStatus.PAID);
			InvoicesEntity updatedInvoices =_invoicesManager.update(invoices);
			return mapper.invoicesEntityToUpdateInvoicesOutput(updatedInvoices);
		}

		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long invoicesId) {

		InvoicesEntity existing = _invoicesManager.findById(invoicesId) ; 

		VisitsEntity visits = existing.getVisits();
		visits.removeInvoices(existing);

		_invoicesManager.delete(existing);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindInvoicesByIdOutput findById(Long invoicesId) {

		InvoicesEntity foundInvoices = _invoicesManager.findById(invoicesId);
		if (foundInvoices == null)  
			return null ; 

		FindInvoicesByIdOutput output=mapper.invoicesEntityToFindInvoicesByIdOutput(foundInvoices); 
		return output;
	}
	//Visits
	// ReST API Call - GET /invoices/1/visits
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetVisitsOutput getVisits(Long invoicesId) {

		InvoicesEntity foundInvoices = _invoicesManager.findById(invoicesId);
		if (foundInvoices == null) {
			logHelper.getLogger().error("There does not exist a invoices wth a id=%s", invoicesId);
			return null;
		}
		VisitsEntity re = _invoicesManager.getVisits(invoicesId);
		return mapper.visitsEntityToGetVisitsOutput(re, foundInvoices);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindInvoicesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<InvoicesEntity> foundInvoices = _invoicesManager.findAll(search(search), pageable);
		List<InvoicesEntity> invoicesList = foundInvoices.getContent();
		Iterator<InvoicesEntity> invoicesIterator = invoicesList.iterator(); 
		List<FindInvoicesByIdOutput> output = new ArrayList<>();

		while (invoicesIterator.hasNext()) {
			output.add(mapper.invoicesEntityToFindInvoicesByIdOutput(invoicesIterator.next()));
		}
		return output;
	}

	public List<FindInvoicesByIdOutput> filterInvoices(List<FindInvoicesByIdOutput> list, Long userId)
	{
		List<FindInvoicesByIdOutput> filteredList = new ArrayList<FindInvoicesByIdOutput>();
		OwnersEntity owner = _ownersManager.findById(userId);
		VetsEntity vet = _vetsManager.findById(userId);

		for(FindInvoicesByIdOutput obj : list)
		{
			InvoicesEntity invoice = _invoicesManager.findById(obj.getId());
			if(owner !=null) {
				if(userId == invoice.getVisits().getPets().getOwners().getId()) {
					filteredList.add(obj);
				}
			}
			if(vet !=null) {
				if(userId == invoice.getVisits().getVets().getId()) {
					filteredList.add(obj);
				}
			}
		}

		return filteredList;
	}

	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QInvoicesEntity invoices= QInvoicesEntity.invoicesEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(invoices, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("visitId") ||
					list.get(i).replace("%20","").trim().equals("amount") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("visits")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchKeyValuePair(QInvoicesEntity invoices, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("amount")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(invoices.amount.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(invoices.amount.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
					if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
						builder.and(invoices.amount.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
					else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
						builder.and(invoices.amount.goe(Long.valueOf(details.getValue().getStartingValue())));
					else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
						builder.and(invoices.amount.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("visitId")) {
				builder.and(invoices.visits.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}




}


