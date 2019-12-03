package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohlik.data.kosik.entities.ProductKosik;

public interface ProductKosikRepository extends JpaRepository<ProductKosik, Integer> {
	Optional<ProductKosik> findByIdProduct(Integer id);
	List<ProductKosik> findByCategory(String category);
}
