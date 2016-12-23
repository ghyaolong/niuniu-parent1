package com.mouchina.base.utils.map;

/**
 * GeoUtils类提供计算两个坐标点间距离 
 * @author Administrator
 *
 */
public class GeoUtils {

	//地球半径
	private static final double EARTHRADIUS = 6370996.81; 
	
	/**
	 * 将v值限定在a,b之间，经度使用
	 * @param v
	 * @param a
	 * @param b
	 * @return
	 */
	private static double _getLoop(Double v, Integer a, Integer b) {
		while (v > b) {
			v -= b - a;
		}
		while (v < a) {
			v += b - a;
		}
		return v;
	}
	
	/**
	 * 将v值限定在a,b之间，纬度使用
	 * @param v
	 * @param a
	 * @param b
	 * @return
	 */
	private static double _getRange(Double v, Integer a, Integer b) {
		if (a != null) {
			v = Math.max(v, a);
		}
		if (b != null) {
			v = Math.min(v, b);
		}
		return v;
	}
    
	/**
	 * 将度转化为弧度
	 * @param degree 度
	 * @return
	 */
	private static double degreeToRad(Double degree){
		return Math.PI * degree/180;
	}
	
	/**
	 * 计算两个坐标点间距离，具体到米
	 * @param pointOne
	 * @param pointTwo
	 * @return -1代表有空值传入
	 */
	public static double getDistance(Point pointOne, Point pointTwo){
		
		//判断类型
		if(!(pointOne instanceof Point) ||
		!(pointTwo instanceof Point)){
		   return -1;
		}

		pointOne.setLongitude(_getLoop(pointOne.getLongitude(), -180, 180));
		pointOne.setLatitude(_getRange(pointOne.getLatitude(), -74, 74));
		pointTwo.setLongitude(_getLoop(pointTwo.getLongitude(), -180, 180));
		pointTwo.setLatitude( _getRange(pointTwo.getLatitude(), -74, 74));
        
		double x1, x2, y1, y2;
		x1 = degreeToRad(pointOne.getLongitude());
		y1 = degreeToRad(pointOne.getLatitude());
		x2 = degreeToRad(pointTwo.getLongitude());
		y2 = degreeToRad(pointTwo.getLatitude());

		return EARTHRADIUS * Math.acos((Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1)));   
	}
	
}
