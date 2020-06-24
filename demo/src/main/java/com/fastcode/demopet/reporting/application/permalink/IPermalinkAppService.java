package com.fastcode.demopet.reporting.application.permalink;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.reporting.application.permalink.dto.*;

@Service
public interface IPermalinkAppService {

	CreatePermalinkOutput create(CreatePermalinkInput permalink);

    void delete(UUID id);

    UpdatePermalinkOutput update(UUID id, UpdatePermalinkInput input);

    FindPermalinkByIdOutput findById(UUID id);

    List<FindPermalinkByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
