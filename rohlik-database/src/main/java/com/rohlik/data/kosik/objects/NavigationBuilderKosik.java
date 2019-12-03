package com.rohlik.data.kosik.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohlik.data.kosik.interfaces.NavigationBuilder;

@Component("navigationBuilder")
public class NavigationBuilderKosik implements NavigationBuilder {
	private CategoryKosikOverview categoryKosikOverview;
	private String uri;
	static final String BASIC_URL = "https://www.kosik.cz";
	private static Logger log = LoggerFactory.getLogger(NavigationBuilderKosik.class);

	@Autowired
	public NavigationBuilderKosik(CategoryKosikOverview categoryKosikOverview) {
		this.categoryKosikOverview=categoryKosikOverview;
	}

	public NavigationItem buildItem(String uri) {
		NavigationItem navigationItem = new NavigationItem();
		Consumer<LinkAndName> setFields = holder -> {
			holder.getLink().ifPresent(navigationItem::setUri);
			holder.getName().ifPresent(navigationItem::setCategoryName);
		};
		BiFunction<LinkAndName, Optional<String>, NavigationSubItem> buildNavigationSubItem = (linkAndName,
				parentUri) -> {
			NavigationSubItem navigationSubItem = new NavigationSubItem();
			linkAndName.getLink().ifPresent(navigationSubItem::setUri);
			linkAndName.getName().ifPresent(navigationSubItem::setCategoryName);
			parentUri.ifPresent(navigationSubItem::setParentUri);
			return navigationSubItem;
		};
		BiFunction<Set<LinkAndName>, Optional<String>, List<NavigationSubItem>> buildNavigationSubItems = (set,
				parentUri) -> set.stream().collect(ArrayList::new, (list, item) -> 
					list.add(buildNavigationSubItem.apply(item, parentUri)), List::addAll);

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
		return nestedLinksAndNames -> 
			 nestedLinksAndNames.entrySet().stream()
				.map(entry -> buildNavigationItem().apply(entry)).collect(Collectors.toList());
	}	

	private Function<Set<LinkAndName>, List<NavigationSubItem>> buildNavigationSubItemsCollection(
			String parentUri) {
		return set -> set.stream().map(entry -> buildSubItem(parentUri).apply(entry)).collect(Collectors.toList());

	}
	
	private Function<LinkAndName, NavigationSubItem> buildSubItem(String parentUri) {
		NavigationSubItem navigationSubItem = new NavigationSubItem();
		return entry -> {
			entry.getLink().ifPresent(navigationSubItem::setUri);
			entry.getName().ifPresent(navigationSubItem::setCategoryName);			
			navigationSubItem.set(navigationSubItem::setParentUri, parentUri);
			return navigationSubItem;};
	}
	
	private Function<Map.Entry<LinkAndName, Set<LinkAndName>>, NavigationItem> buildNavigationItem() {
		NavigationItem navigationItem = new NavigationItem();
		return entry -> {
			entry.getKey().getLink().ifPresent(navigationItem::setUri);
			entry.getKey().getName().ifPresent(navigationItem::setCategoryName);
			entry.getKey().getLink().ifPresent(link->
			navigationItem.set(navigationItem::setSubcategories,
					buildNavigationSubItemsCollection(link).apply(entry.getValue())));
			return navigationItem;
		};
	}	 

	private Function<String, Map<LinkAndName, Set<LinkAndName>>> newnestedLinksAndNames = link -> categoryKosikOverview.allLinksAndNamesOnSecondLevel(BASIC_URL + link);

	@Override
	public List<String> mainCategoriesLinks() {
		return categoryKosikOverview.mainCategoriesLinks();
	}
}
