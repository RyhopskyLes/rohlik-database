package com.rohlik.data.kosik.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.commons.objects.Converter;
import com.rohlik.data.commons.utilities.Source;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ProductKosik;
import com.rohlik.data.kosik.objects.ProducerInfo;

@Component("productOverView")
public class ProductKosikOverview {
	private Source source;
	private Converter converter;
	private CategoryKosikDao catKosikDao;
	private static Logger log = LoggerFactory.getLogger(ProductKosikOverview.class);
	private Optional<Document> document = Optional.empty();
	private String url;
	private JsonParser jp = new JsonParser();
	private static final String BASIC_URL = "https://www.kosik.cz";
	
	@Autowired
	public ProductKosikOverview(Source source, Converter converter, CategoryKosikDao catKosikDao) {		
		this.source = source;
		this.converter = converter;
		this.catKosikDao = catKosikDao;
	}	

	public Optional<Element> getPaginationData(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return document.map(doc -> doc.select("latte-pagination")).map(Elements::first);
	}

	public Optional<Element> getFilterData(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return document.map(doc -> doc.select("filters")).map(Elements::first);
	}

	public Optional<String> getLabelFromProductDetail(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return document.map(doc -> doc.select(".product-detail__main-info__info__link")).map(Elements::text);
	}

	public Optional<String> getUrlPattern(String url) {
		return getPaginationData(url).map(pagination -> pagination.attr("url-pattern"));
	}

	public Optional<String> getStartMaxPage(String url) {
		return getPaginationData(url).map(pagination -> pagination.attr(":start-max-page"));
	}

	public Optional<Pair<String, String>> getUrlPatternAndMaxpage(String url) {
		Optional<String> pattern = getUrlPattern(url);
		String urlPattern = pattern.isPresent() ? pattern.get().replace("#PAGE#", "") : "";
		Optional<String> maxPage = getStartMaxPage(url);
		String startMaxPage = maxPage.isPresent() ? maxPage.get() : "";
		return urlPattern.equals("") ? Optional.empty() : Optional.ofNullable(Pair.of(urlPattern, startMaxPage));
	}

	public Optional<Elements> getProductList(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return document.map(doc -> doc.select(".products-list.products-list--no-swiper")).map(Elements::first)
				.map(Element::children);
	}

	public Optional<Elements> getCompleteProductElementListUsingPagination(String url) {
		Elements completeList = new Elements();
		Optional<Pair<String, String>> urlAndMax = getUrlPatternAndMaxpage(url);
		String urlPattern = urlAndMax.isPresent() ? urlAndMax.get().getLeft() : "";
		String maxPage = urlAndMax.isPresent() ? urlAndMax.get().getRight() : "";
		if (!urlPattern.equals("")) {
			String[] urlParts = new String[] { url, BASIC_URL, urlPattern, maxPage };
			addElementsFromFirstToLastPage(completeList, urlParts);
		} else {
			completeList.addAll(getProductList(url).orElse(new Elements()));
		}
		return Optional.ofNullable(completeList);
	}

	public Optional<Elements> getCompleteProductElementListFilteredByProducersUsingPagination(String url,
			String filterSegment) {
		Elements completeList = new Elements();
		Optional<Pair<String, String>> urlAndMax = getUrlPatternAndMaxpage(url + filterSegment);
		String urlPattern = urlAndMax.isPresent() ? urlAndMax.get().getLeft() : "";
		String maxPage = urlAndMax.isPresent() ? urlAndMax.get().getRight() : "";
		if (!urlPattern.equals("")) {
			String[] urlParts = new String[] { url, BASIC_URL, urlPattern, filterSegment, maxPage };
			addElementsFromFirstToLastPage(completeList, urlParts);
		} else {
			completeList.addAll(getProductList(url + filterSegment).orElse(new Elements()));
		}
		return Optional.ofNullable(completeList);
	}

	private void addElementsFromFirstToLastPage(Elements completeList, String[] urlParts) {
		if (urlParts.length == 5) {
			Integer max = NumberUtils.toInt(urlParts[4], 1);
			completeList.addAll(getProductList(urlParts[0] + urlParts[3]).orElse(new Elements()));
			IntStream.rangeClosed(2, max).forEach(number -> {
				Optional<Elements> temp = getProductList(urlParts[1] + urlParts[2] + number + urlParts[3]);
				temp.ifPresent(completeList::addAll);
			});
		}
		if (urlParts.length == 4) {
			Integer max = NumberUtils.toInt(urlParts[3], 1);
			completeList.addAll(getProductList(urlParts[0]).orElse(new Elements()));
			IntStream.rangeClosed(2, max).forEach(number -> {
				Optional<Elements> temp = getProductList(urlParts[1] + urlParts[2] + number);
				temp.ifPresent(completeList::addAll);
			});
		}
	}

