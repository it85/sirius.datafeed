package model;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import contract.IMarketFeed;
import global.Builder;

public class MarketFeed implements IMarketFeed{

	private String symbol;
	private long refreshRate;
	private YahooQuoteGenerator quoteGen;
	
	public MarketFeed(String symbol, long refreshRate){
		this.symbol = symbol;
		this.refreshRate = refreshRate;

		this.quoteGen = new YahooQuoteGenerator(symbol);		
	}

	@Override
	public CandleStick getQuote() throws IOException {
		
		Deque<RealTimeQuote> quotes = getRealTimeQuotes();
		return Builder.createCandleStick(quotes);
	}
	
	private Deque<RealTimeQuote> getRealTimeQuotes(){
		
		Deque<RealTimeQuote> quotes = new ArrayDeque<RealTimeQuote>();
		
		long elapsedTime = 0;
		
		while(elapsedTime < this.refreshRate){
			long t1 = System.nanoTime();
			
			RealTimeQuote rtq = quoteGen.getQuote();			
			quotes.add(rtq);
			long t2 = System.nanoTime();
			
			long elapsed = t2-t1;
			
			long elapsedSeconds = elapsed/1000000000;
			elapsedTime += elapsedSeconds;
		}
		
		return quotes;
		
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public long getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(long refreshRate) {
		this.refreshRate = refreshRate;
	}
}
