package com.mouchina.admin.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alipay.util.httpClient.HttpResponse;
import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.vo.BusinessVo;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.income.UserIncomeAward;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.income.UserIncomeAwardService;
import com.mouchina.moumou_server_interface.member.BusinessService;
import com.mouchina.moumou_server_interface.system.SystemMessageService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * Created by douzy on 16/2/17.
 */
@Controller
@RequestMapping("/business")
public class BusinessController {
	private static Logger logger = LoggerFactory.getLogger(BusinessController.class);

	@Resource
	BusinessService businessService;

	@Resource
	UserAdminService userAdminService;
	@Resource
	SystemService systemService;
	@Resource
	SystemMessageService systemMessageService;
	@Resource
	UserIncomeAwardService userIncomeAwardService;
	@Resource
	TaskHistoryService taskHistoryService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String advertList(ModelMap modelMap) {
		return "business/list";
	}

	@RequestMapping(value = "/listNot", method = RequestMethod.GET)
	private String advertNotList(ModelMap modelMap) {
		return "business/not";
	}

	@RequestMapping(value = "/listSuccess", method = RequestMethod.GET)
	private String advertSuccessList(ModelMap modelMap) {
		return "business/success";
	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String advertQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		if (request.getParameter("startTime") != null) {
			try {
				map.put("createTimegt", DateUtils.parseDate(request.getParameter("startTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getParameter("endTime") != null) {
			try {
				map.put("createTimelt", DateUtils.parseDate(request.getParameter("endTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserResult<ListRange<BusinessVo>> businessResult = new UserResult<ListRange<BusinessVo>>();
		logger.info("<<传入的<<<<<<<<<<<<<<<<<<<<businessResult : " + businessResult);
		UserResult<ListRange<Business>> businessQuerResult = businessService.selectListBusinessPage(map);

		logger.info("businessQuerResult-------size : " + businessQuerResult.getContent().getData().size());
		ListRange<BusinessVo> listRange = new ListRange<BusinessVo>();
		List<BusinessVo> businessVoList = new ArrayList<BusinessVo>();
		for (Business business : businessQuerResult.getContent().getData()) {
			BusinessVo businessVo = new BusinessVo();
			User user = userAdminService.selectUserByUserId(business.getUserId());
			BeanUtils.copyProperties(business, businessVo);
			businessVo.setPhone(user.getPhone());
			businessVoList.add(businessVo);
		}
		listRange.setData(businessVoList);
		listRange.setPage(businessQuerResult.getContent().getPage());
		businessResult.setContent(listRange);
		modelMap.put("business", businessResult.getContent());

		return "";
	}

	@RequestMapping(value = "/list/queryNot", method = RequestMethod.GET)
	private String advertQueryNot(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		map.put("state", 4);
		UserResult<ListRange<Business>> advertResult = businessService.selectListBusinessPage(map);
		modelMap.put("business", advertResult.getContent());

		return "";
	}

	@RequestMapping(value = "/list/querySuccess", method = RequestMethod.GET)
	private String advertQuerySuccess(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		// Map map = new HashMap();
		// Page page = new Page(0, 10);
		// int begin = 0;
		//
		// if (request.getParameter("begin") != null) {
		// begin =
		// Integer.valueOf(request.getParameter("begin").toString()).intValue();
		// }
		// page.setBegin(begin);
		// map.put("page", page);
		// map.put("order", "create_time desc");
		// map.put("state",1);
		// AdvertResult<ListRange<Advert>> advertResult =
		// advertService.selectListAdvertPage(map);
		// modelMap.put("adverts", advertResult.getContent());

		return "";
	}

	/**
	 * 审核通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/adopt/{adId}")
	private String adopt(@PathVariable Long adId, HttpServletRequest request, ModelMap modelMap) {
		Integer result = 0;
		logger.info("<<传入的<<<<<<<<<<<<<<<<<<<<adID : " + adId);
		if (adId > 0) {
			UserResult<Business> businessResult = businessService.selectBusiness(adId);
			if (businessResult.getState() == 1) {
				Business business = businessResult.getContent();
				int stateflag = (int) business.getState();// 值为1才返现

				// 赠与认证商户指定钱数
				SystemResult<SystemGlobalConfig> configResult = systemService
						.selectSystemGlobalConfigByKey("client.business.register.award");// 有这个配置说明有认证商户返现功能，否则不进入返现逻辑
				if (configResult != null) {
					if (configResult.getState() == 1) {
						if (stateflag == 1) {
							// 判断当前活动是否已返奖励
							Map<String, Object> map = new HashMap<>();
							map.put("userId", business.getUserId());
							map.put("eventId", business.getId());
							map.put("awardType", 1); // 1:认证返现奖励
							UserIncomeAward awardResult = userIncomeAwardService.selectUserIncomeAward(map);
							if (awardResult == null) {
								SystemGlobalConfig config = configResult.getContent();
								Integer awardMoney = config.getConfigNormal();// 返现的金额
								logger.info("返现给商户  ： " + business.getBusinessName() + "的钱数 : " + awardMoney + "分");

								int awardFlag = businessService.updateUserAssetsForCertifiedAward(business, awardMoney);

								if (awardFlag == 1) {
									UserIncomeAward award = new UserIncomeAward();
									award.setUserId(business.getUserId());
									award.setEventId(business.getId());
									award.setAmount(awardMoney);
									award.setAwardType(1); // 1:代表认证返现活动
									userIncomeAwardService.addIncomeAward(award);
									logger.info("商户 ： " + business.getBusinessName() + " 得到 返现金额" + awardMoney);
								} else {
									logger.error("商户 ： " + business.getBusinessName() + "返现失败！");
								}
							}
						}
					}
				}

				business.setModfiyTime(new Date());
				business.setState((byte) 2);
				UserResult<Integer> businessRResult = businessService.updateBusiness(business);
				result = businessRResult.getContent();
			}
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 审核未通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/noThrough/{adId}")
	private String noThrough(@PathVariable Long adId, HttpServletRequest request, ModelMap modelMap) {
		Integer result = 0;
		logger.info("------------------------adID : " + adId);
		if (adId > 0) {
			UserResult<Business> businessResult = businessService.selectBusiness(adId);
			if (businessResult.getState() == 1) {
				Business business = businessResult.getContent();
				business.setState((byte) 3);
				UserResult<Integer> advertResult = businessService.updateBusiness(business);
				result = advertResult.getContent();
			}
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 审核认证信息成功（个人/商户）
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/audit/success", method = RequestMethod.GET)
	private String auditSuccess(HttpServletRequest request, ModelMap modelMap) {
		int result = 0;
		String id = request.getParameter("id");
		BusinessTemp businessTemp = businessService.selectBussinessInTemp(Long.parseLong(id));

		Map<String, Object> businessMap = new HashMap<>();
		businessMap.put("userId", businessTemp.getUserId());
		UserResult<Business> businessResult = businessService.selectBusiness(businessMap);
		// 认证操作
		result = businessService.addAuditSuccess(businessResult, businessTemp);
		if (result == 1) {
			try {
				/* 完成身份认证 */
				logger.info("个人信息已经完善，需要完成任务----------用户名：[{}]", businessTemp.getUserId());
				taskHistoryService.insertTask(businessTemp.getUserId(), (long) 10, "1");
			} catch (Exception e) {
				logger.error("插入完成身份认证任务报错");
			}
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 分页查询认证信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/list/certification", method = RequestMethod.GET)
	private String certificationList(HttpServletRequest request, String businessTel, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		if (businessTel != null && !businessTel.equals("")) {
			map.put("businessTel", businessTel);
		}
		if (request.getParameter("startTime") != null) {
			try {
				map.put("createTimegt", DateUtils.parseDate(request.getParameter("startTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getParameter("endTime") != null) {
			try {
				map.put("createTimelt", DateUtils.parseDate(request.getParameter("endTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserResult<ListRange<BusinessVo>> businessResult = new UserResult<ListRange<BusinessVo>>();
		UserResult<ListRange<BusinessTemp>> businessQuerResult = businessService.selectListBusinessTempPage(map);

		ListRange<BusinessVo> listRange = new ListRange<BusinessVo>();
		List<BusinessVo> businessVoList = new ArrayList<BusinessVo>();
		for (BusinessTemp businessTemp : businessQuerResult.getContent().getData()) {
			BusinessVo businessVo = new BusinessVo();
			User user = userAdminService.selectUserByUserId(businessTemp.getUserId());
			BeanUtils.copyProperties(businessTemp, businessVo);
			businessVo.setPhone(user.getBindingMobile());
			businessVoList.add(businessVo);
		}
		listRange.setData(businessVoList);
		listRange.setPage(businessQuerResult.getContent().getPage());
		businessResult.setContent(listRange);
		modelMap.put("business", businessResult.getContent());

		return "";
	}

	/**
	 * 审核认证信息失败（个人/商户）
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/audit/defeated", method = RequestMethod.GET)
	private String auditDefeated(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		int result = 0;
		String content = request.getParameter("content");
		String id = request.getParameter("id");
		BusinessTemp businessTemp = businessService.selectBussinessInTemp(Long.parseLong(id));
		businessTemp.setState((byte) 3);
		// 认证操作
		result = businessService.updateBussinessInTemp(businessTemp);
		if (result == 1) {
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setContent(content);
			systemMessage.setCreateTime(new Date());
			systemMessage.setUserId(businessTemp.getUserId());
			result = systemMessageService.insertSystemMessage(systemMessage);
		}
		modelMap.put("result", result);
		return "";
	}
}