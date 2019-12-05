package com.rohlik.data.commons.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.SalesRepository;
import com.rohlik.data.entities.Sale;
@Service("jpaSalesService")
@Transactional
public class SalesServiceImpl implements SalesService {
	
	@PersistenceContext
	private EntityManager em;	
	 private SalesRepository salesRepository;
	 
	 @Autowired
	public SalesServiceImpl(SalesRepository salesRepository) {
		super();
		this.salesRepository = salesRepository;
	}

	@Override
	public List<Sale> findAll() {
		return salesRepository.findAll();
	}

	@Override
	public Sale findBySalesId(Integer salesId) {
		return salesRepository.findById(salesId.longValue()).get();
	}

	@Override
	public Sale save(Sale sale) {
		if (sale.getIdSales() == null) {
			
		     em.persist(sale);
		          
		        } else {
		        
		    em.merge(sale);}
		   return sale;
	}

	@Override
	public void remove(Sale sale) {
		salesRepository.delete(sale);
		
	}

}
