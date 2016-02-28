package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import contract.IMarketFeed;

public class MarketFeed implements IMarketFeed{

	private String symbol;
	
	public MarketFeed(String symbol){
		this.symbol = symbol;
	}
	
	public static void main(String[] args){
		MarketFeed feed = new MarketFeed("SPY");
		
		try {
			double d = feed.getQuote();
			double x = d;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getQuote() throws IOException {
		
		return Double.parseDouble(this.getLastTradePrice(this.getQuoteAsJson()));
		
	}
	
	private String getLastTradePrice(String json){
		
		String[] array = json.split(",");
		
		for(String val : array){
			
			if(val.contains("LastTradePriceOnly\":")){
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
		System.out.println("============Output:============");
		
		while ((output = br.readLine()) != null) {
			finalString = finalString + output;
		}
		
		return finalString;
	}
	
	private String getQueryString(){
		String url1 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22";
		String symbol = this.symbol;
		String url2 = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		
		return url1 + symbol + url2;
	}

}
