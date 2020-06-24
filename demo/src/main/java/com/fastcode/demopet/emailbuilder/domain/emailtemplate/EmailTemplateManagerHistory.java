package com.fastcode.demopet.emailbuilder.domain.emailtemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.emailbuilder.domain.irepository.IEmailTemplateRepositoryHistory;
import com.fastcode.demopet.emailbuilder.domain.model.EmailtemplateEntityHistory;

@Repository
public class EmailTemplateManagerHistory implements IEmailTemplateManagerHistory {

	@Autowired
	IEmailTemplateRepositoryHistory  _emailTemplateRepositoryHistory;

	public EmailtemplateEntityHistory create(EmailtemplateEntityHistory email) {
		return _emailTemplateRepositoryHistory.save(email);
	}

	public void delete(EmailtemplateEntityHistory email) {
		_emailTemplateRepositoryHistory.delete(email);
	}

	@Override
	public EmailtemplateEntityHistory findById(Long emailId) {
		return _emailTemplateRepositoryHistory.findById(emailId.longValue());
	}

	@Override
	public EmailtemplateEntityHistory findByName(String name) {
		return _emailTemplateRepositoryHistory.findByEmailName(name);
	}

}
