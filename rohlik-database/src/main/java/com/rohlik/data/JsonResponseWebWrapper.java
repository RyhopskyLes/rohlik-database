package com.rohlik.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class JsonResponseWebWrapper extends WebConnectionWrapper{

    public JsonResponseWebWrapper(WebClient webClient){
    	
        super(webClient);           
    }
    final List<String> list = new ArrayList<>();
    final List<String> responseList = new ArrayList<>();
    String jsonResponse;

    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {;
        WebResponse response = super.getResponse(request);
        if(request.equals("https://www.rohlik.cz/services/frontend-service/products/300101000?offset=0&limit=25")) jsonResponse = response.getContentAsString();
        list.add(request.getHttpMethod() + " " + request.getUrl());
        if(response.getStatusCode()!=200) responseList.add("status code "+response.getStatusCode()+"\r\n"+response.getResponseHeaders()+"\r\n"
        +response.getContentAsString());
        //extract JSON from response
        
        return response;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }
    
    public List<String> getRequestsList() {
    	return list;
    }
    public List<String> getResponseDetailsList() {
    	return responseList;
    }
}
