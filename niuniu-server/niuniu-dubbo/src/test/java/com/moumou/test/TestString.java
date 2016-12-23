package com.moumou.test;

import org.junit.Test;

public class TestString {

	@Test
	public void test01() {
		String a = "0,1567,2566";
		String[] cs = a.split(",|~");
		for(String c:cs) {
			System.out.println(c);
		}
	}
}
