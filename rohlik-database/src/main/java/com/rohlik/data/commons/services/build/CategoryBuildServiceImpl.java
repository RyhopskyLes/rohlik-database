package com.rohlik.data.commons.services.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.NavigationCategoryInfo;

@Service("CategoryBuildService")
@Transactional
public class CategoryBuildServiceImpl implements CategoryBuildService {
	private Navigation navigation;

	@Autowired
	public CategoryBuildServiceImpl(Navigation navigation) {
		super();
		this.navigation = navigation;
	}

	@Override
	public Optional<Category> buildMainCategory(Integer categoryId) {
		Optional<NavigationCategoryInfo> categoryInfo = navigation.getAllMainCategoriesInfo().stream()
				.filter(category -> Objects.equals(category.getId(), categoryId)).findFirst();
		return categoryInfo.map(NavigationCategoryInfo::toCategory);

	}

	@Override
	public Optional<Category> buildMainCategoryWithChildren(Integer categoryId) {
		List<NavigationCategoryInfo> allCategoriesInfo = navigation.getAllCategoriesData();
		Optional<NavigationCategoryInfo> mainCategoryInfo = allCategoriesInfo.stream()
				.filter(category -> Objects.equals(category.getId(), categoryId)).findFirst();
		Optional<Category> mainCategory = mainCategoryInfo.map(NavigationCategoryInfo::toCategory);
		List<Integer> childrenId = mainCategoryInfo.map(NavigationCategoryInfo::getChildren).orElseGet(ArrayList::new);
		allCategoriesInfo.stream().filter(info -> childrenId.contains(info.getId()))
				.map(NavigationCategoryInfo::toChild).forEach(child -> mainCategory.ifPresent(category->category.addChild(child)));
		return mainCategory;
	}

	@Override
	public List<Category> buildAllMainCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> buildAllMainCategoriesWithChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
