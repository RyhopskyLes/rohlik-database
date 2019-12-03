package com.rohlik.data.commons.services;

import java.util.List;
import java.util.Set;

import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.entities.ProductKosik;

public interface ProductKosikService {
	public void saveAllKosikProductsInCategoryToDatabase(String categoryURL);
	public List<ProductKosik> buildAllKosikProductsInCategory(String categoryURL);
	public void addProductToProductKosik(Integer kosikId, Integer rohlikId);
	public void removeProductFromProductKosik(Integer kosikId, Integer rohlikId);
}
