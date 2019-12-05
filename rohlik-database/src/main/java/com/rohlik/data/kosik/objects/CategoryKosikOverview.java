package com.rohlik.data.kosik.objects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rohlik.data.commons.utilities.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jsoup.nodes.Document;

@Component("categoryOverView")

public class CategoryKosikOverview {
	private Source source;
	private static Logger log = LoggerFactory.getLogger(CategoryKosikOverview.class);
	private Optional<Document> document = Optional.empty();
	private String url;
	private JsonParser jp = new JsonParser();
	private static final String BASIC_URL = "https://www.kosik.cz";
	private static final String SUBCATEGORIES = "subcategories";

	@Autowired
	public CategoryKosikOverview(Source source) {
		this.source = source;
	}

	private Optional<Elements> getSubcategoriesAnchors(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return this.document.map(doc -> doc.select("div.display-block.header-navigation__subcategory__values"))
				.map(element -> element.select("a"));
	}

	public Optional<JsonArray> getNav_barData(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return this.document.map(doc -> doc.select("Nav_bar").first()).map(navbar -> navbar.attr("data"))
				.map(data -> jp.parse(data).getAsJsonArray());
	}

	private Optional<Element> getLastBreadCrumbAnchor(String url) {
		if (this.url == null || !this.url.equals(url)) {
			this.document = source.getJsoupDoc(url);
			this.url = url;
		}
		return this.document.map(doc -> doc.select("div.breadcrumps")).map(div -> div.select("a")).map(Elements::last);
	}

	public Map<LinkAndName, Set<LinkAndName>> allLinksAndNamesOnSecondLevel(String url) {
		Function<Optional<Elements>, Set<LinkAndName>> toNameAndLinkSet = as -> as.orElse(new Elements()).stream()
				.collect(HashSet::new, (set, a) -> set.add(new LinkAndName(a)), HashSet::addAll);
		Map<LinkAndName, Set<LinkAndName>> result = new HashMap<>();
		int level = getLevelFromUrl(url);
		if (level == 1) {
			result = secondLevelFromJsonData(url);
		}
		if (level > 1) {
			result = toNameAndLinkSet.apply(getSubcategoriesAnchors(url)).stream().map(LinkAndName::getLink)
					.filter(Optional::isPresent).map(Optional::get)
					.map(link -> allLinksAndNamesOnFirstLevel(BASIC_URL + link))
					.collect(HashMap::new, (map, old) -> map.putAll(old), HashMap::putAll);
		}
		return result;

	}

	private int getLevelFromUrl(String url) {
		if (url != null)
			return url.replace(BASIC_URL, "").split("/").length - 1;
		return -1;
	}

	public Map<LinkAndName, Set<LinkAndName>> allLinksAndNamesOnFirstLevel(String url) {
		Function<Optional<Elements>, Set<LinkAndName>> toNameAndLinkSet = as -> as.orElse(new Elements()).stream()
				.collect(HashSet::new, (set, a) -> set.add(new LinkAndName(a)), HashSet::addAll);
		Map<LinkAndName, Set<LinkAndName>> result = new HashMap<>();
		int level = getLevelFromUrl(url);
		if (level == 1 || level == 2)
			result = firstLevelFromJsonData(url);
		if (level > 2) {
			result.put(getCurrentMainCategoryLinkAndName(url), toNameAndLinkSet.apply(getSubcategoriesAnchors(url)));
		}
		return result;
	}

	private Map<LinkAndName, Set<LinkAndName>> firstLevelFromJsonData(String url) {
		Map<LinkAndName, Set<LinkAndName>> result = new HashMap<>();
		Optional<JsonArray> data = getNav_barData(url);
		JsonObject parent;
		if (url != null) {
			String[] parts = url.replace(BASIC_URL, "").split("/");
			int levels = parts.length - 1;
			switch (levels) {
			case 1:
				parent = getParentCategory("/" + parts[1]).apply(data).orElseGet(() -> null);
				result.put(new LinkAndName(parent), toSet.apply(getSubcategoriesFromJsonObject(parent)));
				break;
			case 2:
				Optional<JsonObject> firstLevelParent = getParentCategory("/" + parts[1]).apply(data);
				Optional<JsonArray> parentLevel = getSubcategoriesFromJsonObject(firstLevelParent);
				parent = getParentCategory("/" + parts[1] + "/" + parts[2]).apply(parentLevel).orElseGet(() -> null);
				log.info("{}", parent);
				result.put(new LinkAndName(parent), toSet.apply(getSubcategoriesFromJsonObject(parent)));
				break;
			}
		}
		return result;
	}

