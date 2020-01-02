package com.rohlik.data.commons.services.build;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.ProductsInCategory;

@Service("ProductBuildService")
public class ProductBuildServiceImpl implements ProductBuildService {
	private ProductsInCategory productsInCategory;
	
@Autowired
	public ProductBuildServiceImpl(ProductsInCategory productsInCategory) {
		super();
		this.productsInCategory = productsInCategory;
	}

	@Override
	public List<Product> buildAllProductsInCategoryWithoutProducers(Integer categoryId) {
		return productsInCategory.getProductListForCategoryWithSales(categoryId, 3000);		
	}

	@Override
	public List<Product> buildAllProductsInMainCategoryWithProducers(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
