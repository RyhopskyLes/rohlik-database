package com.rohlik.data.commons.services;

import java.util.List;
import java.util.Set;

import com.rohlik.data.entities.Product;

public interface ProductService {
public void deletAllDataFromALLTables();
public void showTables();
public void saveAllProductsFromRohlikToDatabase();
public List<Product> findAllProductsWithNearingExpiryDate();
public List<Product> findAllPremiumProducts();
public List<Product> findAllProductsWithoutProducer();
public void updateAllProductsWithoutProducer();
public void setMainCategoryNameByAllProducts();
public void updateAllProductsInCategoryInDatabase(Integer categoryId, Set<Integer>productId);
public void saveAllProductsInCategoryToDatabase(Integer categoryId, Set<Integer> productIdSet);
public List<Product> buildAllProductsInCategory(Integer categoryId);
public void updateHasSalesByProductsInCategory(Integer categoryId);
public void updateHasSalesByAllProductsInDatabase();
public void removeSalesFromProductInDatabase(Product product);
public void updateAllProductsFromRohlikInDatabase();
public void setCategoriesForProductsInDatabaseFromTo(Integer startId, Integer endId);
public void deleteRemovedProductsFromDatabase();
public Integer addMissingImgPathToProducts();
public Integer addMissingMainCategoryNameToProducts();
public Set<Product> updateActiveStateOfProductsInCategory(Integer categoryId);
public Set<Product> updateActiveStateOfAllProducts();
public void saveMissingImages();
public void updateMainCategoryIdAndNameByAllProductsInCategory(Integer categoryId);
}
