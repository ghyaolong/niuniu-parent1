package com.mouchina.moumou_server.entity.system;

import java.io.Serializable;

/**
 * 区域实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AreaData implements Serializable {

	private String areaCode; //区域code
	
	private String areaName; //区域name

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
}
