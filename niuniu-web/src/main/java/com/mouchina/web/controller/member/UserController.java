package com.mouchina.web.controller.member;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.cache.CacheManager;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.Regexp;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.luckybag.LuckyBagHelper;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.BusinessShopHelper;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserHelper;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server.exceptions.UserException;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.BusinessShopService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserAssetsInfo;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.system.SystemMessageService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.config.Env;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.AliDaYuSMSUtils;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.base.utils.TokenUtil;
import com.mouchina.web.controller.enums.EUserLogin;
import com.mouchina.web.service.api.advert.AdvertWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.AdvertVo;
import com.mouchina.web.service.api.vo.AgentTemplateVo;
import com.mouchina.web.service.api.vo.BusinessVo;
import com.mouchina.web.service.api.vo.UserAssetsVo;
import com.mouchina.web.service.api.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Resource
	UserWebService userWebService;
	@Resource
	Env env;
	@Resource
	CacheManager cacheManager;
	@Resource
	SocialWebService socialWebService;
	@Resource
	AdvertWebService advertWebService;
	@Resource
	UserAgentService agentService;
	@Resource
	UserAgentService userAgentService;
	@Resource
	BusinessShopService businessShopService;
	@Resource
	SystemMessageService systemMessageService;
	@Resource
	UserLevelConfigService userLevelConfigService;
	@Resource
	BusinessCouponService businessCouponService;
	@Resource
	UserAssetsService userAssetsService;
	@Resource
	UsersRelationService userRelationService;
	@Resource
	UserVerifyService userVerifyService;
	@Resource
	TaskHistoryService taskHistoryService;
	@Resource
	UsersRelationService usersRelationService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginout", method = { RequestMethod.GET, RequestMethod.POST })
	public String loginOut(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String[] loginAuth = TokenUtil.split(loginKey);
		UserIdentity userIdentity = new UserIdentity();

		userIdentity.setTableNum(Integer.valueOf(loginAuth[1]));
		userIdentity.setToken(loginAuth[0]);
		int result = userWebService.processUserLogOut(userIdentity);

		if (result == 1) {
			modelMap.put("result", "1");
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100102);
		}
		return "";
	}

	/***
	 * 注册手机验证
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/register/phone/check", method = { RequestMethod.GET, RequestMethod.POST })
	public String registerPhoneCheck(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String phone = request.getParameter("phone");
		logger.info("phone=" + request.getParameter("phone"));
		String headValue = request.getHeader("User-Agent");
		logger.info("User-Agent=" + request.getHeader("User-Agent"));
		String host = request.getHeader("Host");
		logger.info("Host=" + request.getParameter("Host"));

		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		Integer checkCode = ((Double) (Math.random() * 9000 + 1000)).intValue();

		User user = userWebService.getUserByPhone(phone);
		logger.info("filter_&&&&调用注册-------phoneNum------------" + phone + "--------------------------");

		logger.info("filter_host ——&&&&&&&&&&&&&&&&&&&&&&&&&&& : " + host);
		logger.info("filter_User-Agent ——&&&&&&&&&&&&&&&&&&&&&&&&&&& : " + headValue);
		if (user == null) {
			boolean isSucc = false;
			Integer count = cacheManager.getEntityByKey(Integer.class, "sended_" + phone);// 从缓存取出该号码在最近30分钟的发送短信数量
			if (count != null) {
				logger.info("filter_电话号码 : " + phone + "在近24小时内发送短信次数 : " + count);
				if (count < 5) {
					cacheManager.addEntityByKey("sended_" + phone, ++count, 24 * 60 * 60);// 计数加1

					isSucc = AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
							"a1e8f4efd4062af22d404362f1e4ece1", "{\"number\" : " + "\"" + checkCode + "\"}", phone,
							"SMS_20230072");
					if (isSucc) {
						logger.info("filter_host ——&&&&&&&&&&&&&&&&&&&&&&&&&&& : " + host + "&&&&phone : " + phone);
						logger.info("filter_User-Agent ——&&&&&&&&&&&&&&&&&&&&&&&&&&& : " + headValue + "&&&&phone : "
								+ phone);
						// cacheManager.addEntityByKey("sended_" + phone,
						// ++count, 30*60);//计数加1
					} else {
						modelMap.put("result", "0");
						modelMap.put("errorCode", Constants.ERROR_CODE_100000);
						return "";
					}
				} else {
					// 说明在30分钟之内该号码发送次数超过5次
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100000);
					return "";
				}
			} else {
				cacheManager.addEntityByKey("sended_" + phone, 1, 24 * 60 * 60);

				// 证明是30分钟内首次发短信
				isSucc = AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
						"a1e8f4efd4062af22d404362f1e4ece1", "{\"number\" : " + "\"" + checkCode + "\"}", phone,
						"SMS_20230072");
				if (isSucc) {
					// cacheManager.addEntityByKey("sended_" + phone, 1, 30*60);
				} else {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100000);
					return "";
				}
			}

			cacheManager.addEntityByKey(Constants.REG_SMS_CODE + phone, checkCode + "", 5 * 60);
			modelMap.put("result", "1");
		} else {
			modelMap.put("result", "2");

		}

		return "";
	}

	/***
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
	public String register(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String type = request.getParameter("type");
		logger.info("type=" + request.getParameter("type"));
		/*
		 * type值 1、内部注册 2、外部注册
		 */
		if (type.equals("1")) {
			return insideRegister(request, response, modelMap);
		} else {
			return outsideRegister(request, response, modelMap);
		}
	}

	/**
	 * 绑定邀请关系
	 * 
	 * @param user
	 *            当前用户(即被邀请人)
	 * @param inviteCode
	 *            邀请码(邀请人的id)
	 */
	public void bindInviteRelation(User user, String inviteCode) {
		/* 若存在邀请码，进行用户邀请关系关联start */
		if (user != null && StringUtil.isNotEmpty(inviteCode)) {
			Map<String, Object> userInviteMap = new HashMap<String, Object>();
			userInviteMap.put("userId", inviteCode);
			userInviteMap.put("newUserId", user.getId());

			List<UserInvite> list = socialWebService.selectListUserInvite(userInviteMap);
			if (list.size() == 0) {
				UserInvite userInvite = new UserInvite();
				userInvite.setUserId(Long.valueOf(inviteCode));
				userInvite.setNewUserId(user.getId());
				userInvite.setCreateTime(new Date());
				socialWebService.addUserInvite(userInvite);
				try {
					/* 完成邀请好友注册 */
					logger.info("个人信息已经完善，需要完成任务----------用户名：[{}]", user.getId());
					taskHistoryService.insertTask(user.getId(), (long) 5, "1");
				} catch (Exception e) {
					logger.error("插入完成邀请好友注册任务报错");
				}
				/* 查询最近一条XX活动 */
				Map<String, Object> map = new HashMap<>();
				map.put("advertType", 11);// 幸运转轮活动
				Advert advert = advertWebService.selectActivity(map);
				if (null != advert) {
					/* 查询活动期间内邀请好友情况 */
					map.put("userId", Long.parseLong(inviteCode));
					map.put("startTime", advert.getStartTime());
					map.put("endTime", advert.getEndTime() + " 23:59:59");
					// 如果当前时间不在活动范围内 不执行下面操作
					Date date = new Date();
					if (date.getTime() >= advert.getStartTime().getTime()
							&& date.getTime() <= advert.getEndTime().getTime()) {
						int count = socialWebService.selectUserInviteBetweenTimeAndTime(map);
						if (count % Constants.invite_num == 0) {
							Map<String, Object> uadMap = new HashMap<>();
							uadMap.put("advertId", advert.getId());
							uadMap.put("userId", Long.parseLong(inviteCode));
							UserActivityDeploy userActivityDeploy = socialWebService.selectUserActivityDeploy(map);
							if (userActivityDeploy != null) {
								/* 邀请注册人数满足抽奖次数加1 */
								userActivityDeploy.setCount(userActivityDeploy.getCount() + 1);
								userActivityDeploy.setCreateTime(new Date());
								socialWebService.updateUserActivityDeploy(userActivityDeploy);
							} else {
								// 没查到生成一条记录
								userActivityDeploy = new UserActivityDeploy();
								userActivityDeploy.setAdvertId(advert.getId());
								userActivityDeploy.setUserId(Long.parseLong(inviteCode));
								userActivityDeploy.setCreateTime(new Date());
								userActivityDeploy.setCount(3);
								socialWebService.addUserActivityDeploy(userActivityDeploy);
							}
						}
					}
				}
			}
		}
		/* 若存在邀请码，进行用户邀请关系关联end */
	}

	/***
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			UserIdentity userIdentity = new UserIdentity();
			{// 数据校验
				final String loginKey = request.getParameter("loginKey");
				if (StringUtils.isNotBlank(loginKey)) {
					if (loginKey.contains("_") == false) {
						throw new UserException(false, EUserLogin.FORMAT_ERRO_LOGINKEY);
					}
					// 解析：loginKey
					String[] loginAuth = TokenUtil.split(loginKey);

					userIdentity.setTableNum(Integer.valueOf(loginAuth[1]));
					userIdentity.setToken(loginAuth[0]);
				} else {
					final String phone = request.getParameter("phone");
					final String passWord = request.getParameter("passWord");
					if (StringUtils.isBlank(phone)) {
						throw new UserException(false, EUserLogin.EXIST_NOT_PHONE);
					}
					if (StringUtils.isBlank(passWord)) {
						throw new UserException(false, EUserLogin.EXIST_NOT_PASSWORD);
					}
					if (Regexp.checkPhone(phone) == false) {
						throw new UserException(false, EUserLogin.FORMAT_ERRO_PHONE);
					}

					userIdentity.setPhone(phone.trim());
					userIdentity.setPwd(passWord.trim());

					if (passWord.trim().length() < 6 || passWord.trim().length() > 20) {
						throw new UserException(false, EUserLogin.FORMAT_ERRO_PASSWORD);
					}
				}
				// 登录手机规格
				userIdentity.setPhoneSpecification(request.getParameter("uadetail"));
			}

			User user = userWebService.processUserLogin(userIdentity);
			if (null == user) {
				throw new UserException(false, EUserLogin.EXIST_NOT_PHONE_PASSWORD);
			} else {
				/* 增加用户封停校验 */
				if (user.getState().intValue() == UserHelper.USER_STATE_2.getMarker().intValue()) {
					modelMap.put("result", Constants.ERROR_ERROR);
					modelMap.put("errorCode", Constants.ERROR_CODE_100103);
					return "";
				} else {
					try {
						/* 完成任务 1登录任务标示 4 首次登陆 */
						taskHistoryService.insertTask(user.getId(), (long) 1, "4");
					} catch (Exception e) {
						logger.error("插入首次登陆任务报错");
					}
				}
			}

			modelMap.put("result", "1");
			// 2.0.3 版本 薪资 用户ID
			modelMap.put("userId", user.getId());
			modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
		} catch (UserException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", ex.code());
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", "100003");
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 短信找回密码验证
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/forget/sms/check", method = { RequestMethod.GET, RequestMethod.POST })
	public String forgetSmsCheck(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String phone = request.getParameter("phone");
		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		Integer checkCode = ((Double) (Math.random() * 9000 + 1000)).intValue();
		User user = userWebService.getUserByPhone(phone);
		logger.info("phone : " + phone);
		if (user != null) {
			boolean isSucc = false;
			Integer count = cacheManager.getEntityByKey(Integer.class, "sended_" + phone);// 从缓存取出该号码在最近30分钟的发送短信数量
			if (count != null) {
				logger.info("filter_用户 :  " + user.getNickName() + "电话号码 : " + phone + "在近24小时内发送短信次数 : " + count);
				if (count < 5) {
					cacheManager.addEntityByKey("sended_" + phone, ++count, 24 * 60 * 60);// 计数加1

					isSucc = AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
							"a1e8f4efd4062af22d404362f1e4ece1", "{\"number\" : " + "\"" + checkCode + "\"}", phone,
							"SMS_21255089");
					if (isSucc) {
						// cacheManager.addEntityByKey("sended_" + phone,
						// ++count, 30*60);//计数加1
					} else {
						modelMap.put("result", "0");
						modelMap.put("errorCode", Constants.ERROR_CODE_100000);
						return "";
					}
				} else {
					// 说明在30分钟之内该号码发送次数超过10次
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100000);
					return "";
				}
			} else {

				cacheManager.addEntityByKey("sended_" + phone, 1, 24 * 60 * 60);
				// 证明是180分钟内首次发短信
				isSucc = AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
						"a1e8f4efd4062af22d404362f1e4ece1", "{\"number\" : " + "\"" + checkCode + "\"}", phone,
						"SMS_21255089");
				if (isSucc) {
					// cacheManager.addEntityByKey("sended_" + phone, 1, 30*60);
				} else {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100000);
					return "";
				}
			}

			cacheManager.addEntityByKey(Constants.FROGET_SMS_CODE + phone, checkCode + "", 5 * 60);
			modelMap.put("result", "1");
		} else {

			modelMap.put("result", "-1");

		}

		return "";
	}

	/**
	 * 忘记密码重置密码
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/forget/password/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String forgetPassWordUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		logger.info("phone=" + request.getParameter("phone"));
		logger.info("smsCode=" + request.getParameter("smsCode"));
		logger.info("passWord=" + request.getParameter("passWord"));
		if (StringUtil.isEmpty(request.getParameter("phone")) || StringUtil.isEmpty(request.getParameter("smsCode"))
				|| StringUtil.isEmpty(request.getParameter("passWord"))
				|| request.getParameter("passWord").trim().length() < 6
				|| request.getParameter("passWord").trim().length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String phone = request.getParameter("phone");
		String smsCode = request.getParameter("smsCode");
		String passWord = request.getParameter("passWord");
		if (StringUtil.isEmpty(passWord) || passWord.length() < 6 || passWord.length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
		}

		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		if (!Regexp.checkDigit(smsCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		if (smsCode.length() != 4) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		String forgetSMSCode = cacheManager.getEntityByKey(String.class, Constants.FROGET_SMS_CODE + phone);
		if (!smsCode.equals(forgetSMSCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100004);
			return "";

		}
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		int result = userWebService.updatePassWdReset(userIdentity, passWord);
		modelMap.put("result", "0");
		if (result != 1) {

			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		} else {

			modelMap.put("result", "1");
			User user = userWebService.getUserByPhone(phone);
			modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
			modelMap.put("userId", user.getId());
			

		}

		return "";
	}

	/**
	 * 重置密码
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/password/reset", method = { RequestMethod.GET, RequestMethod.POST })
	public String passWordReset(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(request.getParameter("passWord"))
				|| StringUtil.isEmpty(request.getParameter("oldPassWord"))
				|| request.getParameter("passWord").trim().length() < 6
				|| request.getParameter("passWord").trim().length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String loginKey = request.getParameter("loginKey");

		String passWord = request.getParameter("passWord");
		String oldPassWord = request.getParameter("oldPassWord");

		if (StringUtil.isEmpty(oldPassWord) || oldPassWord.length() < 6 || oldPassWord.length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		if (StringUtil.isEmpty(passWord) || passWord.length() < 6 || passWord.length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		if (oldPassWord.equals(passWord)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		if (!loginKey.contains("_")) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		String[] loginAuth = TokenUtil.split(loginKey);
		UserIdentity userIdentity = new UserIdentity();

		userIdentity.setTableNum(Integer.valueOf(loginAuth[1]));
		userIdentity.setToken(loginAuth[0]);
		User user = userWebService.getUserByLoginKey(loginKey);
		modelMap.put("result", "0");
		if (user != null && MD5Util.md5Hex(oldPassWord).equals(user.getPassWord())) {
			int result = userWebService.updatePassWdReset(userIdentity, passWord);

			if (result != 1) {

				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			} else {

				modelMap.put("result", "1");

			}
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/info", method = { RequestMethod.GET, RequestMethod.POST })
	public String info(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		final String loginKey = request.getParameter("loginKey");
		// final String userId = request.getParameter("userId");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String[] loginAuth = TokenUtil.split(loginKey);
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setTableNum(Integer.valueOf(loginAuth[1]));
		userIdentity.setToken(loginAuth[0]);

		UserResult<UserAssetsInfo> userResult = userWebService.getUserAssetsInfo(userIdentity);
		if (userResult.getState() == 1) {
			modelMap.put("result", "1");
			User user = userResult.getContent().getUser();
			// 查询用户特权
			Map<String, Object> map = new HashMap<>();
			map.put("level", user.getLevel());
			UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(map);
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
			// 查询代理商标示
			int mark = agentService.selectUserCertificateLevel(userVo.getId());
			userVo.setMarkType(mark);
			// 查询用户认证标示
			UserResult<Business> businessResult = userWebService.getBusinessByUserId(userVo.getId());
			if (businessResult.getState() == 1) {
				userVo.setApproveType(businessResult.getContent().getCertificationType());
			} else {
				userVo.setApproveType(0);
			}
			UserAssets userAssets = userResult.getContent().getUserAssets();
			UserAssetsVo userAssetsVo = new UserAssetsVo();
			BeanUtils.copyProperties(userAssets, userAssetsVo);
			userAssetsVo.setGrowthValue(userAssets.getGrowthValue());// 成长值
			userVo.setGrowthPoint(userLevelConfig.getGrowthPoint());// 升级所需成长值
			int a = (int) (userLevelConfig.getLevelRatio() * 100 - 100);
			userVo.setLevelRatio(a);// 福袋金额加成
			userVo.setHomePageRedEnvelope(userLevelConfig.getHomePageRedEnvelope());// 可领取首页福袋数
			modelMap.put("user", userVo);
			modelMap.put("assets", userAssetsVo);

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 用户信息更新
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/info/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String infoUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, User userVo)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(request.getParameter("nickName"))
				|| StringUtil.isEmpty(request.getParameter("sex"))
				|| StringUtil.isEmpty(request.getParameter("mine"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.remove("user");
			return "";
		}
		String mine = request.getParameter("mine");// 确定修改操作是否是从我的来
		String loginKey = request.getParameter("loginKey");
		String nickName = request.getParameter("nickName");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			/* 用户昵称排重 */
			User u = userWebService.selectByUserNickName(nickName);
			if (u != null && !user.getNickName().equals(nickName)) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_500008);
				modelMap.remove("user");
				return "";
			}
			if (null != userVo.getHobby() || !userVo.getHobby().isEmpty()) {
				userVo.setHobby(this.changeString(userVo.getHobby()));
			} else {
				userVo.setHobby("0");
			}
			userVo.setId(user.getId());
			String[] loginAuth = TokenUtil.split(loginKey);
			userVo.setTableNum(Integer.valueOf(loginAuth[1]));
			userVo.setToken(loginAuth[0]);
			userVo.setPhone(user.getPhone());
			/* 修改个人信息 */
			if (Integer.parseInt(mine) == 1) {
				/* 判断可修改时间 */
				if (TokenUtil.isToday(user.getModifyTime()) == true) {
					userVo.setModifyTime(new Date());
					/* 根据用户生日计算用户年龄段 */
					if (userVo.getBirthday() != null) {
						try {
							userVo.setAgeGroup((byte) getAge(userVo.getBirthday()));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						userVo.setAgeGroup((byte) 0);
					}
					int result = userWebService.updateUser(userVo);
					if (result == 1) {
						try {
							/* 完善个人信息 */
							logger.info("个人信息已经完善，需要完成任务----------用户名：[{}]", user.getId());
							taskHistoryService.insertTask(user.getId(), (long) 9, "3");
						} catch (Exception e) {
							logger.error("插入完善个人信息任务报错");
						}
						modelMap.put("result", result);
					}
				} else {
					// 一天内多次修改
					modelMap.put("result", "2");
					modelMap.put("errorCode", Constants.ERROR_CODE_800100);
				}
				/* 首次登陆用户 */
			} else if (Integer.parseInt(mine) == 0) {
				int result = userWebService.updateUser(userVo);
				if (result == 1) {
					modelMap.put("result", result);
				}
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		modelMap.remove("user");
		return "";
	}

	@RequestMapping(value = "/business/info", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessInfo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Long businessId, String loginKey) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		UserResult<Business> userResult = userWebService.getBusiness(businessId);

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null && userResult.getState() == 1) {
			Business business = userResult.getContent();
			BusinessVo businessVo = new BusinessVo();
			BeanUtils.copyProperties(business, businessVo);

			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("userId", business.getUserId());
			pMap.put("relationType", 1);
			int inviteSize = socialWebService.selectListUserInviteCount(pMap);
			pMap.put("relationType", 2);
			int fansSize = socialWebService.selectListUsersRelationCount(pMap);

			businessVo.setFansSize(fansSize);
			businessVo.setInviteSize(inviteSize);

			Map<String, Object> fansMap = new HashMap<String, Object>();
			fansMap.put("userId", business.getUserId());
			fansMap.put("relationUserId", user.getId());
			fansMap.put("relationType", 2);
			int count = socialWebService.selectListUsersRelationCount(fansMap);
			modelMap.put("isFans", count > 0 ? 1 : 0);
			modelMap.put("business", businessVo);
			modelMap.put("result", "1");
		} else {

			modelMap.put("result", "0");
		}

		return "";
	}

	@RequestMapping(value = "/business", method = { RequestMethod.GET, RequestMethod.POST })
	public String business(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			modelMap.put("result", "1");
			UserResult<Business> userResult = userWebService.getBusinessByUserId(user.getId());
			if (userResult.getState() == 1) {
				Business business = userResult.getContent();
				BusinessVo businessVo = new BusinessVo();
				BeanUtils.copyProperties(business, businessVo);

				Map<String, Object> pMap = new HashMap<String, Object>();
				// 查询用户好友
				pMap.put("userId", user.getId());
				int inviteSize = socialWebService.selectListUserInviteCount(pMap);
				// 查询用户粉丝
				pMap = new HashMap<String, Object>();
				pMap.put("userId", user.getId());
				pMap.put("relationType", 2);
				int fansSize = socialWebService.selectListUsersRelationCount(pMap);
				// 查询所有广告
				pMap = new HashMap<String, Object>();
				pMap.put("states", "1,3,5");
				pMap.put("userId", user.getId());
				int advertCount = advertWebService.selectAdvertCount(pMap); // 增加广告数属性

				businessVo.setFansSize(fansSize);
				businessVo.setInviteSize(inviteSize);
				businessVo.setAdvertCount(advertCount);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", user.getId());
				UserResult<UserAgent> result = agentService.selectUserAgent(map);
				if (result != null && result.getState() == 1) {
					businessVo.setAgentLevel(result.getContent().getAgentLevel());
				} else {
					businessVo.setAgentLevel(-1);
				}
				modelMap.put("business", businessVo);

			} else {

				modelMap.put("result", "2");
			}

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/***
	 * 商户信息提交(2.0.4方法)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/submit", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessSubmit(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Business business) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(business.getBusinessName())
				|| StringUtil.isEmpty(business.getBusinessAddress()) || StringUtil.isEmpty(business.getBusinessTel())) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			modelMap.put("result", "1");
			business.setUserId(user.getId());
			UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(user.getId());
			if (userBusinessResult.getState() == 0) {
				logger.info("《《《《《《《《《《《《《《《《当前用户 :" + user.getId() + "不是商户，添加商户");
				UserResult<Business> userResult = userWebService.addBusiness(business);
				if (userResult.getState() == 1) {
					modelMap.put("business", userResult.getContent());
				} else {
					modelMap.put("result", "2");
				}
			} else {
				logger.info("当前用户 : " + user.getId() + " 是商户，不用添加商户信息");
				modelMap.put("business", userBusinessResult.getContent());
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/***
	 * 商户信息认证(多次)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/certification", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessCertification(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Business business) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(business.getBusinessAbbreviation())
				|| StringUtil.isEmpty(business.getBusinessName()) || StringUtil.isEmpty(business.getBusinessIndustry())
				|| StringUtil.isEmpty(business.getRegistrationNumber())
				|| StringUtil.isEmpty(business.getBusinessCredentialsUp())
				|| StringUtil.isEmpty(business.getBusinessCredentialsDown())
				|| StringUtil.isEmpty(business.getBusinessCredentialsFace())
				|| StringUtil.isEmpty(business.getBusinessAddress()) || StringUtil.isEmpty(business.getDetailAddress())
				|| StringUtil.isEmpty(business.getApplyName()) || StringUtil.isEmpty(business.getBusinessTel())) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			modelMap.put("result", "1");
			business.setUserId(user.getId());
			UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(user.getId());
			business.setCertificationType(2);
			business.setBusinessLogo(business.getBusinessCredentialsUp());
			business.setModfiyTime(new Date());
			if (userBusinessResult.getState() == 0) {
				logger.info("《《《《《《《《《《《《《《《《当前用户 :" + user.getId() + "未做商户认证，添加认证商户信息");
				UserResult<Business> userResult = userWebService.addBusiness(business);
				if (userResult.getState() == 1) {
					modelMap.put("business", userResult.getContent());
				} else {
					modelMap.put("result", "2");
				}
			} else {
				logger.info("当前用户 : " + user.getId() + " 已做认证，二次添加认证信息");
				business.setId(userBusinessResult.getContent().getId());
				UserResult<Business> userResult = userWebService.updateMultipleAuthentication(business);
				if (userResult.getState() == 1) {
					modelMap.put("business", userResult.getContent());
				} else {
					modelMap.put("result", "2");
					modelMap.put("business", userBusinessResult.getContent());
				}
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/***
	 * 个人信息认证(多次)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/personal/certification", method = { RequestMethod.GET, RequestMethod.POST })
	public String personalCertification(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Business business) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(business.getIdentityCard())
				|| StringUtil.isEmpty(business.getBusinessCredentialsUp())
				|| StringUtil.isEmpty(business.getBusinessCredentialsDown())) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			modelMap.put("result", "1");
			business.setUserId(user.getId());
			UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(user.getId());
			business.setBusinessAbbreviation(user.getNickName());
			business.setBusinessTel(user.getPhone());
			business.setBusinessLogo(user.getAvatar());
			business.setCertificationType(1);
			if (userBusinessResult.getState() == 0) {
				logger.info("《《《《《《《《《《《《《《《《当前用户 :" + user.getId() + "未做个人认证，添加个人认证信息");
				UserResult<Business> userResult = userWebService.addBusiness(business);
				if (userResult.getState() == 1) {
					modelMap.put("business", userResult.getContent());
				} else {
					modelMap.put("result", "2");
				}
			} else {
				logger.info("当前用户 : " + user.getId() + " 已做认证，二次添加认证信息");
				business.setId(userBusinessResult.getContent().getId());
				UserResult<Business> userResult = userWebService.updateMultipleAuthentication(business);
				if (userResult.getState() == 1) {
					modelMap.put("business", userResult.getContent());
				} else {
					modelMap.put("result", "2");
					modelMap.put("business", userBusinessResult.getContent());
				}
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 商户信息更新(2.0.4方法)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Business business, Long businessId) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || businessId <= 0) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {

			business.setId(businessId);
			// if(business.getState()==3){
			// business.setState((byte) 5);
			// }

			UserResult<Integer> userResult = userWebService.updateBusiness(business);
			if (userResult.getState() == 1) {
				modelMap.put("result", "1");
			} else {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			}

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 商户(个人)认证信息更新
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/certificationUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessCertificationUpdate(HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap, Business business) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || business.getId() <= 0
				|| business.getCertificationType() <= 0) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {

			// business.setId(businessId);
			// if(business.getState()==3){
			// business.setState((byte) 5);
			// }
			if (business.getCertificationType() == 2) {
				business.setBusinessCredentialsUp(business.getBusinessLogo());
			}
			UserResult<Integer> userResult = userWebService.updateBusiness(business);
			if (userResult.getState() == 1) {
				modelMap.put("result", "1");
			} else {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			}

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 内部注册
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	public String insideRegister(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		if (StringUtil.isEmpty(request.getParameter("phone")) || StringUtil.isEmpty(request.getParameter("smsCode"))
				|| StringUtil.isEmpty(request.getParameter("passWord"))
				|| request.getParameter("passWord").trim().length() < 6
				|| request.getParameter("passWord").trim().length() > 20) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String phone = request.getParameter("phone").trim();
		String smsCode = request.getParameter("smsCode").trim();
		String passWord = request.getParameter("passWord").trim();
		String inviteCode = request.getParameter("inviteCode");

		logger.info("phone=" + phone + ",smsCode=" + smsCode + ",inviteCode=" + inviteCode);
		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		if (!Regexp.checkDigit(smsCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		if (smsCode.length() != 4) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}

		String regSMSCode = cacheManager.getEntityByKey(String.class, Constants.REG_SMS_CODE + phone);
		if (!smsCode.equals(regSMSCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100004);
			return "";

		}

		if (StringUtil.isNotEmpty(inviteCode)) {

			UserResult<User> userResult = userWebService.selectUserByUserId(Long.valueOf(inviteCode));
			if (userResult.getState() != 1) {

				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100015);
				return "";
			}
		}

		User selectUser = userWebService.getUserByPhone(phone);
		if (selectUser == null) {
			User user = new User();
			user.setPhone(phone);
			user.setPassWord(passWord);
			user = userWebService.saveRegister(user);
			// 默认游客昵称为 游客+id后5位
			String userId = user.getId().toString();
			user.setNickName("游客" + userId.substring(userId.length() - 5));
			userWebService.updateUser(user);
			if (inviteCode != null && inviteCode.length() != 0) {
				bindInviteRelation(user, inviteCode);
				/*
				 * // 进行关注 Map<String, Object> map = new HashMap<String,
				 * Object>(); UsersRelation usersRelation = new UsersRelation();
				 * 查询被关注者信息 UserResult<User> followedUserResult =
				 * userWebService.selectUserByUserId(Long.valueOf(inviteCode));
				 * User followedUser = followedUserResult.getContent();
				 * 
				 * usersRelation.setRelationUserId(user.getId());
				 * usersRelation.setRelationType((byte) 2);// 代表粉丝
				 * usersRelation.setUserId(Long.parseLong(inviteCode));
				 * usersRelation.setRelationUserNickName(user.getNickName());
				 * 
				 * usersRelation.setRelationUserAvatar(user.getAvatar());
				 * usersRelation.setCreateTime(new Date());
				 * SocialResult<UsersRelation> relationAddResult =
				 * usersRelationService.addUsersRelation(usersRelation);
				 * 
				 * if (relationAddResult.getState() == 1) {
				 * user.setFollowCount(user.getFollowCount() + 1);// 关注者关注人数加1
				 * followedUser.setFansCount(followedUser.getFansCount() + 1);//
				 * 被关注者粉丝数加1 userWebService.updateUser(user);
				 * userWebService.updateUser(followedUser); }
				 */
			}
			modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
			modelMap.put("userId", user.getId());
			modelMap.put("result", "1");
		} else {

			modelMap.put("result", "2");
		}

		return "";
	}

	/***
	 * 用户注册(外部注册)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	public String outsideRegister(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String thirdUid = request.getParameter("thirdUid");
		logger.info("thirdUid=" + thirdUid);
		String thirdName = request.getParameter("thirdName");
		logger.info("thirdName" + thirdName);
		if (StringUtil.isEmpty(request.getParameter("thirdUid"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		// 查询thirdUid是否存在
		// thirdUid与phone在user_part_mapper表中为同一列
		User selectUser = userWebService.getUserByThird(thirdUid);
		if (selectUser == null) {
			User user = new User();
			user.setThirdUid(thirdUid);
			if (thirdName == null) {
				thirdName = "";
			}

			user.setName(thirdName);
			user = userWebService.saveRegisterByThirdUid(user);
			// 默认游客昵称为 游客+id后5位
			String userId = user.getId().toString();
			user.setNickName("游客" + userId.substring(userId.length() - 5));
			userWebService.updateUser(user);
			try {
				/* 完成任务 1登录任务标示 4 首次登陆 */
				taskHistoryService.insertTask(user.getId(), (long) 1, "4");
			} catch (Exception e) {
				logger.error("插入首次登陆任务报错");
			}
			modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
			modelMap.put("userId", user.getId());
			modelMap.put("result", "1");
		} else {
			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setTableNum(selectUser.getTableNum());
			userIdentity.setThirdUid(selectUser.getThirdUid());
			User user = userWebService.outsideLogin(userIdentity);
			/* 增加用户封停校验 */
			if (user.getState().intValue() == UserHelper.USER_STATE_2.getMarker().intValue()) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_100103);
				return "";
			} else {
				try {
					/* 完成任务 1登录任务标示 4 首次登陆 */
					taskHistoryService.insertTask(user.getId(), (long) 1, "4");
				} catch (Exception e) {
					logger.error("插入首次登陆任务报错");
				}
			}
			modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
			modelMap.put("userId", user.getId());
			modelMap.put("result", "2");
		}

		return "";

	}

	/***
	 * 商户信息认证(3.0版本)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/approve", method = { RequestMethod.GET, RequestMethod.POST })
	public String businessApprove(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			BusinessTemp businessTemp) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(businessTemp.getBusinessName())
				|| StringUtil.isEmpty(businessTemp.getBusinessIndustry())
				|| StringUtil.isEmpty(businessTemp.getBusinessTel()) || StringUtil.isEmpty(businessTemp.getProvince())
				|| StringUtil.isEmpty(businessTemp.getCity()) || StringUtil.isEmpty(businessTemp.getArea())
				|| StringUtil.isEmpty(businessTemp.getPhone())
				|| StringUtil.isEmpty(businessTemp.getBusinessCredentialsUp())
				|| StringUtil.isEmpty(businessTemp.getBusinessCredentialsFace())) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		logger.info("userID:" + user.getId());
		if (user != null) {
			businessTemp.setUserId(user.getId());
			businessTemp.setState((byte) 1);
			// 查找中间表是否存在该用户
			BusinessTemp bus = userWebService.selectBussinessTempByUserId(user.getId());
			if (bus == null) {
				logger.info("《《《《《《《《《《《《《《《《当前用户 :" + user.getId() + "未做商户认证，添加认证商户信息");
				UserResult<BusinessTemp> userResult = userWebService.addBusinessInTemp(businessTemp);
				if (userResult.getState() == 1) {
					modelMap.put("result", "1");
				}
			} else if (bus != null && bus.getState() != (byte) 1) {
				businessTemp.setCertificationType(2);
				businessTemp.setState((byte) 1);
				// 中间表数据状态不为 待审核 可进行修改操作
				logger.info("《《《《《《《《《《《《《《《《当前用户 : " + user.getId() + " 已做认证，二次添加认证信息");
				UserResult<BusinessTemp> userResult = userWebService.updateBussinessInTemp(businessTemp);
				if (userResult.getState() == 1) {
					modelMap.put("result", "1");
				}
			} else {
				logger.info("《《《《《《《《《《《《《《《《当前用户 : " + user.getId() + " 今日已做过新增或修改认证信息操作");
				// 错误原因：多次提交
				modelMap.put("result", "2");
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("businessTemp");
		return "";
	}

	/***
	 * 个人信息认证(3.0版本)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/personal/approve", method = { RequestMethod.GET, RequestMethod.POST })
	public String personalApprove(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			BusinessTemp businessTemp) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(businessTemp.getBusinessCredentialsDown())
				|| StringUtil.isEmpty(businessTemp.getApplyName()) || StringUtil.isEmpty(businessTemp.getPhone())) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			businessTemp.setUserId(user.getId());
			UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(user.getId());
			// 如果认证信息为商户认证 不执行个人认证操作
			if (userBusinessResult.getContent() != null
					&& userBusinessResult.getContent().getCertificationType() == 2) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100000);
				return "";
			}
			businessTemp.setBusinessAbbreviation(user.getNickName());
			businessTemp.setBusinessTel(user.getPhone());
			businessTemp.setBusinessLogo(user.getAvatar());
			businessTemp.setCertificationType(1);
			BusinessTemp bus = userWebService.selectBussinessTempByUserId(user.getId());
			if (bus == null) {
				logger.info("《《《《《《《《《《《《《《《《当前用户 :" + user.getId() + "未做个人认证，添加个人认证信息");
				UserResult<BusinessTemp> userResult = userWebService.addBusinessInTemp(businessTemp);
				if (userResult.getState() == 1) {
					modelMap.put("result", "1");
				} else {
					modelMap.put("result", "2");
				}
			} else if (bus.getState() != (byte) 1) {
				logger.info("当前用户 : " + user.getId() + " 已做认证，二次添加认证信息");
				businessTemp.setUserId(user.getId());
				businessTemp.setState((byte) 1);
				UserResult<BusinessTemp> userResult = userWebService.updateBussinessInTemp(businessTemp);
				if (userResult.getState() == 1) {
					modelMap.put("result", "1");
				}
			} else {
				logger.info("《《《《《《《《《《《《《《《《当前用户 : " + user.getId() + " 今日已做过新增或修改认证信息操作");
				// 错误原因：多次提交
				modelMap.put("result", "2");
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("businessTemp");
		return "";
	}

	/**
	 * 显示商户（个人）认证信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/showInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String UserBusinessInfo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		int result = 0;
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {

			BusinessTemp businessTemp = userWebService.selectBussinessTempByUserId(user.getId());
			if (businessTemp != null && businessTemp.getState() == 1) {
				modelMap.put("business", businessTemp);
				result = 1;
			} else {
				UserResult<Business> userResult = userWebService.getBusinessByUserId(user.getId());
				if (userResult.getContent() != null) {
					modelMap.put("business", userResult.getContent());
					result = 1;
				} else {
					result = 2;// 没有认证信息
				}
			}
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 绑定手机
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/bindingPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public String bindingPhone(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(request.getParameter("bindingMobile"))
				|| StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("smsCode"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String phone = request.getParameter("bindingMobile").trim();
		// 验证码
		String smsCode = request.getParameter("smsCode").trim();
		String loginKey = request.getParameter("loginKey").trim();

		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "2");
			modelMap.put("errorCode", EUserLogin.FORMAT_ERRO_PHONE);
			return "";
		}
		if (!Regexp.checkDigit(smsCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		if (smsCode.length() != 4) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		String regSMSCode = cacheManager.getEntityByKey(String.class, Constants.BINDING_SMS_CODE + phone);
		if (!smsCode.equals(regSMSCode)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100004);
			return "";
		}

		// 解析：loginKey
		String[] loginAuth = TokenUtil.split(loginKey);
		User user = new User();
		user.setTableNum(Integer.valueOf(loginAuth[1]));
		user.setToken(loginAuth[0]);
		user.setBindingMobile(phone);
		int result = userWebService.updateUserBindingPhone(user);
		if (result == 1) {
			modelMap.put("result", "1");
		}
		return "";
	}

	/**
	 * 根据用户id进入商户主页
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/searchHomePageBusiness", method = RequestMethod.GET)
	public String searchHomePageBusiness(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String userId = request.getParameter("userId");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(userId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		Map<String, Object> map = new HashMap<>();
		AdvertVo advertVo = new AdvertVo();
		if (user != null) {
			/* 查询用户昵称、头像、等级、省份、市 、性别、关注人数 */
			UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(userId));
			User rUser = resultUser.getContent();
			if (resultUser != null && rUser != null) {
				advertVo.setNickName(rUser.getNickName());
				advertVo.setAvatar(rUser.getAvatar());
				advertVo.setUserLevel(rUser.getLevel());
				advertVo.setProvince(rUser.getProvince());
				advertVo.setCity(rUser.getCity());
				advertVo.setSex((int) rUser.getSex());
				advertVo.setFansCount(rUser.getFansCount() < 0 ? 0 : rUser.getFansCount());
			}
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(Long.valueOf(userId)).getContent();
			if (business != null) {
				advertVo.setProvince(business.getProvince());
				advertVo.setCity(business.getCity());
				advertVo.setArea(business.getArea());
				advertVo.setBusinessAddress(business.getBusinessAddress());
				advertVo.setCertificationType(business.getCertificationType());
			}
			/* 查询关注状态 */
			map.put("userId", userId); // 被关注者id
			map.put("relationUserId", user.getId()); // 关注者id
			map.put("relationType", AdvertHelper.FOLLOW_TYPE_2.getMarker());
			SocialResult<UsersRelation> userRelationResult = userRelationService.selectUsersRelation(map);
			if (userRelationResult.getState() == AdvertHelper.RESULT_STATE_1.getMarker().intValue()) {
				advertVo.setFollowState(AdvertHelper.FOLLOW_STATE_1.getMarker());
			} else {
				advertVo.setFollowState(AdvertHelper.FOLLOW_STATE_0.getMarker());
			}
			/* 查询人气 */
			map.clear();
			map.put("userId", userId);
			int count = advertWebService.selectViewTimesCount(map);
			count = count < 0 ? 0 : count;
			advertVo.setViewTimes(count);
			/* 查询首页广告 */
			map.clear();
			map.put("userId", userId);
			map.put("advertType", BusinessShopHelper.ADVERT_TYPE_5.getMarker());
			map.put("order", "create_time desc");
			Advert advert = advertWebService.selectAdvertByMap(map);
			if (advert != null) {
				/* 增加首页广告人气 */
				advertWebService.addAdvertViewTimes(advert);
				BeanUtils.copyProperties(advert, advertVo);
				/* 查询未领取金额 */
				map.clear();
				map.put("advertId", advert.getId());
				AdvertStatistics advertStatistics = advertWebService.selectAdvertStatistics(map);
				if (advertStatistics != null) {
					advertVo.setBalance(advertVo.getRedEnvelopeAmount() - advertStatistics.getSendRedEnvelopeAmount());
				}
			}
			/* 查询用户身份类型 */
			map.clear();
			map.put("userId", userId);
			map.put("state", BusinessShopHelper.STATE_1.getMarker());
			UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);
			if (userResult != null && userResult.getContent() != null) {
				int agentLevel = userResult.getContent().getAgentLevel().intValue();
				agentLevel = agentLevel == 0 || agentLevel == 1 ? 0 : agentLevel + 1;
				if (agentLevel > 0) {
					advertVo.setCertificationType(agentLevel);
				}
			}
			/* 查询商户店铺信息 */
			map.clear();
			map.put("userId", userId);
			BusinessShop shop = businessShopService.queryShopByMap(map).getContent();
			if (shop != null) {
				advertVo.setShopSign(shop.getShopSign());
				advertVo.setFirstPic(shop.getFirstPic());
				advertVo.setSecondPic(shop.getSecondPic());
				advertVo.setThirdPic(shop.getThirdPic());
				advertVo.setFourPic(shop.getFourPic());
				advertVo.setFivePic(shop.getFivePic());
				advertVo.setSixPic(shop.getSixPic());
			}
			advertVo.setUserId(Long.valueOf(userId));
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("businessVo", advertVo);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 根据用户id进入商户历史主页
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/searchHistoryHomePageBusiness", method = RequestMethod.GET)
	public String searchHistoryHomePageBusiness(HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String userId = request.getParameter("userId");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(userId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		Map<String, Object> map = new HashMap<>();
		AdvertVo userVo = new AdvertVo();
		List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
		if (user != null) {
			/* 查询用户昵称、头像、等级、省份、市、性别 */
			UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(userId));
			if (resultUser != null && resultUser.getContent() != null) {
				userVo.setNickName(resultUser.getContent().getNickName());
				userVo.setAvatar(resultUser.getContent().getAvatar());
				userVo.setUserLevel(resultUser.getContent().getLevel());
				userVo.setProvince(resultUser.getContent().getProvince());
				userVo.setCity(resultUser.getContent().getCity());
				userVo.setSex((int) resultUser.getContent().getSex());
			}
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(Long.valueOf(userId)).getContent();
			if (business != null) {
				userVo.setProvince(business.getProvince());
				userVo.setCity(business.getCity());
				userVo.setCertificationType(business.getCertificationType());
			}
			/* 查询用户身份类型 */
			map.clear();
			map.put("userId", userId);
			map.put("state", BusinessShopHelper.STATE_1.getMarker());
			UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);
			if (userResult != null && userResult.getContent() != null) {
				int agentLevel = userResult.getContent().getAgentLevel().intValue();
				agentLevel = agentLevel == 0 || agentLevel == 1 ? 0 : agentLevel + 1;
				if (agentLevel > 0) {
					userVo.setCertificationType(agentLevel);
				}
			}
			/* 查询商户店铺信息 */
			map.clear();
			map.put("userId", userId);
			BusinessShop shop = businessShopService.queryShopByMap(map).getContent();
			if (shop != null) {
				userVo.setShopSign(shop.getShopSign());
			}
			/* 查询用户福袋广告 */
			Integer begin = 0;
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty())
				begin = Integer.valueOf(beginStr);
			map.clear();
			map.put("userId", userId);
			map.put("advertTypes",
					LuckyBagHelper.ADVERT_TYPE_0.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_1.getMarker() + ","
							+ LuckyBagHelper.ADVERT_TYPE_2.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_6.getMarker()
							+ "," + LuckyBagHelper.ADVERT_TYPE_8.getMarker());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			ListRange<Advert> advertsList = advertWebService.selectListRangeAdvert(map); // 默认返回按创建时间排序
			if (advertsList != null && advertsList.getData().size() > 0) {
				for (Advert advert : advertsList.getData()) {
					AdvertVo advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					long relationId = advertVo.getRelationId() == null ? 0 : advertVo.getRelationId();
					if (relationId > 0) {
						BusinessCoupon businessCoupon = businessCouponService.selectBusinessCouponById(relationId);
						if (null != businessCoupon) {
							int couponType = businessCoupon.getCouponType();
							advertVo.setCouponType(couponType);
							switch (couponType) {
							case 1:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getMoney()));
								break;
							case 2:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getDiscount()));
								break;
							case 3:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getPhysicalVolume()));
								break;
							default:
								break;
							}
						}
					}

					advertVoList.add(advertVo);
				}
			}
			userVo.setUserId(Long.valueOf(userId));
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("userVo", userVo);
			modelMap.put("advertList", advertVoList);
			modelMap.put("page", advertsList.getPage());
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 根据用户id进入用户主页
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/searchHomePageUser", method = RequestMethod.GET)
	public String searchHomePageUser(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String userId = request.getParameter("userId");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(userId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		Map<String, Object> map = new HashMap<>();
		AdvertVo userVo = new AdvertVo();
		List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
		if (user != null) {
			/* 查询用户昵称、头像、等级、省份、市、性别 、关注人数 */
			UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(userId));
			User rUser = resultUser.getContent();
			if (resultUser != null && rUser != null) {
				userVo.setNickName(rUser.getNickName());
				userVo.setAvatar(rUser.getAvatar());
				userVo.setUserLevel(rUser.getLevel());
				userVo.setProvince(rUser.getProvince());
				userVo.setCity(rUser.getCity());
				userVo.setSex((int) rUser.getSex());
				userVo.setFansCount(rUser.getFansCount() < 0 ? 0 : rUser.getFansCount());
			}
			/* 查询关注状态 */
			map.put("userId", userId); // 被关注者id
			map.put("relationUserId", user.getId()); // 关注者id
			map.put("relationType", AdvertHelper.FOLLOW_TYPE_2.getMarker());
			SocialResult<UsersRelation> userRelationResult = userRelationService.selectUsersRelation(map);
			if (userRelationResult.getState() == AdvertHelper.RESULT_STATE_1.getMarker().intValue()) {
				userVo.setFollowState(AdvertHelper.FOLLOW_STATE_1.getMarker());
			} else {
				userVo.setFollowState(AdvertHelper.FOLLOW_STATE_0.getMarker());
			}
			/* 查询人气 */
			map.clear();
			map.put("userId", userId);
			int count = advertWebService.selectViewTimesCount(map);
			count = count < 0 ? 0 : count;
			userVo.setViewTimes(count);
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(Long.valueOf(userId)).getContent();
			if (business != null) {
				userVo.setProvince(business.getProvince());
				userVo.setCity(business.getCity());
				userVo.setCertificationType(business.getCertificationType());
			}
			/* 查询用户身份类型 */
			map.clear();
			map.put("userId", userId);
			map.put("state", BusinessShopHelper.STATE_1.getMarker());
			UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);
			if (userResult != null && userResult.getContent() != null) {
				int agentLevel = userResult.getContent().getAgentLevel().intValue();
				agentLevel = agentLevel == 0 || agentLevel == 1 ? 0 : agentLevel + 1;
				if (agentLevel > 0) {
					userVo.setCertificationType(agentLevel);
				}
			}
			/* 查询商户店铺信息 */
			// map.clear();
			// map.put("userId", userId);
			// BusinessShop shop =
			// businessShopService.queryShopByMap(map).getContent();
			// if(shop != null){
			// userVo.setShopSign(shop.getShopSign());
			// }
			/* 查询用户福袋广告 */
			Integer begin = 0;
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty())
				begin = Integer.valueOf(beginStr);
			map.clear();
			map.put("userId", userId);
			map.put("advertTypes",
					LuckyBagHelper.ADVERT_TYPE_0.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_1.getMarker() + ","
							+ LuckyBagHelper.ADVERT_TYPE_2.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_6.getMarker()
							+ "," + LuckyBagHelper.ADVERT_TYPE_8.getMarker());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			ListRange<Advert> advertsList = advertWebService.selectListRangeAdvert(map); // 默认返回按创建时间排序
			if (advertsList != null && advertsList.getData().size() > 0) {
				for (Advert advert : advertsList.getData()) {
					AdvertVo advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					advertVoList.add(advertVo);
				}
			}
			userVo.setUserId(Long.valueOf(userId));
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("userVo", userVo);
			modelMap.put("advertList", advertVoList);
			modelMap.put("page", advertsList.getPage());
			modelMap.put("toNative", 1);// H5跳原生界面判断条件
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 根据用户id查询用户身份
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/queryUserIdentity", method = RequestMethod.GET)
	public String queryUserIdentity(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String userId = request.getParameter("userId");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(userId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		Map<String, Object> map = new HashMap<>();
		UserVo userVo = new UserVo();
		int userIdentity = UserHelper.USER_IDENTITY_1.getMarker();
		if (user != null) {
			UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(userId));
			User rUser = resultUser.getContent();
			userVo.setUserIdentity(userIdentity);
			/* 查询用户昵称、头像、性别、省份、城市 、区县、详细地址、电话 */
			BeanUtils.copyProperties(rUser, userVo);
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(Long.valueOf(userId)).getContent();
			if (business != null) {
				userVo.setUserIdentity(business.getCertificationType());
				userVo.setBusinessName(business.getBusinessName());
				userVo.setProvince(business.getProvince());
				userVo.setCity(business.getCity());
				userVo.setArea(business.getArea());
				userVo.setAddress(business.getDetailAddress());
				userVo.setPhone(business.getBusinessTel());
			}
			/* 查询商户店铺信息 */
			map.clear();
			map.put("userId", userId);
			BusinessShop shop = businessShopService.queryShopByMap(map).getContent();
			if (shop != null) {
				userVo.setUserIdentity(UserHelper.USER_IDENTITY_3.getMarker());
			}
			/* 查询用户身份类型 */
			int agentLevel = userAgentService.selectUserCertificateLevel(Long.valueOf(userId));
			userVo.setMarkType(agentLevel);
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("userVo", userVo);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 获取所有系统消息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/message/gain", method = RequestMethod.GET)
	public String gainMessage(HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");
		User user = userWebService.getUserByLoginKey(loginKey);
		int result = 0;
		if (user != null) {
			List<SystemMessage> messageList = systemMessageService.selectAllMessage(user.getId());
			result = 1;
			modelMap.put("messageList", messageList);
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 浏览系统消息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/message/read", method = { RequestMethod.GET, RequestMethod.POST })
	public String readMessage(SystemMessage systemMessage, HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		int result = 0;
		User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));
		if (user != null) {
			// 查询用户所有未读系统消息
			List<SystemMessage> ListMessage = systemMessageService.selectUserNotReadMessage(user);
			if (ListMessage == null || ListMessage.size() == 0) {
				// 查询结果为空 所有消息都读了 返回2
				result = 2;
			} else {
				// 遍历未读消息 逐个添加阅读记录
				for (SystemMessage sysMessage : ListMessage) {
					sysMessage.setUserId(user.getId());
					sysMessage.setSysId(sysMessage.getId());
					sysMessage.setType(1);
					int num = systemMessageService.insertUserReadMessage(sysMessage);
					if (num == 1) {
						result = num;
					} else {
						modelMap.put("errorCode", Constants.ERROR_CODE_100102);
						break;
					}
				}
			}
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		modelMap.remove("systemMessage");

		modelMap.put("result", result);
		return "";
	}

	/**
	 * 获取用户未读系统消息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/message", method = { RequestMethod.GET, RequestMethod.POST })
	public String message(HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String loginKey = request.getParameter("loginKey");
		// 解析：loginKey
		String[] loginAuth = TokenUtil.split(loginKey);
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setTableNum(Integer.valueOf(loginAuth[1]));
		userIdentity.setToken(loginAuth[0]);
		int result = 0;
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			// 获取用户所有未读的系统消息
			List<SystemMessage> messageList = systemMessageService.selectUserNotReadMessage(user);
			result = 1;
			modelMap.put("messageList", messageList);
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 删除系统消息（逻辑删除）
	 * 
	 * @param systemMessage
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/message/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteMessage(SystemMessage systemMessage, HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("sysId")) || StringUtil.isEmpty(request.getParameter("userId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		int result = 0;
		// 查询用户是否有浏览记录
		List<SystemMessage> ListMessage = systemMessageService.selectSystemMessageByUserId(systemMessage);
		if (ListMessage == null || ListMessage.size() == 0) {
			// 查询结果为空 添加阅读用户记录
			systemMessage.setType(2);

			result = systemMessageService.insertUserReadMessage(systemMessage);
		} else {
			if (ListMessage.get(0).getType() != 2) {
				// 用户读取状态不等于2 修改状态信息
				systemMessage.setType(2);
				result = systemMessageService.updateUserReadMessage(systemMessage);
			}
		}
		modelMap.remove("systemMessage");

		modelMap.put("result", result);
		return "";
	}

	/**
	 * 根据用户loginKey查询代理商维护模板信息
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/queryAgentTemplate", method = RequestMethod.GET)
	public String queryAgentTemplate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		AdvertVo userVo = new AdvertVo();
		if (user != null) {
			/* 添加代理商模板信息 */
			List<AgentTemplateVo> templateVos = new ArrayList<>();
			AgentTemplateVo templateVo = new AgentTemplateVo();
			String starLevelStr = "除享受普通会员政策之外，还可获取所推广普通会员广告费3%收益。"; // 特权信息为一条数据时不需要加\n
			int starLevelAmount = 980;
			String centreStr = "1.享受所推广普通会员广告费6%收益；\n2.获得所推广星级代理广告费3%收益；\n3.可分流20个星级代理，免费参加公司大型活动。";
			int centreAmount = 20000;
			String countyStr = "1.享受所推广会员广告费7%收益；\n2.享受所推广中心代理广告费1%收益；\n3.享受推广星级代理广告费4%收益；\n4.享受推广区域广告费总额3%收益；\n5.按比例分流中心代理/星级代理；\n前199名可享受公司价值5万元的原始期权。";
			int countyAmount = 100000;
			templateVo = new AgentTemplateVo((long) 1, 4, starLevelStr, starLevelAmount);
			templateVos.add(templateVo);
			templateVo = new AgentTemplateVo((long) 2, 3, centreStr, centreAmount);
			templateVos.add(templateVo);
			templateVo = new AgentTemplateVo((long) 3, 2, countyStr, countyAmount);
			templateVos.add(templateVo);
			/* 查询用户身份类型 */
			int certificationType = userAgentService.selectUserCertificateLevel(user.getId());
			userVo.setCertificationType(certificationType);
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("userVo", userVo);
			modelMap.put("templateVos", templateVos);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 绑定手机验证码
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/binding/sms/check", method = { RequestMethod.GET, RequestMethod.POST })
	public String bindingSmsCheck(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String phone = request.getParameter("phone");
		if (!Regexp.checkPhone(phone)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		Integer checkCode = ((Double) (Math.random() * 9000 + 1000)).intValue();
		logger.info("phone : " + phone);
		boolean isSucc = false;
		isSucc = AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
				"a1e8f4efd4062af22d404362f1e4ece1", "{\"number\" : " + "\"" + checkCode + "\"}", phone, "SMS_31975020");
		if (isSucc) {
			// cacheManager.addEntityByKey("sended_" + phone,
			// ++count, 30*60);//计数加1
			cacheManager.addEntityByKey(Constants.BINDING_SMS_CODE + phone, checkCode + "", 5 * 60);
			modelMap.put("result", "1");
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			return "";
		}
		return "";
	}
	// /**
	// * 查询我的主页
	// * @param request
	// * @param modelMap
	// * @return
	// */
	//
	// @RequestMapping(value = "/mine/myInfo", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String myInfo(HttpServletRequest request, ModelMap modelMap){
	// String loginKey = request.getParameter("loginKey");
	// if(StringUtil.isEmpty(loginKey)){
	// modelMap.put("result", Constants.ERROR_ERROR);
	// modelMap.put("errorCode", Constants.ERROR_CODE_100001);
	// return "";
	// }
	// User user = userWebService.getUserByLoginKey(loginKey);
	// if(user != null){
	// Map<String,Object> map =new HashMap<String,Object>();
	// Integer level=user.getLevel()==0 ? 1 :user.getLevel();
	// map.put("level", level);
	// UserLevelConfig levelConfig
	// =userLevelConfigService.selectUserLevelConfig(map);
	// if(levelConfig != null){
	// Integer homePageRedEnvelope=levelConfig.getHomePageRedEnvelope();
	// Integer growthPoint =levelConfig.getGrowthPoint();
	// Map<String,Object> myInfo =new HashMap<String,Object>();
	// UserAssets assets = userAssetsService.selectUserAssets(user.getId());
	// Integer cashBalance = assets.getCashBalance();
	// Integer redEnvelopeBalance=assets.getRedEnvelopeBalance();
	// String nickName =user.getNickName();
	// String avatar =user.getAvatar();
	//
	// Integer userCertifcate
	// =userAgentService.selectUserCertificateLevel(user.getId());
	//
	// Map<String,Object> mapConfig =new HashMap<String,Object>();
	// List list =new ArrayList();
	//
	// myInfo.put("avatar", avatar); //用户图像
	// myInfo.put("nickName", nickName); //用户昵称
	// myInfo.put("userCertifcate",userCertifcate); //用户认证标识
	// myInfo.put("level", level); //用户等级
	// myInfo.put("growthPoint", growthPoint); //升级所需成长值
	//
	// mapConfig.put("redEnvelopeAdditionalAward", 0.1); //福袋金额
	// mapConfig.put("homePageRedEnvelope", homePageRedEnvelope);//可领福袋个数
	// mapConfig.put("isShopping", 1); //商城购买
	// list.add(mapConfig);
	//
	// myInfo.put("cashBalance", cashBalance); //广告币
	// myInfo.put("redEnvelopeBalance", redEnvelopeBalance); //零钱
	// myInfo.put("redEnvelope", list);
	//
	// modelMap.put("result", Constants.ERROR_OK);
	// modelMap.put("myInfoMap", myInfo);
	// }
	//
	// }else{
	// modelMap.put("result", Constants.ERROR_ERROR);
	// modelMap.put("errorCode", Constants.ERROR_CODE_100101);
	// }
	// return "";
	// }

	@RequestMapping(value = "/fansAndFollows", method = { RequestMethod.GET, RequestMethod.POST })
	public String fansAndFollows(HttpServletRequest request, ModelMap modelMap) throws Exception {
		String loginKey = request.getParameter("loginKey");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		Integer begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		String relationType = request.getParameter("relationType").toString();
		byte relationNum = Byte.valueOf(relationType).byteValue();
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			if (relationNum == 1) {
				Map<String, Object> fansMap = new HashMap<String, Object>();

				fansMap.put("userId", user.getId());

				fansMap.put("relationType", 2);
				fansMap.put("order", "create_time desc");
				fansMap.put("page", new Page(begin, 10));
				// List<UsersRelation> fansList =
				// userRelationService.selectRelation(fansMap);
				List<UsersRelation> fansList = userRelationService.selectListUsersRelationPage(fansMap).getContent()
						.getData();
				int fansCount = userRelationService.selectListUsersRelationCount(fansMap);
				List fansListConfig = new ArrayList<>(); // 存粉丝的集合
				if (fansList != null && fansList.size() > 0) {
					// int fansListCount =fansList.size();
					// user.setFansCount(fansListCount);
					// userWebService.updateUser(user); //更改用户粉丝数量
					// fansCount =user.getFansCount();
					Map<String, Object> fansUser;
					for (UsersRelation usersRelation : fansList) {

						fansUser = new HashMap<String, Object>();
						Date createTime = usersRelation.getCreateTime(); // 建立时间

						Long relationUserId = usersRelation.getRelationUserId(); // 粉丝ID

						UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(relationUserId));
						User fUser = resultUser.getContent();
						if (fUser != null) {
							String relationUserAvatar = fUser.getAvatar(); // 用户图像
							Integer relationUserSex = fUser.getSex(); // 用户性别
							String relationUserNickName = fUser.getNickName(); // 关系用户名称

							String province = fUser.getProvince(); // 粉丝的省份
							String city = fUser.getCity(); // 粉丝的城市
							if (province == null) {
								province = "";
							}

							if (province != null && province.endsWith("省")) {
								province = province.substring(0, province.length() - 1);
							}
							if (city == null) {
								city = "";
							}
							if (city != null && city.endsWith("市")) {
								city = city.substring(0, city.length() - 1);
							}
							String address = province + city;

							Integer userCertifcate = userAgentService.selectUserCertificateLevel(fUser.getId());// 用户认证标识

							Business business = userWebService.getBusinessByUserId(fUser.getId()).getContent();

							if (business != null) {
								String businessAddress = business.getBusinessAddress(); // 商家地址
								String businessTel = business.getBusinessTel(); // 商家电话
								String businessName = business.getBusinessName(); // 商家名称
								String businessArea = business.getArea(); // 区
								String businessAreaProvince = business.getProvince(); // 省
								String businessCity = business.getCity(); // 市
								fansUser.put("businessAddress", businessAddress);
								fansUser.put("businessTel", businessTel);
								fansUser.put("businessName", businessName);
								fansUser.put("businessAreaProvince", businessAreaProvince);
								fansUser.put("businessCity", businessCity);
								fansUser.put("area", businessArea);
							}

							fansUser.put("relationUserAvatar", relationUserAvatar);
							fansUser.put("relationUserSex", relationUserSex);
							fansUser.put("createTime", DateUtils.format(createTime));
							fansUser.put("relationUserNickName", relationUserNickName);
							fansUser.put("userCertifcate", userCertifcate);
							fansUser.put("address", address);
							fansUser.put("relationUserId", relationUserId);
							fansListConfig.add(fansUser);
						}

					}

				}
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("fansConfig", fansListConfig);
				modelMap.put("fansCount", fansCount);

			}

			if (relationNum == 2) {
				Map<String, Object> followsMap = new HashMap<String, Object>();
				followsMap.put("relationUserId", user.getId());
				followsMap.put("relationType", 2);
				followsMap.put("order", "create_time desc");
				followsMap.put("page", new Page(begin, 10));
				// List<UsersRelation> followsList =
				// userRelationService.selectRelation(followsMap);
				List<UsersRelation> followsList = userRelationService.selectListUsersRelationPage(followsMap)
						.getContent().getData();
				Integer followCount = userRelationService.selectListUsersRelationCount(followsMap);
				List followsListConfig = new ArrayList<>(); // 存放关注的集合
				if (followsList != null && followsList.size() > 0) {
					// int followsListCount =followsList.size();
					// user.setFollowCount(followsListCount);
					// userWebService.updateUser(user); //更改用户关注数
					// followCount =user.getFollowCount();
					Map<String, Object> followsUser;
					for (UsersRelation usersRelation : followsList) {
						followsUser = new HashMap<String, Object>();
						Date createTime = usersRelation.getCreateTime(); // 建立时间
						Long relationUserId = usersRelation.getUserId(); // 关注ID
						UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(relationUserId));
						User foUser = resultUser.getContent();
						if (foUser != null) {
							String relationUserAvatar = foUser.getAvatar(); // 用户图像
							Integer relationUserSex = foUser.getSex(); // 用户性别
							String relationUserNickName = foUser.getNickName(); // 关系用户名称
							String province = foUser.getProvince(); // 粉丝的省份
							String city = foUser.getCity(); // 粉丝的城市
							if (province == null) {
								province = "";
							}
							if (province != null && province.endsWith("省")) {
								province = province.substring(0, province.length() - 1);
							}
							if (city == null) {
								city = "";
							}
							if (city != null && city.endsWith("市")) {
								city = city.substring(0, city.length() - 1);
							}
							String address = province + city;

							Integer userCertifcate = userAgentService.selectUserCertificateLevel(foUser.getId());// 用户认证标识

							Business business = userWebService.getBusinessByUserId(foUser.getId()).getContent();
							if (business != null) {
								String businessAddress = business.getBusinessAddress(); // 商家地址
								String businessTel = business.getBusinessTel(); // 商家电话
								String businessName = business.getBusinessName(); // 商家名称
								String businessArea = business.getArea(); // 区
								String businessAreaProvince = business.getProvince(); // 省
								String businessCity = business.getCity(); // 市
								followsUser.put("businessAddress", businessAddress);
								followsUser.put("businessTel", businessTel);
								followsUser.put("businessName", businessName);
								followsUser.put("businessAreaProvince", businessAreaProvince);
								followsUser.put("businessCity", businessCity);
								followsUser.put("area", businessArea);

							}

							followsUser.put("relationUserAvatar", relationUserAvatar);
							followsUser.put("relationUserSex", relationUserSex);
							followsUser.put("createTime", DateUtils.format(createTime));
							followsUser.put("relationUserNickName", relationUserNickName);
							followsUser.put("userCertifcate", userCertifcate);
							followsUser.put("address", address);
							followsUser.put("relationUserId", relationUserId);

							followsListConfig.add(followsUser);

						}
					}

				}

				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("followsConfig", followsListConfig);
				modelMap.put("followsCount", followCount);
			}

		} else {

			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	@RequestMapping(value = "/modifyDonationFlag", method = { RequestMethod.GET, RequestMethod.POST })
	public String modifyDonationFlag(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String donateFlag = request.getParameter("donateFlag");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(donateFlag)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		Long donate = Long.parseLong(donateFlag);
		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			int result = 0;
			if (donate == 1) {
				user.setDonateFlag(donate);
				result = userVerifyService.updateUser(user);
				modelMap.put("donateFlag", user.getDonateFlag());
			}
			if (donate == 0) {
				user.setDonateFlag(donate);
				result = userVerifyService.updateUser(user);
				modelMap.put("donateFlag", user.getDonateFlag());
			}
			modelMap.put("result", Constants.ERROR_OK);

		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 商城地址的接口，暂未开通
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/mallAddress", method = { RequestMethod.GET, RequestMethod.POST })
	public String mallAddress(HttpServletRequest request, ModelMap modelMap) {

		modelMap.put("mallAddress", Constants.MALLADDRESS);

		return "";
	}

	/**
	 * 幸运转轮验证用户手机号是否使用
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/checkPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkPhone(HttpServletRequest request, ModelMap modelMap) {
		String phone = request.getParameter("phone");
		if (StringUtil.isEmpty(request.getParameter("phone"))) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		/* 查询手机号是否作为注册或绑定使用 */
		User user = userWebService.getUserByCheckPhone(phone);
		if (user != null) {
			/* 查询最近一条XX活动 */
			Map<String, Object> map = new HashMap<>();
			map.put("advertType", 11);// 幸运转轮活动
			Advert advert = advertWebService.selectActivity(map);
			if (advert != null) {
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("user", user);
				modelMap.put("loginKey", TokenUtil.combination(user.getToken(), user.getTableNum()));
				modelMap.put("avertId", advert.getId());
			}

		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	public String changeString(String hobby) {
		switch (hobby) {
		case "不限":
			hobby = "0";
			break;
		case "美容":
			hobby = "1";
			break;
		case "汽车":
			hobby = "2";
			break;
		case "时尚":
			hobby = "3";
			break;
		case "旅游":
			hobby = "4";
			break;
		case "游戏":
			hobby = "5";
			break;
		case "动漫":
			hobby = "6";
			break;
		case "音乐":
			hobby = "7";
			break;
		case "饮食":
			hobby = "8";
			break;
		case "艺术":
			hobby = "9";
			break;
		case "摄影":
			hobby = "10";
			break;
		case "读书":
			hobby = "11";
			break;
		case "社交":
			hobby = "12";
			break;
		case "体育":
			hobby = "13";
			break;
		}
		return hobby;
	}

	/**
	 * 生日转换年龄段
	 * 
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) throws Exception {
		int ageGroup = 0;
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("出生时间大于当前时间!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
					// do nothing
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}
		if (age > 0 && age <= 20) {
			ageGroup = 5;
		}
		if (age > 20 && age <= 30) {
			ageGroup = 1;
		}
		if (age > 30 && age <= 40) {
			ageGroup = 2;
		}
		if (age > 40 && age <= 50) {
			ageGroup = 3;
		}
		if (age > 50 && age <= 150) {
			ageGroup = 4;
		}
		return ageGroup;
	}
}
