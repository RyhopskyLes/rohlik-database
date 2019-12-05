package com.rohlik.data.commons.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Product;
import com.rohlik.data.entities.Sale;
import com.rohlik.data.objects.Filters;
import com.rohlik.data.objects.RootObject;
import com.rohlik.data.objects.SlugAndName;

@Service("dataRohlik")
public class DataRohlik {
	public static final String NAVIGATION_URL = "https://www.rohlik.cz/services/frontend-service/renderer/navigation/flat.json";
	private static Logger log = LoggerFactory.getLogger(DataRohlik.class);

	public DataRohlik() {
	}

	@Autowired
	private Source source;
	@Autowired
	private Filters filters;
	@Autowired
	private RootObject rootObject;

	public Product extractProductFromJson(JsonObject productData, Map<String, Set<Integer>> producers,
			Map<Integer, String> categories) {
		Product product = new Product();
		Integer productId = productData.get("productId").getAsInt();
		product.setProductId(productId);
		String productName = productData.get("productName").getAsString();
		product.setProductName(productName);
		Double originalPrice = !productData.get("originalPrice").isJsonNull()
				? productData.get("originalPrice").getAsJsonObject().get("full").getAsDouble()
				: null;
		product.setOriginalPrice(originalPrice);
		Double price = productData.get("price").getAsJsonObject().get("full").getAsDouble();
		product.setPrice(price);
		String textualAmount = productData.get("textualAmount").getAsString();
		product.setTextualAmount(textualAmount);
		String unit = productData.get("unit").getAsString();
		product.setUnit(unit);
		String baseLink = productData.get("baseLink").getAsString();
		product.setBaseLink(baseLink);
		String imgPath = !productData.get("imgPath").isJsonNull() ? productData.get("imgPath").getAsString() : "";
		product.setImgPath(imgPath);
		boolean inStock = productData.get("inStock").getAsBoolean();
		product.setInStock(inStock);
		String link = productData.get("link").getAsString();
		product.setLink(link);
		Double pricePerUnit = productData.get("pricePerUnit").getAsJsonObject().get("full").getAsDouble();
		product.setPricePerUnit(pricePerUnit);
		Integer mainCategoryId = productData.get("mainCategoryId").getAsInt();
		product.setMainCategoryId(mainCategoryId);
		String mainCategoryName = getCategoryName(mainCategoryId, categories);
		product.setMainCategoryName(mainCategoryName);
		String producer = producerName(product, producers);
		product.setProducer(producer);
		product.setFromRohlik(true);
		return product;
	}

	public static Sale extractSalesFromJson(JsonObject dataFromArray) {
		Sale sale = new Sale();
		String typeSales = dataFromArray.get("type").getAsString();
		sale.setType(typeSales);
		Double discountPercentageSales = dataFromArray.get("discountPercentage").getAsDouble();
		sale.setDiscountPercentage(discountPercentageSales);
		Double discountPriceSales = dataFromArray.get("discountPrice").getAsJsonObject().get("full").getAsDouble();
		sale.setDiscountPrice(discountPriceSales);
		Double originalPriceSales = dataFromArray.get("originalPrice").getAsJsonObject().get("full").getAsDouble();
		sale.setOriginalPrice(originalPriceSales);
		Double priceSales = dataFromArray.get("price").getAsJsonObject().get("full").getAsDouble();
		sale.setPrice(priceSales);
		Double pricePerUnitSales = dataFromArray.getAsJsonObject().get("priceForUnit").getAsJsonObject().get("full")
				.getAsDouble();
		sale.setPricePerUnit(pricePerUnitSales);
		Integer idSales = dataFromArray.get("id").getAsInt();
		sale.setId(idSales);
		boolean promotedSales = dataFromArray.get("promoted").getAsBoolean();
		sale.setPromoted(promotedSales);
		return sale;
	}

	

	public Set<Sale> createSalesSetForProductFromJson(JsonArray salesData) {
		Set<Sale> setSales = new HashSet<>();
		for (int j = 0; j < salesData.size(); j++) {
			Sale sale = extractSalesFromJson(salesData.get(j).getAsJsonObject());
			setSales.add(sale);

		}
		return setSales;
	}
	
	private String producerName(Product product, Map<String, Set<Integer>> producers) {
		String producer = "";
		Integer productId = product.getProductId();
		for (Map.Entry<String, Set<Integer>> entry : producers.entrySet()) {
			for (Integer item : entry.getValue()) {
				if (productId.equals(item))
					return entry.getKey();
			}
		}
		return producer;
	}

	
	public Map<String, Set<Integer>> producersWithProducts(List<SlugAndName> slugsAndNames, Integer categoryNum) {
		Map<String, Set<Integer>> productMap = new HashMap<>();
		String producer = "";
		for (SlugAndName slugAndName : slugsAndNames) {
			Optional<JsonObject> data = rootObject.dataForCategoryAndProducer(categoryNum, slugAndName.getSlug())
					.map(object -> object.getAsJsonObject("data"));
			Optional<JsonArray> productList = data.map(object -> object.get("productList").getAsJsonArray());
			producer = slugAndName.getName();
			if (productList.isPresent()) {
				Set<Integer> products = new HashSet<>();
				for (int i = 0; i < productList.get().size(); i++) {
					JsonObject descriptionData = productList.get().get(i).getAsJsonObject();
					Integer product = descriptionData.get("productId").getAsInt();
					products.add(product);
				}
				productMap.put(producer, products);
			}
		}
		return productMap;
	}

	public String getCategoryName(Integer catNum, Map<Integer, String> categoriesMap) {
		String catName = categoriesMap.get(catNum);
		return catName == null ? "" : catName;
	}

}
