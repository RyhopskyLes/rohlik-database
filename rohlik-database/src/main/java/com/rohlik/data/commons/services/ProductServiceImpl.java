package com.rohlik.data.commons.services;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.objects.FullProductInfo;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.ProductsInCategory;
import com.rohlik.data.objects.RawProduct;
import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.entities.Sale;

@Service("jpaProductService")
@Transactional
@SuppressWarnings("unchecked")
public class ProductServiceImpl implements ProductService {
	public static final Long LEKARNA_KEY = 300112985L;
	public static final String ROOT_DIRECTORY = "C:/Images";
	public static final String NO_IMAGE_ICON = "https://www.rohlik.cz/static/img/icons/product-no-image.svg";
	public static final String NAVIGATION_URL = "https://www.rohlik.cz/services/frontend-service/renderer/navigation/flat.json";
	public static final String CATEGORY_URL = "https://www.rohlik.cz/services/frontend-service/products/";
	public static final String BASIC_LIMIT = "25";
	public static final String CATEGORY_METADATA_URL = "https://www.rohlik.cz/services/frontend-service/category-metadata/";
	public static final String OFFSET_LIMIT = "?offset=0&limit=";
	public static final String ROHLIK_IMAGES_START = "https://images.rohlik.cz";
	public static final String ROHLIK_STATIC_START = "https://www.rohlik.cz";
	@PersistenceContext
	private EntityManager em;
	
	private ProductDao productDao;
	private CategoryDao categoryDao;
	private CategoryService categoryService;
	private ProductsInCategory productsInCategory;
	private Navigation navigation;
	private FullProductInfo fullProductInfo;
	
	private static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	private long countUpdated = 0;
	private long countSaved = 0;

	@Autowired
	public ProductServiceImpl(ProductDao productDao, CategoryDao categoryDao,
			CategoryService categoryService, ProductsInCategory productsInCategory, Navigation navigation,
			FullProductInfo fullProductInfo) {
		super();
		this.productDao = productDao;
		this.categoryDao = categoryDao;
		this.categoryService = categoryService;
		this.productsInCategory = productsInCategory;
		this.navigation = navigation;
		this.fullProductInfo = fullProductInfo;
	}

	
	@Override
	public void deletAllDataFromALLTables() {
		StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("deleteAllData");
		storedProcedure.execute();
	}

	@Override
	public void showTables() {
		Query query1 = em.createNativeQuery("show columns from product");
		List<Object[]> results = query1.getResultList();
		results.forEach(result->log.info("field: {}", Arrays.toString(result)));		
	}

	@Override
	public void saveAllProductsInCategoryToDatabase(Integer categoryId, Set<Integer> productIdSet) {
		Instant earlier = Instant.now();
		buildAllProductsInCategory(categoryId).stream().filter(product->!productIdSet.contains(product.getProductId())).forEach(
				product->{productIdSet.add(product.getProductId());
				saveImageIfNotSaved(product.getImgPath(), ROOT_DIRECTORY);
					productDao.save(product);});
		Instant later = Instant.now();

		Duration d = Duration.between(earlier, later);
		String output = d.toString();
		log.info("Kategorie {} trvala: {}", categoryId, output);
	}
	
	
	@Override
	public List<Product> buildAllProductsInCategory(Integer categoryId) {
		List<Product> products = productsInCategory.getProductListForCategoryWithSalesAndProducers(categoryId, 3000);
		products.forEach(this::setCategoriesForProduct);
		return products;
	}	
	
	private List<Product> buildAllProductsInCategoryForUpdate(Integer categoryId) {
		return productsInCategory.getProductListForCategoryWithSales(categoryId, 3000);		
	}	
	
	@Override
	public void saveAllProductsFromRohlikToDatabase() {
		Instant earlier = Instant.now();
		Set<Integer> productId = new HashSet<>();
		Map<Integer, String> categories = navigation.getAllMainCategoriesIdandName();
		for (Integer key : categories.keySet()) {
			saveAllProductsInCategoryToDatabase(key, productId);
		}
		Instant later = Instant.now();
		printDuration(earlier, later);
	}

