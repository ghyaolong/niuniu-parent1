package com.mouchina.web.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server_interface.pay.CashFlowService;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.UserCashFlowVo;

@Controller
@RequestMapping("/cashflow")
public class CashFlowController extends BaseController  {
	
	@Resource
	UserWebService  userWebService;
	
	@Resource
	CashFlowService  cashFlowService;
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String inviteList(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws IOException {

		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString())
					.intValue();

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			modelMap.put("result", "1");
			Map map = new HashMap();
			map.put("page", new Page(begin, 10));
			map.put("order", "create_time desc");
			map.put("userId", user.getId());
			ListRange<UserCashFlow> listRange = cashFlowService.selectListUserCashFlowPage(map).getContent();
			ListRange<UserCashFlowVo> userCashFlowVoListVo = new ListRange<UserCashFlowVo>();
			userCashFlowVoListVo.setPage(listRange.getPage());
			List<UserCashFlowVo> listVo = new ArrayList<UserCashFlowVo>();
			for (UserCashFlow userCashFlow : listRange.getData()) {
				UserCashFlowVo userCashFlowVo = new UserCashFlowVo();
				BeanUtils.copyProperties(
						userCashFlow,
						userCashFlowVo);
				listVo.add(userCashFlowVo);
			}
			userCashFlowVoListVo.setData(listVo);

			modelMap.put("userCashFlowList",
					userCashFlowVoListVo.getData());
			modelMap.put("page", userCashFlowVoListVo.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

}
