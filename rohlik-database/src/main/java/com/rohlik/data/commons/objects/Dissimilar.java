package com.rohlik.data.commons.objects;

import java.util.Objects;
import java.util.Optional;

public class Dissimilar<T> {
	private T entity;
	private Double dissimilarity;
	
public Dissimilar() {}
	public Dissimilar(T entity, Double dissimilarity) {
		this.entity = entity;
		this.dissimilarity = dissimilarity;
	}
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	public Double getDissimilarity() {
		return dissimilarity;
	}
	public void setDissimilarity(Double dissimilarity) {
		this.dissimilarity = dissimilarity;
	}
	@Override
	public String toString() {
		return "Dissimilar [entity=" + entity + ", dissimilarity=" + dissimilarity + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(dissimilarity, entity);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dissimilar<T> other = (Dissimilar<T>) obj;
		return Objects.equals(dissimilarity, other.dissimilarity) && Objects.equals(entity, other.entity);
	}
	
	
	
}
