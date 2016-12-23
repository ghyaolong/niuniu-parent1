package com.mouchina.base.utils.map;

/**
 * 坐标点实体类
 * @author Administrator
 *
 */
public class Point {

	private Double longitude; //地理经度
	
	private Double latitude; //地理纬度

	public Point(Double longitude, Double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	
}
