package com.moumou.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.mouchina.moumou_server_dubbo.provider.award.TreasureAwardHistoryServiceSupport;
import com.mouchina.moumou_server_interface.award.TreasureAwardHistoryService;

public class TestTime {

	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void testTime() throws ParseException {
		Date currentTime = new Date();
		System.out.println();
		System.out.println(sdf.parse(sdf.format(currentTime)));
	}
	
	@Test
	public void test0Time() throws ParseException {
		
		System.out.println(sdf.parse("00:00:00"));
	}
	
	@Test
	public void testOpenTime() throws ParseException {
		TreasureAwardHistoryServiceSupport treasureAwardHistoryServiceSupport = new TreasureAwardHistoryServiceSupport();
		treasureAwardHistoryServiceSupport.openAwardBox(sdf.parse("09:16:00"), null);
	}
	
	@Test
	public void test03() {
		//Date getAwardDate = 
		//Date currentDate = new Date();
	}
	
}
