package com.moumou.test;

import java.util.Map;

import org.junit.Test;

import com.mouchina.moumou_server_dubbo.provider.award.SignHistoryServiceSupport;

public class TestByteString {
	
	@Test
	public void test01() {
		SignHistoryServiceSupport signHistoryServiceSupport = new SignHistoryServiceSupport();
		
		System.out.println(signHistoryServiceSupport.byteToChar((byte) 1));
	}
	
	@Test
	public void test02() {
		SignHistoryServiceSupport signHistoryServiceSupport = new SignHistoryServiceSupport();
		Map<String,String> map = signHistoryServiceSupport.charToMap("1-0-1-1-0", "2-3-4-8-9");
		System.out.println(map.toString());
	}
}
