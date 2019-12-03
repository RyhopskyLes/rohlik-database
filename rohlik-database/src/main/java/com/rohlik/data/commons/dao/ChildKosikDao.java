package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.rohlik.data.kosik.entities.ChildKosik;

public interface ChildKosikDao {
	List<ChildKosik> findAll();
	Optional<ChildKosik> findById(Integer id);
	List<ChildKosik> findByCategoryName(String name);
	Set<ChildKosik> findByEquiCategoryName(String name);
	Set<ChildKosik> findByEquiId(Integer id);
	ChildKosik save(ChildKosik child);
	void delete(ChildKosik child);
}
