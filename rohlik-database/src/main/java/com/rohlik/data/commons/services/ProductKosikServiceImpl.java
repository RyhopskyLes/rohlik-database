package com.rohlik.data.commons.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.dao.ProductKosikDao;
import com.rohlik.data.commons.objects.ProductMatcher;
import com.rohlik.data.commons.objects.Result;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.components.ProductKosikOverview;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ProductKosik;

@Service("jpaProductKosikService")
@Transactional
@SuppressWarnings("unchecked")
@PropertySource("classpath:app.properties")
public class ProductKosikServiceImpl implements ProductKosikService {
	private static Logger log = LoggerFactory.getLogger(ProductKosikServiceImpl.class);
	private static final String BASIC_URL = "https://www.kosik.cz";
	private static final String IMAGE_URL = "https://static.kosik.cz";
	@PersistenceContext
	private EntityManager em;
	private ProductKosikDao productKosikDao;
	private ProductDao productDao;
	private CategoryService catService;
	private ProductKosikOverview productKosikOverview;
	private CategoryKosikService catKosikService;
	@Value("${rootDirectory}")
	private  String rootDirectory;
	private ProductMatcher matcher;
	
	@Autowired
	public ProductKosikServiceImpl(ProductKosikDao productKosikDao, ProductDao productDao, CategoryService catService,
			ProductKosikOverview productKosikOverview, CategoryKosikService catKosikService) {
		super();
		this.productKosikDao = productKosikDao;
		this.productDao = productDao;
		this.catService = catService;
		this.productKosikOverview = productKosikOverview;
		this.catKosikService = catKosikService;		
	}	

	@Override
	public void saveAllKosikProductsInCategoryToDatabase(String categoryURL) {
		List<ProductKosik> products = buildAllKosikProductsInCategory(categoryURL);
		products.stream().forEach(product -> {
			saveImageIfNotSaved(getImageSrc(product));
			productKosikDao.save(product);
		});
		products.stream().filter(product -> product.getEquiId() != null)
				.forEach(product -> log.info("{} {}", product.getEquiId(), product.getDissimilarity()));
		log.info("Products size: {}", products.size());
	}

	private List<ProductKosik> setEquiIdAndDissimilarityForProductsInCategory(List<Product> rohliky,
			List<ProductKosik> products, Double limit) {
		matcher = new ProductMatcher();
		return products.stream().map(setDissimilarityAndEquiId(rohliky, limit)::apply)
				.collect(Collectors.toCollection(ArrayList::new));

	}

	private UnaryOperator<ProductKosik> setDissimilarityAndEquiId(List<Product> rohliky, Double limit) {
		return product -> {
			Result<Product> result = matcher.findMatchCosine(product, rohliky).getResultForLimit(limit);
			result.getDissimilarity().ifPresent(product::setDissimilarity);
			result.getEquiId().ifPresent(product::setEquiId);
			return product;
		};
	}

	private String getImageSrc(ProductKosik product) {
		String[] links = product.getImageSrc().split(", ");
		String imageLink = "";
		if (links.length > 1) {
			imageLink = links[1].replace(" 1x", "");
		} else {
			log.info("Image link for {} not extracted", product);
		}
		return imageLink;
	}

	public String saveImageFromUrl(String url) {

		int index = url.lastIndexOf('/');
		String urlwithoutFile = url.substring(0, index) + "/";
		String fileName = url.substring(index + 1, url.length());
		String pathToFile = urlwithoutFile.replace(IMAGE_URL, "");
		String[] pathParts = new String[] { url, pathToFile, fileName };
		try {
			URL imgURL = new URL(url);
			log.info("Root directory {}", this.rootDirectory);
			File file = new File(this.rootDirectory + pathToFile);
			file.mkdirs();
			transferImageToTargetDirectory(pathParts, imgURL);
		} catch (MalformedURLException e1) {
			log.info("Malformed url {}", url);
		}
		return this.rootDirectory + pathToFile;
	}

