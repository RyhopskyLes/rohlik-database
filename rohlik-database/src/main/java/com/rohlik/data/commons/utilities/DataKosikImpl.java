package com.rohlik.data.commons.utilities;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.rohlik.data.kosik.entities.ProductKosik;
import com.google.common.base.Stopwatch;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rohlik.data.entities.Category;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.kosik.objects.LinkAndName;
import com.rohlik.data.kosik.objects.MapEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import info.debatty.java.stringsimilarity.Cosine;

@Service("dataKosik")
public class DataKosikImpl implements DataKosik {
	private static Logger log = LoggerFactory.getLogger(DataKosikImpl.class);
	@Autowired
	private Source source;
	@Autowired
	CategoryKosikOverview categoryKosikOverview;
//	@Autowired
//	CategoryDao categoryDao;
	private static final String BASIC_URL = "https://www.kosik.cz";

	public DataKosikImpl() {
	}

/*	

	public Map<Element, Elements> nestedLinks(String categoryURL) {
		Optional<Elements> elements = getJsoupElementsFromURL.apply(categoryURL);
		Map<Element, Elements> mapped = elements.orElse(new Elements()).stream().map(element -> element.children())
				.collect(HashMap::new, (map, children) -> map.put(children.select("a").first(), children.select("ul")),
						HashMap::putAll);
		mapped.entrySet().forEach(System.out::println);
		return mapped;
	}

	private Function<String, Optional<Elements>> getJsoupElementsFromURL = link -> source.getJsoupDoc(link)
			.map(document -> document.select("div.display-block.header-navigation__subcategory__values"))
			.map(element -> element.select("a"));

	public Map<String, Set<String>> allSubcategoriesLinksForCategory(String categoryURL) {	
		return categoryKosikOverview.allLinksOfCategory(categoryURL);
	}

	public Map<String, Set<String>> subcategoriesNamesOnTwoLevels(String categoryURL) {
		Map<String, Set<String>> categories = new HashMap<>();
		Optional<Elements> links = getJsoupElementsFromURL.apply(categoryURL);
		links.ifPresent(theLinks -> theLinks.forEach(link -> {
			new LinkAndName(link).getName()
					.ifPresent(name -> categories.put(name, collectNamesOfSubcategories.apply(link)));
		}));
		return categories;
	}

	public Map<LinkAndName, Set<LinkAndName>> subcategoriesNamesAndLinksOnTwoLevels(String categoryURL) {

		return categoryKosikOverview.allLinksAndNamesOnSecondLevel(categoryURL);
	}

	Function<Element, Set<String>> collectNamesOfSubcategories = link -> Optional.ofNullable(link.select("ul > li"))
			.orElse(new Elements()).stream().map(el -> new LinkAndName(el).getName()).filter(name -> name.isPresent())
			.map(Optional::get).collect(Collectors.toCollection(HashSet::new));

	Function<Element, Set<Entry<String, String>>> collectUriNamePairsOfSubcategories = link -> Optional
			.ofNullable(link.select("ul > li")).orElse(new Elements()).stream().map(el -> new LinkAndName(el).toEntry())
			.filter(entry -> entry.isPresent()).map(Optional::get).collect(Collectors.toCollection(HashSet::new));

	public Set<Map<String, String>> getLabelsForBrands(String subcategoryURI) {
		String basicURL = "https://www.kosik.cz";
		Optional<Document> doc = source.getJsoupDoc(basicURL + subcategoryURI);
		Set<Map<String, String>> refs = new HashSet<>();
		if (doc.isPresent()) {
			Element filterCategories = doc.get().selectFirst("#filter-brand");
			Elements brands = filterCategories.select(".form-row");
			for (Element brand : brands) {
				Map<String, String> pairs = new HashMap<>();
				String labelFor = brand.select("label").attr("for");
				String labedlValue = brand.select("label").text();
				pairs.put(labelFor, labedlValue);
				refs.add(pairs);
			}
		}
		return refs;
	}

	

	public List<String> mainCategoriesLinks() {	
		return categoryKosikOverview.mainCategoriesLinks();
	}

	public Optional<String> getCategoryNameFromURL(String categoryURL) {
		return categoryKosikOverview.getCurrentMainCategoryName(categoryURL);
	}

	@Override
	public Set<LinkAndName> subcategoriesNamesAndLinksOnOneLevel(String categoryURL) {
		return categoryKosikOverview.allLinksAndNamesOnFirstLevel(categoryURL).values().stream().flatMap(set->set.stream())
				.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public Set<String> allLinks() {
		List<String> mainLinks = mainCategoriesLinks();
		Set<String> subcat = mainLinks.stream()
				.flatMap(link -> categoryKosikOverview.allLinksOfCategory("https://www.kosik.cz" + link).entrySet().stream())
				.collect(HashSet::new, (set, entry) -> {
					set.add(entry.getKey());
					set.addAll(entry.getValue());
				}, HashSet::addAll);
		subcat.addAll(mainLinks);
		return subcat;
	}
*/
}
