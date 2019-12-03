package com.rohlik.data.commons.objects;

import java.util.Objects;
import java.util.Optional;

import com.rohlik.data.commons.interfaces.IdMediator;

public class Result<T extends IdMediator> {
		
	private T entity;
	private Double dissimilarity;
	private Double relativeFrequency;
	private Double relativeSize;
	public Result() {
	}

	public Result(T entity, Double dissimilarity, Double relativeFrequency, Double relativeSize) {
		this.entity = entity;
		this.dissimilarity = dissimilarity;
		this.relativeFrequency=relativeFrequency;
		this.relativeSize=relativeSize;
	}

	public Result(Dissimilar<T> match) {
		this.entity = match.getEntity();
		this.dissimilarity = match.getDissimilarity();
	}

	public Result(WeightedDissimilar<T> match) {
		this.entity = match.getEntity();;
		this.dissimilarity = match.getDissimilarity();
		this.relativeFrequency=match.getRelativeFrequency();
		this.relativeSize=match.getRelativeSize();
	}
	
	public Optional<T> getEntity() {
		return Optional.<T>ofNullable(this.entity);
	}

	public Optional<Double> getDissimilaritytForLimit(Double limit) {
		if (this.dissimilarity!=null&&this.dissimilarity<=limit) {			
				return Optional.<Double>ofNullable(this.dissimilarity);
		}
		return Optional.<Double>empty();
	}

	public Optional<Integer> getEquiIdtForLimit(Double limit) {
		if (this.dissimilarity!=null&&this.dissimilarity<=limit) {			
				return this.getEquiId();
		}
		return Optional.<Integer>empty();
	}
	
	public Result<T> getResultForLimit(Double limit) {
		if (this.dissimilarity!=null&&limit!=null&&this.dissimilarity<=limit) {			
				return this;
		}
		return new Result<>();
	}
	
	public Optional<T> getEntityForLimit(Double limit) {
		if (this.dissimilarity!=null&&this.dissimilarity<=limit) {			
				return Optional.<T>ofNullable(this.entity);
		}
		return Optional.empty();
	}
	
	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Optional<Double> getDissimilarity() {
		return Optional.<Double>ofNullable(this.dissimilarity);
	}

	public void setDissimilarity(Double dissimilarity) {
		this.dissimilarity = dissimilarity;
	}

	@Override
	public String toString() {
		return "Result [entity=" + entity + ", dissimilarity=" + dissimilarity + ", relativeFrequency="
				+ relativeFrequency + ", relativeSize=" + relativeSize + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dissimilarity, entity, relativeFrequency, relativeSize);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result<T> other = (Result<T>) obj;
		return Objects.equals(dissimilarity, other.dissimilarity) && Objects.equals(entity, other.entity)
				&& Objects.equals(relativeFrequency, other.relativeFrequency)
				&& Objects.equals(relativeSize, other.relativeSize);
	}

	public Optional<Double> getRelativeFrequency() {
		return Optional.<Double>ofNullable(this.relativeFrequency);
	}

	public void setRelativeFrequency(Double relativeFrequency) {
		this.relativeFrequency = relativeFrequency;
	}

	public Optional<Double> getRelativeSize() {
		return Optional.<Double>ofNullable(this.relativeSize);
	}

	public void setRelativeSize(Double relativeSize) {
		this.relativeSize = relativeSize;
	}	
	
	public Optional<Integer> getEquiId() {
		return this.getEntity().map(IdMediator::provideId);				
	}

}
