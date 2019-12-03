package com.rohlik.data.kosik.objects;

import java.util.Objects;

import com.rohlik.data.kosik.interfaces.Setter;

public class NavigationSubItem  {
	String categoryName;
	String uri;
	String parentUri;
	public NavigationSubItem() {		
	}
	public NavigationSubItem(String name) {
		super();
		this.categoryName = name;
	}
	public NavigationSubItem(String name, String uri) {
		super();
		this.categoryName = name;
		this.uri = uri;
	}
	public <T> NavigationSubItem set(Setter<T> setter, T param) {
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
	@Override
	public int hashCode() {
		return Objects.hash(categoryName, uri, parentUri);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavigationSubItem other = (NavigationSubItem) obj;
		return Objects.equals(categoryName, other.categoryName) && Objects.equals(uri, other.uri)
				&& Objects.equals(parentUri, other.parentUri);
	}
	@Override
	public String toString() {
		return "NavigationSubItem [categoryName=" + categoryName + ", uri=" + uri + ", parentUri=" + parentUri +"]";
	}
	public String getParentUri() {
		return parentUri;
	}
	public void setParentUri(String parentUri) {
		this.parentUri = parentUri;
	}
	
	

}