	@Override
	public void removeSalesFromProductInDatabase(Product product) {
		Set<Sale> salesSet = product.getSales();
		for (Iterator<Sale> i = salesSet.iterator(); i.hasNext();) {
			Sale element = i.next();
			i.remove();
			product.removeSales(element);
		}
		productDao.save(product);
	}

	@Override
	public List<Product> findAllProductsWithNearingExpiryDate() {
		return productDao.findAllProductsWithNearingExpiryDate();
	}

	@Override
	public List<Product> findAllPremiumProducts() {
		return productDao.findAllPremiumProducts();
	}

	@Override
	public List<Product> findAllProductsWithoutProducer() {
		return productDao.findAllProductsWithoutProducer();
	}

	@Override
	public void updateAllProductsWithoutProducer() {
		Map<Integer, Set<Product>> productsByCategories = findAllProductsWithoutProducer().stream()
				.collect(groupingBy(Product::getMainCategoryId, Collectors.toSet()));
		for (Map.Entry<Integer, Set<Product>> item : productsByCategories.entrySet()) {
			Instant earlier = Instant.now();
			Integer categoryNum = item.getKey();
			Set<Product> products = item.getValue();

			for (Product product : products) {
				Optional<String> producer = findProducerForProduct(product, categoryNum);
				if (producer.isPresent()) {
					product.setProducer(producer.get());
					productDao.save(product);
					log.info("Updated: {} {}", product.getProductName(), product.getProducer());
				}
			}
			Instant later = Instant.now();
			printDuration(earlier, later, categoryNum);
		}
	}

	@Override
	public void setMainCategoryNameByAllProducts() {
		List<Product> products = productDao.findAll();
		Set<Category> categories = navigation.getAllCategories();
		products.stream().filter(Product::getIsFromRohlik).forEach(product -> {
			product.setMainCategoryName(navigation.getCategoryName(product.getMainCategoryId(), categories));
			productDao.save(product);
		});
	}

	@Override
	public void updateAllProductsInCategoryInDatabase(Integer categoryId, Set<Integer> productIdSet) {
		Instant earlier = Instant.now();
		Map<String, Set<Integer>> producersWithProductIds = productsInCategory.producersWithProductIdsForCategory(categoryId);
			buildAllProductsInCategoryForUpdate(categoryId).stream().forEach(
				product->{
				if(!productIdSet.contains(product.getProductId()))	
				{Optional<Product> oldProduct = loadOldProductForUpdate(product);
				saveNewOrUpdateOld(product, oldProduct, producersWithProductIds);
				productIdSet.add(product.getProductId());}
				else {
				Optional<Product> duplicate =	productDao.findByIdEagerlyWithCategories(product.getProductId());
				duplicate.ifPresent(theDuplicate->{
				Set<Category> categories =	product.getCategories();
				categories.forEach(theDuplicate::addCategory);
				productDao.save(theDuplicate);
				log.info("Added categories {}", categories);
				});
				}
				});
		Instant later = Instant.now();
		printDuration(earlier, later, categoryId);

	}

	private Optional<String> findProducerForProduct(Product product, Map<String, Set<Integer>> producersWithProductIds) {
		Integer productId = product.getProductId();
		Optional<String> producer = Optional.empty();
		for (Map.Entry<String, Set<Integer>> producerEntry : producersWithProductIds.entrySet()) {
			Set<Integer> idSet = producerEntry.getValue();
			if (idSet.contains(productId)) {
				return Optional.ofNullable(producerEntry.getKey());
			}
		}
		return producer;
	}

	private Optional<String> findProducerForProduct(Product product, Integer categoryId) {
		Integer productId = product.getProductId();
		Optional<String> producer = Optional.empty();
		Map<String, Set<Integer>> producersWithProductIds = productsInCategory.producersWithProductIdsForCategory(categoryId);
		for (Map.Entry<String, Set<Integer>> producerEntry : producersWithProductIds.entrySet()) {
			Set<Integer> idSet = producerEntry.getValue();
			if (idSet.contains(productId)) {
				return Optional.ofNullable(producerEntry.getKey());
			}
		}
		return producer;
	}
	
