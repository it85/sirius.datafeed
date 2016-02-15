package contract;

import java.io.FileNotFoundException;
import java.util.List;

import model.CandleStick;

public interface IMarketFeed {
	
	List<CandleStick> staticFeedAsList(String filePath) throws FileNotFoundException;
//	CandleStick liveFeed

}
