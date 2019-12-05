package com.rohlik.data.kosik.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohlik.data.commons.objects.Dissimilar;
import com.rohlik.data.commons.objects.ProductMatcher;
import com.rohlik.data.commons.objects.Result;
import com.rohlik.data.commons.objects.WeightedDissimilar;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ProductKosik;

import info.debatty.java.stringsimilarity.Cosine;

@Component
public class CategoryMatcher {
	private static Logger log = LoggerFactory.getLogger(CategoryMatcher.class);
	private static final String BASIC_URL = "https://www.kosik.cz";
	@Autowired
	ProductKosikOverview overview;
	private Cosine cos;

	public CategoryMatcher() {
		cos = new Cosine();
	}

	public Result<Category> findMatch(CategoryKosik kosik, List<Category> rohliky) {
		List<Dissimilar<Category>> matches = new ArrayList<>();
		rohliky = rohliky.stream().filter(Category::getActive)
				.collect(Collectors.toCollection(ArrayList::new));
		for (Category rohlik : rohliky) {
			String nameRohlik = rohlik.getCategoryName();
			Double disimilarity = this.cos.distance(kosik.getCategoryName(), nameRohlik);
			matches.add(new Dissimilar<Category>(rohlik, disimilarity));
		}
		Dissimilar<Category> leastDissimilar = Collections.min(matches,
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	public List<Result<Category>> findMatchBasedOnProducts(CategoryKosik kosik, List<Category> rohliky) {
		List<WeightedDissimilar<Category>> matches = new ArrayList<>();
		List<ProductKosik> productsForCategory = overview
				.getCompleteProductListUsingPaginationForCategoryMatching(BASIC_URL + kosik.getUri());
		ProductMatcher matcher = new ProductMatcher();
		for (Category rohlik : rohliky) {
			List<Product> products = rohlik.getProducts().stream().collect(Collectors.toCollection(ArrayList::new));
			Long productCount = (long) productsForCategory.size();			
			if (!products.isEmpty()) {
				Long match = productsForCategory.stream().map(product -> matcher.findMatchCosine(product, products))
						.map(result -> result.getEntityForLimit(0.4)).filter(Optional::isPresent).count();
				Double dissimilarity = productCount == 0 ? 1.0 : 1.0 - ((double) match) / productCount;
				Double relativeFrequency = ((double) match) / (products.size());
				Double relativeSize = productCount == 0 ? 0.0 : ((double) products.size()) / (productCount);
				matches.add(new WeightedDissimilar<Category>(rohlik, dissimilarity, relativeFrequency, relativeSize));
			}
		}

matches.stream().forEach(System.out::println);		
 List<WeightedDissimilar<Category>>	filtered = matches.stream().filter(dis -> dis.getDissimilarity() < 1.0)
			.filter(dis -> dis.getRelativeFrequency() > 0.1 )
			.filter(dis -> dis.getRelativeSize() > 0.1 && dis.getRelativeSize() < 3.0).collect(Collectors.toList());
 List<Double> dissimilarity =filtered.stream().map(WeightedDissimilar<Category>::getDissimilarity).collect(Collectors.toList());
 List<Double> frequency =filtered.stream().map(WeightedDissimilar<Category>::getRelativeFrequency).collect(Collectors.toList());
 List<Double> relSize =filtered.stream().map(WeightedDissimilar<Category>::getRelativeSize).collect(Collectors.toList());
matches.stream().filter(dis -> dis.getDissimilarity() <= getMedian.apply(dissimilarity))
.filter(dis -> dis.getRelativeFrequency() >=getSumStat.apply(frequency).getMin())
.filter(dis -> /*dis.getRelativeSize() > getMedian.apply(relSize) &&*/ dis.getRelativeSize() < 3.0)
.sorted(Comparator.comparing(WeightedDissimilar<Category>::getRelativeSize,
		Comparator.reverseOrder()))
.forEach(x->System.out.println(x.getEntity().getCategoryName()+" "+x.getDissimilarity()+" "+x.getRelativeFrequency()+" "+x.getRelativeSize()));
		System.out.println("Relative Frequency odchylka: " + Math.sqrt(getVariance.apply(frequency))+" average "+getSumStat.apply(frequency).getAverage()+" median "+getMedian.apply(frequency)+ " max "+getSumStat.apply(frequency).getMax()+ " min "+getSumStat.apply(frequency).getMin());
		System.out.println("Dissimilarity odchylka: " + Math.sqrt(getVariance.apply(dissimilarity))+" average "+getSumStat.apply(dissimilarity).getAverage()+" median "+getMedian.apply(dissimilarity)+ " max "+getSumStat.apply(dissimilarity).getMax()+ " min "+getSumStat.apply(dissimilarity).getMin());
		System.out.println("Relative Size odchylka: " + Math.sqrt(getVariance.apply(relSize))+" average "+getSumStat.apply(relSize).getAverage()+" median "+getMedian.apply(relSize)+ " max "+getSumStat.apply(relSize).getMax()+ " min "+getSumStat.apply(relSize).getMin());
		matches.stream().filter(dis -> dis.getDissimilarity() <= getMedian.apply(dissimilarity))
		.filter(dis -> dis.getRelativeFrequency() >=getSumStat.apply(frequency).getMin())
		.filter(dis -> /*dis.getRelativeSize() > getMedian.apply(relSize) &&*/ dis.getRelativeSize() < 3.0)
				.sorted(Comparator.comparing(WeightedDissimilar<Category>::getRelativeSize,
						Comparator.reverseOrder()))
				.map(dis -> new Result<Category>(dis)).collect(Collectors.toCollection(ArrayList::new))
				.forEach(System.out::println);
		return matches.stream().filter(dis -> dis.getDissimilarity() <= getMedian.apply(dissimilarity))
				.filter(dis -> dis.getRelativeFrequency() >=getSumStat.apply(frequency).getMin())
				.filter(dis -> dis.getRelativeSize() > 0.1 && dis.getRelativeSize() < 3.0)
				.sorted(Comparator.comparing(WeightedDissimilar<Category>::getRelativeSize,
						Comparator.reverseOrder()))
				.map(dis -> new Result<Category>(dis)).limit(2).collect(Collectors.toCollection(ArrayList::new));
	}

	public List<Result<Category>> preMatchBasedOnProducts(CategoryKosik kosik, List<Category> rohliky) {
		List<WeightedDissimilar<Category>> matches = new ArrayList<>();
		rohliky = rohliky.stream().filter(Category::getActive)
				.collect(Collectors.toCollection(ArrayList::new));
		List<ProductKosik> productsForCategory = overview
				.getCompleteProductListUsingPaginationForCategoryMatching(BASIC_URL + kosik.getUri());
		for (Category rohlik : rohliky) {
			List<Product> products = rohlik.getProducts().stream().collect(Collectors.toCollection(ArrayList::new));
			Long productCount = (long) productsForCategory.size();
			ProductMatcher matcher = new ProductMatcher();
			if (!products.isEmpty()) {
				Long match = productsForCategory.stream().map(product -> matcher.findMatchCosine(product, products))
						.map(result -> result.getEntityForLimit(0.4)).filter(Optional::isPresent).count();
				Double dissimilarity = products.isEmpty() ? 1.0 : 1.0 - ((double) match) / productCount;
				Double relativeFrequency = ((double) match) / products.size();
				Double relativeSize = ((double) products.size()) / productCount;
				matches.add(new WeightedDissimilar<Category>(rohlik, dissimilarity, relativeFrequency, relativeSize));
			}
		}
		return matches.stream().filter(dis -> dis.getDissimilarity() < 1.0).map(dis -> new Result<Category>(dis))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	Function<List<Double>, DoubleSummaryStatistics> getSumStat = matches -> matches.stream()
			.mapToDouble(Double::doubleValue).summaryStatistics();

	Function<List<Double>, Double> getVariance = matches -> getSumForVariance(getSumStat.apply(matches)).apply(matches)
			/ getSumStat.apply(matches).getCount();

	Function<List<Double>, Double> getSumForVariance(DoubleSummaryStatistics sumStat) {
		return matches -> matches.stream().map(dis -> dis - sumStat.getAverage()).map(res -> res * res)
				.mapToDouble(Double::doubleValue).sum();
	}
Function<List<Double>, Double> getMedian = list -> list.size()%2 == 0?
	 list.size()>1 ?   list.stream().sorted().skip(list.size()/2-1).limit(2).mapToDouble(Double::doubleValue).average().getAsDouble(): list.stream().findFirst().orElseGet(()->0.0) :        
	        list.stream().sorted().skip(list.size()/2).mapToDouble(Double::doubleValue).findFirst().getAsDouble();
}