	@Override
	public void updateHasSalesByProductsInCategory(Integer number) {
		List<Product> products = productDao.findByMainCategoryIdEagerly(number);
		products.stream().filter(Product::getIsFromRohlik).filter(product->!product.getSales().isEmpty()).forEach(setHasSalesAndSave::accept);		
	}
	
	private Consumer<Product> setHasSalesAndSave = product-> {
		product.setHasSales(true);
		productDao.save(product);
		log.info("Updated: {} {}", product.getId(), product.getProductName());
	};

	@Override
	public void updateHasSalesByAllProductsInDatabase() {
		productDao.findAll().stream().filter(Product::getIsFromRohlik).filter(Product::getActive).map(Product::getMainCategoryId)
		.forEach(this::updateHasSalesByProductsInCategory);
				
	}

		private void saveNewProductByUpdate(Product product, Map<String, Set<Integer>>producersWithProductIds) {
		saveImageIfNotSaved(product.getImgPath(), ROOT_DIRECTORY);
		Optional<String> producer = findProducerForProduct(product, producersWithProductIds);
		product.setProducer(producer.orElseGet(()->""));
		setCategoriesForProduct(product);
		Product saved = productDao.save(product);
		countSaved++;
		log.info("{} saved new: {} {} {}", countSaved,  saved, saved.getSales(), saved.getCategories());
	}

	private void saveNewOrUpdateOld(Product product, Optional<Product> oldproduct, Map<String, Set<Integer>>producersWithProductIds) {
		if (!oldproduct.isPresent()) {
			saveNewProductByUpdate(product, producersWithProductIds);
		} else if (!product.equals(oldproduct.get())) {
			updateOldProduct(product, oldproduct);
		}

	}

	private void updateOldProduct(Product product, Optional<Product> oldProduct) {
		if (oldProduct.isPresent()) {
			saveImageIfNotSaved(product.getImgPath(), ROOT_DIRECTORY);
			product.setId(oldProduct.get().getId());
			product.setProducer(oldProduct.get().getProducer());
			product.setProductKosik(oldProduct.get().getProductKosik());
			product.setCategories(oldProduct.get().getCategories());
			Product saved = productDao.save(product);
			countUpdated++;
			log.info("{} updated old: {} {}", countUpdated, saved, saved.getSales());
		}
	}

	private Optional<Product> loadOldProductForUpdate(Product product) {
		return productDao.findByProductIdEagerlyWithCategories(product.getProductId());

	}

	private void printDuration(Instant earlier, Instant later, Integer number) {
		Duration d = Duration.between(earlier, later);
		String output = d.toString();
		log.info("Kategorie {} trvala: {}", number, output);
	}

	private void printDuration(Instant earlier, Instant later) {
		Duration d = Duration.between(earlier, later);
		String output = d.toString();
		log.info("Celkem: {}", output);
	}

	@Override
	public void updateAllProductsFromRohlikInDatabase() {
		Instant earlier = Instant.now();
		Set<Integer> productId = new HashSet<>();
		Map<Integer, String> categories = navigation.getAllMainCategoriesIdandName();
		for (Integer key : categories.keySet()) {
			updateAllProductsInCategoryInDatabase(key, productId);
		}
		Instant later = Instant.now();
		printDuration(earlier, later);
		log.info("Total updated: {}, total new saved: {}", countUpdated, countSaved);
	}

	@Override
	public void setCategoriesForProductsInDatabaseFromTo(Integer startId, Integer endId) {
		IntStream.rangeClosed(startId, endId)
		.mapToObj(productDao::findByIdEagerlyWithCategories)
		.filter(Optional::isPresent)
		.map(Optional::get)
		.filter(Product::getIsFromRohlik)
		.forEach(product-> {
				setCategoriesForProduct(product);
				productDao.save(product);
			});	
	}

	private void setCategoriesForProduct(Product product) {
		if (product!=null) {
			Set<Category> categories = categoryService.saveUnsavedCategories(product);
			categories.stream().map(Category::getCategoryId).forEach(categoryService::addMissingChildToParent);
			categories.stream().map(Category::getCategoryId).map(categoryDao::findByCategoryId)
					.filter(Optional::isPresent).map(Optional::get).forEach(product::addCategory);
		}
	}

