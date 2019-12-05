package com.rohlik.data.commons.services;

import java.util.List;
import com.rohlik.data.kosik.entities.ProductKosik;

public interface ProductKosikService {
	public void saveAllKosikProductsInCategoryToDatabase(String categoryURL);
	public List<ProductKosik> buildAllKosikProductsInCategory(String categoryURL);
	public void addProductToProductKosik(Integer kosikId, Integer rohlikId);
	public void removeProductFromProductKosik(Integer kosikId, Integer rohlikId);
}
