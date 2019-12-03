package com.rohlik.data.kosik.objects;

import java.util.ArrayList;
import java.util.List;

import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;

public class Cancelled {
private List<CategoryKosik> categories = new ArrayList<>();
private List<ChildKosik> children = new ArrayList<>();

public Cancelled() {	
}

public Cancelled(List<CategoryKosik> categories, List<ChildKosik> children) {	
	this.categories = categories;
	this.children = children;
}

public List<CategoryKosik> getCategories() {
	return categories;
}

public void setCategories(List<CategoryKosik> categories) {
	this.categories = categories;
}

public List<ChildKosik> getChildren() {
	return children;
}

public void setChildren(List<ChildKosik> children) {
	this.children = children;
}

}
