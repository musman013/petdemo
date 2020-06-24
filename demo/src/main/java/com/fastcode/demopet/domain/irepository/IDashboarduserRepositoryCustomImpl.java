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

import com.fastcode.demopet.domain.model.UserEntity;

@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class IDashboarduserRepositoryCustomImpl implements IDashboarduserRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired 
	private Environment env;

	@Override
	public Page<UserEntity> getAvailableDashboardusersList(Long dashboardId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.user e "
				+ "WHERE e.id NOT IN "
				+ "    (SELECT user_id "
				+ "     FROM %s.dashboarduser "
				+ "     WHERE dashboard_id = :dashboardId "
				+ "       AND owner_sharing_status = TRUE ) "
				+ "  AND e.id NOT IN "
				+ "    (SELECT owner_id "
				+ "     FROM %s.dashboard "
				+ "     WHERE id = :dashboardId) "
				+ "  AND (:search IS NULL "
				+ "       OR e.user_name ILIKE :search)", schema, schema, schema );
		
		Query query = entityManager.createNativeQuery(qlString, UserEntity.class)
				.setParameter("dashboardId",dashboardId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		List results = query.getResultList();
		int totalRows = results.size();

		Page<UserEntity> result = new PageImpl<UserEntity>(results, pageable, totalRows);

		return result;
	}
	
	@Override
	public Page<UserEntity> getDashboardusersList(Long dashboardId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.user e "
				+ "WHERE e.id IN "
				+ "    (SELECT user_id "
				+ "     FROM %s.dashboarduser du "
				+ "     WHERE dashboard_id = :dashboardId "
				+ "       AND du.owner_sharing_status = TRUE ) "
				+ "  AND (:search IS NULL "
				+ "       OR e.user_name ILIKE :search)"
				, schema, schema);
		
		Query query = entityManager.createNativeQuery(qlString, UserEntity.class)
				.setParameter("dashboardId",dashboardId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());

		List results = query.getResultList();
		int totalRows = results.size();

		Page<UserEntity> result = new PageImpl<UserEntity>(results, pageable, totalRows);

		return result;
	}
}
