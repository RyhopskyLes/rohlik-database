package com.rohlik.data.commons.objects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.rohlik.data.commons.exceptions.NullIdException;
import com.rohlik.data.commons.exceptions.WrongOrMissingClassException;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.entities.Product;
import com.rohlik.data.entities.Sale;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.entities.ProductKosik;

public final class Record {
	private final Integer persistedId;
	private final Integer nativeId;
	private final Class<?> clazz;
	private static final List<Class<?>> CLASSES = Arrays.asList(Category.class, Child.class, Product.class, Sale.class, ProductKosik.class, CategoryKosik.class, ChildKosik.class);
	
		 

	public Record(Integer persistedId, Integer nativeId, Class<?> clazz) throws NullIdException, WrongOrMissingClassException {
		super();
		if (persistedId == null || nativeId == null) {
			throw new NullIdException(
					"neither persistedId: " + persistedId + " nor nativeId: " + nativeId + " can be null");
		}
		String message = "Paremeter clazz: \r\n"+clazz+"cannot be nul\r\n"
				+"and must be one o these classes:\r\n "
				+"\tcom.rohlik.data.entities.Category\r\n" + 
				"\tcom.rohlik.data.entities.Child\r\n" + 
				"\timport com.rohlik.data.entities.Product\r\n" + 
				"\tcom.rohlik.data.entities.Sale\r\n" + 
				"\tcom.rohlik.data.kosik.entities.CategoryKosik\r\n" + 
				"\tcom.rohlik.data.kosik.entities.ChildKosik\r\n" + 
				"\tcom.rohlik.data.kosik.entities.ProductKosik";
		if(clazz==null || !CLASSES.contains(clazz)) {throw new WrongOrMissingClassException(message);}
		this.persistedId = persistedId;
		this.nativeId = nativeId;
		this.clazz = clazz;
	}

	public Integer getPersistedId() {
		return persistedId;
	}

	public Integer getNativeId() {
		return nativeId;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(nativeId, persistedId, clazz);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		return Objects.equals(nativeId, other.nativeId) && Objects.equals(persistedId, other.persistedId)
				&&Objects.equals(clazz, other.clazz);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	@Override
	public String toString() {
		return "Record [persistedId=" + persistedId + ", nativeId=" + nativeId + ", clazz=" + clazz + "]";
	}

}
