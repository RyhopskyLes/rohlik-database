package com.rohlik.data.kosik.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rohlik.data.kosik.interfaces.Setter;

public class NavigationItem  {
	static final String BASIC_URL = "https://www.kosik.cz";
	private String categoryName;
	private String uri;
	private List<NavigationSubItem> subcategories = new ArrayList<>();
	public NavigationItem() {		
	}
	public NavigationItem(String name) {
		this.categoryName = name;
	}
	public NavigationItem(String name, String uri) {
		this.categoryName = name;
		this.uri = uri;
	}
	
	public <T> NavigationItem set(Setter<T> setter, T param) {
		setter.accept(param);
		return this;		
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String name) {
		this.categoryName = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public List<NavigationSubItem> getSubcategories() {
		return subcategories;
	}
	public void setSubcategories(List<NavigationSubItem> subcategories) {
		this.subcategories = subcategories;
	}
	@Override
	public int hashCode() {
		return Objects.hash(categoryName, subcategories, uri);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavigationItem other = (NavigationItem) obj;
		return Objects.equals(categoryName, other.categoryName) && Objects.equals(subcategories, other.subcategories)
				&& Objects.equals(uri, other.uri);
	}
	@Override
	public String toString() {
		return "NavigationItem [name=" + categoryName + ", uri=" + uri + "]";
	}
	

}
