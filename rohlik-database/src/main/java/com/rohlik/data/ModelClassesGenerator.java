package com.rohlik.data;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.components.CategoryMatcher;
import com.rohlik.data.kosik.components.ProductKosikOverview;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.objects.Filters;
import com.rohlik.data.objects.Full;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.ProductsInCategory;
import com.rohlik.data.objects.RawProduct;
import com.rohlik.data.objects.RootObject;

public class ModelClassesGenerator {

	public static void main(String[] args)  {
		
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
		
List<Product> chleb =prInCa.getProductListForCategory(300116465, 20);
chleb.forEach(pr->System.out.println(pr+"\n"+pr.getSales()));
//productService.updateAllProductsFromRohlikInDatabase();
		//catService.updateActiveStateByAllChildren();
		//catService.saveAllMissingSubcategories(300112985);	
		//catService.deactivateDeadSubcategories(300112985);

//productService.updateActiveStateOfAllProducts();
//catDao.findAllWithCategoriesKosikAndProducts().forEach(category-> productService.updateMainCategoryIdAndNameByAllProductsInCategory(category.getCategoryId()));
		//productService.updateMainCategoryIdAndNameByAllProductsInCategory(300106000);
		//productService.updateActiveStateOfProductsInCategory(300109082);
	
/*String kategorie = "/uzeniny-a-lahudky";
catKosikService.saveMainCategoryWithChildren(kategorie);
catKosikService.saveSecondLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveThirdLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveFourthLevelCategoriesWithChildrenBuiltFromURI(kategorie);
catKosikService.saveFifthLevelCategoriesWithChildrenBuiltFromURI(kategorie);*/
		



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
