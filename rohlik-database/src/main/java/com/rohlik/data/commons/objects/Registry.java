package com.rohlik.data.commons.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohlik.data.commons.repos.RegistryRepository;
import com.rohlik.data.entities.Category;

@Service("Registry")
public class Registry {
	private RegistryRepository regRepository;
	private List<Record> categoryRecords;

	@Autowired
	public Registry(RegistryRepository regRepository) {
		super();
		this.regRepository = regRepository;
		this.categoryRecords = new CopyOnWriteArrayList<>(regRepository.findAllCategoryRecords());
	}

	public synchronized boolean removeCategoryRecord(Record record) {
		boolean removed = false;
		if (record != null && record.getClazz().equals(Category.class)) {

			removed = categoryRecords.remove(record);
		}
		return removed;
	}

	public synchronized boolean addCategoryRecord(Record record) {
		boolean added = false;
		if (record != null && record.getClazz().equals(Category.class) && !categoryRecords.contains(record) ) {

			added = categoryRecords.add(record);
		}
		return added;
	}

	public List<Record> getCategoryRecords() {
		return new ArrayList<>(categoryRecords);
	}

	public synchronized void refreshCategoryRegistry() {
		this.categoryRecords = new CopyOnWriteArrayList<>(regRepository.findAllCategoryRecords());
	}
}
