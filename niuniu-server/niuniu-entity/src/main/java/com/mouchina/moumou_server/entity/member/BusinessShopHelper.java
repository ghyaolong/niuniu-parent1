package com.mouchina.moumou_server.entity.member;

/**
 * 商户店铺信息帮助枚举类
 * @author Administrator
 *
 */
public enum BusinessShopHelper {

	AUDIT_STATE_0(0,"待审核"),
	AUDIT_STATE_1(1,"审核通过"),
	AUDIT_STATE_2(2,"审核未通过"),
	
	RED_ENVELOPE_TYPE_1(1,"拼手气红包"),
	
	RED_ENVELOPE_COUNT_0(0,"福袋数量"),
	
	ADVERT_TYPE_5(5,"首页广告"),
	
	STATE_1(1,"正确状态"),
	STATE_2(2,"错误状态"),
	
	SEPARATOR_1(1,";"),
	
	ADVERT_MIN_AMOUNT(10000,"首页广告最低竞价(单位:分)");
	
	private Integer marker;
	
	private String display;

	private BusinessShopHelper(Integer marker, String display) {
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
