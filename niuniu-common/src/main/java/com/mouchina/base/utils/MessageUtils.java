package com.mouchina.base.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageUtils {
	private static Logger logger = LogManager.getLogger("FileUtil");

	public static Integer sendSms(String smsUrl, String phone, String content)
			throws IOException {

		Integer result = 0;
		String response = null;
		try {

			logger.debug("----------------------------------sendSms requstUrl="
					+ smsUrl+",phone="+phone+",content="+content);
			Map<String, String> params=new HashMap<String,String>();
			params.put("phone", phone);
			params.put("content", content);
			response = HttpUtil.executePost(smsUrl, params);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(response);
//		Map json = JsonUtils.jsonToObject(response, HashMap.class);
//		if (json != null && json.containsKey("result"))
//
//			result = (Integer) json.get("result");

		// ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");

		return result;
	}

	public static Integer sendPrivateMessage(String smsUrl,
			Long userId, String tableNum,Integer  targetType,String content) throws IOException {

		Integer result = 0;
		String response = null;
		try {
			
			logger.debug("----------------------------------sendSms requstUrl="
					+ smsUrl + "? userid="
					+ userId + "&tablenum=" + tableNum + "&targetType="+targetType+"&content="
					+ content);
			Map<String, String> params=new HashMap<String,String>();
			params.put("content", content);
			params.put("content", content);
			params.put("tablenum", tableNum);
			params.put("targettype", targetType+"");
			params.put("userid", userId+"");
			response = HttpUtil.executePostNoRequest(smsUrl, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(response);
//		Map json = JsonUtils.jsonToObject(response, HashMap.class);
//		if (json != null && json.containsKey("result"))
//
//			result = (Integer) json.get("result");

		// ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");

		return result;
	}

	
	
	public static Integer sendUserPush(String userPushUrl,
			String  platform, String alias,String  title,String message,String tagValue,String tagAndValue,String sound,String extras) throws IOException {

		Integer result = 0;
		String response = null;
		try {
			
			logger.debug("----------------------------------userPushUrl requstUrl="
					+ userPushUrl + "? platform="
					+ platform + "&alias=" + alias + "&title="+title+"&message="
					+ message+",tagValue="+tagValue+",tagAndValue="+tagAndValue+",sound="+sound+",extras="+extras);
			Map<String, String> params=new HashMap<String,String>();
			params.put("platform", platform);
			params.put("alias", alias);
			params.put("title", title);
			params.put("message", message);
			params.put("tagValue", tagValue);
			params.put("tagAndValue", tagAndValue);
			params.put("sound", sound);
			params.put("extras", extras);
			response = HttpUtil.executePostNoRequest(userPushUrl, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(response);
		return result;
	}
	public static Integer sendTeacherPush(String teacherPushUrl,
			String  platform, String alias,String  title,String message,String tagValue,String tagAndValue,String sound,String extras) throws IOException {

		Integer result = 0;
		String response = null;
		try {
			
			logger.debug("----------------------------------teacherPushUrl requstUrl="
					+ teacherPushUrl + "? platform="
					+ platform + "&alias=" + alias + "&title="+title+"&message="
					+ message+",tagValue="+tagValue+",tagAndValue="+tagAndValue+",sound="+sound+",extras="+extras);
			Map<String, String> params=new HashMap<String,String>();
			params.put("platform", platform);
			params.put("alias", alias);
			params.put("title", title);
			params.put("message", message);
			params.put("tagValue", tagValue);
			params.put("tagAndValue", tagAndValue);
			params.put("sound", sound);
			params.put("extras", extras);
			response = HttpUtil.executePostNoRequest(teacherPushUrl, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(response);
		return result;
	}

	public static Integer sendPoublicMessage(String smsUrl, String phone,
			String content, int state) throws IOException {

		Integer result = 0;
		String response = null;
		try {
			logger.debug("----------------------------------sendSms requstUrl="
					+ "?phone=" + phone + "&content="
					+ content+ "&state="
					+ state);
			Map<String, String> params=new HashMap<String,String>();
			params.put("content", content);
			params.put("phone", phone);
			params.put("content", content);
			response = HttpUtil.executePostNoRequest(smsUrl, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(response);
//		Map json = JsonUtils.jsonToObject(response, HashMap.class);
//		if (json != null && json.containsKey("result"))
//
//			result = (Integer) json.get("result");

		// ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");

		return result;
	}

}
