package com.mouchina.base.utils;

import org.apache.commons.lang.StringUtils;

public final class UtilMaskCode {
	/**
	 * 是否掩码
	 */
	private static boolean maskCode = true;
	public static String value(String value){
		if(maskCode == false){return value;}
		
		if(StringUtils.isBlank(value)){return "";}
	
		if(value.length() >= 2){
			return value.replaceAll("(.{1}).{0,}", "$1**");
		}
		return value;
	}
	
	/**
	 * 掩码：手机号码 & 座机
	 * @param value
	 * @return
	 */
	public static String telephone(String value){
		if(maskCode == false){return value;}
		
		if(StringUtils.isBlank(value)){return "";}
		if(value.length() >= 7 && value.length() <= 11){
			return value.replaceAll("(.{3}).{0,}(.{4})", "$1****$2");
		}
		return value;
	}
}
