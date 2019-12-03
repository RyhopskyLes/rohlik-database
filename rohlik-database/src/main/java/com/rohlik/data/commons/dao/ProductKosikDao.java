package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ProductKosik;



public interface ProductKosikDao {
Optional<ProductKosik> findById(Integer id);
public List<ProductKosik> findAll();
public List<ProductKosik> findByCategory(String category);
ProductKosik save(ProductKosik product);
public void delete(ProductKosik product);
public Optional<ProductKosik> findByIdProduct(Integer id);

}
