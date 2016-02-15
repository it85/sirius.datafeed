package contract;

import java.util.List;

import model.CandleStick;

public interface IMarketFeed {
	
	List<CandleStick> feedAsList(String filePath);

}
