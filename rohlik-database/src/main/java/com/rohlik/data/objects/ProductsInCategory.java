package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component("ProductInCategory")
public class ProductsInCategory {
	@Autowired
	private RootObject rootObject;

	public ProductsInCategory() {
		//no args constr for dependency injection
	}

	public List<RawProduct> getProductListForCategory(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<RawProduct> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray("productList"));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			products.add(converted);
		});

		return products;
	}

	public List<Integer> getProductIdsForCategory(Integer categoryId, Integer limitResults) {
		List<Integer> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray("productList"));

		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).map(Optional::ofNullable).forEach(productElement -> {
			Integer productId = productElement.map(JsonElement::getAsJsonObject)
					.map(object -> object.get("productId")).map(JsonElement::getAsInt).orElse(null);
			if(productId!=null) products.add(productId);
		});
		return products;
	}
	
	private Optional<JsonObject> getJsonDataObject(Integer categoryId, Integer limitResults) {
		return Objects.equals(rootObject.getCategoryId(), categoryId)&&Objects.equals(rootObject.getLimitResults(), limitResults)
				&& Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.CATEGORYDATA) ? rootObject.getJsonObject().map(object -> object.getAsJsonObject("data"))
				:rootObject.dataForCategory(categoryId, limitResults).map(object -> object.getAsJsonObject("data"));
	}
	
	public Optional<String> getTotalHitsForCategory(Integer number) {
		return rootObject.dataForCategory(number, 25)
				.map(object -> object.getAsJsonObject("data").get("totalHits").getAsString());
	}

}