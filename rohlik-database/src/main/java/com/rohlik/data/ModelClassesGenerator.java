package com.rohlik.data;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.rohlik.data.commons.config.AppConfig;
import com.rohlik.data.commons.config.DataConfig;
import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.commons.dao.ChildKosikDao;
import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.dao.ProductKosikDao;
import com.rohlik.data.commons.objects.ProductMatcher;
import com.rohlik.data.commons.services.CategoryKosikService;
import com.rohlik.data.commons.services.CategoryService;
import com.rohlik.data.commons.services.ChildKosikService;
import com.rohlik.data.commons.services.ProductKosikService;
import com.rohlik.data.commons.services.ProductService;
import com.rohlik.data.commons.utilities.DataRohlik;
import com.rohlik.data.commons.utilities.Source;
import com.rohlik.data.entities.Category;
import com.rohlik.data.kosik.components.ProductKosikOverview;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.kosik.objects.CategoryMatcher;
import com.rohlik.data.objects.Filters;
import com.rohlik.data.objects.Full;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.ProductsInCategory;
import com.rohlik.data.objects.RootObject;

public class ModelClassesGenerator {

	public static void main(String[] args)  {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.getEnvironment().setActiveProfiles("production");
		ctx.register(AppConfig.class, DataConfig.class);
		ctx.refresh();
		for (String beanName : ctx.getBeanDefinitionNames()) {
			System.out.println(beanName);
		}
		ProductService productService = ctx.getBean(ProductService.class);
		CategoryService catService = ctx.getBean(CategoryService.class);
		CategoryKosikService catKosikService = ctx.getBean(CategoryKosikService.class);
		ProductDao productDao = ctx.getBean(ProductDao.class);
		CategoryDao catDao = ctx.getBean(CategoryDao.class);
		ChildKosikDao childKosikDao = ctx.getBean(ChildKosikDao.class);
		CategoryKosikDao catKosikDao = ctx.getBean(CategoryKosikDao.class);
		ProductKosikDao kosikDao = ctx.getBean(ProductKosikDao.class);
		ProductKosikService kosikService = ctx.getBean(ProductKosikService.class);
		ChildKosikService childKosikService = ctx.getBean(ChildKosikService.class);
		Source scrap = ctx.getBean(Source.class);
		CategoryKosikOverview review = ctx.getBean("categoryOverView", CategoryKosikOverview.class);
		ProductKosikOverview productKosikOverview = ctx.getBean("productOverView", ProductKosikOverview.class);
		NavSections nav = ctx.getBean("navSections", NavSections.class);
		NavigationBuilder navigationBuilder = ctx.getBean(NavigationBuilder.class);
		DataRohlik extractFile = ctx.getBean(DataRohlik.class);
		Filters filters = ctx.getBean(Filters.class);
		RootObject rootObject = ctx.getBean(RootObject.class);
		ProductsInCategory prInCa = ctx.getBean(ProductsInCategory.class);
		CategoryMatcher catmatcher = ctx.getBean(CategoryMatcher.class);
		Navigation navigation = ctx.getBean(Navigation.class);
		Full full = ctx.getBean(Full.class);
		ProductMatcher matcher = new ProductMatcher();
		AtomicInteger counter = new AtomicInteger(0);
	//productService.updateActiveStateOfAllProducts();
	
		//	catKosikService.addCategorytoCategoryKosik(95, 300105051, true);
		//removeCategories(94, 300105046,catKosikDao, catKosikService);
		//catKosikDao.findByIdWithCategories(95).get().getCategories().forEach(System.out::println);
		
		//catKosikService.addCategorytoCategoryKosik(idCategoryKosik, categoryIdCategoryRohlik, setParameters);
	//catKosikService.saveThirdLevelCategoriesWithChildrenBuiltFromURI("/mlecne-a-chlazene");
						
	//	catKosikService.addCategorytoCategoryKosik(58, 300101016, true);
	//	catKosikDao.findByIdWithCategories(58).get().getCategories().forEach(System.out::println);
				
//System.out.println("sumitKumarChaurasia".replaceAll("(.)([A-Z])", "$1_$2").toLowerCase());
		/*
		 * List<Result<Category>> result
		 * =catmatcher.findMatchBasedOnProducts(catKosikDao.findByIdWithCategories(17).
		 * get(), catDao.findAllWithCategoriesKosikAndProducts());
		 * result.stream().forEach(x->x.getClass());
		 */
		/*
		 * Set<CategoryKosik> lowest =
		 * catKosikService.getLowestLevelCategoriesInTreeOf("/napoje/vina/bila");
		 * List<ProductKosik> products = lowest.stream().map(CategoryKosik::getUri)
		 * .map(uri->productKosikOverview.
		 * getProductKosikListForcategoryGroupedByProducers("https://www.kosik.cz"+uri))
		 * .flatMap(List::stream) .collect(Collectors.toCollection(ArrayList::new));
		 * System.out.println(products.size()); products.forEach(System.out::println);
		 * List<Product> rohliky = productDao.findAll(); products.forEach(product->{
		 * Optional<Product> ekvi = matcher.findMatchCosine(product,
		 * rohliky).getEntityForLimit(0.38); Optional<Product> ekvi2 =
		 * matcher.findMatchSorensen(product, rohliky).getEntityForLimit(0.33);
		 * Optional<Product> ekvi3 = matcher.findMatchJaccard(product,
		 * rohliky).getEntityForLimit(0.45); Optional<Product> ekvi4 =
		 * matcher.findMatchNGramm(product, rohliky).getEntityForLimit(0.35);
		 * if(ekvi.isPresent() || ekvi2.isPresent() || ekvi3.isPresent() ||
		 * ekvi4.isPresent()) { counter.getAndIncrement();
		 * System.out.println(product.getName()); System.out.println(ekvi.orElse(new
		 * Product()).getProductName()); System.out.println(ekvi2.orElse(new
		 * Product()).getProductName()); System.out.println(ekvi3.orElse(new
		 * Product()).getProductName()); System.out.println(ekvi4.orElse(new
		 * Product()).getProductName()); System.out.println(counter.get());
		 * System.out.println("---------------------------------------");}});
		 */

//review.allLinksAndNamesOnFirstLevel("https://www.kosik.cz/napoje/vina/cervena").entrySet().forEach(System.out::println);
		/*
		 * StreamSupport.stream(subcategories.spliterator(),
		 * false).forEach(System.out::println); String levels=
		 * "https://www.kosik.cz/napoje";
		 * System.out.println(levels.replace("https://www.kosik.cz",
		 * "").split("/").length);
		 */
		// System.out.println(scrap.rootObject("https://www.kosik.cz/pekarna-a-cukrarna?brands[0]=1087"));

		/*
		 * List<CategoryKosik> allCategories = catKosikDao.findAllWithChildren();
		 * List<ChildKosik> missing = allCategories.stream().map(CategoryKosik::getUri)
		 * .map(catKosikService::buildMissingChildrenOfCategory)
		 * .flatMap(list->list.stream()) .collect(Collectors.toList());
		 * missing.forEach(System.out::println); System.out.println(missing.size());
		 */
//productService.updateAllProductsFromRohlikInDatabase();
String kategorie = "/napoje";	
catKosikService.saveMainCategoryWithChildren(kategorie);
catKosikService.saveSecondLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveThirdLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveFourthLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveFifthLevelCategoriesWithChildrenBuiltFromURI(kategorie);
			
//catDao.findAll().stream().filter(category->category.getActive()).forEach(category->catService.saveAllMissingSubcategories(category.getCategoryId()));
//catDao.findAll().stream().filter(category->category.getActive()).forEach(category->catService.deactivateDeadSubcategories(category.getCategoryId()));
//catService.updateActiveStateByAllChildren();
//catService.saveAllMissingSubcategories(300112985);	
//catService.deactivateDeadSubcategories(300112985);
//prInCa.getProductIdsForCategory(300116467, 20).stream().forEach(System.out::println);
//prInCa.getProductListForCategory(300116467, 20).stream().forEach(System.out::println);  
//catService.addMissingProductsToCategory(300101000);
//productService.updateAllProductsFromRohlikInDatabase();

//productService.updateMainCategoryIdAndNameByAllProductsInCategory(300106000);
//productService.updateActiveStateOfProductsInCategory(300109082);

		/*
		 * int initial; int initialChildren; try { List<Category> categories =
		 * catDao.findAllWithChildren(); Category child =
		 * catDao.findByCategoryIdWithChildren(300115119); System.out.println(child);
		 * categories.add(child); initial=categories.size(); initialChildren =
		 * categories.stream().map(category->category.getChildren())
		 * .map(set->set.size()) .collect(Collectors.summingInt(Integer::valueOf));
		 * FileOutputStream f = new FileOutputStream(new File("categories.txt"));
		 * ObjectOutputStream o = new ObjectOutputStream(f);
		 * categories.stream().forEach(category->{ try { o.writeObject(category); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }); // Write objects to file
		 * 
		 * 
		 * 
		 * o.close(); f.close();
		 * 
		 * FileInputStream fi = new FileInputStream(new File("categories.txt"));
		 * ObjectInputStream oi = new ObjectInputStream(fi); List<Category> retrieved =
		 * new ArrayList<>(); for(int i=0; i<categories.size(); i++) { Category
		 * category=(Category) oi.readObject(); retrieved.add(category); } int
		 * endChildren = retrieved.stream().map(category->category.getChildren())
		 * .map(set->set.size()) .collect(Collectors.summingInt(Integer::valueOf));
		 * System.out.println(retrieved.size()+"\t"+initial+"\t"+initialChildren+"\t"+
		 * endChildren); oi.close(); fi.close();
		 * 
		 * } catch (FileNotFoundException e) { System.out.println("File not found"); }
		 * catch (IOException e) { System.out.println("Error initializing stream"); }
		 * catch (ClassNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * String
		 * categoryURL="https://www.kosik.cz/mlecne-a-chlazene?do=productList-load"; /*
		 * Optional<CategoryKosik> racio =
		 * catKosikDao.findByCategoryNameWithChildren("Racio a Knäckebrot");
		 * System.out.println(racio);
		 * racio.get().getChildren().forEach(System.out::println); ChildKosik child =
		 * new ChildKosik(); child.setCategoryName("Pufované");
		 * child.setEquiCategoryName("Sladké pufované pečivo");
		 * child.setEquiId(300101048);
		 * 
		 * racio.get().addChildKosik(child); catKosikDao.save(racio.get()); racio =
		 * catKosikDao.findByCategoryNameWithChildren("Racio a Knäckebrot");
		 * System.out.println(racio);
		 * racio.get().getChildren().forEach(System.out::println);
		 */
		// DataKosik.mainCategoriesLinks().stream().forEach(link->catKosikService.saveSecondLevelCategoriesWithChildren("https://www.kosik.cz"+link));
ctx.close();
	}
public static void removeCategories(Integer idKosik, Integer idRohlikNotToRemove, CategoryKosikDao catKosikDao, CategoryKosikService catKosikService) {
	Optional<CategoryKosik> kosik= catKosikDao.findByIdWithCategories(idKosik);
	if(kosik.isPresent()) {
	kosik.get().getCategories().stream()
	.map(Category::getCategoryId).filter(id->!id.equals(idRohlikNotToRemove))
	.forEach(id->catKosikService.removeCategoryFromCategoryKosik(idKosik, id));
	}
}
}
