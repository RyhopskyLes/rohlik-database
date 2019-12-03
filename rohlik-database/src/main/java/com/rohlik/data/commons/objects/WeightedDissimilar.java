package com.rohlik.data.commons.objects;

import java.util.Objects;

public class WeightedDissimilar<T>  {
	private T entity;
	private Double dissimilarity;	
	private Double relativeFrequency;
	private Double relativeSize;
public WeightedDissimilar() {}
	public WeightedDissimilar(T entity, Double dissimilarity, Double relativeFrequency, Double relativeSize) {
		this.entity = entity;
		this.dissimilarity = dissimilarity;
		this.relativeFrequency=relativeFrequency;
		this.relativeSize=relativeSize;
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
	public Double getRelativeFrequency() {
		return relativeFrequency;
	}
	public void setRelativeFrequency(Double relativeFrequency) {
		this.relativeFrequency = relativeFrequency;
	}
	@Override
	public int hashCode() {
		return Objects.hash(relativeSize, dissimilarity, entity, relativeFrequency);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightedDissimilar<T> other = (WeightedDissimilar<T>) obj;
		return Objects.equals(relativeSize, other.relativeSize)
				&& Objects.equals(dissimilarity, other.dissimilarity) && Objects.equals(entity, other.entity)
				&& Objects.equals(relativeFrequency, other.relativeFrequency);
	}
	@Override
	public String toString() {
		return "WeightedDissimilar [entity=" + entity + ", dissimilarity=" + dissimilarity + ", relativeFrequency="
				+ relativeFrequency + ", relativeSize=" + relativeSize + "]";
	}
	public Double getRelativeSize() {
		return relativeSize;
	}
	public void setRelativeSize(Double relativeSize) {
		this.relativeSize = relativeSize;
	}
		
	
}
