package com.mouchina.web.controller.award;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.base.exception.BusinessException;
import com.mouchina.moumou_server.entity.award.SignHistory;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.award.SignHistoryService;
import com.mouchina.web.service.api.member.UserWebService;

@Controller
@RequestMapping(value="/signHistory")
public class SignHistoryController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@Resource
	private SignHistoryService signHistoryService;
	@Resource
	private UserWebService userWebService;
	
	@RequestMapping(value="/userSign")
	public @ResponseBody SignHistory userSign(HttpServletRequest request,String loginKey,String isSign,ModelMap modelMap) {
		
		System.out.println("请求的参数"+request.getParameter("loginKey") + "--------" + request.getParameter("isSign"));
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			logger.error("用户不存在!");
			modelMap.put("result", "0");
			throw new BusinessException("用户不存在!");
		}
		Long userId = user.getId();
		
		SignHistory signHistory = signHistoryService.insertUserSign(userId,isSign);
		modelMap.put("result", "1");
		return signHistory;
	}
}
