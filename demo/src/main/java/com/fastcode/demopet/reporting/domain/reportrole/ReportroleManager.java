package com.fastcode.demopet.reporting.domain.reportrole;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.irepository.IReportRepository;
import com.fastcode.demopet.domain.irepository.IReportroleRepository;
import com.fastcode.demopet.domain.irepository.IRoleRepository;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportroleEntity;
import com.fastcode.demopet.domain.model.ReportroleId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.querydsl.core.types.Predicate;

@Component
public class ReportroleManager implements IReportroleManager {

	@Autowired
	IReportroleRepository  _reportroleRepository;

	@Autowired
	IRoleRepository  _roleRepository;

	@Autowired
	IReportRepository  _reportRepository;

	public ReportroleEntity create(ReportroleEntity reportrole) {

		return _reportroleRepository.save(reportrole);
	}

	public void delete(ReportroleEntity reportrole) {

		_reportroleRepository.delete(reportrole);	
	}

	public ReportroleEntity update(ReportroleEntity reportrole) {

		return _reportroleRepository.save(reportrole);
	}

	public ReportroleEntity findById(ReportroleId reportroleId) {

		Optional<ReportroleEntity> dbReportrole= _reportroleRepository.findById(reportroleId);
		if(dbReportrole.isPresent()) {
			ReportroleEntity existingReportrole = dbReportrole.get();
			return existingReportrole;
		} else {
			return null;
		}

	}

	public List<ReportroleEntity> findByRoleId(Long id)
	{
		return _reportroleRepository.findByRoleId(id);
	}

	public Page<RoleEntity> getAvailableRolesList(Long reportId, String search, Pageable pageable)
	{
		return _reportroleRepository.getAvailableReportrolesList(reportId, search, pageable);
	}

	public Page<RoleEntity> getReportRolesList(Long reportId, String search, Pageable pageable) {
		return _reportroleRepository.getReportrolesList(reportId, search, pageable);
	}

	public Page<ReportroleEntity> findAll(Predicate predicate, Pageable pageable) {

		return _reportroleRepository.findAll(predicate,pageable);
	}

	//Role
	public RoleEntity getRole(ReportroleId reportroleId) {

		Optional<ReportroleEntity> dbReportrole= _reportroleRepository.findById(reportroleId);
		if(dbReportrole.isPresent()) {
			ReportroleEntity existingReportrole = dbReportrole.get();
			return existingReportrole.getRole();
		} else {
			return null;
		}
	}

	//Report
	public ReportEntity getReport(ReportroleId reportroleId) {

		Optional<ReportroleEntity> dbReportrole= _reportroleRepository.findById(reportroleId);
		if(dbReportrole.isPresent()) {
			ReportroleEntity existingReportrole = dbReportrole.get();
			return existingReportrole.getReport();
		} else {
			return null;
		}
	}
}
