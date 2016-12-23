package com.mouchina.base.utils.map;

/**
 * baidu纠偏算法，需要先把gps坐标做google纠偏后，再做此纠偏
 * @author Administrator
 *
 */
public class BaiduGPSCorrect {
	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	
	public static double[] bd_encrypt(double gg_lat, double gg_lon){
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi); 
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		double[] tmp = new double[2];
		tmp[0] = bd_lat;
		tmp[1] = bd_lon;
		return tmp;
	}
	
}
