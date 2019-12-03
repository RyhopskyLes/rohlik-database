package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.rohlik.data.entities.Child;



public interface ChildRepository extends JpaRepository<Child, Integer> {
	List<Child> findAll();
	Optional<Child> findById(Integer id);
	Optional<Child> findByCategoryId(Integer id);
}
