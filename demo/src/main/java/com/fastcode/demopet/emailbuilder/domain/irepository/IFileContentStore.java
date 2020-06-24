package com.fastcode.demopet.emailbuilder.domain.irepository;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.emailbuilder.domain.model.File;

@Repository
public interface IFileContentStore extends ContentStore<File, String> {
}
