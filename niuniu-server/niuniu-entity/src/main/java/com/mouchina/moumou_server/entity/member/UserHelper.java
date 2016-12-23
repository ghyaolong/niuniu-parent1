package com.mouchina.moumou_server.entity.member;

/**
 * 用户枚举类
 * @author Administrator
 *
 */
public enum UserHelper {
	
	USER_STATE_1(1,"正常用户"),
	USER_STATE_2(2,"封停用户"),
	
	USER_IDENTITY_1(1,"个人用户"),
	USER_IDENTITY_2(2,"商户用户"),
	USER_IDENTITY_3(3,"商户店铺用户");
	
	private Integer marker;
	private String display;

	private UserHelper(Integer marker, String display) {
		this.marker = marker;
		this.display = display;
	}

	public Integer getMarker() {
		return marker;
	}

	public void setMarker(Integer marker) {
		this.marker = marker;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
