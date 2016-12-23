package com.mouchina.base.utils;

import java.text.NumberFormat;

public class NumberUtil {

	/**
	 * 返回随机数0.03~0.1(首页红包随机数用)
	 * @return
	 */
	public static double getHomePageRandomNumber(){
		double a = ((7 * Math.random()) + 3)/100;
		return a;
	}
	
	/**
	 * 返回随机数0.5~1.3(普通红包随机数用)
	 * @return
	 */
	public static double getCommonAdvertRandomNumber(){
		double a = ((Math.random() * 8) + 5)/10;
		return a;
	}
	
	/**
	 * 将double数字转换成百分数显示  0.2345 -> 23.45%
	 * @param decimal
	 * @return
	 */
	public static String formattedDecimalToPercentage(double decimal)  
    {  
        //获取格式化对象  
        NumberFormat nt = NumberFormat.getPercentInstance();  
        //设置百分数精确度2即保留两位小数  
        nt.setMinimumFractionDigits(2);  
        return nt.format(decimal);  
    }  
}
