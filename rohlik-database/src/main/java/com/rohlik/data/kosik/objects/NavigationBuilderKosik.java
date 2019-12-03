package com.rohlik.data.kosik.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohlik.data.commons.services.CategoryKosikServiceImpl;
import com.rohlik.data.commons.utilities.DataKosik;
import com.rohlik.data.commons.utilities.DataKosikImpl;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;

@Component("navigationBuilder")
public class NavigationBuilderKosik implements NavigationBuilder {
	@Autowired
	CategoryKosikOverview categoryKosikOverview;
	String uri;
	static final String BASIC_URL = "https://www.kosik.cz";
	private static Logger log = LoggerFactory.getLogger(NavigationBuilderKosik.class);

	public NavigationBuilderKosik() {
	}

	public NavigationItem buildItem(String uri) {
		NavigationItem navigationItem = new NavigationItem();
		Consumer<LinkAndName> setFields = holder -> {
			holder.getLink().ifPresent(link -> navigationItem.setUri(link));
			holder.getName().ifPresent(name -> navigationItem.setCategoryName(name));
		};
		BiFunction<LinkAndName, Optional<String>, NavigationSubItem> buildNavigationSubItem = (linkAndName,
				parentUri) -> {
			NavigationSubItem navigationSubItem = new NavigationSubItem();
			linkAndName.getLink().ifPresent(link -> navigationSubItem.setUri(link));
			linkAndName.getName().ifPresent(name -> navigationSubItem.setCategoryName(name));
			parentUri.ifPresent(link -> navigationSubItem.setParentUri(link));
			return navigationSubItem;
		};
		BiFunction<Set<LinkAndName>, Optional<String>, List<NavigationSubItem>> buildNavigationSubItems = (set,
				parentUri) -> set.stream().collect(ArrayList::new, (list, item) -> {
					list.add(buildNavigationSubItem.apply(item, parentUri));
				}, List::addAll);

		Map<LinkAndName, Set<LinkAndName>> firstLevel = categoryKosikOverview.allLinksAndNamesOnFirstLevel(BASIC_URL + uri);
		firstLevel.forEach((key, value) -> {
			setFields.accept(key);
			navigationItem.setSubcategories(buildNavigationSubItems.apply(value, key.getLink()));
		});

		return navigationItem;
	}


	public List<NavigationItem> buildLevel(String uri) {
		return newnestedLinksAndNames.andThen(buildNavigationItemsCollection()).apply(uri);
	}

	public Function<String, List<NavigationItem>> buildNavigationLevel() {
		return string -> newnestedLinksAndNames.andThen(buildNavigationItemsCollection()).apply(string);
	}	

	private Function<Map<LinkAndName, Set<LinkAndName>>, List<NavigationItem>> buildNavigationItemsCollection() {
		return nestedLinksAndNames -> {
			return nestedLinksAndNames.entrySet().stream()
				.map(entry -> buildNavigationItem().apply(entry)).collect(Collectors.toList());};
	}	

	private Function<Set<LinkAndName>, List<NavigationSubItem>> buildNavigationSubItemsCollection(
			String parentUri) {
		return set -> set.stream().map(entry -> buildSubItem(parentUri).apply(entry)).collect(Collectors.toList());

	}
	
	private Function<LinkAndName, NavigationSubItem> buildSubItem(String parentUri) {
		NavigationSubItem navigationSubItem = new NavigationSubItem();
		return entry -> {
			entry.getLink().ifPresent(link -> navigationSubItem.setUri(link));
			entry.getName().ifPresent(name -> navigationSubItem.setCategoryName(name));			
			navigationSubItem.set(navigationSubItem::setParentUri, parentUri);
			return navigationSubItem;};
	}
	
	private Function<Map.Entry<LinkAndName, Set<LinkAndName>>, NavigationItem> buildNavigationItem() {
		NavigationItem navigationItem = new NavigationItem();
		return entry -> {
			entry.getKey().getLink().ifPresent(link -> navigationItem.setUri(link));
			entry.getKey().getName().ifPresent(name -> navigationItem.setCategoryName(name));
			entry.getKey().getLink().ifPresent(link->
			navigationItem.set(navigationItem::setSubcategories,
					buildNavigationSubItemsCollection(link).apply(entry.getValue())));
			return navigationItem;
		};
	}	 

	private Function<String, Map<LinkAndName, Set<LinkAndName>>> newnestedLinksAndNames = uri -> categoryKosikOverview.allLinksAndNamesOnSecondLevel(BASIC_URL + uri);
}
