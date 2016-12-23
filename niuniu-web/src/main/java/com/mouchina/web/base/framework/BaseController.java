package com.mouchina.web.base.framework;

import com.mouchina.web.base.config.Env;
import com.mouchina.web.base.utils.AppSginUtils;
import org.apache.commons.collections.map.HashedMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

	public boolean baseSign(HttpServletRequest request, Env env, Map map) {
		boolean flag = false;

		int ua = Integer.valueOf(request.getParameter("ua").toString());
		String appkey = env.getProp("moumou.app.ios.key");
		if (ua == 3 || ua == 4) {
			appkey = env.getProp("moumou.app.android.key");
		}

		AppSginUtils.commonCheckSign(map, appkey, request.getParameter("sgin"));
		return flag;
	}

	public boolean checkRequstIntegrity(HttpServletRequest request) {
		boolean flag = false;
		return flag;
	}

	public boolean checkPhoneSign(HttpServletRequest request, Env env) {
		boolean flag = false;

		Map map = new HashedMap();

		map.put("appid", "11002");
		map.put("ua", "2");
		map.put("uadetail", "iPad4,4+9.200000");
		map.put("udid", "F365614D-9A32-418B-9846-89F98884B555");
		map.put("t", "1893283868632");
		map.put("loginKey", "FA8D192328289398JJKUOOO_01");
		map.put("phone", "1347568368");
		map.put("orderNo", "20151347568368");
		map.put("paySource", "1");
		map.put("payChannel", "1");
		flag = baseSign(request, env, map);

		return flag;
	}

}
