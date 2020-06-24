package com.fastcode.demopet.domain.irepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.RoleEntity;

@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class IReportroleRepositoryCustomImpl implements IReportroleRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired 
	private Environment env;

	@Override
	public Page<RoleEntity> getAvailableReportrolesList(Long reportId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.role r "
				+ "WHERE r.id NOT IN "
				+ "    (SELECT role_id "
				+ "     FROM %s.reportrole "
				+ "     WHERE report_id = :reportId "
				+ "       AND owner_sharing_status = true) "
				+ "  AND (:search IS NULL "
				+ "       OR r.name ILIKE :search)",
				schema, schema);

		Query query = entityManager.createNativeQuery(qlString, RoleEntity.class)
				.setParameter("reportId",reportId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		List results = query.getResultList();
		int totalRows = results.size();

		Page<RoleEntity> result = new PageImpl<RoleEntity>(results, pageable, totalRows);

		return result;
	}

	@Override
	public Page<RoleEntity> getReportrolesList(Long reportId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.role r "
				+ "WHERE r.id IN " 
				+ "    (SELECT role_id "
				+ "     FROM %s.reportrole "
				+ "     WHERE report_id = :reportId "
				+ "       AND owner_sharing_status = true) "
				+ "  AND (:search IS NULL "
				+ "       OR r.name ILIKE :search)",
				schema, schema);
		Query query = entityManager.createNativeQuery(qlString, RoleEntity.class)
				.setParameter("reportId",reportId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());

		List results = query.getResultList();
		int totalRows = results.size();

		Page<RoleEntity> result = new PageImpl<RoleEntity>(results, pageable, totalRows);

		return result;
	}

}
