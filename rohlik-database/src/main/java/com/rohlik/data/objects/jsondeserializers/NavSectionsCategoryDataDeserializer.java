package com.rohlik.data.objects.jsondeserializers;

import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rohlik.data.objects.NavSectionsCategoryData;

public class NavSectionsCategoryDataDeserializer implements JsonDeserializer<NavSectionsCategoryData> {

	@Override
	public NavSectionsCategoryData deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
			 {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Integer categoryId =jsonObject.get("categoryId").isJsonNull()? null :jsonObject.get("categoryId").getAsInt();
		String categoryName = jsonObject.get("name").isJsonNull()? null :jsonObject.get("name").getAsString();
		Integer productDisplayCount =jsonObject.get("productDisplayCount").isJsonNull()? null : jsonObject.get("productDisplayCount").getAsInt();
		Integer productsCount = jsonObject.get("productsCount").isJsonNull()? null : jsonObject.get("productsCount").getAsInt();
		
		return new NavSectionsCategoryData(categoryId, categoryName, productDisplayCount, productsCount);
	}

}
