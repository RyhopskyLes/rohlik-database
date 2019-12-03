package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.ChildKosikRepository;
import com.rohlik.data.kosik.entities.ChildKosik;

@Repository("childKosikDao")
@Transactional
public class ChildKosikDaoImpl implements ChildKosikDao {
	@Autowired 
	ChildKosikRepository childRepository;

	@Override
	public List<ChildKosik> findAll() {
		return childRepository.findAll();
	}

	@Override
	public Optional<ChildKosik> findById(Integer id) {
		return childRepository.findById(id);
	}

	@Override
	public List<ChildKosik> findByCategoryName(String name) {
		return childRepository.findByCategoryName(name);
	}

	@Override
	public Set<ChildKosik> findByEquiCategoryName(String name) {
		return childRepository.findByEquiCategoryName(name);
	}

	@Override
	public Set<ChildKosik> findByEquiId(Integer id) {
		return childRepository.findByEquiId(id);
	}

	@Override
	public ChildKosik save(ChildKosik child) {
		return childRepository.save(child);
	}

	@Override
	public void delete(ChildKosik child) {
		childRepository.delete(child);
		
	}
}
