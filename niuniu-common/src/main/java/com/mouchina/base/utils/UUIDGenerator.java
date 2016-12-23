package com.mouchina.base.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.HashedMap;

import com.mouchina.base.redis.RedisHelper;

public class UUIDGenerator {

	public UUIDGenerator() {
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUIDs(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

	public static Long userIdGenerator(RedisHelper redisHelper) {
		/***
		 * 用户全局唯一idKey
		 */
		String GLOBLLYUNIQUE_USERID_KEY = "globllUnique_userid_key";
		int baseKey = 1000000000;

		Long sequence = redisHelper.getIncr(GLOBLLYUNIQUE_USERID_KEY);

		Long userId = baseKey + sequence;

		return userId;
	}

	public static Long badyIdGenerator(RedisHelper redisHelper) {
		/***
		 * 用户全局唯一idKey
		 */
		String GLOBLLYUNIQUE_BADYID_KEY = "globllUnique_badyid_key";
		int baseKey = 1000000000;
		Long sequence = redisHelper.getIncr(GLOBLLYUNIQUE_BADYID_KEY);
		Long userId = baseKey + sequence;

		return userId;
	}

	public static String orderNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getNowDateStringNo();

		Long sequence = redisHelper.getIncr("ORDER_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static String orderDetailNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getDatailsNowDateStringNo();

		Long sequence = redisHelper.getIncr("ORDER_DETAILS_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static String payOrderNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getPayNowDateStringNo();

		Long sequence = redisHelper.getIncr("PAY_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static String teacherCashFlowNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getNowDateStringNo();

		Long sequence = redisHelper.getIncr("TEACHER_CASH_FLOW_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static String userReimburseNoGenerator(RedisHelper redisHelper) {
		String reimburseNo = "";
		String key = DateUtils.getNowDateStringNo();

		Long sequence = redisHelper.getIncr("USER_REIMBURSE_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		reimburseNo = key + mid + sequence;

		return reimburseNo;
	}

	public static String reimburseNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getNowDateStringNo();

		Long sequence = redisHelper.getIncr("REIMBURSE_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static String userCashFlowNoGenerator(RedisHelper redisHelper) {
		String orderNo = "";
		String key = DateUtils.getNowDateStringNo();

		Long sequence = redisHelper.getIncr("USER_CASH_FLOW_NO" + key);

		int sequenceLength = 8 - sequence.toString().length();
		String mid = "";
		switch (sequenceLength) {
		case 1:
			mid = "0";
			break;
		case 2:
			mid = "00";
			break;
		case 3:
			mid = "000";
			break;
		case 4:
			mid = "0000";
			break;
		case 5:
			mid = "00000";
			break;
		case 6:
			mid = "000000";
			break;
		case 7:
			mid = "0000000";
			break;
		case 8:
			mid = "00000000";
			break;
		default:
			break;
		}
		orderNo = key + mid + sequence;

		return orderNo;
	}

	public static void main(String arg[]) {
		for (int i = 0; i < 1000; i++)
			System.out.println(getUUID());
		// String regex = "^http://127.0.0.1:8080/weduty/manage/industry/.*";
		//
		// String
		// uri="http://127.0.0.1:8080/weduty/manage/industry/myassistant/head/viewprekeyword.json";
		//
		// boolean res = uri.matches(regex);
		// System.out.println("res:"+res);

		Map map = new HashedMap();

		map.put("appid", "11002");
		map.put("ua", "2");
		map.put("uadetail", "iPad4,4+9.200000");
		map.put("udid", "F365614D-9A32-418B-9846-89F98884B555");
		map.put("t", "1893283868632");
		map.put("loginKey", "FA8D192328289398JJKUOOO_01");
		map.put("phone", "1347568368");
		map.put("orderNo", "20151347568368");

		map.put("paySource", "1");
		map.put("payChannel", "1");

		Iterator<Map.Entry> entries = map.entrySet().iterator();

		String singString = "";

		while (entries.hasNext()) {

			Map.Entry entry = entries.next();

			singString += entry.getKey() + "=" + entry.getValue() + "&";

		}
		singString += "bbb981f42b0d4f629dc5d3612b86acde";

		System.out.println(singString);
		System.out.println(MD5Util.md5Hex(singString));
	}

}
