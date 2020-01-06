package com.rohlik.data.commons.repos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rohlik.data.commons.exceptions.NullIdException;
import com.rohlik.data.commons.exceptions.WrongOrMissingClassException;
import com.rohlik.data.commons.objects.Record;
import com.rohlik.data.entities.Category;

@Repository("RegistryRepository")
public class RegistryRepositoryMySqlImpl implements RegistryRepository {
	@PersistenceContext
	private EntityManager em;
	private static final String FIND_ALL_CATEGORIES_QUERY = "select id as persistedId, categoryId as nativeId from category;";
	private static Logger logger = LoggerFactory.getLogger(RegistryRepositoryMySqlImpl.class);	
	
	@Autowired
	public RegistryRepositoryMySqlImpl(EntityManager em) {
		super();
		this.em = em;			
	}

	@Override
	public List<Record> findAllCategoryRecords() {

		Query q = em.createNativeQuery(FIND_ALL_CATEGORIES_QUERY);
		List<Object[]> rs = q.getResultList();
		
		return rs.stream().map(toRecord(Category.class)::apply).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		
		
	}

	@Override
	public List<Record> findAllProductRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findAllChildRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findAllSaleRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findAllCategoryKosikRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findAllChildKosikRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findAllProductKosikRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	private Function<Object[], Record> toRecord(Class<?> clazz) {
		return row -> {
			Record parsed = null;
			try {
				parsed = new Record((Integer) row[0], (Integer) row[1], clazz);
			} catch (NullIdException | WrongOrMissingClassException e) {
				logger.info("{}", e);}
			return parsed;
		};
	}
}
