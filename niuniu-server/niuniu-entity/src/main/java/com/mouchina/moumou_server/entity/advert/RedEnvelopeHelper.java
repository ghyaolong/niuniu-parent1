package com.mouchina.moumou_server.entity.advert;

/**
 * 红包枚举类
 * @author Administrator
 *
 */
public enum RedEnvelopeHelper {

	REDENVELOPE_TYPE_0(0,"红包"),
	REDENVELOPE_TYPE_1(1,"优惠券"),
	REDENVELOPE_TYPE_2(2,"接力福袋"),
	REDENVELOPE_TYPE_3(3,"公益"),
	REDENVELOPE_TYPE_4(4,"首页广告福袋");
	
	private Integer marker;
	private String display;

	private RedEnvelopeHelper(Integer marker, String display) {
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
