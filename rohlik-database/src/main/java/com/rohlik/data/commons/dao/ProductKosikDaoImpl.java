package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.ProductKosikRepository;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ProductKosik;


@Repository("productKosikDao")
@Transactional
public class ProductKosikDaoImpl implements ProductKosikDao {
	@Autowired
	 private ProductKosikRepository productKosikRepository;
	@Override
	public Optional<ProductKosik> findById(Integer id) {
		
		return productKosikRepository.findById(id);
	}

	@Override
	public List<ProductKosik> findAll() {		
		return productKosikRepository.findAll();
	}

	@Override
	public ProductKosik save(ProductKosik product) {		
		return productKosikRepository.save(product);
	}

	@Override
	public void delete(ProductKosik product) {
		productKosikRepository.delete(product);
		
	}

	@Override
	public Optional<ProductKosik> findByIdProduct(Integer id) {
		return productKosikRepository.findByIdProduct(id);
	}

	@Override
	public List<ProductKosik> findByCategory(String category) {
		return productKosikRepository.findByCategory(category);
	}

	

}
