package com.mouchina.web.base.utils;

import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.StringUtil;

/**
 * app签名工具
 * 
 * @author larry
 * 
 */
public class AppSginUtils {

	static Logger logger = LogManager.getLogger();

	
	public static boolean commonCheckSign(Map map, String appKey, String sgin) {

		boolean flag = false;
		// Map map = new HashedMap();
		//
		// map.put("appid", "11002");
		// map.put("ua", "2");
		// map.put("uadetail", "iPad4,4+9.200000");
		// map.put("udid", "F365614D-9A32-418B-9846-89F98884B555");
		// map.put("t", "1893283868632");
		// map.put("loginKey", "FA8D192328289398JJKUOOO_01");
		// map.put("phone", "1347568368");
		// map.put("orderNo", "20151347568368");
		//
		// map.put("paySource", "1");
		// map.put("payChannel", "1");

		Iterator<Map.Entry> entries = map.entrySet().iterator();

		String sginString = "";

		while (entries.hasNext()) {

			Map.Entry entry = entries.next();

			sginString += entry.getKey() + "=" + entry.getValue() + "&";

		}
		sginString += appKey;

		logger.debug("sginString ="+sginString);
		String checkSignString = MD5Util.md5Hex(sginString);
		
		logger.debug("checkSignString ="+checkSignString+",sgin="+sgin);

		if (StringUtil.isNotEmpty(checkSignString)
				&& checkSignString.equalsIgnoreCase(sgin)) {
			flag = true;

		}

		return flag;

	}
}
