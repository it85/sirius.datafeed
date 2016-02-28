package contract;

import java.io.IOException;

public interface IMarketFeed {
	
	double getQuote() throws IOException;

}
