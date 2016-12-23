package com.mouchina.base.utils.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
 
/** 
* 获取经纬度
* 
*/ 
public class GetLatAndLngByBaidu { 
     
    /** 
    * @param addr 
    * 查询的地址 
    * @return 
    * @throws IOException 
    */ 
    public Double[] getCoordinate(String addr) throws IOException { 
        double lng = 0.0;//经度
        double lat = 0.0;//纬度
        String address = null; 
        try { 
            address = java.net.URLEncoder.encode(addr, "UTF-8"); 
        }catch (UnsupportedEncodingException e1) { 
            e1.printStackTrace(); 
        } 
        String key = "f247cdb592eb43ebac6ccd27f796e2d2"; 
        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key); 
        URL myURL = null; 
        URLConnection httpsConn = null; 
        try { 
            myURL = new URL(url); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } 
        InputStreamReader insr = null;
        BufferedReader br = null;
        try { 
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理 
            if (httpsConn != null) { 
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
                br = new BufferedReader(insr); 
                String data = null; 
                int count = 1;
                while((data= br.readLine())!=null){ 
                    if(count==5){
                        lng = Double.parseDouble((String)data.subSequence(data.indexOf(":")+1, data.indexOf(",")));//经度
                        count++;
                    }else if(count==6){
                        lat = Double.parseDouble(data.substring(data.indexOf(":")+1));//纬度
                        count++;
                    }else{
                        count++;
                    }
                } 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new Double[]{lng,lat}; 
    } 
 
 
    public static void main(String[] args) throws IOException {
        GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
        Double[] o = getLatAndLngByBaidu.getCoordinate("陕西省西安市碑林区张家村街道二环南路西段101号怡兰星空");
        System.out.println(o[0]);//经度
        System.out.println(o[1]);//纬度
        double[] latlng = new double[2];
        GpsCorrect.transform(o[1], o[0], latlng);
        latlng = BaiduGPSCorrect.bd_encrypt(latlng[0], latlng[1]);
        System.out.println(latlng[0]);//纬度
        System.out.println(latlng[1]);//经度
    }
 
}
