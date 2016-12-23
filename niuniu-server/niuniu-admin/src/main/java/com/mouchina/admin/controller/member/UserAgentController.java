package com.mouchina.admin.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.income.AgentIncomeAdminService;
import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.member.UserAgentAdminService;
import com.mouchina.admin.service.api.vo.AgentIncomeSumVo;
import com.mouchina.admin.service.api.vo.AgentIncomeVo;
import com.mouchina.admin.service.api.vo.AgentVo;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.view.IncomeResult;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * Created by douzy on 16/2/16.
 */
@Controller
@RequestMapping("/user/agent")
public class UserAgentController {
	
	private static final Logger LOGGER = Logger.getLogger(UserAgentController.class);
	
	@Resource
	UserAgentAdminService userAgentAdminService;

	@Resource
	UserAdminService userAdminService;
	@Resource
	AgentIncomeAdminService agentIncomeAdminService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String orderList(ModelMap modelMap) {
		return "/members/agent/list";
	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String orderQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		if (request.getParameter("agentLevel") != null) {
			map.put("agentLevel", request.getParameter("agentLevel"));
		}

		if (request.getParameter("phone") != null) {

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setPhone(request.getParameter("phone"));
			User user = userAdminService.selectUser(userIdentity);
			if (user != null) {
				UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
				map.put("userId", userAgentResult.getContent().getUserId());
			}
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		UserResult<ListRange<UserAgent>> userResult = userAgentAdminService.selectListUserAgentPage(map);

		ListRange<AgentVo> agentVoListRange = new ListRange<AgentVo>();

		if (userResult.getContent() != null && userResult.getContent().getData() != null) {

			ListRange<UserAgent> userAgentListRange = userResult.getContent();

			List<AgentVo> agentVolist = new ArrayList<AgentVo>();
			for (UserAgent userAgent : userAgentListRange.getData()) {
				AgentVo agentVo = new AgentVo();
				BeanUtils.copyProperties(userAgent, agentVo);

				User user = userAdminService.selectUserByUserId(userAgent.getUserId());
				if (user != null) {
					agentVo.setUserName(user.getNickName());
					agentVo.setPhone(user.getPhone());
				}

				agentVolist.add(agentVo);

			}
			agentVoListRange.setPage(userAgentListRange.getPage());
			agentVoListRange.setData(agentVolist);
			modelMap.put("userAagentVos", agentVoListRange);
		}
		return "";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	private String userAdd(@ModelAttribute UserAgent userAgent, ModelMap modelMap) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		if (userAgent != null) {
			Integer level = userAgent.getAgentLevel();
			if (level == 2) {
				userAgent.setAgentPiont(100);
			}
			if (level == 3) {
				userAgent.setAgentPiont(20);
			}
			if (level == 4) {
				userAgent.setAgentPiont(0);
			}
		}
		if(userAgent.getId() != null && userAgent.getId() > 0){
			userAgentAdminService.updateUserAgent(userAgent);
			LOGGER.info("代理商管理员(用户ID:" + user.getId() + ")更新代理商(信息ID:" + userAgent.getId() +")用户信息");
			return "redirect:show/" + userAgent.getId() + ".html";
		}else{
			UserResult<UserAgent> userResult = userAgentAdminService.addUserAgent(userAgent);
			LOGGER.info("代理商管理员(用户ID:" + user.getId() + ")维护代理商用户(用户ID:" + userAgent.getUserId() +")信息");
			return "redirect:show/" + userResult.getContent().getId() + ".html";
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	private String userUpdate(UserAgent userAgent, ModelMap modelMap) {
		userAgentAdminService.updateUserAgent(userAgent);

		return "redirect:show/" + userAgent.getId() + ".html";
	}

	@RequestMapping(value = "/edit/{userAgentId}", method = RequestMethod.GET)
	private String editUser(@PathVariable long userAgentId, ModelMap modelMap) {
		UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgent(userAgentId);

		if (userAgentResult.getState() == 1) {
			AgentVo agentVo = new AgentVo();

			BeanUtils.copyProperties(userAgentResult.getContent(), agentVo);

			User user = userAdminService.selectUserByUserId(userAgentResult.getContent().getUserId());
			if (user != null) {
				agentVo.setUserName(user.getNickName());
				agentVo.setPhone(user.getPhone());
			}
			modelMap.put("agentVo", agentVo);
		}

		return "/members/agent/edit";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	private String newAgent(ModelMap modelMap) {

		return "/members/agent/new";
	}

	@RequestMapping(value = "/check/phone/{phone}")
	private String userCheckPhone(@PathVariable String phone, ModelMap modelMap) {
		
		UserResult<User> userResult = userAgentAdminService.checkAgent(phone);
		
		int result = userResult.getState();

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String myPhone = userDetails.getUsername();
		
		if (result >= 0) {
			modelMap.put("myphone", myPhone);
			modelMap.put("user", userResult.getContent());
			modelMap.put("userAgent", userResult.getUserAgent());
			modelMap.put("parentUserAgent", userResult.getParentUserAgent());
		}
		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/parent/{userId}")
	private String userCheckUserId(@PathVariable long userId, ModelMap modelMap) {

		UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgentByUserId(userId);
		int result = 0;
		AgentVo agentVo = null;
		if (userAgentResult.getState() == 1 && userAgentResult.getContent() != null) {
			User user = userAdminService.selectUserByUserId(userAgentResult.getContent().getUserId());
			if (user != null) {
				agentVo = new AgentVo();
				BeanUtils.copyProperties(userAgentResult.getContent(), agentVo);
				agentVo.setUserName(user.getNickName());
				agentVo.setPhone(user.getPhone());
				result = 1;
			}
			modelMap.put("agentVo", agentVo);
		}
		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/{phone}")
	private String agentByPhone(@PathVariable String phone, ModelMap modelMap) {
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		int result = 0;
		if (user != null) {
			UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());

			AgentVo agentVo = null;
			if (userAgentResult.getState() == 1 && userAgentResult.getContent() != null) {

				agentVo = new AgentVo();
				BeanUtils.copyProperties(userAgentResult.getContent(), agentVo);
				agentVo.setUserName(user.getNickName());
				agentVo.setPhone(user.getPhone());
				modelMap.put("agentVo", agentVo);
				result = 1;
			}
		}
		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/show/{userAgentId}", method = RequestMethod.GET)
	private String showUser(@PathVariable long userAgentId, ModelMap modelMap) {
		UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgent(userAgentId);

		if (userAgentResult.getState() == 1) {
			AgentVo agentVo = new AgentVo();

			BeanUtils.copyProperties(userAgentResult.getContent(), agentVo);

			User user = userAdminService.selectUserByUserId(userAgentResult.getContent().getUserId());
			if (user != null) {
				agentVo.setUserName(user.getNickName());
				agentVo.setPhone(user.getPhone());
			}
			modelMap.put("agentVo", agentVo);
		}

		return "/members/agent/show";
	}

	@RequestMapping(value = "/incomesum/edit/{agentIncomeSumId}", method = RequestMethod.GET)
	private String agentIncomeSumUpdate(@PathVariable long agentIncomeSumId, ModelMap modelMap) {
		IncomeResult<AgentIncomeSum> incomeResult = agentIncomeAdminService.selectAgentIncomeSum(agentIncomeSumId);
		AgentIncomeSumVo agentIncomeSumVo = new AgentIncomeSumVo();
		BeanUtils.copyProperties(incomeResult.getContent(), agentIncomeSumVo);
		modelMap.put("agentIncomeSum", agentIncomeSumVo);
		return "/members/agent/incomesum/edit";
	}

	@RequestMapping(value = "/incomesum/update", method = RequestMethod.POST)
	private String agentIncomeSumUpdate(AgentIncomeSum agentIncomeSum, ModelMap modelMap) {
		IncomeResult<Integer> incomeResult = agentIncomeAdminService.updateAgentIncomeSum(agentIncomeSum);

		return "redirect:list.html";
	}

	@RequestMapping(value = "/income/list", method = RequestMethod.GET)
	private String incomeList(long userId, ModelMap modelMap) {

		modelMap.put("userId", userId);
		return "/members/agent/income/list";
	}

	@RequestMapping(value = "/incomesum/day/list", method = RequestMethod.GET)
	private String incomeDayList(ModelMap modelMap) {

		modelMap.put("userId", "0");
		return "/members/agent/income/list";
	}

	@RequestMapping(value = "/incomme/list/query", method = RequestMethod.GET)
	private String incomeQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		if (request.getParameter("agentLevel") != null) {
			map.put("agentLevel", request.getParameter("agentLevel"));
		}
		if (request.getParameter("userId") != null) {
			if (Integer.valueOf(request.getParameter("userId").toString()) > 0)
				map.put("userId", request.getParameter("userId"));
		}

		if (request.getParameter("phone") != null) {

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setPhone(request.getParameter("phone"));
			User user = userAdminService.selectUser(userIdentity);
			if (user != null) {
				UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
				map.put("userId", userAgentResult.getContent().getUserId());
			}
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		IncomeResult<ListRange<AgentIncome>> agentIncomeListRange = agentIncomeAdminService
				.selectListAgentIncomePage(map);

		ListRange<AgentIncomeVo> agentIncomeVoListRange = new ListRange<AgentIncomeVo>();

		if (agentIncomeListRange.getContent() != null && agentIncomeListRange.getContent().getData() != null) {

			List<AgentIncomeVo> agentIncomeVoList = new ArrayList<AgentIncomeVo>();
			for (AgentIncome agentIncome : agentIncomeListRange.getContent().getData()) {
				AgentIncomeVo agentIncomeVo = new AgentIncomeVo();
				BeanUtils.copyProperties(agentIncome, agentIncomeVo);

				User user = userAdminService.selectUserByUserId(agentIncomeVo.getUserId());
				if (user != null) {
					agentIncomeVo.setUserName(user.getNickName());
					agentIncomeVo.setPhone(user.getPhone());
				}

				agentIncomeVoList.add(agentIncomeVo);

			}
			agentIncomeVoListRange.setPage(agentIncomeListRange.getContent().getPage());
			agentIncomeVoListRange.setData(agentIncomeVoList);
		}

		modelMap.put("agentIncomeList", agentIncomeVoListRange);

		return "";
	}

	@RequestMapping(value = "/incommesum/list/query", method = RequestMethod.GET)
	private String incomeSumQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		if (request.getParameter("agentLevel") != null) {
			map.put("agentLevel", request.getParameter("agentLevel"));
		}
		if (request.getParameter("userId") != null) {
			map.put("userId", request.getParameter("userId"));
		}
		if (request.getParameter("phone") != null) {

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setPhone(request.getParameter("phone"));
			User user = userAdminService.selectUser(userIdentity);
			if (user != null) {
				UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
				map.put("userId", userAgentResult.getContent().getUserId());
			}
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		IncomeResult<ListRange<AgentIncomeSum>> agentIncomeSumListRange = agentIncomeAdminService
				.selectListAgentIncomeSumPage(map);

		ListRange<AgentIncomeSumVo> agentIncomeSumVoListRange = new ListRange<AgentIncomeSumVo>();

		if (agentIncomeSumListRange.getContent() != null && agentIncomeSumListRange.getContent().getData() != null) {

			ListRange<AgentIncomeSum> userAgentIncomeSumListRange = agentIncomeSumListRange.getContent();

			List<AgentIncomeSumVo> agentIncomeSumVoList = new ArrayList<AgentIncomeSumVo>();
			for (AgentIncomeSum agentIncomeSum : userAgentIncomeSumListRange.getData()) {
				AgentIncomeSumVo agentIncomeSumVo = new AgentIncomeSumVo();
				BeanUtils.copyProperties(agentIncomeSum, agentIncomeSumVo);

				User user = userAdminService.selectUserByUserId(agentIncomeSumVo.getUserId());
				if (user != null) {
					agentIncomeSumVo.setUserName(user.getNickName());
					agentIncomeSumVo.setPhone(user.getPhone());
				}

				agentIncomeSumVoList.add(agentIncomeSumVo);

			}
			agentIncomeSumVoListRange.setPage(agentIncomeSumListRange.getContent().getPage());
			agentIncomeSumVoListRange.setData(agentIncomeSumVoList);
		}

		modelMap.put("agentIncomeSumList", agentIncomeSumVoListRange);

		return "";
	}

	@RequestMapping(value = "/incomesum/list", method = RequestMethod.GET)
	private String incomeSumList(ModelMap modelMap) {
		return "/members/agent/incomesum/list";
	}

}