	private void transferImageToTargetDirectory(String[] pathParts, URL imgURL) {
		try (FileOutputStream fos = new FileOutputStream(this.rootDirectory + pathParts[1] + pathParts[2]);
				ReadableByteChannel rbc = Channels.newChannel(imgURL.openStream());) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			log.info("Image: {} saved to {}{} ", pathParts[2], this.rootDirectory, pathParts[1]);
		} catch (IOException e) {
			log.info("Image from {} not saved", pathParts[0]);
		}
	}

	public void saveImageIfNotSaved(String imgPath) {
		if (!"".equals(imgPath) && !existFile(imgPath)) {
			saveImageFromUrl(imgPath);
		}
	}

	private boolean existFile(String url) {
		String pathToFile = url.replace(IMAGE_URL, this.rootDirectory);
		return new File(pathToFile).isFile();
	}

	@Override
	public List<ProductKosik> buildAllKosikProductsInCategory(String categoryURL) {
		List<Product> rohliky = productDao.findAll();
		Map<Integer, Product> sortedById = rohliky.stream()
				.collect(Collectors.toMap(Product::getId, Function.identity(), (o1, o2) -> o1, HashMap::new));
		List<ProductKosik> products = productKosikOverview
				.getProductKosikListForcategoryGroupedByProducers(categoryURL);
		TreeMap<Integer, CategoryKosik> categories = catKosikService
				.findAllParentCategoriesUpToTheHighestParent(categoryURL.replace(BASIC_URL, ""));
		products.forEach(product -> categories.entrySet().forEach(entry -> product.addCategory(entry.getValue())));
		setEquiIdAndDissimilarityForProductsInCategory(rohliky, products, 0.35);
		products = reduceDuplicateEquiIds.apply(products);
		products.forEach(setEquivalentProduct(sortedById)::accept);
		products.stream().filter(product->product.getProduct()==null).forEach(setEquivalentEmptyProductWithKosikData()::accept);
		return products;
	}

	private Consumer<ProductKosik> setEquivalentProduct(Map<Integer, Product> sortedById) {
		return product -> {
			if (product.getEquiId() != null) {
				Product equivalent = sortedById.get(product.getEquiId());
				product.setProduct(equivalent);
			}
		};
	}

	private Consumer<ProductKosik> setEquivalentEmptyProductWithKosikData() {
		return product -> {
			Product productRohlik = new Product();
			productRohlik.setFromRohlik(false);
			productRohlik.setActive(product.getActive());
			productRohlik.setInStock(product.getInStock());
			productRohlik.setProductName(product.getName());
			productRohlik.setHasSales(false);
			productRohlik.setProducer(product.getProducer());
			productRohlik.setImgPath(getImageSrc(product).replace(IMAGE_URL, ""));
			getCategoriesForEmptyEquivalentProduct().apply(product).forEach(productRohlik::addCategory);
			product.setProduct(productRohlik);
		};
	}
	
	private Function<ProductKosik, Set<Category>> getCategoriesForEmptyEquivalentProduct() {
	return	product-> 
		 product.getCategories().stream().map(CategoryKosik::getCategories).flatMap(Set::stream)
				.map(category->{
					Set<Category> temp=	catService.findParentsUpToHighestParent(category.getCategoryId()).values().stream().collect(Collectors.toCollection(HashSet::new));
					temp.add(category);
					return temp;
				}).flatMap(Set::stream).collect(Collectors.toCollection(HashSet::new));			
	}

	private UnaryOperator<List<ProductKosik>> reduceDuplicateEquiIds = products -> {
		Map<Integer, List<ProductKosik>> grouped = products.stream()
				.collect(groupingBy(product -> product.getEquiId() != null ? product.getEquiId() : -1));

		return grouped.values().stream().map(reduceDuplicatesInGroup()::apply).flatMap(List::stream)
				.collect(Collectors.toCollection(ArrayList::new));
	};

	private Consumer<ProductKosik> setEquiIdAndDissimilarityToNullOnDuplicates(Double min) {
		return product -> {
			if (product.getEquiId() != null && !product.getDissimilarity().equals(min)) {
				product.setEquiId(null);
				product.setDissimilarity(null);
			}
		};
	}

	private UnaryOperator<List<ProductKosik>> reduceDuplicatesInGroup() {
		return list -> {
			Double min = list.stream().filter(product -> product.getDissimilarity() != null)
					.mapToDouble(ProductKosik::getDissimilarity).min().orElseGet(() -> 0.0);
			return list.stream().map(product -> {
				setEquiIdAndDissimilarityToNullOnDuplicates(min).accept(product);
				return product;
			}).collect(Collectors.toCollection(ArrayList::new));
		};
	}

	@Override
	public void addProductToProductKosik(Integer kosikId, Integer rohlikId) {
		Optional<ProductKosik> kosik = productKosikDao.findById(kosikId);
		Product rohlik = productDao.findById(rohlikId);
		kosik.ifPresent(theKosik -> theKosik.setProduct(rohlik));
	}

	@Override
	public void removeProductFromProductKosik(Integer kosikId, Integer rohlikId) {
		Optional<ProductKosik> kosik = productKosikDao.findById(kosikId);
		kosik.ifPresent(theKosik -> theKosik.setProduct(null));
	}

}
