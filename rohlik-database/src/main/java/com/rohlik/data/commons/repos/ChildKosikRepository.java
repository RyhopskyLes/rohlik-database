package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rohlik.data.kosik.entities.ChildKosik;

public interface ChildKosikRepository extends JpaRepository<ChildKosik, Integer> {
	List<ChildKosik> findAll();
	Optional<ChildKosik> findById(Integer id);
	List<ChildKosik> findByCategoryName(String name);
	Set<ChildKosik> findByEquiCategoryName(String name);
	Set<ChildKosik> findByEquiId(Integer id);
	
}
