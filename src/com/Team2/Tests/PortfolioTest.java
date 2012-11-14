package com.Team2.Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.shares.Portfolio;
import com.example.shares.YahooFinanceAPI;

public class PortfolioTest {
	
	static final String MONDAY = "2012-11-12"; 

	@Before
	public void setUp() throws Exception {
		YahooFinanceAPI.stockData = new String[5][6];
		YahooFinanceAPI.historicData = new String[5][1][3];
		Portfolio.addShares();
	}

	@Test
	public void testGetPercentageChange() {
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.BP, "400", MONDAY, "400");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "400", MONDAY, "400");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.HSBA, "440", MONDAY, "400");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.SN, "400", MONDAY, "400");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.MKS, "400", MONDAY, "800");
		
		String[] change = Portfolio.getPercentageChange();
		
		assertEquals(change[0], "BP: 0%");
		assertEquals(change[1], "Experian: 0%");
		assertEquals(change[2], "HSBC: 10%");
		assertEquals(change[3], "M&S: -50%");
		assertEquals(change[4], "S&N: 0%");
	}
	
	@Test
	public void testGetInvalidPercentageChange() {
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.BP, "400", MONDAY, "0");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.EXPN, "0", MONDAY, "400");
		ShareTest.populateApiData(YahooFinanceAPI.StockSymbol.HSBA, "-1", MONDAY, "400");
		
		String[] change = Portfolio.getPercentageChange();
		
		assertEquals(change[0], "BP: N/A for week");
		assertEquals(change[1], "Experian: unavailable");
		assertEquals(change[2], "HSBC: unavailable");
	}
	
	@After
	public void teardown()
	{
		YahooFinanceAPI.stockData = null;
		YahooFinanceAPI.historicData = null;
	}
	

}
