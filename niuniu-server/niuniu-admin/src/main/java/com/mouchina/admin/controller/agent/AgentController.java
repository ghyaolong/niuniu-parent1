package com.mouchina.admin.controller.agent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mouchina.admin.service.api.invite.IUserInviteService;
import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.member.UserAgentAdminService;
import com.mouchina.admin.service.api.vo.AgentIncomeSumVo;
import com.mouchina.admin.service.api.vo.AgentIncomeVo;
import com.mouchina.admin.service.api.vo.AgentVo;
import com.mouchina.admin.service.api.vo.UserInviteVO;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.vo.AgentIncomeStatisticsVo;
import com.mouchina.moumou_server_interface.income.AgentIncomeService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.view.IncomeResult;
import com.mouchina.moumou_server_interface.view.Result;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * Created by douzy on 16/2/16.
 */
@Controller
@RequestMapping("/agent")
public class AgentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);
	
	@Resource
	UserAgentAdminService userAgentAdminService;

	@Resource
	UserAdminService userAdminService;
	@Resource
	UserAgentService agentService;
	@Resource
	AgentIncomeAdminService agentIncomeAdminService;
	@Resource
	AgentIncomeService agentIncomeService;
	@Resource
	IUserInviteService inviteService;

	@RequestMapping(value = "/income/list", method = RequestMethod.GET)
	private String orderList(ModelMap modelMap) {

		return "/agent/income/list";
	}

	@RequestMapping(value = "/incomesum/day/list", method = RequestMethod.GET)
	private String orderDayList(ModelMap modelMap) {
		return "/agent/income/list";
	}

	@RequestMapping(value = "/incomme/list/query", method = RequestMethod.GET)
	private String incomeQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		UserResult<UserAgent> agentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
		map.put("userId", agentResult.getContent().getUserId());

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
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		UserResult<UserAgent> agentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
		map.put("userId", agentResult.getContent().getUserId());

		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		if (request.getParameter("agentLevel") != null) {
			map.put("agentLevel", request.getParameter("agentLevel"));
		}

		if (request.getParameter("parentId") != null) {
			map.put("parentId", request.getParameter("parentId"));
		}
		if (request.getParameter("userId") != null) {
			map.put("userId", request.getParameter("userId"));
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
		return "/agent/incomesum/list";
	}

	/**
	 * 代理商粉丝列表
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/invite/list", method = RequestMethod.GET)
	private String inviteList(ModelMap modelMap) {
		return "/agent/invite/list";
	}

	@RequestMapping(value = "/invite/list/query", method = RequestMethod.GET)
	private String inviteQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		System.out.println(String.format("----------------phone=%s----------", phone));
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);

		System.out.println(String.format("----------------user.getId()=%s----------", user.getId()));
		UserResult<UserAgent> agentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
		map.put("userId", agentResult.getContent().getUserId());

		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");

		ListRange<UserInviteVO> inviteList = new ListRange<UserInviteVO>();

		Result<ListRange<UserInvite>> result = inviteService.queryInviteList(map);
		if (result == null || result.getContent() == null || null == result.getContent().getData()) {
			modelMap.put("inviteList", inviteList);
			return "";
		}

		List<UserInviteVO> list = new ArrayList<UserInviteVO>();
		for (UserInvite invite : result.getContent().getData()) {
			UserInviteVO inviteVO = new UserInviteVO();
			BeanUtils.copyProperties(invite, inviteVO);

			User inviteUser = userAdminService.selectUserByUserId(invite.getNewUserId());

			inviteVO.setTargetNickName(inviteUser.getNickName());
			inviteVO.setTargetRedpackMoney(inviteUser.getRedpackMoney());
			inviteVO.setTargetSex((byte)inviteUser.getSex().intValue());
			inviteVO.setTargetAvatar(inviteUser.getAvatar());
			inviteVO.setTargetPhone(inviteUser.getPhone());
			inviteVO.setTargetPhoneSpecification(inviteUser.getPhoneSpecification());
			inviteVO.setTargetInstallTime(inviteUser.getFristLoginTime());
			inviteVO.setTargetRegisterTime(inviteUser.getCreateTime());
			list.add(inviteVO);
		}
		inviteList.setData(list);
		inviteList.setPage(result.getContent().getPage());
		modelMap.put("inviteList", inviteList);
		return "";
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	private String showUser(ModelMap modelMap) {

		Map map = new HashMap();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		UserResult<UserAgent> agentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
		map.put("userId", agentResult.getContent().getUserId());

		UserResult<UserAgent> userAgentResult = userAgentAdminService.selectUserAgent(map);

		if (userAgentResult.getState() == 1) {
			AgentVo agentVo = new AgentVo();

			BeanUtils.copyProperties(userAgentResult.getContent(), agentVo);

			user = userAdminService.selectUserByUserId(userAgentResult.getContent().getUserId());
			if (user != null) {
				agentVo.setUserName(user.getNickName());
				agentVo.setPhone(user.getPhone());
			}
			modelMap.put("agentVo", agentVo);
		}

		return "/agent/show";
	}

	@RequestMapping(value = "/sub/list", method = RequestMethod.GET)
	private String subList(ModelMap modelMap) {
		return "/agent/sub/list";
	}

	@RequestMapping(value = "/sub/summary/list", method = RequestMethod.GET)
	private String summaryList(ModelMap modelMap) {
		LOGGER.info("进入agent/sub/summary/list Action");
		return "/agent/summary/list";
	}

	@RequestMapping(value = "/sub/list/query", method = RequestMethod.GET)
	private String orderQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		UserResult<UserAgent> agentResult = userAgentAdminService.selectUserAgentByUserId(user.getId());
		map.put("parentId", agentResult.getContent().getUserId());

		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		if (request.getParameter("agentLevel") != null) {
			map.put("agentLevel", request.getParameter("agentLevel"));
		}

		if (request.getParameter("phone") != null) {

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

				user = userAdminService.selectUserByUserId(userAgent.getUserId());
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

	@RequestMapping(value = "/statistics/list", method = RequestMethod.GET)
	private String statisticsList(ModelMap modelMap) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		modelMap.put("userId", user.getId());
		return "/agent/statistics/list";
	}

	@RequestMapping(value = "/statistics/list/query", method = RequestMethod.GET)
	private String statisticsListQuery(HttpServletRequest request, ModelMap modelMap) {
		Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
       
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("incomeDategt", DateUtils.parseDate( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("incomeDatelt", DateUtils.parseDate( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		map.put("incomeUserId", user.getId());
		page.setBegin(begin);
		map.put("page", page);
		map.put("group","income_user_id, income_date");
        map.put("order","income_date desc");
		try {
			ListRange<AgentIncomeStatisticsVo> agentIncomeStatisticsVos = agentIncomeService.selectAgentIncomeStatisticsPageList(map);
			modelMap.put("agentIncomeList", agentIncomeStatisticsVos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.GET)
	private String agentUserAdd(ModelMap modelMap) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		Map map = new HashMap<>();
		map.put("userId", user.getId());
		try {
			UserResult<UserAgent> userAgent = agentService.selectUserAgent(map);
			Integer agentPiont = userAgent.getContent().getAgentPiont();
			modelMap.put("agentPiont", agentPiont);
			modelMap.put("agentLevel", userAgent.getContent().getAgentLevel());
		} catch (Exception e) {
			modelMap.put("agentLevel", "1");
		}
		modelMap.put("userId", user.getId());
		return "/agent/statistics/new";
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
	private String agentAdd(@ModelAttribute UserAgent userAgent, ModelMap modelMap){
		Integer agentLevel = userAgent.getAgentLevel();
		Map map = new HashMap<>();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();

		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		map.put("userId", user.getId());
		UserResult<UserAgent> parentUserAgent = agentService.selectUserAgent(map);
		UserAgent pAgent = parentUserAgent.getContent();
		if(agentLevel == 2){
			userAgent.setAgentPiont(100);
		}
		if (agentLevel == 3) {
			userAgent.setAgentPiont(20);
			if(pAgent.getAgentPiont() != null){
				int point = pAgent.getAgentPiont() - 20;
				if(point < 0){
					return "redirect:/agent/show/message/centre.html";
				}
				pAgent.setAgentPiont(point);
			}
		}
		if (agentLevel == 4) {
			userAgent.setAgentPiont(0);
			if(pAgent.getAgentPiont() != null){
				int point = pAgent.getAgentPiont() - 1;
				if(point < 0){
					return "redirect:/agent/show/message/starts.html";
				}
				pAgent.setAgentPiont(point);
			}
		}
		agentService.updateUserAgent(pAgent);
		userAgent.setParentId(pAgent.getUserId());
		userAgent.setCreateTime(new Date());
		byte state = 1;
		userAgent.setState(state);
		userAgent.setAvailableAmount(0);
		UserResult<UserAgent> userResult = agentService.addUserAgent(userAgent);
		LOGGER.info("代理商用户(用户ID:"+ user.getId() +")添加代理商");
		return "redirect:/user/agent/show/" + userResult.getContent().getId() + ".html";
	}
	
	
	@RequestMapping(value = "/show/message/{name}", method = RequestMethod.GET)
	private String showMessageInfo(@PathVariable String name, ModelMap modelMap) {
		String msg = "您的点数不够任命中心代理商";
		if(name.equals("stars")){
			msg = "您的点数不够任命星级代理商";
		}
		modelMap.put("message", msg);
		return "/message/message";
	}
}
