package com.mouchina.base.enums;

public interface IEnum {
	/**
	 * 本地值
	 * @return
	 */
	String key();
	
	/**
	 * 描述
	 * @return
	 */
	String value();
	
	/**
	 * 获取枚举签名
	 * @return
	 */
	String signature();
}