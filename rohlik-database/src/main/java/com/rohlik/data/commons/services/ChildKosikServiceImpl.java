package com.rohlik.data.commons.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.ChildKosikDao;
import com.rohlik.data.entities.Category;

@Service("ChildKosikService")
@Transactional
@SuppressWarnings("unchecked")
public class ChildKosikServiceImpl implements ChildKosikService {
	@Autowired
	ChildKosikDao childDao;
	@Autowired
	CategoryDao catDao;

	@Override
	public void addMissingEquiCategoryNameToAllChildren() {
		List<ChildKosik> children = childDao.findAll();
		children.stream().filter(child -> child.getEquiId() != null)
				.filter(child -> child.getEquiCategoryName() == null)
				.forEach(child -> {
					catDao.findByCategoryId(child.getEquiId())
					.ifPresent(category->{child.setEquiCategoryName(category.getCategoryName());
					childDao.save(child);});
				});

	}

	@Override
	public Set<ChildKosik> findChildrenWithDeadEquiIds() {
		List<ChildKosik> all = childDao.findAll();
		List<Category> allCategories = catDao.findAll();
		Function<Integer, Category> getByCategoryId = id-> allCategories.stream().filter(category->category.getCategoryId().equals(id)).findFirst().orElse(null);
		Predicate<ChildKosik> getActiveFalse = child-> getByCategoryId.apply(child.getEquiId())==null||getByCategoryId.apply(child.getEquiId()).getActive().equals(false);
		Predicate<ChildKosik> isActiveFalse = child-> child.getEquiId()!=null&&getActiveFalse.test(child);
		return all.stream().filter(isActiveFalse::test).collect(Collectors.toCollection(HashSet::new));
		
	}

}
