package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import com.rohlik.data.entities.Child;

public interface ChildDao {
	List<Child> findAll();
	Optional<Child> findById(Integer id);
	Optional<Child> findByCategoryId(Integer id);
}
