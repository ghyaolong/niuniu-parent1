package com.mouchina.moumou_server_interface.view;

public enum ResultHelper {

	STATE_0(0,"无查询结果"),
	STATE_1(1,"有查询结果");
	
	private Integer marker;
	
	private String display;

	private ResultHelper(Integer marker, String display) {
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
