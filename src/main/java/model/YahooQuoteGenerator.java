package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import contract.QuoteGenerator;
import global.Builder;

public class YahooQuoteGenerator implements QuoteGenerator{
	
	String symbol;
	
	public YahooQuoteGenerator(String symbol){
		this.symbol = symbol;
	}
	
	public RealTimeQuote getQuote(){
		try {
			String json = getQuoteAsJson();
			
			String[] arr = new String[2];
			arr[0] = getLastTradePrice(json);
			arr[1] = getVolume(json);
			
			return Builder.createRealTimeQuote(arr);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getLastTradePrice(String json){
		
		String[] array1 = json.split("quote\":");		
		String[] array = array1[1].split(",");
		
		for(String val : array){
			
			if(val.contains("LastTradePriceOnly\":")){
				String[] priceArray = val.split("\"");
				return priceArray[3];
			}			
		}
		
		return null;
	}
	
	private String getVolume(String json){
		
		String[] array1 = json.split("quote\":");		
		String[] array = array1[1].split(",");
		
		for(String val : array){
			
			if(val.contains("Volume\":")){
				String[] priceArray = val.split("\"");
				return priceArray[3];
			}			
		}
		
		return null;
	}
	
	private String getQuoteAsJson() throws IOException{
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		String url = this.getQueryString();
		
		HttpGet getRequest = new HttpGet(url);
		
		getRequest.addHeader("accept", "application/xml");
		
		HttpResponse response = httpClient.execute(getRequest);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		
		
		String finalString = "";
		
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String output;
		
		while ((output = br.readLine()) != null) {
			finalString = finalString + output;
		}
		
		return finalString;
	}
	
	private String getQueryString(){
				
		String url1 = "https://query.yahooapis.com/v1/public/yql?q=select%20";
		String columns = "LastTradePriceOnly,Volume";		
		String url2 = "%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22";
		String symbol = this.symbol;
		String url3 = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		
		return url1 + columns + url2 + symbol + url3;
	}

}
