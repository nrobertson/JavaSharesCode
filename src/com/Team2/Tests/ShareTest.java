package com.Team2.Tests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.shares.*;

public class ShareTest {

	static final String MONDAY = "2012-11-12"; 
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
		
		cal.set(2012, Calendar.NOVEMBER, 12);
		
		assertEquals(cal2, cal);
		assertEquals(cal2.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
	}
	
	@Test
	public void testPositiveGetWeekPercentChange() {
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "428", MONDAY, "400");
		
		testFunction(7);
	}
	
	@Test
	public void testNegativeGetWeekPercentChange() {		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "379", MONDAY, "400");
		
		testFunction(-5);
	}
	
	@Test
	public void testZeroGetWeekPercentChange() {		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "400", MONDAY, "400");
		
		testFunction(0);
	}
	
	@Test (expected = DataUnavailableException.class)
	public void testNoConnection() throws DataUnavailableException 
	{
		//Don't populate API with data to simulate no data
		share.getWeekPercentChange();
	}
	
	//Test e , testing to check that an exception is thrown when Mondays open is 0
	@Test (expected = DataUnavailableException.class)
	public void testZeroMondayPrice() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "1", MONDAY ,"0");
		share.getWeekPercentChange();
	}
	
	//Test b, testing to check that an exception is thrown when the current price is 0
	@Test (expected = DataUnavailableException.class)
	public void testZeroCurrentPrice() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "0", MONDAY ,"1");
		share.getWeekPercentChange();
	}
	
	//Test j, testing to check that an exception is thrown if there is a loss of more than 100%
	@Test (expected= DataUnavailableException.class)
	public void testFallmorethan100() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "-2", MONDAY, "200");
		share.getWeekPercentChange();
	}
	//Test c, testing to check that an exception is thrown if the current price is a negative value
	@Test (expected= DataUnavailableException.class)
	public void testNegativeCurrentPrice() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "-1", MONDAY, "100");
		share.getWeekPercentChange();
	}
	
	//Test k, testing to check that an exception is thrown if the current price is 0
	@Test (expected= DataUnavailableException.class)
	public void testCurrent0() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "0", MONDAY, "1");
		share.getWeekPercentChange();
	}
	
	//Test f, testing to check that an exception is thrown if the monday price is 0
	@Test (expected= DataUnavailableException.class)
	public void testMonday0value() throws DataUnavailableException
	{
		populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "1", MONDAY, "0");
		share.getWeekPercentChange();
	}
	
	//Test a,d and h. Testing to check that 0% is returned when the current price and the monday price both equal each other
	@Test
	public void testZeroReturnValue(){		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "1", MONDAY, "1");
		
		testFunction(0);
	}
	
	//Test i. Testing to check that a 1% rise returned when the current price is 1% greater than the monday value
	@Test
	public void testRiseReturnValue(){		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "101", MONDAY, "100");
		
		testFunction(1);
	}
	
	//Test g, Testing to check that a 99% fall is returned when the current price is 99% less than the Monday value 
	@Test
	public void testFallReturnValue(){		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "1", MONDAY, "100");
		
		testFunction(-99);
	}
	
	//Test l,Testing to check that a 1% fall is returned when the current price is 1% less than the Monday value 
	@Test
	public void testFallReturnValue1(){		
		populateApiData(YahooFinanceAPI.StockSymbol.BP, "99", MONDAY, "100");
		
		testFunction(-1);
	}
	
	private void testFunction(int expectedValue) {
		int rounded1=0;
		try {
			rounded1 = Math.round(share.getWeekPercentChange());
		} catch (DataUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(rounded1, expectedValue);
	}
	
	public static void populateApiData(YahooFinanceAPI.StockSymbol symbol, String price, String date, String dateOpenPrice)
	{
		YahooFinanceAPI.stockData[symbol.ordinal()][YahooFinanceAPI.DataType.ASK.ordinal()] = price;
		YahooFinanceAPI.stockData[symbol.ordinal()][YahooFinanceAPI.DataType.BID.ordinal()] = price;
		YahooFinanceAPI.historicData[symbol.ordinal()][0][YahooFinanceAPI.HistoricDataType.DATE.ordinal()] = date;
		YahooFinanceAPI.historicData[symbol.ordinal()][0][YahooFinanceAPI.HistoricDataType.OPEN.ordinal()] = dateOpenPrice;
	}
	
	@After
	public void tearDown()
	{
	YahooFinanceAPI.stockData = null;
	YahooFinanceAPI.historicData = null;
	share = null;
	}
	
}
