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
public class IReportuserRepositoryCustomImpl implements IReportuserRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired 
	private Environment env;

	@Override
	public Page<UserEntity> getAvailableReportusersList(Long reportId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.f_user e "
				+ "WHERE e.id NOT IN "
				+ "    (SELECT user_id "
				+ "     FROM %s.reportuser ru "
				+ "     WHERE report_id = :reportId "
				+ "       AND ru.owner_sharing_status = TRUE ) "
				+ "  AND e.id NOT IN "
				+ "    (SELECT owner_id "
				+ "     FROM %s.report "
				+ "     WHERE id = :reportId) "
				+ "  AND (:search IS NULL "
				+ "       OR e.user_name ILIKE :search)", schema, schema, schema );
		
		Query query = entityManager.createNativeQuery(qlString, UserEntity.class)
				.setParameter("reportId",reportId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		List results = query.getResultList();
		int totalRows = results.size();

		Page<UserEntity> result = new PageImpl<UserEntity>(results, pageable, totalRows);

		return result;
	}
	
	@Override
	public Page<UserEntity> getReportusersList(Long reportId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT * "
				+ "FROM %s.f_user e "
				+ "WHERE e.id IN "
				+ "    (SELECT user_id "
				+ "     FROM %s.reportuser ru "
				+ "     WHERE report_id = :reportId "
				+ "       AND ru.owner_sharing_status = TRUE ) "
				+ "  AND (:search IS NULL "
				+ "       OR e.user_name ILIKE :search)"
				, schema, schema);
				
		Query query = entityManager.createNativeQuery(qlString, UserEntity.class)
				.setParameter("reportId",reportId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());

		List results = query.getResultList();
		int totalRows = results.size();

		Page<UserEntity> result = new PageImpl<UserEntity>(results, pageable, totalRows);

		return result;
	}
	
//	@Override
//	public List<ReportuserEntity> findByReportId(Long reportId)
//	{
//		
//		String qlString = "SELECT * FROM "+ env.getProperty("spring.jpa.properties.hibernate.default_schema")+".reportuser r WHERE" + 
//				" r.report_id = :reportId" ;
//		Query query = entityManager.createNativeQuery(qlString, ReportuserEntity.class)
//				.setParameter("reportId",reportId);
//				
//		List<ReportuserEntity> results = query.getResultList();
//		
//		return results;
//		
//	}

}
