package com.rohlik.data;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.net.ssl.HttpsURLConnection;

import org.brotli.dec.BrotliInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HttpUrlConnectionExample {
	private List<String> cookies;
	  private HttpsURLConnection conn;
WebClient webclient;
	  private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36";

	  public static void main(String[] args) throws Exception {
		  String url1 ="https://www.rohlik.cz";
		String url = "https://www.rohlik.cz/services/frontend-service/login";
		//String gmail = "https://mail.google.com/mail/";
		WebClient webclient1 = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webclient1.getOptions().setJavaScriptEnabled(false);
	    webclient1.getOptions().setThrowExceptionOnScriptError(false);
	    webclient1.getOptions().setCssEnabled(false);
	    webclient1.setAjaxController(new NicelyResynchronizingAjaxController());
	    Map< Integer, String> books = new LinkedHashMap<>();
	    for(int i=0; i<2284; i++)
	    {	String autor_cons="";
	    	HtmlPage page = webclient1.getPage("https://www.oikoymenh.cz/product_info.php?products_id="+i);
	    HtmlElement autor = page.getByXPath("//td[@class='pageHeading narrowOnlyBlock']/a").size() > 0? (HtmlElement) page.getByXPath("//td[@class='pageHeading narrowOnlyBlock']/a").get(0) : null;  
	HtmlElement info = page.getByXPath("//td[@class='pageHeading narrowOnlyBlock']/h1").size() > 0? (HtmlElement) page.getByXPath("//td[@class='pageHeading narrowOnlyBlock']/h1").get(0) : null;
	if(autor!= null) autor_cons=autor.asText();
	if(info != null) {books.put(i,autor_cons+", "+ info.asText());} else {books.put(i, "nic");}
	    }
	    books.entrySet().stream().filter(x -> !x.getValue().equals("nic")).forEach(x -> System.out.println(x.getKey()+". "+x.getValue()));
	webclient1.close();
	    /*	HtmlPage page = webclient1.getPage("https://www.rohlik.cz");
		page.getElementById("headerLogin").click();
		 HtmlForm form = page.getForms().get(0);
		 form.getInputByName("email").type("radim.pekarek@centrum.cz");
		 form.getInputByName("password").type("75BLBOvina75");
		page= form.getElementsByAttribute("button", "class", "LoginBox__button").get(0).click();*/
		//System.out.println(button.asXml());
	//	System.out.println(page.asText());
	//	HttpUrlConnectionExample http = new HttpUrlConnectionExample();;

		// make sure cookies is turn on
	//	CookieHandler.setDefault(new CookieManager());

		// 1. Send a "GET" request, so that you can extract the form's data.
	//	String page = http.GetPageContent(url1);
	//	String postParams =http.getFormParams(page, "radim.pekarek@centrum.cz", "75BLBOvina75");

		// 2. Construct above post's content and then send a POST request for
		// authentication
	//	http.sendPost(url, postParams);

		// 3. success then go to gmail.
		
	  }

	  private void sendPost(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestProperty("authority", "www.rohlik.cz");
		conn.setRequestMethod("POST");		
		conn.setRequestProperty("path", "/services/frontend-service/login");
		conn.setRequestProperty("scheme", "https");
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("accept-encoding", "gzip, deflate, br");		
		conn.setRequestProperty("accept-language", "cs-CZ,cs;q=0.9,en;q=0.8");
		conn.setRequestProperty("content-length", "62");
		conn.setRequestProperty("content-type", "application/json");
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.addRequestProperty("origin", "https://www.rohlik.cz");
		conn.addRequestProperty("referer", "https://www.rohlik.cz/");
		conn.setRequestProperty("user-agent", USER_AGENT);
		conn.setRequestProperty("x-origin", "WEB");
				
		

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);
		
		System.out.println(conn.getContentEncoding());
		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(new BrotliInputStream(conn.getInputStream())));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
			response.append(inputLine);
		}
		in.close();
		 System.out.println(response.toString());

	  }

	  private String GetPageContent(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser		
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		conn.setRequestProperty("Accept-Language", "cs-CZ,cs;q=0.9,en;q=0.8");
		conn.setRequestProperty("Connection", "keep-alive");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		conn.setRequestProperty("Host", "www.rohlik.cz");
		conn.setRequestProperty("Referer", "https://www.rohlik.cz/");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("X-Origin", "WEB");
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
System.out.println(conn.getContentEncoding());
		BufferedReader in = 
	            new BufferedReader(new InputStreamReader(new BrotliInputStream(conn.getInputStream())));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		//	System.out.println(inputLine);
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

	  }

	  public String getFormParams(String html, String username, String password)
			throws IOException {

		System.out.println("Extracting form's data...");
		String url1 ="https://www.rohlik.cz";
		webclient = new WebClient(BrowserVersion.CHROME);
		webclient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webclient.getPage("https://www.rohlik.cz");
page.getElementById("headerLogin").click();
	//	 System.out.println(page.asXml()) ;  
		Document doc = Jsoup.parse(page.asXml());
	//	System.out.println(doc);
				//Jsoup.parse(html);

		// Google form id
		Elements loginform = doc.getElementsByTag("input");
				System.out.println(loginform);
		
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : loginform) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");

			if (key.equals("email"))
				value = username;
			else if (key.equals("password"))
				value = password;
						
			if(!key.equals("search"))
			paramList.add(key + "=" + value);
			
		}

		// build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	  }

	  public List<String> getCookies() {
		return cookies;
	  }

	  public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	  }
}
