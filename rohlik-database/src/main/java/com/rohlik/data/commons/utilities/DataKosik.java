package com.rohlik.data.commons.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.rohlik.data.kosik.entities.ProductKosik;
import com.rohlik.data.kosik.objects.LinkAndName;
import com.rohlik.data.entities.Category;

import info.debatty.java.stringsimilarity.Cosine;

public interface DataKosik {
//	public Set<ProductKosik> extractAllProductsInfoInCategory(String categoryURL);
//	public ProductKosik extractProductInfo(Element imageInfo, Element itemInfo);	
//	public List<String> extractProducersForCategory(String categoryURL, Elements producers);	
//	public Map<String, Set<String>> subcategoriesNamesOnTwoLevels(String categoryURL);	
//	public Map<LinkAndName, Set<LinkAndName>> subcategoriesNamesAndLinksOnTwoLevels(String categoryURL);
//	public Set<LinkAndName> subcategoriesNamesAndLinksOnOneLevel(String categoryURL);
//	public Set<Map<String, String>> getLabelsForBrands(String subcategoryURI);	
//	public Set<ProductKosik> buildProductsWithProducersInSubcategory(String subcategoryURI);
/*	public List<String> mainCategoriesLinks();
	public Set<String> allLinks();
	public Optional<String> getCategoryNameFromURL(String categoryURL);
	public Map<String, Set<String>> allSubcategoriesLinksForCategory(String categoryURL);
	public Map<Element, Elements> nestedLinks(String categoryURL);*/
}
