package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.entities.Child;
import com.rohlik.data.commons.repos.ChildRepository;
@Repository("childDao")
@Transactional
public class ChildDaoImpl implements ChildDao {
@Autowired
ChildRepository childRepository;
	@Override
	public List<Child> findAll() {		
		return childRepository.findAll();
	}

	@Override
	public Optional<Child> findById(Integer id) {
		return childRepository.findById(id);
	}

	@Override
	public Optional<Child> findByCategoryId(Integer id) {
		return childRepository.findByCategoryId(id);
	}

}
