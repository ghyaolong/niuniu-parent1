package com.mouchina.moumou_server.entity.advert;

/**
 * 接力福袋帮助枚举类
 * @author Administrator
 *
 */
public enum RelayLuckyBagHelper {

	OPEN_STATE_0((double)0, "开启失败"),
	OPEN_STATE_1((double)1, "开启成功"),
	
	OPEN_PARAMETER_1(0.0,"开启参数一"),
	OPEN_PARAMETER_2(0.3,"开启参数二"),
	OPEN_PARAMETER_3(1.0,"开启参数三"),
	OPEN_PARAMETER_4(3.0,"开启参数四"),
	OPEN_PARAMETER_5(6.0,"开启参数五"),
	OPEN_PARAMETER_6(10.0,"开启参数六");
	
	private Double marker;
	
	private String display;

	private RelayLuckyBagHelper(Double marker, String display) {
		this.marker = marker;
		this.display = display;
	}

	public Double getMarker() {
		return marker;
	}

	public void setMarker(Double marker) {
		this.marker = marker;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
}
