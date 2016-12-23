package com.mouchina.web.controller.system;

import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.social.Feedback;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server_interface.social.FeedBackService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.system.SystemMessageService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.vo.SystemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by douzy on 16/2/22.
 */
@Controller
@RequestMapping("/sys")
public class SystemController extends BaseController {

	@Resource
	SystemService systemService;

	@Resource
	FeedBackService feedBackService;
	@Resource
	UserCommentService userCommentService;


	@RequestMapping("/client/config")
	public String configList(HttpServletRequest request, ModelMap modelMap) {

		Map map = new HashMap();
		map.put("configGroup", "client");
		map.put("state", "1");
		SystemResult<List<SystemGlobalConfig>> systemGlobalConfigSystemResult = systemService
				.selectListSystemGlobalConfig(map);

		List<SystemVo> systemVos = new ArrayList<>();
		for (SystemGlobalConfig systemGlobalConfig : systemGlobalConfigSystemResult.getContent()) {
			SystemVo systemVo = new SystemVo();
			systemVo.setKey(systemGlobalConfig.getConfigKey());
			systemVo.setValue(systemGlobalConfig.getConfigContent());
			systemVos.add(systemVo);
		}
		modelMap.put("result", 1);
		modelMap.put("configList", systemVos);
		return "";
	}

	@RequestMapping("/client/feedback")
	public String addFeedBack(HttpServletRequest request, ModelMap modelMap) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String phone = request.getParameter("phone");
		String appVersion = request.getParameter("appVersion");
		String uadetail = request.getParameter("uadetail");
		String udid = request.getParameter("udid");

		Feedback feedback = new Feedback();
		feedback.setTitle(title);
		feedback.setContent(content);
		feedback.setPhone(phone);
		feedback.setCreateTime(new Date());
		feedback.setAppVersion(appVersion);
		feedback.setUadetail(uadetail);
		feedback.setUdid(udid);
		Integer result = feedBackService.insertFeedback(feedback);
		if (result > 0) {
			modelMap.put("result", 1);
		} else {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
		}

		return "";
	}

	@RequestMapping("/common/info")
	public String commonInfo(HttpServletRequest request, ModelMap modelMap) {

		Map map = new HashMap();
		map.put("type", 1);
		Map map1 = new HashMap();
		map.put("type", 2);
		Map map2 = new HashMap();
		map.put("type", 3);
		List<SystemDataVo> systemDataVos = systemService.selectListSystemData(map);
		List<SystemDataVo> systemDataVos1 = systemService.selectListSystemData(map1);
		List<SystemDataVo> systemDataVos2 = systemService.selectListSystemData(map2);
		modelMap.put("result", 1);
		modelMap.put("workList", systemDataVos);
		modelMap.put("hobbyList", systemDataVos1);
		modelMap.put("advertTypeList", systemDataVos2);
		return "";
	}
}
