package com.rohlik.data.commons.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class KosikCategoryMap {
public static Map<String, List<String>>	categories; 
/*= new HashMap<String, List<String>>() {{
    put("https://www.kosik.cz/pekarna-a-cukrarna?do=productList-load", Arrays.asList("L´Chefs", "Coppenrath&Wiese", "Kabátův", "Olz"));
    put("https://www.kosik.cz/deti?do=productList-load", Arrays.asList("L´Chefs", "Coppenrath&Wiese", "Kabátův", "Olz"));
}};*/
public static Map<String, Map<String, String>>	correctedProducers = new HashMap<String, Map<String, String>>() {{
    put("https://www.kosik.cz/pekarna-a-cukrarna?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
        put("PAC ", "PAC Hořovice");
    }});
    put("https://www.kosik.cz/deti?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
    }});
    put("https://www.kosik.cz/mlecne-a-chlazene?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
    }});
    put("https://www.kosik.cz/ovoce-a-zelenina?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
    }});
    put("https://www.kosik.cz/maso-a-ryby?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
    }});
    put("https://www.kosik.cz/uzeniny-a-lahudky?do=productList-load", new HashMap<String, String>() {{
        put("L´Chefs", "L'Chefs");
        put("Coppenrath&Wiese", "Coppenrath&Wiese");
        put("Kabátův", "Pekárna Kabát");
        put("dijo", "Dijo");
        put("Olz", "Ölz");
    }});
}};	
private KosikCategoryMap( ) {
	ObjectMapper mapper = new ObjectMapper();

    // read JSON from a file
    try {
		categories = mapper.readValue(
		        new File("categories.json"),
		        new TypeReference<Map<String, List<String>>>() {
		});
	} catch (IOException e) {
		System.out.println("File not found");
	}	
	
	
}
}
