package com.fastcode.demopet.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.emailbuilder.domain.model.EmailVariableTypeEntity;

@Repository
public interface IEmailVariableTypeRepository extends JpaRepository<EmailVariableTypeEntity, Long> {

}