package contract;

import java.io.IOException;

import model.CandleStick;

public interface IMarketFeed {
	
	CandleStick getQuote() throws IOException;

}
