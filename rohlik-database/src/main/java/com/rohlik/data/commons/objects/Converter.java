package com.rohlik.data.commons.objects;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.primitives.Ints;
import com.rohlik.data.entities.Category;
import com.rohlik.data.kosik.entities.ProductKosik;
import com.rohlik.data.kosik.objects.ElementMapper;
import com.rohlik.data.kosik.objects.ProducerInfo;
import com.rohlik.data.objects.productparts.CategoryByProduct;

@Component
public class Converter {
	private static final String BASIC_URL = "https://www.kosik.cz";
	@Autowired
	FieldHandler field;
	@Autowired
	ElementMapper mapper;

	public Converter() {
	}

	public Optional<ProductKosik> toProductKosik(ProducerInfo info, Optional<Element> element) {
		Optional<ProductKosik> product = Optional.empty();
		if (element.isPresent()) {
			product = buildFromElement(info, element);
		}
		return product;
	}

	public Optional<ProductKosik> toProductKosik(ProducerInfo info, Element element) {
		Optional<ProductKosik> product = Optional.<ProductKosik>empty();
		if (element != null) {
			Optional<Element> elementOpt = Optional.of(element);
			product = buildFromElement(info, elementOpt);
		}
		return product;
	}

	

	private Optional<ProductKosik> buildFromElement(ProducerInfo info, Optional<Element> element) {
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
		Optional<String> producer = Optional.ofNullable(info).map(inf -> inf.getName());
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

	private <T> Boolean setField(ProductKosik product, FieldData<T> data) throws Throwable {
		Boolean wasSet = false;
		if (data.getFieldValue().isPresent()) {
			field.getSetter(ProductKosik.class, data.getFieldName(), data.getFieldtype()).set(product,
					data.getFieldValue().get());
			wasSet = true;
		}
		return wasSet;
	}

	private Boolean setFields(ProductKosik product, Set<FieldData<?>> fieldsToSet) {
		Boolean[] wasSet = new Boolean[] { false };
		fieldsToSet.stream().forEach(data -> {
			try {
				Boolean set = setField(product, data);
				if (!wasSet[0])
					wasSet[0] = set;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
		return wasSet[0];
	}
}
