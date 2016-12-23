package com.mouchina.moumou_server_interface.view;

import java.util.Map;

/**
 *广告数据
 *@author larry
 * 2015年6月19日下午5:56:10
 */

public class AdvertResult<T> extends Result<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4016673720515430794L;
	
	private Map<String,Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	} 

}
