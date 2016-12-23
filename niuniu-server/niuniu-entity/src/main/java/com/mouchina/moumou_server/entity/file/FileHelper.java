package com.mouchina.moumou_server.entity.file;

public enum FileHelper {

	FILE_SEPARATE("/","文件路径分隔符"),
	FILE_DOT(".","点分隔符"),
	FILE_ROOT("/moumou/resource","文件存放根路径"),
	PIC_BASE_URL("https://res3.mouchina.com","网络请求根地址"),
	PIC_PHOTO("/image","用户图片路径");
	
	private String marker;
	
	private String display;

	private FileHelper(String marker, String display) {
		this.marker = marker;
		this.display = display;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
}
