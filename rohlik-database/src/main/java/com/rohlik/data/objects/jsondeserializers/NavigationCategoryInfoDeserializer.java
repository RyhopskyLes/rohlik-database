package com.rohlik.data.objects.jsondeserializers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rohlik.data.objects.NavigationCategoryInfo;

public class NavigationCategoryInfoDeserializer implements JsonDeserializer<NavigationCategoryInfo>  {

	@Override
	public NavigationCategoryInfo deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
			{
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		Integer categoryId =jsonObject.get("id").isJsonNull()? null : jsonObject.get("id").getAsInt();
		String categoryName =jsonObject.get("name").isJsonNull()? null :jsonObject.get("name").getAsString();
		Integer parentId=jsonObject.get("parentId").isJsonNull()? null : jsonObject.get("parentId").getAsInt();
		Integer occurrence =jsonObject.get("occurrence").isJsonNull()? null :jsonObject.get("occurrence").getAsInt();
		Integer position =jsonObject.get("position").isJsonNull()? null :jsonObject.get("position").getAsInt();		
		Boolean aboveAverage=jsonObject.get("aboveAverage").isJsonNull()? null :jsonObject.get("aboveAverage").getAsBoolean();
		String link=jsonObject.get("link").isJsonNull()? null : jsonObject.get("link").getAsString();
		Integer companyId=jsonObject.get("companyId").isJsonNull()? null : jsonObject.get("companyId").getAsInt();
		JsonElement childrenJson = jsonObject.get("children");
		Type integerList = new TypeToken<List<Integer>>() {}.getType();
		List<Integer> children =jsonObject.get("children").isJsonArray() ? new Gson().fromJson(childrenJson, integerList) : new ArrayList<>();
		return new NavigationCategoryInfo(categoryId, categoryName, parentId, occurrence, position,
				aboveAverage, link, companyId, children);
	}

}
