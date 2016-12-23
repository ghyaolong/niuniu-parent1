package com.mouchina.base.exceptions;

public interface IException {
	/**
	 * 获取code类型
	 * @return true 异常、false 错误
	 */
	boolean type();
	/**
	 * code
	 * @return
	 */
	String code();
	
	/**
	 * 若 code 不存在,则默认
	 * @param defaultCode
	 * @return
	 */
	String code(String defaultCode);
	
	/**
	 * 消息
	 * @return
	 */
	String getMessage();
}
