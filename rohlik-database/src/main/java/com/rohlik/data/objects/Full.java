package com.rohlik.data.objects;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component("full")
public class Full {
	private RootObject rootObject;
	private Gson gson;
	
	@Autowired
	public Full(RootObject rootObject) {
		this.rootObject = rootObject;
		this.gson= new Gson();
	}
	
	public ProductFull getProductFull(Integer productId) {
		Optional<JsonObject> product = rootObject.dataForProduct(productId).map(obj -> obj.getAsJsonObject("data"))
				.map(data -> data.get("product")).map(JsonElement::getAsJsonObject);
		return gson.fromJson(product.orElse(new JsonObject()), ProductFull.class);		
	}

	

}
