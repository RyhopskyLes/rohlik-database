package com.rohlik.data.commons.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service("Source")
public class Source {
	public static long count = 0;
	public static long countKosik = 0;
	private static Logger log = LoggerFactory.getLogger(Source.class);
	public Source() {}

	public Optional<JsonObject> rootObject(String url) {	
		count++;
		 JsonParser jp = new JsonParser();	
		 log.info("Called " +count+" URL "+url);
		try {
			URL url1 = new URL(url);			
			final HttpURLConnection request1 = (HttpURLConnection) url1.openConnection();
			return processConnection(jp, request1);
		} catch (IOException e1) {			
		//	e1.printStackTrace();	
		log.info("Caused by: "+e1.getCause().toString()+"\n" +e1.getCause().getMessage());
		
		} 
		 
		
		return Optional.empty();
	}
	
		
	public Optional<Document> getJsoupDoc(String categoryURL)  {
		countKosik++;
		log.info("Called " +countKosik+" URL "+categoryURL);
			try {
				HttpResponse<String> response = Unirest.get(categoryURL).asString();
				
				if(response.getStatus()==301) {
					String location = response.getHeaders().get("Location").get(0);
					response = Unirest.get(location).asString();}
				String resp = response.getBody();				 	
				Optional<Document> doc = Optional.ofNullable(Jsoup.parse(resp));	
				return doc;	
			
			} catch (UnirestException e1) {
				e1.printStackTrace();
				
			}			
	return Optional.empty();	
}
	
	
	public Document getFilteredPage(String URI, String label) {
	   String chromeDriverPath = "C:\\Users\\RadimP\\chromedriver.exe" ;
	   String basicURL ="https://www.kosik.cz";
       System.setProperty("webdriver.chrome.driver", chromeDriverPath);
       ChromeOptions options = new ChromeOptions();
       options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent", "--start-maximized", "--start-fullscreen");
        WebDriver driver = new ChromeDriver(options);  	        	
        driver.get(basicURL+URI);	      
         WebElement strBold = driver.findElement(By.cssSelector("label[for='"+label+"']"));
         WebElement parent = strBold.findElement(By.xpath("./.."));
          if(!parent.isDisplayed()) {
         ((JavascriptExecutor) driver).executeScript("arguments[0].style='display: block;'", parent);}
         String value =driver.findElement(By.cssSelector("label[for='"+label+"']")).getText();
          WebElement count =driver.findElement(By.cssSelector(".count > p"));
          String actualValue = count.getText();
          String withoutNumber=actualValue.replaceAll("[0-9]", "");
          String[] splitted = value.split(" ");
         String expectedValue = splitted[splitted.length-1].replace("(", "").replace(")", "")+withoutNumber;
          Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
         		.withTimeout(15, TimeUnit.SECONDS)        		
         	    .pollingEvery(200, TimeUnit.MILLISECONDS)
         	    .ignoring(NoSuchElementException.class)
         	    .ignoring(StaleElementReferenceException.class)
         	    .ignoring(TimeoutException.class);

         Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
 			public Boolean apply(WebDriver arg0) {
 				WebElement element = arg0.findElement(By.cssSelector(".count > p"));
 				String actualValue = element.getText();
 				String expectedValue = splitted[splitted.length-1].replace("(", "").replace(")", "")+withoutNumber;
 				System.out.println("Nalezeno produktů :"+expectedValue+" " + actualValue);
 				if (actualValue.equals(expectedValue)) {
 					return true;
 				}
 				return false;
 			}
 		};
        strBold.click();

          wait.until(function);
       
       String source = driver.getPageSource();
         driver.quit();  
       
	return Jsoup.parse(source);	
	}
	
	public Document getFilteredPage(String URI) {
		   String chromeDriverPath = "C:\\Users\\RadimP\\chromedriver.exe" ;
		   String basicURL ="https://www.kosik.cz";
	       System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	       ChromeOptions options = new ChromeOptions();
	       options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent", "--start-maximized", "--start-fullscreen");
	        WebDriver driver = new ChromeDriver(options);  
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	        driver.get(basicURL+URI);
	        WebElement filter = driver.findElement(By.cssSelector("div.filter.filter--brands"));
	        		filter.click();
	        List<WebElement> filterElements= filter.findElements(By.cssSelector("div.filter__item"));
	        filterElements.get(0).click();
	        WebDriverWait wait = new WebDriverWait(driver, 10); // seconds
	        wait.until(ExpectedConditions.urlToBe("https://www.kosik.cz/pekarna-a-cukrarna?brands[0]=1087"));
	        String source = driver.getPageSource();
	         driver.close();
	         driver.quit();  
	       
	         return Jsoup.parse(source);	
		}
	
	
	private Optional<JsonObject> processConnection(JsonParser jp, HttpURLConnection con) {
		try (AutoCloseable conc = () -> con.disconnect()) {
		     int responseCode = con.getResponseCode();
		    try (InputStream ins = responseCode >= 400 ? con.getErrorStream() : con.getInputStream();
		    		InputStreamReader inRead = new InputStreamReader(ins)) {
		    	return Optional.ofNullable(jp.parse(inRead).getAsJsonObject());
		    }
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return Optional.empty();			
	}
}