	@Override
	public void deleteRemovedProductsFromDatabase() {
		// TODO Auto-generated method stub

	}	

	@Override
	public Integer addMissingImgPathToProducts() {
		Function<Product, String> getImgPath = prod -> fullProductInfo.getProductFull(prod.getProductId()).getImgPath();

		
		List<Product> products = productDao.findAllWithoutImgPath();
		products.stream().filter(Product::getIsFromRohlik).forEach(product -> 
		product.setImgPath(getImgPath.apply(product)));
		return products.size();
	}

	
	@Override
	public Integer addMissingMainCategoryNameToProducts() {
		Function<Product, Optional<Category>> getCategory = prod -> fullProductInfo.getProductFull(prod.getProductId())
				.getCategoriesConverted().stream().limit(1).findFirst();

		Consumer<Product> setMainCategoryName = product -> {
			Optional<Category> mainCategory = getCategory.apply(product);
			mainCategory.ifPresent(category -> {
					product.setMainCategoryName(category.getCategoryName());
					product.setMainCategoryId(category.getCategoryId());
					});			
		};

		List<Product> products = productDao.findAllWithoutMainCategoryName();
		products.stream().filter(Product::getIsFromRohlik).forEach(setMainCategoryName::accept);

		products = productDao.findAllWithoutMainCategoryName().stream().filter(Product::getIsFromRohlik).collect(Collectors.toCollection(ArrayList::new));		
		return products.size();
	}

	@Override
	public Set<Product> updateActiveStateOfProductsInCategory(Integer categoryId) {
		List<Integer> activeIds = productsInCategory.getProductIdsForCategory(categoryId, 3000);

		List<Product> allProducts = productDao.findAllProductsByCategoryId(categoryId);
		Set<Product> active = allProducts.stream().filter(Product::getIsFromRohlik).filter(product -> activeIds.contains(product.getProductId()))
				.map(product -> {
					product.setActive(true);
					return product;
				}).collect(Collectors.toCollection(HashSet::new));
		Set<Product> inactive = new HashSet<>(allProducts);
		inactive.removeAll(active);
		inactive.forEach(product->log.info("{}", product));
		log.info("activeIds {} active {} inactive {} all {}", activeIds.size(), active.size(), inactive.size(), allProducts.size());
		inactive.forEach(product -> product.setActive(false));
		return active;
	}

	@Override
	public void saveMissingImages() {
		List<Product> all = productDao.findAllEagerlyWithCategories();
		all.stream().map(Product::getImgPath).forEach(path -> saveImageIfNotSaved(path, ROOT_DIRECTORY));
	}

	@Override
	public void updateMainCategoryIdAndNameByAllProductsInCategory(Integer categoryId) {
		BiFunction<List<RawProduct>, Product, Optional<Pair<Product, RawProduct>>> getEquivalentProducts = (rawproducts,
				product) -> rawproducts.stream()
						.filter(rawproduct -> Objects.equals(rawproduct.getProductId(), product.getProductId()))
						.map(rawproduct -> Pair.of(product, rawproduct)).findFirst();
		Predicate<Pair<Product, RawProduct>> mainCategoryIdIsNotEqual = pair -> !Objects
				.equals(pair.getLeft().getMainCategoryId(), pair.getRight().getMainCategoryId());
		IntFunction<String> getMainCategoryName = id -> {
			Optional<Category> category = categoryDao.findByCategoryId(id);
			log.info("{} Befor if {}", id, category);
			if (!category.isPresent()) {
				categoryService.addMissingChildToParent(id);
				category = categoryDao.findByCategoryId(id);
							}
			return category.orElseGet(Category::new).getCategoryName();
		};
		Consumer<Pair<Product, RawProduct>> updateMainCategoryIdAndName = pair -> {
			Integer mainCategoryId = pair.getRight().getMainCategoryId();
			pair.getLeft().setMainCategoryId(mainCategoryId);
			pair.getLeft().setMainCategoryName(getMainCategoryName.apply(mainCategoryId));
		};

		List<RawProduct> rawProducts = productsInCategory.getRawProductListForCategory(categoryId, 3000);
		List<Product> active = productDao.findAllProductsByCategoryId(categoryId).stream().filter(Product::getIsFromRohlik)
				.filter(Product::getActive).collect(Collectors.toCollection(ArrayList::new));
		active.stream().map(product -> getEquivalentProducts.apply(rawProducts, product)).filter(Optional::isPresent)
				.map(Optional::get).filter(mainCategoryIdIsNotEqual::test)
				.forEach(updateMainCategoryIdAndName::accept);
	}

