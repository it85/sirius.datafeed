package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import contract.IMarketFeed;

public class MarketFeed implements IMarketFeed{

	@Override
	public List<CandleStick> staticFeedAsList(String filePath) throws FileNotFoundException {
		
		if(!fileExists(filePath)){
			throw new FileNotFoundException();
		}
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		ArrayList<CandleStick> candleStickList = new ArrayList<CandleStick>();
		
		try {
			
			br = new BufferedReader(new FileReader(filePath));
			line = br.readLine();
				
			while ((line = br.readLine()) != null) {
				String[] arr = line.split(cvsSplitBy);
				CandleStick candle = new CandleStick(arr);
				candleStickList.add(candle);
			}
	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
				br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
		return candleStickList;
	}
	
	private boolean fileExists(String filePath){
		return false;
	}

}
