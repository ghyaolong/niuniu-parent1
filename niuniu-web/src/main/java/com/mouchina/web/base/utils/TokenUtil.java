package com.mouchina.web.base.utils;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * Description: TokenUtil
 * Author: ourufeng
 * Update: ourufeng(2015-06-16 17:21)
 */
public final class TokenUtil {

    /**
     * 解析
     *
     * @param loginKey
     * @return
     */
    public static String[] split(String loginKey) {
        return StringUtils.split(loginKey, "_");
    }

    /**
     * 组合
     *
     * @param token
     * @param tableNum
     * @return
     */
    public static String combination(String token, Integer tableNum) {
        return token + "_" + tableNum;
    }
    /**
     * 
     * 判断时间是否为一天
     * 
     * @param date
     * @return
     */
	public static boolean isToday(Date date) { 
        Calendar c1 = Calendar.getInstance();
        Boolean booleans = true;
        if(date != null){
        	c1.setTime(date);                                 
            int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH)+1;
                int day1 = c1.get(Calendar.DAY_OF_MONTH);     
                Calendar c2 = Calendar.getInstance();    
                c2.setTime(new Date());      
                int year2 = c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH)+1;
                int day2 = c2.get(Calendar.DAY_OF_MONTH);   
                if(year1 == year2 && month1 == month2 && day1 == day2){
                	booleans = false;
                }
        }
                 return booleans;               
    }
}