	private Map<String, Set<String>> firstLevelLinksFromJsonData(String url) {
		Map<String, Set<String>> result = new HashMap<>();
		Optional<JsonArray> data = getNav_barData(url);
		JsonObject parent;
		if (url != null) {
			String[] parts = url.replace(BASIC_URL, "").split("/");
			int levels = parts.length - 1;
			switch (levels) {
			case 1:
				parent = getParentCategory("/" + parts[1]).apply(data).orElseGet(() -> null);
				Optional<String> link = new LinkAndName(parent).getLink();
				if (link.isPresent())
					result.put(link.get(), toLinkSet.apply(getSubcategoriesFromJsonObject(parent)));
				break;
			case 2:
				Optional<JsonObject> firstLevelParent = getParentCategory("/" + parts[1]).apply(data);
				Optional<JsonArray> parentLevel = getSubcategoriesFromJsonObject(firstLevelParent);
				parent = getParentCategory("/" + parts[1] + "/" + parts[2]).apply(parentLevel).orElseGet(() -> null);
				log.info("{}", parent);
				link = new LinkAndName(parent).getLink();
				if (link.isPresent())
					result.put(link.get(), toLinkSet.apply(getSubcategoriesFromJsonObject(parent)));
				break;
			}
		}
		return result;
	}

	private Optional<JsonArray> getSubcategoriesFromJsonObject(JsonObject parent) {
		return Optional.ofNullable(parent).map(categoryObject -> categoryObject.get(SUBCATEGORIES))
				.map(JsonElement::getAsJsonArray);
	}

	private Optional<JsonArray> getSubcategoriesFromJsonObject(Optional<JsonObject> parent) {
		return parent.map(categoryObject -> categoryObject.get(SUBCATEGORIES)).map(JsonElement::getAsJsonArray);
	}

	private Map<String, Set<String>> secondLevelLinksFromJsonData(String url) {
		Map<String, Set<String>> result = new HashMap<>();
		Optional<JsonArray> data = getNav_barData(url);
		if (url != null) {
			String[] parts = url.replace(BASIC_URL, "").split("/");
			int levels = parts.length - 1;
			if (levels == 1) {
				Optional<JsonObject> parent = getParentCategory("/" + parts[1]).apply(data);
				Optional<JsonArray> subcategories = getSubcategoriesFromJsonObject(parent);
				toLinkSet.apply(subcategories).stream().filter(link -> !link.isEmpty()).forEach(item -> result.put(item,
						getSubcategoriesFromLink(subcategories).andThen(toLinkSet).apply(item)));
			}
		}
		return result;
	}

	private Map<LinkAndName, Set<LinkAndName>> secondLevelFromJsonData(String url) {
		Map<LinkAndName, Set<LinkAndName>> result = new HashMap<>();
		Optional<JsonArray> data = getNav_barData(url);
		if (url != null) {
			String[] parts = url.replace(BASIC_URL, "").split("/");
			int levels = parts.length - 1;
			if (levels == 1) {
				Optional<JsonObject> parent = getParentCategory("/" + parts[1]).apply(data);
				Optional<JsonArray> subcategories = getSubcategoriesFromJsonObject(parent);
				toSet.apply(subcategories).stream().filter(item -> item.getLink().isPresent())
						.forEach(item -> result.put(item, getSubcategories(subcategories).andThen(toSet).apply(item)));
			}
		}
		return result;
	}

	private Function<LinkAndName, Optional<JsonArray>> getSubcategories(Optional<JsonArray> data) {
		return item -> getParentCategory(item.getLink().get()).apply(data)
				.map(categoryObject -> categoryObject.get(SUBCATEGORIES)).map(JsonElement::getAsJsonArray);

	}

	private Function<String, Optional<JsonArray>> getSubcategoriesFromLink(Optional<JsonArray> data) {
		return link -> getParentCategory(link).apply(data).map(categoryObject -> categoryObject.get(SUBCATEGORIES))
				.map(JsonElement::getAsJsonArray);

	}

	private Function<Optional<JsonArray>, Optional<JsonObject>> getParentCategory(String categoryUri) {
		return data -> data.isPresent() ? StreamSupport.stream(data.get().spliterator(), false)
				.map(element -> Optional.ofNullable(element.getAsJsonObject()))
				.filter(object -> object.map(obj -> obj.get("url")).map(JsonElement::getAsString).isPresent())
				.map(Optional::get).filter(categoryObj -> categoryObj.get("url").getAsString().equals(categoryUri))
				.map(JsonObject::getAsJsonObject).findFirst() : Optional.empty();
	}

