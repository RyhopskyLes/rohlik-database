package com.rohlik.data.commons.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.entities.ProductKosik;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;


public class ProductMatcher {
	private static Logger log = LoggerFactory.getLogger(ProductMatcher.class);
	private Cosine cos;
	private SorensenDice sc;
	private JaroWinkler jw;
	private Jaccard jc;
	private NGram ngram;

	public ProductMatcher() {
		cos = new Cosine();
		sc = new SorensenDice(2);
		jw = new JaroWinkler();
		jc = new Jaccard();
		ngram = new NGram(3);
	}

	public Result<Product> findMatchCosine(ProductKosik kosik, List<Product> rohliky) {
		Dissimilar<Product> leastDissimilar = Collections.min(collectDissimilars(kosik, rohliky, this.cos),
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	public Result<Product> findMatchSorensen(ProductKosik kosik, List<Product> rohliky) {
		Dissimilar<Product> leastDissimilar = Collections.min(collectDissimilars(kosik, rohliky, this.sc),
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	public Result<Product> findMatchJaroWinkler(ProductKosik kosik, List<Product> rohliky) {
		Dissimilar<Product> leastDissimilar = Collections.min(collectDissimilars(kosik, rohliky, this.jw),
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	public Result<Product> findMatchJaccard(ProductKosik kosik, List<Product> rohliky) {
		Dissimilar<Product> leastDissimilar = Collections.min(collectDissimilars(kosik, rohliky, this.jc),
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	public Result<Product> findMatchNGramm(ProductKosik kosik, List<Product> rohliky) {
		Dissimilar<Product> leastDissimilar = Collections.min(collectDissimilars(kosik, rohliky, this.ngram),
				Comparator.comparing(Dissimilar::getDissimilarity));
		return new Result<>(leastDissimilar);
	}

	private List<Dissimilar<Product>> collectDissimilars(ProductKosik kosik, List<Product> rohliky,
			NormalizedStringDistance algorithm) {
		return rohliky.stream().filter(Product::getActive).map(getDissimilar(kosik, algorithm)::apply)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private Function<Product, Dissimilar<Product>> getDissimilar(ProductKosik kosik,
			NormalizedStringDistance algorithm) {
		return product -> {
			Double dissimilarity = algorithm.distance(kosik.getName(), product.getProductName());
			return new Dissimilar<>(product, dissimilarity);
		};
	}
}
