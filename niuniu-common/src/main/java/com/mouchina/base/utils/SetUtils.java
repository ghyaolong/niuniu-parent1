package com.mouchina.base.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 类说明
 * 
 * @author larry 2015年7月9日下午5:13:30
 */
public class SetUtils {
	public static String setToString(final Set set) {
		String result = "";
		Iterator setIterator = set.iterator();

		while (setIterator.hasNext()) { // 遍历
			result += (String.valueOf(setIterator.next()) + ",");
		}

		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}

	public static Set stringToSet(String str) {
		Set result = new HashSet();
		for (String s : str.split(",")) {
			result.add(s);
			
		}
		return result;
	}

	public static void main(String[] args) {
		Set<Long> sets = new HashSet<Long>();
		sets.add((long) 1232);
		sets.add((long) 290239023);
		System.out.println("setToString=" + setToString(sets));

		Set<String> setsss = new HashSet<String>();
		setsss.add("23232323");
		setsss.add("setsss" + 290239023);
		System.out.println("setToString=" + setToString(setsss));
	}
}