	@Override
	public Set<Product> updateActiveStateOfAllProducts() {
		List<Category> mainCategories = categoryDao.findMainCategoriesFromNavigation();
		Set<Integer> activeIds = mainCategories.stream().map(Category::getCategoryId)
				.map(categoryId -> productsInCategory.getProductIdsForCategory(categoryId, 3000)).flatMap(List::stream)
				.collect(Collectors.toCollection(HashSet::new));
		Set<RawProduct> activeRaw = mainCategories.stream().map(Category::getCategoryId)
				.map(categoryId -> productsInCategory.getRawProductListForCategory(categoryId, 3000)).flatMap(List::stream)
				.collect(Collectors.toCollection(HashSet::new));
		Set<Product> allProducts = mainCategories.stream().map(Category::getCategoryId)
				.map(productDao::findAllProductsByCategoryId).flatMap(List::stream).filter(Product::getIsFromRohlik)
				.collect(Collectors.toCollection(HashSet::new));
		Set<Product> active = allProducts.stream().filter(product -> activeIds.contains(product.getProductId()))
				.map(product -> {
					product.setActive(true);
					return product;
				}).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> resultIds = active.stream().map(Product::getProductId)
				.collect(Collectors.toCollection(HashSet::new));
		Set<Product> inactive = new HashSet<>(allProducts);
		Set<RawProduct> surplus = activeRaw.stream().filter(product -> !resultIds.contains(product.getProductId()))
				.collect(Collectors.toCollection(HashSet::new));
		inactive.removeAll(active);
		inactive.stream().forEach(product -> product.setActive(false));
		surplus.forEach(product->log.info("{}", product));
		log.info("activeIds {} active {} inactive {} all {}", activeIds.size(), active.size(), inactive.size(), allProducts.size());
		return active;
	}

	public String saveImageFromUrl(String url, String rootDirectory) {
		log.info("Image path as url: {}", url);
		int index = url.lastIndexOf('/');
		String urlwithoutFile = url.substring(0, index) + "/";
		String fileName = url.substring(index + 1, url.length());
		String pathToFile = urlwithoutFile.replace(ROHLIK_IMAGES_START, "");
		try {
			URL imgURL = new URL(url);
			File file = new File(rootDirectory + pathToFile);
			file.mkdirs();
			try (FileOutputStream fos = new FileOutputStream(rootDirectory + pathToFile + fileName);
					ReadableByteChannel rbc = Channels.newChannel(imgURL.openStream());) {

				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				log.info("Image: {} saved to {}{}", fileName, rootDirectory, pathToFile);
			} catch (IOException e) {
				log.info("Image from {} not saved.", url);
			}
		} catch (MalformedURLException e1) {
			log.info("Malformed url {}", url);
		}

		return rootDirectory + pathToFile;
	}

	public void saveImageIfNotSaved(String imgPath, String rootDirectory) {
		log.info("Image path: {}", imgPath);
		if (imgPath!= null && !"".equals(imgPath) && !existFile(imgPath, rootDirectory)) {
			if(imgPath.contains(ROHLIK_IMAGES_START))
			{saveImageFromUrl(imgPath, rootDirectory);}
			else {saveImageFromUrl(ROHLIK_IMAGES_START+imgPath, rootDirectory);}
		} 

	}
	
	
	private static boolean existFile(String url, String rootDirectory) {
		url = url.replace(ROHLIK_IMAGES_START, "");
		String pathToFile = rootDirectory+url;	
		File file = new File(pathToFile);
		Long size = file.length();
		log.info("length {} ", size);
		boolean isFile = file.isFile();
		log.info("Is file {} {}", isFile, pathToFile);
		return isFile && size>0;

	}
	
}
