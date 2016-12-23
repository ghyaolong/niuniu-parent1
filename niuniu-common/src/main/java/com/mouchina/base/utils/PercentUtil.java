package com.mouchina.base.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PercentUtil {
	
	private static Logger logger = LogManager.getLogger(PercentUtil.class);
	
	/**
     * 根据Math.random()产生一个double型的随机数
     * @param percents
     * @return  Map.get("index"):被选中百分数的索引    Map.get("percent"):被选中百分数的值
     */
    public static Map<String,Object> getRandomPercent(List<Double> percents) {
        DecimalFormat df = new DecimalFormat("######0.00");
        
        Map<String,Object> map=new HashMap<String,Object>(); 
        
        int random = -1;
        try{
            //计算总权重
            double sumWeight = 0;
            for(Double p : percents){
                sumWeight += p;
            }

            //产生随机数
            double randomNumber;
            randomNumber = Math.random();
            
            logger.info("randomNumber:"+randomNumber);

            //根据随机数在所有分布的区域并确定被选中百分数
            double d1 = 0;
            double d2 = 0;          
            for(int i=0;i<percents.size();i++){
                d2 += Double.parseDouble(String.valueOf(percents.get(i)))/sumWeight;
                if(i==0){
                    d1 = 0;
                }else{
                    d1 +=Double.parseDouble(String.valueOf(percents.get(i-1)))/sumWeight;
                }
                if(randomNumber >= d1 && randomNumber <= d2){
                    random = i;
                    map.put("index", i);
                    map.put("percent", percents.get(i));
                    logger.info(map.get("percent"));
                    break;
                }
            }
        }catch(Exception e){
        	logger.error("生成随机数出错，出错原因：" +e.getMessage());
        }
        return map;
    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Double> list = new ArrayList<Double>();
		list.add(0.1);
		list.add(0.2);
		list.add(0.3);
		list.add(0.4);
		list.add(0.5);
		Map maps = getRandomPercent(list);
		System.out.println(maps);
//		System.exit(0);
	}

}