	private Function<JsonObject, String> getUrl = categoryObject -> categoryObject.get("url").getAsString();
	private Function<JsonObject, String> getName = categoryObject -> categoryObject.get("name").getAsString();
	private Function<Optional<JsonArray>, Set<LinkAndName>> toSet = jsonArray -> jsonArray.isPresent()
			? StreamSupport.stream(jsonArray.get().spliterator(), false).map(item -> item.getAsJsonObject())
					.map(LinkAndName::new).collect(Collectors.toCollection(HashSet::new))
			: new HashSet<>();
	private Function<Optional<JsonArray>, Set<String>> toLinkSet = jsonArray -> jsonArray.isPresent()
			? StreamSupport.stream(jsonArray.get().spliterator(), false).map(item -> item.getAsJsonObject())
					.map(getUrl::apply).collect(Collectors.toCollection(HashSet::new))
			: new HashSet<>();

	public Map<String, Set<String>> allLinksOnFirstLevel(String url) {
		Function<Optional<Elements>, Set<String>> toLinksSet = anchorElements -> anchorElements.orElse(new Elements())
				.stream().collect(HashSet::new, (set, a) -> {
					Optional<String> link = new LinkAndName(a).getLink();
					link.ifPresent(set::add);
				}, HashSet::addAll);
		Map<String, Set<String>> result = new HashMap<>();
		int level = getLevelFromUrl(url);
		if (level == 1 || level == 2) {
			result = firstLevelLinksFromJsonData(url);
		}
		if (level > 2) {
			Optional<String> link = getCurrentMainCategoryLink(url);
			if (link.isPresent()) {
				result.put(link.get(), toLinksSet.apply(getSubcategoriesAnchors(url)));
			}
		}
		return result;

	}

	public List<String> mainCategoriesLinks() {
		Optional<JsonArray> data = getNav_barData(BASIC_URL);
		System.out.println(data);
		List<String> links = new ArrayList<>();
		if (data.isPresent()) {
			links = StreamSupport.stream(data.get().spliterator(), false)
					.map(element -> Optional.ofNullable(element.getAsJsonObject()))
					.filter(object -> object.map(obj -> obj.get("url")).map(JsonElement::getAsString).isPresent())
					.map(Optional::get).map(categoryObject -> categoryObject.get("url").getAsString())
					.collect(Collectors.toCollection(ArrayList::new));
		}
		return links;
	}

	public Map<String, Set<String>> allLinksOnSecondLevel(String url) {
		Function<Optional<Elements>, Set<LinkAndName>> toLinksSet = as -> as.orElse(new Elements()).stream()
				.collect(HashSet::new, (set, a) -> set.add(new LinkAndName(a)), HashSet::addAll);

		Map<String, Set<String>> result = new HashMap<>();
		int level = getLevelFromUrl(url);
		if (level == 1) {
			result = secondLevelLinksFromJsonData(url);
		}
		if (level > 1) {
			result = toLinksSet.apply(getSubcategoriesAnchors(url)).stream().map(LinkAndName::getLink)
					.filter(Optional::isPresent).map(Optional::get).map(link -> allLinksOnFirstLevel(BASIC_URL + link))
					.collect(HashMap::new, (map, old) -> map.putAll(old), HashMap::putAll);
		}
		return result;
	}

	public Map<String, Set<String>> allLinksOfCategory(String url) {

		Map<String, Set<String>> subcategoriesLinks = new LinkedHashMap<>();
		Map<String, Set<String>> workingMap = new HashMap<>();
		subcategoriesLinks.putAll(allLinksOnFirstLevel(url));
		Map<String, Set<String>> secondLevel = allLinksOnSecondLevel(url);
		subcategoriesLinks.putAll(secondLevel);
		workingMap.putAll(secondLevel);
			
		while (!workingMap.isEmpty()) {
			Map<String, Set<String>> temporaryMap = new HashMap<>();
			workingMap.values().forEach(set -> set.stream().filter(link -> !link.isEmpty()).forEach(
					addNewLinksToResultAndNewLevelToTemporaryCollection(subcategoriesLinks, temporaryMap)::accept));
			workingMap = temporaryMap;
		}
		return subcategoriesLinks;
	}

	private Consumer<String> addNewLinksToResultAndNewLevelToTemporaryCollection(
			Map<String, Set<String>> subcategoriesLinks, Map<String, Set<String>> temporaryMap) {
		return link -> {
			subcategoriesLinks.putAll(allLinksOnFirstLevel(BASIC_URL + link));
			Map<String, Set<String>> secondLevelTemp = allLinksOnSecondLevel(BASIC_URL + link);
			subcategoriesLinks.putAll(secondLevelTemp);
			temporaryMap.putAll(secondLevelTemp);
		};
	}

	public LinkAndName getCurrentMainCategoryLinkAndName(String url) {
		return new LinkAndName(getLastBreadCrumbAnchor(url));
	}

	public Optional<String> getCurrentMainCategoryName(String url) {
		return new LinkAndName(getLastBreadCrumbAnchor(url)).getName();
	}

	public Optional<String> getCurrentMainCategoryLink(String url) {
		return new LinkAndName(getLastBreadCrumbAnchor(url)).getLink();
	}

}
