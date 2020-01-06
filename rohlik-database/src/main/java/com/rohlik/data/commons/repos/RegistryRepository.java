package com.rohlik.data.commons.repos;

import java.util.List;

import com.rohlik.data.commons.objects.Record;

public interface RegistryRepository {
public List<Record> findAllCategoryRecords();
public List<Record> findAllProductRecords();
public List<Record> findAllChildRecords();
public List<Record> findAllSaleRecords();
public List<Record> findAllCategoryKosikRecords();
public List<Record> findAllChildKosikRecords();
public List<Record> findAllProductKosikRecords();
}
