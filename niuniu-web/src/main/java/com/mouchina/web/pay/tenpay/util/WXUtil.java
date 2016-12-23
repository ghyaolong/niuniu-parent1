package com.mouchina.web.pay.tenpay.util;

import java.util.Random;

/**
 * WXUtil
 * @author ourufeng
 *
 */
public class WXUtil {
	
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
