package com.fastcode.demopet.emailbuilder.domain.irepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.emailbuilder.domain.model.File;

@Repository
public interface IFileRepository extends JpaRepository<File, Long> {

	@Modifying
	@Transactional
	@Query("update  File u set u.deleted = true  where u.id not in (:ids) and u.emailTemplateId = :emailTemplateId ")
	void setDeleteAdditionalFileEmailTemplate(@Param("ids") List<Long> allHistoryFiles, @Param("emailTemplateId") Long emailTemplateId);
	
	@Modifying
	@Transactional
	@Query("update  File u set u.deleted = true  where  u.emailTemplateId = :emailTemplateId ")
	void deletePreviousTemplate( @Param("emailTemplateId") Long emailTemplateId);
	
	@Modifying
	@Transactional
	@Query("update File u set u.emailTemplateId = :emailTemplateId , u.deleted = false where u.id = :id")
	void updateFileEmailTemplate(@Param("id") Long id, @Param("emailTemplateId") Long emailTemplateId);

	
	@Modifying
	@Transactional
	@Query("update File u set u.emailVariableId = :emailVariableId where u.id = :id")
	void updateFileVariableTemplate(@Param("id") Long id, @Param("emailVariableId") Long emailVariableId);
	
	@Modifying
	@Transactional
	@Query("update File u set u.emailVariableId = :emailVariableId where u.id in( :ids ) ")
	void updateFileVariableTemplateList(@Param("ids") List<Long> ids, @Param("emailVariableId") Long emailVariableId);

	
	List<File> getFileByEmailTemplateIdAndDeletedFalse(Long emailTemplateId);
	
	
	List<File> getFileByEmailVariableIdAndDeletedFalse(Long emailTemplateId);

}
