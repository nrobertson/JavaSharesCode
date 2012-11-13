package com.Team2.Tests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.example.shares.*;

public class ShareTest {

	static final String MONDAY = "2012-11-05"; 
	private Share share;
	
	
	@Before
	public void setUp() throws Exception {		
		YahooFinanceAPI.stockData = new String[5][6];
		YahooFinanceAPI.historicData = new String[5][1][3];
		share = new Share(1, YahooFinanceAPI.StockSymbol.BP, "BP");

	}

	@Test
	public void testSetCalendarToLastMonday() {
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Share.setCalendarToLastMonday(Calendar.getInstance());
		
		cal.set(2012, Calendar.NOVEMBER, 5);
		
		//assertEquals(cal2, cal);
		assertEquals(cal2.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
	}
	
	@Test
	public void testPositiveGetWeekPercentChange() {
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "428", MONDAY, "400");
		
		int rounded1=7;
		try {
			rounded1 = Math.round(share.getWeekPercentChange());
		} catch (DataUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(rounded1, 7);
	}
	
	@Test
	public void testNegativeGetWeekPercentChange() {		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "379", MONDAY, "400");
		
		int rounded2=-5;
		try {
			rounded2 = Math.round(share.getWeekPercentChange());
		} catch (DataUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(rounded2, -5);
	}
	
	@Test
	public void testZeroGetWeekPercentChange() {		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "400", MONDAY, "400");
		
		int rounded3=0;
		try {
			rounded3 = Math.round(share.getWeekPercentChange());
		} catch (DataUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(rounded3, 0);
	}
	
	@Test (expected = DataUnavailableException.class)
	public void testNoConnection() throws DataUnavailableException 
	{
		//Don't populate API with data to simulate no data
		share.getWeekPercentChange();
	}
	
	//@Test (expected = DataUnavailableException.class)
	//public void testZeroMondayPrice() throws DataUnavailableException
	//{
	//	populateApiData(YahooFinanceAPI.StockSymbol.BP, "1", MONDAY ,"0");
	//	share.getWeekPercentChange();
	//}
	
	
	
	public static void populateApiData(YahooFinanceAPI.StockSymbol symbol, String price, String date, String dateOpenPrice)
	{
		YahooFinanceAPI.stockData[symbol.ordinal()][YahooFinanceAPI.DataType.ASK.ordinal()] = price;
		YahooFinanceAPI.stockData[symbol.ordinal()][YahooFinanceAPI.DataType.BID.ordinal()] = price;
		YahooFinanceAPI.historicData[symbol.ordinal()][0][YahooFinanceAPI.HistoricDataType.DATE.ordinal()] = date;
		YahooFinanceAPI.historicData[symbol.ordinal()][0][YahooFinanceAPI.HistoricDataType.OPEN.ordinal()] = dateOpenPrice;
	}
	
}
