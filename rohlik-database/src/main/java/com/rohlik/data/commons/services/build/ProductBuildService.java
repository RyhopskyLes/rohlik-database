package com.rohlik.data.commons.services.build;

import java.util.List;

import com.rohlik.data.entities.Product;

public interface ProductBuildService {
public List<Product> buildAllProductsInCategoryWithoutProducers(Integer categoryId);
public List<Product> buildAllProductsInMainCategoryWithProducers(Integer categoryId);
}