	public JsonArray getProducersList(String url) {
		Optional<Element> filtry = getFilterData(url);
		JsonArray znacky = new JsonArray();
		if (filtry.isPresent()) {
			JsonArray types = jp.parse(filtry.get().attr("data")).getAsJsonArray();
			znacky = StreamSupport.stream(types.spliterator(), false).map(JsonElement::getAsJsonObject)
					.filter(object -> object.has("name"))
					.filter(object -> object.get("name").getAsString().equals("Značka"))
					.map(object -> object.get("items")).map(JsonElement::getAsJsonArray).findFirst()
					.orElseGet(JsonArray::new);
		}
		return znacky;
	}

	public List<ProducerInfo> getProducerInfoListForCategory(String url) {
		Gson gson = new Gson();
		JsonArray producerList = getProducersList(url);
		List<ProducerInfo> producers = new ArrayList<>();
		Spliterator<JsonElement> elements = producerList.spliterator();
		StreamSupport.stream(elements, false).forEach(producer -> {
			ProducerInfo converted = gson.fromJson(producer, ProducerInfo.class);
			producers.add(converted);
		});
		return producers;
	}

	public Map<ProducerInfo, Optional<Elements>> getProductElementsForcategoryGroupedByProducers(String url) {
		Map<ProducerInfo, Optional<Elements>> groupedByProducers = new HashMap<>();
		String urlPattern = "?brands[0]=";
		List<ProducerInfo> producerList = getProducerInfoListForCategory(url);
		producerList.stream().forEach(info -> {
			Optional<Elements> products = getCompleteProductElementListFilteredByProducersUsingPagination(url,
					urlPattern + info.getId());
			groupedByProducers.put(info, products);
		});
		return groupedByProducers;
	}

	public List<ProductKosik> getProductKosikListForcategoryGroupedByProducers(String url) {
		String urlPattern = "?brands[0]=";
		String categoryUri = url.replace(BASIC_URL, "");
		List<ProducerInfo> producerList = getProducerInfoListForCategory(url);
		List<ProductKosik> result = new ArrayList<>();
		Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(categoryUri);

		if (!producerList.isEmpty()) {
			producerList.stream().forEach(info -> {
				Optional<Elements> products = getCompleteProductElementListFilteredByProducersUsingPagination(url,
						urlPattern + info.getId());
				products.orElseGet(Elements::new).stream()
						.forEach(convertToProductAndAddToList(result, info, category)::accept);
			});
		} else {
			getProductList(url).orElseGet(Elements::new).stream()
					.forEach(convertToProductAndAddToList(result, null, category)::accept);
			log.info("Url bez výrobce: {}", url);
		}
		return result;
	}

	private Consumer<Element> convertToProductAndAddToList(List<ProductKosik> result, ProducerInfo info,
			Optional<CategoryKosik> category) {
		log.info("{}", info);
		return element -> {
			Optional<ProductKosik> temp = converter.toProductKosik(info, element);
			temp.ifPresent(theTemp -> {
				addCategoryAndOptionallySetProducer(category, info == null).accept(theTemp);
				result.add(theTemp);
			});
		};

	}

	private Consumer<ProductKosik> addCategoryAndOptionallySetProducer(Optional<CategoryKosik> category,
			boolean setProducer) {
		return product -> {
			addCategoryToProduct(product, category);
			log.info("{}", setProducer);
			if (setProducer)
				setProducerFromProductDetail(product);
		};

	}

	private void addCategoryToProduct(ProductKosik product, Optional<CategoryKosik> category) {
		category.ifPresent(theCategory -> {
			product.addCategory(theCategory);
			if (theCategory.getChildren().isEmpty())
				product.setCategory(theCategory.getUri());
		});
	}

	private void setProducerFromProductDetail(ProductKosik product) {
		Optional<String> producer = getLabelFromProductDetail(product.getProductPath());
		producer.ifPresent(product::setProducer);
	}

	public List<ProductKosik> getCompleteProductListUsingPagination(String url) {
		List<ProductKosik> result = new ArrayList<>();
		Optional<Elements> products = getCompleteProductElementListUsingPagination(url);		
		if (products.isPresent()) {
			Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(url.replace(BASIC_URL, ""));
			products.get().stream().forEach(convertToProductAndAddToList(result, null, category)::accept);			
		}
		return result;
	}
	
	public List<ProductKosik> getCompleteProductListUsingPaginationForCategoryMatching(String url) {
		List<ProductKosik> result = new ArrayList<>();
		Optional<Elements> products = getCompleteProductElementListUsingPagination(url);		
		if (products.isPresent()) {
			Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(url.replace(BASIC_URL, ""));
			products.get().stream().forEach(convertToProductAndAddToList(result, new ProducerInfo(), category)::accept);			
		}
		return result;
	}
}
