package com.mouchina.web.base.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.MD5Util;

/**
 * Description: ApiRequestInterceptor Author: ourufeng Update:
 * zhangkun(2016-10-25 16:22)
 */
@Component
public class ApiRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ApiRequestInterceptor.class);

	private String appIoskey;

	private String appAndroidkey;

	@SuppressWarnings("unused")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String headValue = request.getHeader("User-Agent");
		String host = request.getHeader("Host");
//		logger.info("--------" + headValue + "---------" + host);

		String appversion = request.getParameter("appversion");
		String ua = request.getParameter("ua");
		String uadetail = request.getParameter("uadetail");
		String udid = request.getParameter("udid");
		String s = request.getParameter("s");
		String t = request.getParameter("t");
		

		// if (StringUtil.isEmpty(appversion) || StringUtil.isEmpty(ua)
		// || StringUtil.isEmpty(udid) || StringUtil.isEmpty(s)
		// || StringUtil.isEmpty(t) || StringUtil.isEmpty(uadetail)
		// ) {
		// return false;
		// }

		// if (!isValidKey(udid, t, s)) {
		// return false;
		// }

		// return true;
		return filterParameterStr(request);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		logger.info("invoke ApiRequestInterceptor -> postHandle--------方法");

		/*String loginKey = request.getParameter("loginKey");
		User user = userWebService.getUserByLoginKey(loginKey);
		
		String uri = request.getRequestURI().replace(request.getContextPath(), "");
		List<TaskConfig> taskConfigs = taskConfigService.findAll();
		for(TaskConfig taskConfig:taskConfigs) {
			String[] uriArray = taskConfig.getUri().split(",");
			List<String> uris = Arrays.asList(uriArray);
			if(uris.contains(uri)) {
				if(modelAndView.getModelMap().get("result").equals("1")) {
					
					Long taskConfigId = taskConfig.getId();
					if(9 == taskConfigId) {
						//特殊任务接口
						taskHistoryService.selectUserInfo(user.getId(), taskConfigId);
					} else {
						taskHistoryService.finishTask(user.getId(), taskConfigId);
					}
				}
			}
		}*/
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * 根据请求User-Agent中的信息决定是否拒绝此请求
	 * 
	 * @param request
	 * @return
	 */
	boolean filterParameterStr(HttpServletRequest request) {
		boolean flag = true;
		String userAgent = request.getHeader("User-Agent");
		String uri = request.getRequestURI();// "/user/phone/check"
//		String timeStamp = request.getParameter("timestamp");// 调用接口的时间戳
//		String token = request.getParameter("token");// 调用接口的token
//		logger.info("当前拦截的uri : " + uri);
		Map<String, String[]> params = request.getParameterMap();
		if ("/user/register/phone/check".equals(uri)) {
			logger.info("请求短信发送接口/user/phone/check(注册手机验证)-----");
			Iterator<Map.Entry<String, String[]>> entries = params.entrySet().iterator();
			while (entries.hasNext()) {
				StringBuffer strArray = new StringBuffer();
				Map.Entry<String, String[]> entry = entries.next();
				for (String str : entry.getValue()) {
					strArray.append(str);
					strArray.append(",");
				}
				logger.info("键 : " + entry.getKey() + "值 : " + strArray.toString());
				logger.info("User-Agent : " + userAgent);
			}

			if (userAgent != null) {
				if (userAgent.contains("Mozilla") && userAgent.contains("Windows") && !userAgent.contains("Android")) {
					logger.error("该请求头包含异常信息 ,关键字 : " + "Mozilla&&Windows");
					flag = false;
				} else {
					flag = true;
				}
			} else {
				flag = true;
			}
		}

		if ("/user/forget/sms/check".equals(uri)) {
			logger.info("请求短信发送接口/user/forget/sms/check(忘记密码)-----");
			Iterator<Map.Entry<String, String[]>> entries = params.entrySet().iterator();
			while (entries.hasNext()) {
				StringBuffer strArray = new StringBuffer();
				Map.Entry<String, String[]> entry = entries.next();
				for (String str : entry.getValue()) {
					strArray.append(str);
					strArray.append(",");
				}
				logger.info("键 : " + entry.getKey() + "值 : " + strArray.toString());
				logger.info("User-Agent : " + userAgent);
			}

		}

		if ("/user/advert/fetch".equals(uri)) {
			logger.info("请求抢红包/user/advert/fetch()-----");
			Iterator<Map.Entry<String, String[]>> entries = params.entrySet().iterator();
			while (entries.hasNext()) {
				StringBuffer strArray = new StringBuffer();
				Map.Entry<String, String[]> entry = entries.next();
				for (String str : entry.getValue()) {
					strArray.append(str);
					strArray.append(",");
				}
				logger.info("键 : " + entry.getKey() + "值 : " + strArray.toString());
				logger.info("User-Agent : " + userAgent);
			}

		}

		// flag = isValidKey(timeStamp, token);

		return flag;
	}

	/**
	 * 前端调用接口校验(签名校验)
	 * 
	 * @param timeStamp
	 *            调用发起的时间戳
	 * @param token
	 *            加密token
	 * @return
	 */
	boolean isValidKey(String timeStamp, String token) {
//		logger.debug("=============appAndroidkey=" + getAppAndroidkey() + ",appIoskey=" + getAppIoskey());
		boolean flag = false;

		long currentTimeMillion = System.currentTimeMillis();

		String serverIosToken = MD5Util.md5Hex("timestamp=" + timeStamp + "&key=bbb981f42b0d4f629dc5d3612b86acde");// Ios传入的对应的token
		String serverAndroidToken = MD5Util.md5Hex("timestamp=" + timeStamp + "&key=f3e9aaa6672d41e38036b9cd8492a1eb");// Android&H5传入的token
		if (timeStamp != null || !"".equals(timeStamp) || token != null || !"".equals(token)) {
			try {
				long invokeTime = Long.parseLong(timeStamp);
				if (currentTimeMillion > invokeTime) {
					long twoMinutesAgo = DateUtils
							.getSeveralMinutesAgoDate(DateUtils.timeStampToDate(currentTimeMillion), 2).getTime();// 当前时间推前2分钟对应的时间戳
					if (twoMinutesAgo <= invokeTime) {
						// 说明调用时间未超过2分钟
						if (token.equals(serverAndroidToken) || token.equals(serverIosToken)) {
							flag = true;
						}
					} else {
						// 调用时间超过2分钟摒弃
						flag = false;
					}
				} else {
					// 调用时间非法
					flag = false;
				}
			} catch (Exception e) {
				logger.error("解析签名参数异常!");
				flag = false;
			}
		} else {
			flag = false;
		}

		return flag;
	}

	public String getAppIoskey() {
		return appIoskey;
	}

	public void setAppIoskey(String appIoskey) {
		this.appIoskey = appIoskey;
	}

	public String getAppAndroidkey() {
		return appAndroidkey;
	}

	public void setAppAndroidkey(String appAndroidkey) {
		this.appAndroidkey = appAndroidkey;
	}
	// boolean isRepeatRequest(String udid, String t, String s) {
	// String key = CacheCenter.KEY_REQUEST_PREFIX + udid + "-" + t + "-" + s;
	// try {
	// if (redisService.exists(key)) {
	// if (logger.isDebugEnabled()) {
	// logger.debug("return the cached validRepeatRequest by key=" + key);
	// }
	// return true;
	// } else {
	// redisService.set(key, "1", CacheCenter.REQUEST_TIMEOUT);
	// return false;
	// }
	// } catch (Exception e) {
	// logger.error(e.toString());
	// }
	// return false;
	// }

}
