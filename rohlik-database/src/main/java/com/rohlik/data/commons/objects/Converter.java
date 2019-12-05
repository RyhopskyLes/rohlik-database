package com.rohlik.data.commons.objects;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rohlik.data.kosik.entities.ProductKosik;
import com.rohlik.data.kosik.objects.ElementMapper;
import com.rohlik.data.kosik.objects.ProducerInfo;

@Component
public class Converter {
	private static final String BASIC_URL = "https://www.kosik.cz";
	private FieldHandler field;	
	private ElementMapper mapper;

	@Autowired
	public Converter(FieldHandler field, ElementMapper mapper) {
		this.field = field;
		this.mapper = mapper;
	}

	public Converter() {
	}

	public Optional<ProductKosik> toProductKosik(ProducerInfo info, Optional<Element> element) {
		Optional<ProductKosik> product = Optional.empty();
		if (element.isPresent()) {
			product = buildProductKosikFromElement(info, element);
		}
		return product;
	}

	public Optional<ProductKosik> toProductKosik(ProducerInfo info, Element element) {
		Optional<ProductKosik> product = Optional.<ProductKosik>empty();
		if (element != null) {
			Optional<Element> elementOpt = Optional.of(element);
			product = buildProductKosikFromElement(info, elementOpt);
		}
		return product;
	}

	

	private Optional<ProductKosik> buildProductKosikFromElement(ProducerInfo info, Optional<Element> element) {
		boolean wasSet = false;
		ProductKosik product = new ProductKosik();
		wasSet = setFields(product, fieldsToSet(info, element));
		if (wasSet) {
			product.setActive(true);
			return Optional.of(product);
		}

		return Optional.<ProductKosik>empty();
	}

	private Set<FieldData<?>> fieldsToSet(ProducerInfo info, Optional<Element> element) {
		Set<FieldData<?>> data = new HashSet<>();
		data.add(new FieldData<String>("name", String.class, mapper.toName(element)));
		data.add(new FieldData<Integer>("idProduct", Integer.class, mapper.toIdProduct(element)));
		Optional<String> producer = Optional.ofNullable(info).map(ProducerInfo::getName);
		data.add(new FieldData<String>("producer", String.class, producer));
		data.add(new FieldData<Double>("origPrice", Double.class, mapper.toOrigPrice(element)));
		data.add(new FieldData<Double>("actualPrice", Double.class, mapper.toActualPrice(element)));
		data.add(new FieldData<String>("productPath", String.class, mapper.toProductPath(element)));
		data.add(new FieldData<String>("imageSrc", String.class, mapper.toImageSrc(element)));
		data.add(new FieldData<String>("amountProduct", String.class, mapper.toAmountProduct(element)));
		data.add(new FieldData<String>("unitPrice", String.class, mapper.toUnitPrice(element)));
		data.add(new FieldData<Boolean>("inStock", Boolean.class, mapper.inStock(element)));
		return data;
	}

	private <T> boolean setField(ProductKosik product, FieldData<T> data) throws Throwable {
		boolean wasSet = false;
		if (data.getFieldValue().isPresent()) {
			field.getSetter(ProductKosik.class, data.getFieldName(), data.getFieldtype()).set(product,
					data.getFieldValue().get());
			wasSet = true;
		}
		return wasSet;
	}

	private boolean setFields(ProductKosik product, Set<FieldData<?>> fieldsToSet) {
		boolean[] wasSet = new boolean[] { false };
		fieldsToSet.stream().forEach(data -> {
			try {
				boolean set = setField(product, data);
				if (!wasSet[0])
					wasSet[0] = set;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
		return wasSet[0];
	}
}
