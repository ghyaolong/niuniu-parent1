package com.mouchina.web.controller.advert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.entity.ReturnEntity;
import com.mouchina.base.exceptions.BusinessException;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.base.utils.UtilMaskCode;
import com.mouchina.base.utils.map.GpsCorrect;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.luckybag.LuckyBagHelper;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.BusinessShopHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserHelper;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.vo.HomePageAwardVo;
import com.mouchina.moumou_server.entity.vo.RelayLuckyBagVo;
import com.mouchina.moumou_server.exceptions.AdvertException;
import com.mouchina.moumou_server_interface.activityConfig.ActivityConfigService;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.BusinessService;
import com.mouchina.moumou_server_interface.member.BusinessShopService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.redEnvelope.RedEnvelopeService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.base.utils.TokenUtil;
import com.mouchina.web.service.api.advert.AdvertWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.AdvertHomePageVo;
import com.mouchina.web.service.api.vo.AdvertVo;
import com.mouchina.web.service.api.vo.BaseResultVo;
import com.mouchina.web.service.api.vo.BusinessAdvertVo;
import com.mouchina.web.service.api.vo.RedEnvelopeTopVO;
import com.mouchina.web.service.api.vo.RedEnvelopeVo;
import com.mouchina.web.service.api.vo.UserVo;

@Controller
@RequestMapping("/user/advert")
public class UserAdvertController {

	private Logger logger = Logger.getLogger(getClass());

	@Resource
	private UserWebService userWebService;
	@Resource
	private UsersRelationService usersRelationService;
	@Resource
	private AdvertWebService advertWebService;
	@Resource
	private SocialWebService socialWebService;
	@Resource
	private UserAgentService userAgentService;
	@Resource
	private SystemService systemService;
	@Resource
	private BusinessCouponService businessCouponService;
	@Resource
	private UserCommentService userCommentService;
	@Resource
	private BusinessService businessService;
	@Resource
	private RedEnvelopeService redEnvelopeService;
	@Resource
	private AdvertService advertService;
	@Resource
	private ActivityConfigService activityConfigService;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	private TaskHistoryService taskHistoryService;
	@Resource
	private BusinessShopService businessShopService;

//	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
//	public String fetch(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
//			throws IOException {
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		String loginKey = request.getParameter("loginKey");
//		String provinceCode = request.getParameter("provinceCode");
//		String cityCode = request.getParameter("cityCode");
//		String countyCode = request.getParameter("countyCode");
//
//		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
//			modelMap.put("result", "0");
//			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
//			return "";
//		}
//
//		User user = userWebService.getUserByLoginKey(loginKey);// 获取当前登陆用户
//
//		if (user != null) {
//
//			user.setProvince(provinceCode != null && !provinceCode.isEmpty() ? provinceCode : "all");
//			user.setCity(cityCode != null && !cityCode.isEmpty() ? cityCode : "all");
//			user.setArea(countyCode != null && !countyCode.isEmpty() ? countyCode : "all");
//
//			Advert advert = advertWebService.updateAdvertFetchFreeze(user);
//			if (advert != null) {
//				// advertWebService.addAdvertViewTimes(advert);// 增加广告人气
//
//				modelMap.put("result", "1");
//				modelMap.put("name", advert.getAdvertName());
//				modelMap.put("advertId", advert.getId());
//				modelMap.put("advertContent", advert.getAdvertContent());
//				/* modelMap.put("website", advert.getWebsite()); */
//				modelMap.put("websiteName", advert.getWebsiteName());
//				UserResult<Business> userBusinessResult = userWebService.getBusinessByUserId(advert.getUserId());
//
//				Map<String, Object> fansMap = new HashMap<String, Object>();
//				fansMap.put("userId", advert.getUserId());
//				fansMap.put("relationUserId", user.getId());
//				fansMap.put("relationType", 2);
//				int count = socialWebService.selectListUsersRelationCount(fansMap);
//				modelMap.put("isFans", count > 0 ? 1 : 0);
//
//				if (userBusinessResult.getState() == 1) {
//					BusinessAdvertVo businessAdvertVo = new BusinessAdvertVo();
//					Business business = userBusinessResult.getContent();
//
//					BeanUtils.copyProperties(business, businessAdvertVo);
//					modelMap.put("business", business);
//					// 广告名称 = 商家名称
//					modelMap.put("name", business.getBusinessName());
//				}
//
//				Map<String, Object> userRelationSqlMap = new HashMap<String, Object>();
//				userRelationSqlMap.put("eventId", advert.getId());
//				userRelationSqlMap.put("relationUserId", user.getId());// relationUserId标示的是
//																		// 收藏人的id
//																		// userId标识发布广告人的id
//				userRelationSqlMap.put("relationType", 1);
//				SocialResult<List<UsersRelation>> socialSelectResult = usersRelationService
//						.selectListPageUsersRelation(userRelationSqlMap);
//
//				if (socialSelectResult.getContent().size() != 0) {
//					// 说明收藏
//					modelMap.put("isCollectByLoginUser", 1);
//				} else {
//					modelMap.put("isCollectByLoginUser", 0);
//				}
//
//			} else {
//				// 未能领取到广告
//				modelMap.put("result", "0");
//				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
//				return "";
//			}
//
//		} else {
//			modelMap.put("result", "0");
//			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
//		}
//
//		return "";
//	}

	/**
	 * 获取广告福袋接口3.0 针对福袋类型：0普通福袋 1优惠券福袋 2公告 6定时广告 7运营活动广告(官方) 8定时活动广告(官方)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fetch3", method = RequestMethod.GET)
	public String fetch3(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String provinceCode = request.getParameter("provinceCode");
		String cityCode = request.getParameter("cityCode");
		String countyCode = request.getParameter("countyCode");
		String longitudeStr = request.getParameter("longitude"); // 经度
		String latitudeStr = request.getParameter("latitude"); // 纬度
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		User user = userWebService.getUserByLoginKey(loginKey);// 获取当前登陆用户

		if (user != null) {

			user.setProvince(provinceCode != null && !provinceCode.isEmpty() ? provinceCode : "all");
			user.setCity(cityCode != null && !cityCode.isEmpty() ? cityCode : "all");
			user.setArea(countyCode != null && !countyCode.isEmpty() ? countyCode : "all");

			user.setLongitude(longitudeStr != null && !longitudeStr.isEmpty() ? Double.valueOf(longitudeStr) : 0.0);
			user.setLatitude(latitudeStr != null && !latitudeStr.isEmpty() ? Double.valueOf(latitudeStr) : 0.0);

			// 如果用户有经纬度需要做gps纠偏
			Double lng = user.getLongitude(); // 经度
			Double lat = user.getLatitude(); // 纬度
			double[] latlng = new double[2];
			if (lng != null && lat != null && lng.doubleValue() > 0 && lat.doubleValue() > 0) {
				GpsCorrect.transform(lat, lng, latlng);
				user.setLongitude(latlng[1]);
				user.setLatitude(latlng[0]);
			}

			Advert advert = advertWebService.updateAdvertFetchFreeze3(user);
			AdvertVo advertVo = new AdvertVo();
			if (advert != null) {
				
				advertWebService.addAdvertViewTimes(advert);// 增加广告人气
				
				modelMap.put("result", AdvertHelper.RESULT_STATE_1.getMarker());
				BeanUtils.copyProperties(advert, advertVo);
				/* 查询用户昵称、头像、等级、 身份 */
				UserResult<User> resultUser = userWebService.selectUserByUserId(advertVo.getUserId());
				if (resultUser != null && resultUser.getContent() != null) {
					advertVo.setNickName(resultUser.getContent().getNickName());
					advertVo.setAvatar(resultUser.getContent().getAvatar());
					advertVo.setUserLevel(resultUser.getContent().getLevel());
					advertVo.setCertificationType(userAgentService.selectUserCertificateLevel(advertVo.getUserId()));
				}
				/* 查询运营活动奖品名称及图片 */
				Map<String, Object> map = new HashMap<>();
				map.put("advertId", advertVo.getId());
				map.put("order", "rank");
				List<ActivityConfig> activityConfigs = activityConfigService.selectListActivityConfigs(map);
				advertVo.setActivityConfigs(activityConfigs);
				modelMap.put("advert", advertVo);
				// modelMap.put("name", advert.getAdvertName());
				// modelMap.put("advertId", advert.getId());
				// modelMap.put("advertContent", advert.getAdvertContent());
				// /* modelMap.put("website", advert.getWebsite()); */
				// modelMap.put("websiteName", advert.getWebsiteName());

				// Map<String, Object> fansMap = new HashMap<String, Object>();
				// fansMap.put("userId", advert.getUserId());
				// fansMap.put("relationUserId", user.getId());
				// fansMap.put("relationType", 2);
				// int count =
				// socialWebService.selectListUsersRelationCount(fansMap);
				// modelMap.put("isFans", count > 0 ? 1 : 0);

				// UserResult<Business> userBusinessResult =
				// userWebService.getBusinessByUserId(advert.getUserId());
				// if (userBusinessResult.getState() == 1) {
				// BusinessAdvertVo businessAdvertVo = new BusinessAdvertVo();
				// Business business = userBusinessResult.getContent();
				//
				// BeanUtils.copyProperties(business, businessAdvertVo);
				// modelMap.put("business", business);
				// // 广告名称 = 商家名称
				// modelMap.put("name", business.getBusinessName());
				// }

				// Map<String, Object> userRelationSqlMap = new HashMap<String,
				// Object>();
				// userRelationSqlMap.put("eventId", advert.getId());
				// userRelationSqlMap.put("relationUserId", user.getId());//
				// relationUserId标示的是
				// // 收藏人的id
				// // userId标识发布广告人的id
				// userRelationSqlMap.put("relationType", 1);
				// SocialResult<List<UsersRelation>> socialSelectResult =
				// usersRelationService
				// .selectListPageUsersRelation(userRelationSqlMap);
				//
				// if (socialSelectResult.getContent().size() != 0) {
				// // 说明收藏
				// modelMap.put("isCollectByLoginUser", 1);
				// } else {
				// modelMap.put("isCollectByLoginUser", 0);
				// }

			} else {
				// 未能领取到广告
				modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
				return "";
			}
		} else {
			modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	@RequestMapping(value = "/award", method = RequestMethod.GET)
	public String award(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		final String loginKey = request.getParameter("loginKey");
		final String advertId = request.getParameter("advertId");
		try {
			if (StringUtils.isBlank(loginKey)) {
				throw new AdvertException(false, "loginkey is null");
			}
			if (StringUtils.isBlank(advertId)) {
				throw new AdvertException(false, "advertId is null");
			}
			User user = userWebService.getUserByLoginKey(loginKey);
			if (null == user || null == user.getId()) {
				throw new AdvertException(false, String.format("用户不存在，参数：loginKey=%s", loginKey),
						String.valueOf(Constants.ERROR_CODE_100101));
			}

			Long advertIdLong = Long.valueOf(advertId.trim());
			Advert advert = advertWebService.selectAdvert(advertIdLong);
			if (null == advert || null == advert.getUserId()) {
				throw new AdvertException(false, String.format("广告不存在，参数：advertId=%s", advertId));
			}
			// 领取广告红包
			RedEnvelope redEnvelope = advertWebService.updateAdvertAwart(advert, user);
			if (null == redEnvelope) {
				throw new AdvertException(false,
						String.format("没有可领取的广告，参数：userId=%s,advertId=%s", user.getId(), advertId),
						String.valueOf(Constants.ERROR_CODE_900100));
			}
			modelMap.put("result", "1");
			modelMap.put("awardAmount", redEnvelope.getAmout());
		} catch (AdvertException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", ex.code(String.valueOf(Constants.ERROR_CODE_100003)));
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100000);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	/*
	 * 拆福袋方法(适用于3.0版本) 针对福袋类型：0普通福袋 1优惠券福袋 6定时广告 7运营活动广告(官方) 8定时活动广告(官方)
	 */
	@RequestMapping(value = "/award3", method = RequestMethod.GET)
	public String award3(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		final String loginKey = request.getParameter("loginKey");
		final String advertId = request.getParameter("advertId");
		try {
			if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId)) {
				modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				return "";
			}
			User user = userWebService.getUserByLoginKey(loginKey);
			if (null != user) {
				Long advertIdLong = Long.valueOf(advertId.trim());
				Advert advert = advertWebService.selectAdvert(advertIdLong);
				if (null == advert || null == advert.getUserId()) {
					modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
					modelMap.put("errorCode", Constants.ERROR_CODE_500004);
					return "";
				}
				// 领取福袋红包
				AdvertResult<RedEnvelope> advertResult = advertWebService.updateAdvertAwart3(advert, user);
				RedEnvelope redEnvelope = advertResult.getContent();
				Map<String, Object> actityMap = advertResult.getData();
				if (null == redEnvelope) {
					modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
					modelMap.put("errorCode", Constants.ERROR_CODE_200002);
					return "";
				}
				modelMap.put("result", AdvertHelper.RESULT_STATE_1.getMarker());
				if (null != actityMap) {
					/* 查询运营活动奖品名称、等级、图片 */
					Map<String, Object> map = new HashMap<>();
					map.put("advertId", advert.getId());
					map.put("rank", actityMap.get("rank"));
					List<ActivityConfig> activityConfigs = activityConfigService.selectListActivityConfigs(map);
					if (null != activityConfigs && activityConfigs.size() > 0) {
						ActivityConfig activityConfig = activityConfigs.get(0);
						modelMap.put("rank", activityConfig.getRank());
						modelMap.put("prize", activityConfig.getPrize());
						modelMap.put("prizePic", activityConfig.getPrizePic());
					}
				} else {
					modelMap.put("awardAmount", redEnvelope.getAmout());
				}
				/*抢一次福袋任务*/
				try {
					taskHistoryService.insertTask(user.getId(), (long)3, "1");
				} catch (Exception e) {
					logger.error("插入抢一次福袋任务报错");
				}
			} else {
				modelMap.put("result", AdvertHelper.RESULT_STATE_0.getMarker());
				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			}
		} catch (AdvertException ex) {
			modelMap.put("result", AdvertHelper.RESULT_STATE_5.getMarker());
			modelMap.put("errorCode", ex.code(String.valueOf(Constants.ERROR_CODE_100003)));
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", AdvertHelper.RESULT_STATE_5.getMarker());
			modelMap.put("errorCode", Constants.ERROR_CODE_100003);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/redenvelope/list", method = RequestMethod.GET)
	public String redEnvelopeList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");

		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			modelMap.put("result", "1");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			map.put("state", 1);
			ListRange<RedEnvelope> listRange = advertWebService.selectListRangetRedEnvelope(map);

			List<RedEnvelopeVo> redEnvelopeVos = new ArrayList<RedEnvelopeVo>();
			for (RedEnvelope redEnvelope : listRange.getData()) {
				RedEnvelopeVo redEnvelopeVo = new RedEnvelopeVo();
				BeanUtils.copyProperties(redEnvelope, redEnvelopeVo);
				redEnvelopeVos.add(redEnvelopeVo);
			}
			modelMap.put("redEnvelopeList", redEnvelopeVos);
			modelMap.put("page", listRange.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 收入详情【3.0】
	 * 
	 * @param loginKey
	 * @param begin
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "redEnvelopeList3", method = RequestMethod.POST)
	public @ResponseBody BaseResultVo<List<RedEnvelope>> redEnvelopeList3(String loginKey, Integer begin,
			ModelMap modelMap) {
		BaseResultVo<List<RedEnvelope>> baseResultVo = new BaseResultVo<List<RedEnvelope>>();

		if (StringUtil.isEmpty(loginKey)) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(Constants.ERROR_CODE_100001);
			return baseResultVo;
		}

		if (begin == null)
			begin = 0;

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(Constants.ERROR_CODE_100001);
			return baseResultVo;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("order", "create_time desc");
		map.put("page", new Page(begin, 10));
		map.put("state", 1);
		List<RedEnvelope> redEnvelopes = advertService.selectListRedEnvelopePage3(map);

		baseResultVo.setResult("1");
		baseResultVo.setData(redEnvelopes);
		return baseResultVo;
	}

	/**
	 * 首页广告列表接口
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllAdverts", method = RequestMethod.GET)
	public String getAllHomePageAdverts(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		Integer begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			return "";
		}
		// 更新用户登录时间操作
		if (TokenUtil.isToday(user.getLastLoginTime()) == true) {
			user.setLastLoginTime(new Date());
			userWebService.updateUser(user);
		}
		// 获取福袋排序时长配置参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("configKey", "client.advert.sort.duration"); // 只查询福袋排序时长配置参数
		map.put("configGroup", "client");
		map.put("state", "1");
		int interval = 30;
		SystemResult<List<SystemGlobalConfig>> systemGlobalConfigSystemResult = systemService
				.selectListSystemGlobalConfig(map);
		List<SystemGlobalConfig> globalConfigs = systemGlobalConfigSystemResult.getContent();
		if (globalConfigs != null && globalConfigs.size() > 0) {
			interval = Integer.valueOf(globalConfigs.get(0).getConfigContent());
		}
		// 查询2分钟之内所有广告createTime
		Date end = new Date();// 当前时间
		Date start = DateUtils.getSeveralMinutesAgoDate(end, interval);// interval分钟之前
		Map<String, Object> advertSqlMap = new HashMap<String, Object>();
		advertSqlMap.put("states", "1,3,5");
		advertSqlMap.put("page", new Page(begin, 10));
		advertSqlMap.put("order", "start_time desc");
		advertSqlMap.put("createTimelt", end);// <if test="startTimegt != null"
												// >and ad.start_time >=
												// #{startTimegt,jdbcType=TIMESTAMP}</if>
		advertSqlMap.put("createTimegt", start);

		// 查询所有广告amount
		Map<String, Object> advertAmountMap = new HashMap<String, Object>();
		advertAmountMap.put("states", "1,3,5");
		advertAmountMap.put("page", new Page(begin, 10));
		advertAmountMap.put("order", "red_envelope_amount desc");// 先按照金额排序

		ListRange<Advert> allAdvertsList = advertWebService.selectListRangeAdvert(advertAmountMap);// 默认返回按金额排序
		Collections.sort(allAdvertsList.getData(), new Comparator<Advert>() {
			// 金额相同的情况下按时间进行降序
			@Override
			public int compare(Advert o1, Advert o2) {
				if (o1.getRedEnvelopeAmount().equals(o2.getRedEnvelopeAmount())) {
					logger.info("广告金额相等比较开始时间");
					return o2.getStartTime().compareTo(o1.getStartTime());
				} else {
					return o2.getRedEnvelopeAmount().compareTo(o1.getRedEnvelopeAmount());
				}
			}

		});

		if (advertWebService.checkNewAdvertInTwoMinutes()) {
			// 说明2分钟之内有新广告发布，需要按发布时间排序
			ListRange<Advert> allAdvertsListInTwo = advertWebService.selectListRangeAdvert(advertSqlMap);
			for (int i = 0; i < allAdvertsListInTwo.getData().size(); i++) {
				// 去重
				if (allAdvertsList.getData().contains(allAdvertsListInTwo.getData().get(i))) {
					allAdvertsList.getData().remove(allAdvertsListInTwo.getData().get(i));
				}
				allAdvertsList.getData().set(i, allAdvertsListInTwo.getData().get(i));
			}
		} else {

		}
		logger.info("advertSqlMap : " + advertSqlMap);

		List<AdvertHomePageVo> advertList = new ArrayList<AdvertHomePageVo>();// 返回给调用者的
																				// List
		int curUserIsBusiness = 0;

		if (allAdvertsList == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_200001);
			return "";
		} else {
			for (Advert vo : allAdvertsList.getData()) {
				AdvertHomePageVo currentAdvertVo = new AdvertHomePageVo();
				BeanUtils.copyProperties(vo, currentAdvertVo);

				// 根据当前广告的 userId查出对应的商家(以便拿到商家Logo、名称)
				UserResult<Business> userResult = userWebService.getBusinessByUserId(vo.getUserId());
				// 查询当前登录用户是否收藏此广告
				if (user != null) {

					Map<String, Object> userRelationSqlMap = new HashMap<String, Object>();
					userRelationSqlMap.put("eventId", vo.getId());
					userRelationSqlMap.put("relationUserId", user.getId());// relationUserId标示的是
																			// 收藏人的id
																			// userId标识发布广告人的id
					userRelationSqlMap.put("relationType", 1);
					SocialResult<List<UsersRelation>> socialSelectResult = usersRelationService
							.selectListPageUsersRelation(userRelationSqlMap);

					if (socialSelectResult.getContent().size() == 0) {
						// 说明没有收藏
						currentAdvertVo.setIsCollectByLoginUser(new Byte((byte) 0));
					} else {

						currentAdvertVo.setIsCollectByLoginUser(new Byte((byte) 1));
					}
					// 进一步防止出现非法数据导致null出现
					if (currentAdvertVo.getIsCollectByLoginUser() == null) {
						currentAdvertVo.setIsCollectByLoginUser(new Byte((byte) 0));
					}
				} else {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_100101);
					return "";
				}

				// 设置 商户Logo 和 商户名称
				if (userResult != null && userResult.getState() == 1) {
					Business business = userResult.getContent();// 拿到商家对象
					if (business != null) {
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("userId", business.getUserId());
						UserResult<UserAgent> result = userAgentService.selectUserAgent(map1);
						currentAdvertVo.setBusinessId(business.getId());
						currentAdvertVo.setBusinessLogo(business.getBusinessLogo());
						currentAdvertVo.setBusinessName(business.getBusinessName());
						if (result != null && result.getState() == 1) {
							currentAdvertVo.setAgentLevel(result.getContent().getAgentLevel());
						} else {
							currentAdvertVo.setAgentLevel(-1);
						}
					}
				} else {
					continue;
				}

				// 设置sendRedEnvelopeCount 已领取红包人数
				Map<String, Object> advertStatisSqlMap = new HashMap<String, Object>();
				advertStatisSqlMap.put("advertId", vo.getId());
				AdvertStatistics advertStatistics = advertWebService.selectAdvertStatistics(advertStatisSqlMap);

				if (advertStatistics != null) {
					currentAdvertVo.setSendRedEnvelopeCount(advertStatistics.getSendRedEnvelopeCount());

					currentAdvertVo
							.setViewTimes(advertStatistics.getViewTimes() + advertStatistics.getSendRedEnvelopeCount());
				}

				// 进一步防止null
				if (currentAdvertVo.getSendRedEnvelopeCount() == null) {
					currentAdvertVo.setSendRedEnvelopeCount(0);
				}
				advertList.add(currentAdvertVo);
			}

			// 查出当前用户是否是商户
			Business curBusiness = userWebService.getBusinessByUserId(user.getId()).getContent();
			if (curBusiness == null) {
				curUserIsBusiness = 0;
			} else {
				if (curBusiness.getState().intValue() != 2) {
					curUserIsBusiness = 0;
				} else {
					curUserIsBusiness = 1;
				}
			}

			modelMap.put("advertList", advertList);
			modelMap.put("curUserIsBusiness", curUserIsBusiness);
			modelMap.put("result", "1");
		}

		return "";
	}

	/**
	 * 首页广告统计(广告总金额、人气、抢到红包人数)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAllAdvertSumInfo", method = RequestMethod.GET)
	public String getAllAdvertSumInfo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String loginKey = request.getParameter("loginKey");
		if (StringUtils.isBlank(loginKey)) {
			throw new BusinessException(false, "loginKey or inviteCode is null",
					String.valueOf(Constants.ERROR_CODE_100101));
		}

		/*
		 * String publishAdvertMoney;// 全网广告发布总金额 String advertScanCount;//
		 * 全网广告浏览量 String redEnvelopePersons;// 全网抢到红包总人数
		 */
		// 读取system_data表的三个数值
		Map<String, String> returnMap = new HashMap<String, String>();
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("keyNames", "\"publishAdvertMoney\",\"advertScanCount\",\"redEnvelopePersons\"");
		sqlMap.put("type", "1");
		List<SystemDataVo> sysDataVo = systemService.selectListSystemData(sqlMap);
		for (SystemDataVo vo : sysDataVo) {
			String key = vo.getName();
			String val = vo.getValue().toString();
			logger.info("key : " + key + ",\n " + "value : " + val);
			if (key != null && !"".equals(key)) {
				returnMap.put(key, val);
			}
		}

		modelMap.put("advertSumMap", returnMap);
		modelMap.put("result", "1");
		// Map<String, Object> sumMap = advertWebService.getAdvertSumMap();
		// Map<String, String> returnMap;
		// if (sumMap.size() > 0) {
		// // 统一返回《String,String》的map返回给前端以便适配
		// returnMap = new HashMap<String, String>();
		// for (String key : sumMap.keySet()) {
		// if ("publishAdvertMoney".equals(key)) {
		// // 钱数除100转换成元
		// try {
		// if (sumMap.get(key) instanceof BigDecimal) {
		// BigDecimal totalAdvertAmount = (BigDecimal) sumMap.get(key);//
		// 数据库中存储的总钱数（单位为分）
		// // log.info("数据库中发布广告总金额是 : " + totalAdvertAmount);
		// String amount = AmountUtils.changeF2Y(totalAdvertAmount.toString());
		// // log.info("处理后的总金额是 ： " + amount);
		// returnMap.put(key, amount);
		// }
		// } catch (Exception e) {
		// log.error("将总钱数为 分 转换成元时出现异常！");
		// modelMap.put("result", "0");
		// modelMap.put("errorCode", Constants.ERROR_CODE_100003);
		// modelMap.put("errorMsg", "总钱数转换发生异常！");
		// }
		// } else {
		// returnMap.put(key, sumMap.get(key).toString());
		// }
		//
		// }
		//
		// modelMap.put("advertSumMap", returnMap);
		// modelMap.put("result", "1");
		//
		// } else {
		// log.error("获取广告摘要统计出现异常!");
		// modelMap.put("result", "0");
		// modelMap.put("errorCode", Constants.ERROR_CODE_100003);
		// modelMap.put("errorMsg", "获取到广告摘要信息异常!");
		// throw new BusinessException(false, "获取广告摘要异常！",
		// String.valueOf(Constants.ERROR_CODE_200001));
		// }

		return "";
	}

	/**
	 * 排行榜接口(适用于2.0.4版本)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/redenvelope/top/list", method = RequestMethod.GET)
	public String userRedEnvelopeTopList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			final String loginKey = request.getParameter("loginKey");
			final String inviteCode = request.getParameter("inviteCode");
			final String currentPage = request.getParameter("currentPage");
			if (StringUtils.isBlank(loginKey) && StringUtils.isBlank(inviteCode)) {
				throw new BusinessException(false, "loginKey or inviteCode is null",
						String.valueOf(Constants.ERROR_CODE_100001));
			}
			final User user;
			if (StringUtils.isNotBlank(loginKey)) {
				user = userWebService.getUserByLoginKey(loginKey);
			} else {
				UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(inviteCode));
				user = resultUser.getContent();
			}
			if (null == user) {
				throw new BusinessException(false, String.format("用户不存! 参数：loginKey=", loginKey),
						String.valueOf(Constants.ERROR_CODE_100101));
			}
			final String userId = String.valueOf(user.getId());
			UserSearchSO search = new UserSearchSO();
			search.setUserId(userId);
			search.setCurrentPage(currentPage);
			search.setLimit(10);
			List<RedEnvelopeTopVO> redEnvelopeTopList = new ArrayList<RedEnvelopeTopVO>();

			ReturnEntity<RedEnvelopeTop> result = advertWebService.selectListPageRedEnvelopeTop(search);
			if (null == result) {
				throw new BusinessException(false, String.format("查询用户邀请好友领取排行榜数据异常，参数：userId=%s", userId));
			}
			for (RedEnvelopeTop redEnvelope : result.getList()) {
				String targetUserId = redEnvelope.getUserId();
				UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(targetUserId));

				User targetUser = null;
				if (null == resultUser || null == (targetUser = resultUser.getContent())) {
					logger.error(String.format("用户(userId=%s)邀请的好友(userId=%s)不存在", userId, targetUserId));
					continue;
				}
				RedEnvelopeTopVO redEnvelopeTopVO = new RedEnvelopeTopVO();
				BeanUtils.copyProperties(redEnvelope, redEnvelopeTopVO);

				redEnvelopeTopVO.setUserNickName(targetUser.getNickName());
				redEnvelopeTopVO.setUserAvatar(targetUser.getAvatar());

				if (StringUtils.isBlank(redEnvelopeTopVO.getUserNickName())) {
					// 若昵称不存在，则保存手机号码
					redEnvelopeTopVO.setUserNickName(UtilMaskCode.telephone(targetUser.getPhone()));
				}
				if (userId.equals(targetUserId)) {
					modelMap.put("currentUserTop", redEnvelopeTopVO);
				} else {
					redEnvelopeTopList.add(redEnvelopeTopVO);
				}
			}
			modelMap.put("userRedEnvelopeList", redEnvelopeTopList);
			modelMap.put("result", "1");
		} catch (BusinessException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", ex.code(String.valueOf(Constants.ERROR_CODE_100003)));
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100003);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 排行榜接口(2.0.5版本)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/redenvelope/top/listnew", method = RequestMethod.GET)
	public String userRedEnvelopeTopListNew(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		try {
			String loginKey = request.getParameter("loginKey");
			if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				return "";
			}
			int begin = 0;
			begin = request.getParameter("begin") != null
					? Integer.valueOf(request.getParameter("begin").toString()).intValue() : begin;
			final User user = userWebService.getUserByLoginKey(loginKey);
			if (null == user) {
				throw new BusinessException(false, String.format("用户登陆失效! 参数：loginKey=" + loginKey, loginKey),
						String.valueOf(Constants.ERROR_CODE_100101));
			}
			final String userId = String.valueOf(user.getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("page", new Page(begin, 10));
			List<RedEnvelopeTopVO> redEnvelopeTopList = new ArrayList<RedEnvelopeTopVO>();
			// 排行榜首页加载个人排行信息
			if (begin == 0) {
				RedEnvelopeTop resultMe = advertWebService.selectRedEnvelopeTopInfoMe(user.getId());
				if (null == resultMe)
					throw new BusinessException(false, String.format("查询用户排行榜数据异常，参数：userId=%s", userId));

				RedEnvelopeTopVO redEnvelopeTopVO = new RedEnvelopeTopVO();
				BeanUtils.copyProperties(resultMe, redEnvelopeTopVO);

				redEnvelopeTopVO.setUserNickName(user.getNickName());
				redEnvelopeTopVO.setUserAvatar(user.getAvatar());

				if (StringUtils.isBlank(redEnvelopeTopVO.getUserNickName())) {
					// 若昵称不存在，则保存手机号码
					redEnvelopeTopVO.setUserNickName(UtilMaskCode.telephone(user.getPhone()));
				}
				modelMap.put("currentUserTop", redEnvelopeTopVO);
			}
			// 排行榜加载好友排行信息
			ListRange<RedEnvelopeTop> result = advertWebService.selectListPageRedEnvelopeTopNew(map);
			if (null == result) {
				throw new BusinessException(false, String.format("查询用户邀请好友领取排行榜数据异常，参数：userId=%s", userId));
			}
			for (RedEnvelopeTop redEnvelope : result.getData()) {
				String targetUserId = redEnvelope.getUserId();
				UserResult<User> resultUser = userWebService.selectUserByUserId(Long.valueOf(targetUserId));

				User targetUser = null;
				if (null == resultUser || null == (targetUser = resultUser.getContent())) {
					logger.error(String.format("用户(userId=%s)邀请的好友(userId=%s)不存在", userId, targetUserId));
					continue;
				}
				RedEnvelopeTopVO redEnvelopeTopVO = new RedEnvelopeTopVO();
				BeanUtils.copyProperties(redEnvelope, redEnvelopeTopVO);

				redEnvelopeTopVO.setUserNickName(targetUser.getNickName());
				redEnvelopeTopVO.setUserAvatar(targetUser.getAvatar());

				if (StringUtils.isBlank(redEnvelopeTopVO.getUserNickName())) {
					// 若昵称不存在，则保存手机号码
					redEnvelopeTopVO.setUserNickName(UtilMaskCode.telephone(targetUser.getPhone()));
				}
				redEnvelopeTopList.add(redEnvelopeTopVO);
			}
			modelMap.put("userRedEnvelopeList", redEnvelopeTopList);
			modelMap.put("page", result.getPage());
			modelMap.put("result", "1");
		} catch (BusinessException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", ex.code(String.valueOf(Constants.ERROR_CODE_100003)));
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100003);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 排行榜接口(3.0版本)
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/redenvelope/top/list3", method = RequestMethod.GET)
	public String userRedEnvelopeTopList3(String loginKey, HttpServletResponse response, ModelMap modelMap) {
		logger.info("request parame is : " + loginKey);
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			if (StringUtil.isEmpty(loginKey)) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				modelMap.put("errorMsg", "loginKey是空!");
				return "";
			}

			User user = userWebService.getUserByLoginKey(loginKey);
			if (user == null) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				modelMap.put("errorMsg", "用户登陆失效!");
				return "";
			}

			Long userId = user.getId();
			// 红包全服排行榜前30名
			ListRange<RedEnvelopeTop> result = advertWebService.selectListPageRedEnvelopeTop3(userId);

			if (null == result) {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
				modelMap.put("errorMsg", "查询用户邀请好友领取排行榜数据异常，参数：userId=%s" + userId);
				return "";
			}

			List<RedEnvelopeTop> envelopeTops = result.getData();
			modelMap.put("currentUserTop", envelopeTops.get(envelopeTops.size() - 1));
			envelopeTops.remove(envelopeTops.size() - 1);
			modelMap.put("RedEnvelopeList", result);
			modelMap.put("page", 1);
			modelMap.put("result", "1");
		} catch (BusinessException ex) {
			logger.error("出现业务异常:", ex.getCause());
			modelMap.put("result", "0");
			modelMap.put("errorCode", ex.code(String.valueOf(Constants.ERROR_CODE_100003)));
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			logger.error("出现业务异常:", ex.getCause());
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100003);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";

	}

	/** 查询首页广告列表(只包含首页广告)信息 **/
	@RequestMapping(value = "/getHomePageAdverts", method = RequestMethod.GET)
	public String queryHomePageAdverts(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			if(user.getState().intValue() == UserHelper.USER_STATE_2.getMarker().intValue()){
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_100103);
				return "";
			}
			Integer begin = 0;
			Map<String, Object> map = new HashMap<>();
			UserVo userVo = new UserVo();
			userVo.setId(user.getId());
			userVo.setNickName(user.getNickName());
			userVo.setAvatar(user.getAvatar());
			userVo.setSex(user.getSex());
			userVo.setLevel(user.getLevel());
			userVo.setState(user.getState());
			userVo.setCashBalance(user.getCashBalance());
			userVo.setRedEnvelopeBalance(user.getRedEnvelopeBalance());
			/* 查询用户认证信息 */
			Business business = userWebService.getBusinessByUserId(user.getId()).getContent();
			if (business != null) {
				userVo.setApproveType(business.getCertificationType());
			}
			/* 查询用户身份信息 */
			userVo.setMarkType(userAgentService.selectUserCertificateLevel(user.getId()));
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty())
				begin = Integer.valueOf(beginStr);
			map.put("advertType", BusinessShopHelper.ADVERT_TYPE_5.getMarker());
			map.put("state", BusinessShopHelper.AUDIT_STATE_1.getMarker());
			map.put("order", "red_envelope_amount desc, create_time desc");
			map.put("homePage", new Page(0, Page.PAGE_COUNT));
			map.put("page", new Page(begin, 10));
			ListRange<Advert> advertsList = advertWebService.selectListRangeHomeAdvert(map); // 默认返回按竞价排名、创建时间排序
			if (advertsList == null || advertsList.getData().size() <= 0) {
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("userVo", userVo);
				modelMap.put("advertList", null);
				return "";
			} else {
				List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
				for (Advert advert : advertsList.getData()) {
					AdvertVo advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					Long userId = advertVo.getUserId();
					/* 查询当前登录用户当天是否领取当前广告 */
					map.clear();
					map.put("advertId", advert.getId());
					map.put("userId", user.getId());
					map.put("states", "" + AdvertHelper.RED_ENVELOPE_STATE_0.getMarker() + ","
							+ AdvertHelper.RED_ENVELOPE_STATE_1.getMarker());
					map.put("crateTimeDay", new Date());
					RedEnvelope redEnvelope = redEnvelopeService.selectModel(map);
					if (null != redEnvelope) {
						advertVo.setDrawState(AdvertHelper.DRAW_STATE_1.getMarker());
					} else {
						advertVo.setDrawState(AdvertHelper.DRAW_STATE_0.getMarker());
					}
					/* 查询用户昵称、头像、性别、等级 */
					UserResult<User> resultUser = userWebService.selectUserByUserId(userId);
					if (resultUser != null && resultUser.getContent() != null) {
						advertVo.setNickName(resultUser.getContent().getNickName());
						advertVo.setAvatar(resultUser.getContent().getAvatar());
					}
					/* 查询用户身份类型 */
					map.clear();
					map.put("userId", userId);
					map.put("state", BusinessShopHelper.STATE_1.getMarker());
					UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);
					if (userResult != null && userResult.getContent() != null) {
						advertVo.setCertificationType(userResult.getContent().getAgentLevel() + 1);
					}
					/* 查询人气 */
					map.clear();
					map.put("userId", userId);
					int count = advertWebService.selectViewTimesCount(map);
					count = count < 0 ? 0 : count;
					advertVo.setViewTimes(count);
					/* 查询商户店铺信息 */
					map.clear();
					map.put("userId", userId);
					BusinessShop shop = businessShopService.queryShopByMap(map).getContent();
					if (shop != null) {
						advertVo.setShopSign(shop.getShopSign());
					}
					/* 未领取金额 */
					map.clear();
					map.put("advertId", advert.getId());
					AdvertStatistics advertStatistics = advertWebService.selectAdvertStatistics(map);
					if (advertStatistics != null) {
						advertVo.setBalance(
								advertVo.getRedEnvelopeAmount() - advertStatistics.getSendRedEnvelopeAmount());
					}
					advertVoList.add(advertVo);
				}
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("userVo", userVo);
				modelMap.put("advertList", advertVoList);
				modelMap.put("page", advertsList.getPage());
				/*首次登陆任务*/
				try {
					taskHistoryService.insertTask(user.getId(), (long)1, "4");
				} catch (Exception e) {
					logger.error("插入首页首次登陆任务报错");
				}
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 拆接力福袋
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/unseamRelayLuckyBag", method = RequestMethod.GET)
	public String unseamRelayLuckyBag(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String advertId = request.getParameter("advertId").trim();
		String openAmountStr = request.getParameter("openAmount");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId) || StringUtil.isEmpty(openAmountStr)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Advert advert = advertWebService.selectAdvert(Long.valueOf(advertId));
			if (null == advert || advert.getState() != 1) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500001);
				return "";
			}
			int openAmount = Integer.valueOf(openAmountStr);
			RelayLuckyBagVo relayLuckyBagVo = advertWebService.updateRelayLuckyBag(user, advert, openAmount);
			int errorCode = relayLuckyBagVo.getErrorCode();
			if (errorCode == (int) Constants.ERROR_CODE_500002) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500002);
				return "";
			}
			if (errorCode == (int) Constants.ERROR_CODE_500006) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500006);
				return "";
			}
			if (errorCode == (int) Constants.ERROR_CODE_900100) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_900100);
				return "";
			}
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("relayLuckyBagVo", relayLuckyBagVo);
			try {
				/* 增加广告人气 */
				advertWebService.addAdvertViewTimes(advert);
			} catch (Exception e) {
				logger.error("增加接力福袋人气报错");
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 格式化评论(格式化成前台需要的格式)
	 * 
	 * @param userCommentPage
	 * @return
	 */
	public Map formatUserComments(SocialResult<ListRange<UserComment>> userCommentPage) {
		Map<String, Object> map = new HashMap<>();

		if (userCommentPage != null) {
			List<UserComment> userComments = userCommentPage.getContent().getData();// 所有评论
			int commentCounts = userComments.size();// 评论数
			List commentsList = new ArrayList<>();
			for (UserComment userComment : userComments) {
				Map<String, Object> curMap = new HashMap<>();
				String commentNickName = userComment.getNickName();// 评论人
				String commentedNickName = "";// 被评论人
				String commentContent = userComment.getContent();
				long commentUserId = userComment.getUserId();// 评论人id
				long commentedUserId = 0;// 被评论人id
				if (userComment.getParentId() == 0) {
					commentedNickName = "";
					commentedUserId = 0;
				} else {
					commentedNickName = userComment.getParentNickName();// 被评论人昵称
					commentedUserId = userComment.getParentId();// 被评论人id
				}
				Date commentTime = userComment.getCreateTime();// 评论时间
				Long commentId = userComment.getId();// 评论id
				curMap.put("commentNickName", commentNickName);
				curMap.put("commentUserId", commentUserId);
				curMap.put("commentedUserId", commentedUserId);
				curMap.put("commentedNickName", commentedNickName);
				curMap.put("content", commentContent);
				curMap.put("commentTime", commentTime);
				curMap.put("commentId", commentId);
				commentsList.add(curMap);
			}
			map.put("commentsCount", commentCounts);
			map.put("commentList", commentsList);
		} else {
			map.put("commentsCount", 0);
			map.put("commentList", null);
		}

		return map;
	}

	/**
	 * 格式化点赞(格式化成前台需要的格式)
	 * 
	 * @param userCommentPage
	 * @return
	 */
	public Map formatUserPraised(SocialResult<ListRange<UserComment>> userCommentPage) {
		Map<String, Object> map = new HashMap<>();

		if (userCommentPage != null) {
			List<UserComment> userComments = userCommentPage.getContent().getData();// 所有评论
			int commentCounts = userComments.size();// 评论数
			List commentsList = new ArrayList<>();
			for (UserComment userComment : userComments) {
				Map<String, Object> curMap = new HashMap<>();
				String praisedNickName = userComment.getNickName();// 点赞人昵称
				long praisedUserId = userComment.getUserId();// 点赞人id

				Date commentTime = userComment.getCreateTime();// 评论时间
				Long praiseId = userComment.getId();// 点赞记录id
				curMap.put("praisedNickName", praisedNickName);
				curMap.put("praisedUserId", praisedUserId);
				commentsList.add(curMap);
			}
			map.put("praisedCount", commentCounts);
			map.put("praisedList", commentsList);
		} else {
			map.put("praisedCount", 0);
			map.put("praisedList", null);
		}

		return map;
	}

	/**
	 * 查询某用户是否抢过某广告
	 * 
	 * @param userId
	 * @param advertId
	 * @return true代表抢过 false代表未抢过
	 */
	public boolean checkUserHasFetchAdvert(long userId, long advertId) {
		Map<String, Object> sqlMap = new HashMap<>();
		sqlMap.put("userId", userId);
		sqlMap.put("advertId", advertId);
		int count = redEnvelopeService.selectCount(sqlMap);

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询首页圈子列表 advertType 3：优惠券，4：接力福袋，9：说说
	 * 
	 * @return
	 */
	@RequestMapping(value = "/communityAdvertList", method = RequestMethod.GET)
	public String communityAdvertList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		String loginKey = request.getParameter("loginKey");
		String longitudeStr = request.getParameter("longitude");// 经度
		String latitudeStr = request.getParameter("latitude");// 纬度

		List returnJsonList = new ArrayList<>();// 将来返回的总list

		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {

			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {

			Integer begin = 0;
			Map<String, Object> map = new HashMap<>();
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty()) {
				begin = Integer.valueOf(beginStr);
			}
			map.put("advertTypes", LuckyBagHelper.ADVERT_TYPE_3.getMarker() + ","
					+ LuckyBagHelper.ADVERT_TYPE_4.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_9.getMarker());
			map.put("order", "create_time desc");
			map.put("states", "" + AdvertHelper.STATE_1.getMarker() + "," + AdvertHelper.STATE_3.getMarker() + "," + AdvertHelper.STATE_5.getMarker());
			map.put("page", new Page(begin, 10));
			ListRange<Advert> advertsList = advertWebService.selectListRangeAdvert(map); // 默认返回按创建时间排序

			if (advertsList == null || advertsList.getData().size() <= 0) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
				return "";
			} else {
				for (Advert advert : advertsList.getData()) {
					/* 用户当前定位的经纬度距离广告发布经纬度的距离 */
					double userAndAdvertDistance = 0;
					if (StringUtils.isEmpty(latitudeStr) || StringUtils.isEmpty(longitudeStr)) {
						userAndAdvertDistance = -1;
					} else {
						userAndAdvertDistance = advertWebService.calAdvertDistanceBylongiandlati(advert,
								Double.valueOf(longitudeStr), Double.valueOf(latitudeStr));
					}
					final long publishUserId = advert.getUserId();// 发布当前广告用户Id
					final long advertId = advert.getId();
					final long userId = user.getId();
					UserResult<Business> businessResult = businessService
							.selectBusiness(new HashMap<String, Object>() {
								{
									put("userId", publishUserId);
								}
							});
					Map<String, Boolean> praiseAndCommentMap = userCommentService.selectUserPraiseAndComment(userId,
							advertId);// 是否点赞和评论map
					AdvertStatistics advertStatistics = advertWebService
							.selectAdvertStatistics(new HashMap<String, Object>() {
								{
									put("advertId", advertId);
								}
							});
					String publishNickName = userWebService.selectUserByUserId(advert.getUserId()).getContent()
							.getNickName();// 发布广告用户昵称
					String publishUserAvatar = userWebService.selectUserByUserId(advert.getUserId()).getContent().getAvatar();//发布广告用户头像
					int certificateLevel = userAgentService.selectUserCertificateLevel(advert.getUserId());
					boolean isFans = usersRelationService.isAFansB(userId, publishUserId);
					String businessAddress = "";
					if(businessResult != null){
						Business publishBusiness = businessResult.getContent();
						String province = publishBusiness.getProvince();//省份 
						String city = publishBusiness.getCity();//市
						String area = publishBusiness.getArea();//区
						String address = publishBusiness.getBusinessAddress();
						
						businessAddress = province + city + area + address;//商户地址
					}
					// 优惠券福袋广告start
					if (advert.getAdvertType() == 3) {

						HashMap<String, Object> couponAdvertMap = new HashMap<String, Object>();
						couponAdvertMap.put("publishId", publishUserId);// 发布广告用户id
						couponAdvertMap.put("publishNickName", publishNickName);// 发布广告用户昵称
						couponAdvertMap.put("publishUserCertificate", certificateLevel);// 用户认证信息
						couponAdvertMap.put("publishTime", advert.getCreateTime().getTime());// 发布时间
						couponAdvertMap.put("couponCount", advert.getRedEnvelopeCount());// 优惠券总数量
						couponAdvertMap.put("overPlusCouponCount",
								advert.getRedEnvelopeCount() - advertStatistics.getSendRedEnvelopeCount());// 优惠券剩余数量
						couponAdvertMap.put("fetchFlag", checkUserHasFetchAdvert(user.getId(), advertId));
						couponAdvertMap.put("userAndAdvertDistance", userAndAdvertDistance);
						couponAdvertMap.put("advertContent", advert.getAdvertContent());
						
						couponAdvertMap.put("isPraised", praiseAndCommentMap.get("praiseFlag"));// 用户是否点赞
						couponAdvertMap.put("isComment", praiseAndCommentMap.get("commentFlag"));// 用户是否评论
						couponAdvertMap.put("advertId", advertId);// 当前广告id
						couponAdvertMap.put("isFans", isFans);// 当前用户是否是发布这条广告用户的粉丝
						couponAdvertMap.put("avatar", user.getAvatar());// 当前用户用户头像
						couponAdvertMap.put("businessAddress", businessAddress);//商户地址
						couponAdvertMap.put("publisherAvatar", publishUserAvatar);// 发布广告的用户头像
						Long relationId = advert.getRelationId();// 对应的优惠券id
						BusinessCoupon businessCoupon = null;
						if (null != relationId) {
							businessCoupon = businessCouponService.selectBusinessCouponById(relationId);
//							sdfsfd
							if (businessResult.getState() == 1) {
								couponAdvertMap.put("businessAdress", businessResult.getContent().getBusinessAddress());
								couponAdvertMap.put("businessCouponId", businessCoupon.getId());// 优惠券ID
								couponAdvertMap.put("disCount", businessCoupon.getDiscount());// 折扣数量
								couponAdvertMap.put("businessCouponName", businessCoupon.getCouponTitle());// 优惠券的名称
								couponAdvertMap.put("startDate", businessCoupon.getStartDate().getTime());
								couponAdvertMap.put("endDate", businessCoupon.getEndDate().getTime());
								couponAdvertMap.put("businessCouponDetail", businessCoupon.getCouponContent());// 优惠券的详情
								couponAdvertMap.put("longitude", advert.getLongitude());// 经度
								couponAdvertMap.put("latitude", advert.getLatitude());// 纬度
								couponAdvertMap.put("advertType", 3);// 广告类型
								couponAdvertMap.put("couponType", businessCoupon.getCouponType());// 优惠券类型
																									// 1现金券
																									// 2折扣券
																									// 3实物券
								switch (businessCoupon.getCouponType()) {
								case 1:
									couponAdvertMap.put("couponTypeVal", businessCoupon.getMoney().toString());
									break;
								case 2:
									couponAdvertMap.put("couponTypeVal", businessCoupon.getDiscount().toString());
									break;
								case 3:
									couponAdvertMap.put("couponTypeVal", businessCoupon.getPhysicalVolume().toString());
									break;
								}
								if (businessResult.getState() == 1) {
									couponAdvertMap.put("businessName", businessResult.getContent().getBusinessName());// 商户名称
								}
							}
							/* 查询评论start */
							HashMap<String, Object> userCommentSqlMap = new HashMap<String, Object>();
							userCommentSqlMap.put("sourceId", advert.getId());
							userCommentSqlMap.put("commentType", 1);
							userCommentSqlMap.put("state", 1);
							userCommentSqlMap.put("order", "create_time");
							userCommentSqlMap.put("page", new Page(0, 100));
							SocialResult<ListRange<UserComment>> userCommentPage = userCommentService
									.selectListUserCommentPage(userCommentSqlMap);

							couponAdvertMap.put("userCommentList", formatUserComments(userCommentPage));// 用户评论列表
							/* 查询评论end */

							/* 点赞数 start */
							userCommentSqlMap.put("commentType", 2);
							SocialResult<ListRange<UserComment>> userPraisedPage = userCommentService
									.selectListUserCommentPage(userCommentSqlMap);
							couponAdvertMap.put("praisedList", formatUserPraised(userPraisedPage));// 点赞列表
							/* 点赞数end */

						}
						returnJsonList.add(couponAdvertMap);
					}

					// 接力福袋广告start
					if (advert.getAdvertType() == 4) {
						HashMap<String, Object> relayAdvertMap = new HashMap<String, Object>();
						relayAdvertMap.put("publishId", publishUserId);// 发布广告用户id
						relayAdvertMap.put("publishNickName", publishNickName);// 发布广告用户昵称
						relayAdvertMap.put("publishUserCertificate", certificateLevel);// 用户认证信息
						relayAdvertMap.put("publishTime", advert.getCreateTime().getTime());// 发布时间
						relayAdvertMap.put("basicMoney", advert.getRedEnvelopeAmount());// 接力福袋基础金额
						relayAdvertMap.put("advertType", advert.getAdvertType());// 广告类型
						relayAdvertMap.put("advertContent", advert.getAdvertContent());
						relayAdvertMap.put("fetchFlag", checkUserHasFetchAdvert(user.getId(), advertId));
						relayAdvertMap.put("userAndAdvertDistance", userAndAdvertDistance);
						relayAdvertMap.put("currentTotalMoney", advertStatistics.getSendRedEnvelopeAmount());// 接力广告当前累计金额
						relayAdvertMap.put("isPraised", praiseAndCommentMap.get("praiseFlag"));// 用户是否点赞
						relayAdvertMap.put("isComment", praiseAndCommentMap.get("commentFlag"));// 用户是否评论
						relayAdvertMap.put("advertId", advertId);// 当前广告id
						relayAdvertMap.put("isFans", usersRelationService.isAFansB(userId, publishUserId));// 当前用户是否是发布这条广告用户的粉丝
						relayAdvertMap.put("publisherAvatar", publishUserAvatar);// 发布广告的用户头像
						Map<String, Object> redEnvelopeSqlMap = new HashMap<String, Object>();

						redEnvelopeSqlMap.put("advertId", advert.getId());
						redEnvelopeSqlMap.put("order", "create_time desc");
						redEnvelopeSqlMap.put("businessAddress", businessAddress);//商户地址
						List<RedEnvelope> redEnvelopeList = redEnvelopeService.selectListRedEnvelope(redEnvelopeSqlMap);

						if (redEnvelopeList != null) {
							for (RedEnvelope redEnvelope : redEnvelopeList) {
								if (redEnvelope.getAmout() > 0) {
									relayAdvertMap.put("totalMoney", redEnvelope.getAmout() / 100);// 福袋总金额
									relayAdvertMap.put("finalUserId", redEnvelope.getUserId());// 获奖用户id
									relayAdvertMap.put("finalUserNickName", redEnvelope.getUserNickName());// 获奖用户昵称
								}
								break;
							}
						}

						int advertDeliveryCounts = redEnvelopeService.selectCount(redEnvelopeSqlMap);// 接力福袋传递次数
						relayAdvertMap.put("advertDeliveryCounts", advertDeliveryCounts);// 传递人数

						/* 评论列表 */
						HashMap<String, Object> userCommentSqlMap = new HashMap<String, Object>();
						userCommentSqlMap.put("sourceId", advert.getId());
						userCommentSqlMap.put("commentType", 1);
						userCommentSqlMap.put("state", 1);
						userCommentSqlMap.put("order", "create_time");
						userCommentSqlMap.put("page", new Page(0, 100));
						SocialResult<ListRange<UserComment>> userCommentPage = userCommentService
								.selectListUserCommentPage(userCommentSqlMap);
						relayAdvertMap.put("userCommentList", formatUserComments(userCommentPage));// 用户评论列表

						/* 评论列表end */

						/* 点赞数 start */
						userCommentSqlMap.put("commentType", 2);
						SocialResult<ListRange<UserComment>> userPraisedPage = userCommentService
								.selectListUserCommentPage(userCommentSqlMap);
						relayAdvertMap.put("praisedList", formatUserPraised(userPraisedPage));// 点赞列表
						/* 点赞数end */

						/* 接力福袋参与最后2条信息 */
						List<Map> relayRedInfoList = redEnvelopeService.getLastRedEnvelopeRecords(2, advertId);

						relayAdvertMap.put("lastRelayBagSummaryList", relayRedInfoList);
						/* 接力福袋参与最后2条信息 */

						returnJsonList.add(relayAdvertMap);
					}

					// 说说start
					if (advert.getAdvertType() == 9) {
						if(userId != publishUserId){
							if(!isFans){
								//如果不是粉丝并且发布广告的人不是自己
								continue;
							}
						}
						
						
						HashMap<String, Object> shuoshuoMap = new HashMap<String, Object>();
						shuoshuoMap.put("publishId", publishUserId);// 发布广告用户id
						shuoshuoMap.put("publishNickName", publishNickName);// 发布广告用户昵称
						shuoshuoMap.put("publishUserCertificate", certificateLevel);// 用户认证信息
						shuoshuoMap.put("publishTime", advert.getCreateTime().getTime());// 发布时间

						shuoshuoMap.put("advertContent", advert.getAdvertContent());// 广告内容
						shuoshuoMap.put("fetchFlag", checkUserHasFetchAdvert(user.getId(), advertId));
						shuoshuoMap.put("userAndAdvertDistance", userAndAdvertDistance);

						shuoshuoMap.put("isPraised", praiseAndCommentMap.get("praiseFlag"));// 用户是否点赞
						shuoshuoMap.put("isComment", praiseAndCommentMap.get("commentFlag"));// 用户是否评论
						shuoshuoMap.put("advertId", advertId);// 当前广告id
						shuoshuoMap.put("isFans", usersRelationService.isAFansB(userId, publishUserId));// 当前用户是否是发布这条广告用户的粉丝
						shuoshuoMap.put("avatar", user.getAvatar());// 发布广告的用户头像
						shuoshuoMap.put("businessAddress", businessAddress);//商户地址
						shuoshuoMap.put("publisherAvatar", publishUserAvatar);// 发布广告的用户头像
						if (!StringUtil.isEmpty(advert.getAdvertContent())) {

							shuoshuoMap.put("advertPic", advert.getAdvertContent());
						}
						HashMap<String, Object> userCommentMap = new HashMap<String, Object>();

						userCommentMap.put("sourceId", advert.getId());
						userCommentMap.put("commentType", 1);
						userCommentMap.put("state", 1);
						userCommentMap.put("order", "create_time");
						userCommentMap.put("page", new Page(0, 100));
						SocialResult<ListRange<UserComment>> userCommentPage = userCommentService
								.selectListUserCommentPage(userCommentMap);
						shuoshuoMap.put("userCommentList", formatUserComments(userCommentPage));// 用户评论列表
						/* 点赞数 start */
						userCommentMap.put("commentType", 2);
						SocialResult<ListRange<UserComment>> userPraisedPage = userCommentService
								.selectListUserCommentPage(userCommentMap);
						shuoshuoMap.put("praisedList", formatUserPraised(userPraisedPage));// 点赞列表
						/* 点赞数end */
						shuoshuoMap.put("advertType", advert.getAdvertType());// 广告类型

						returnJsonList.add(shuoshuoMap);
					}

				}
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		modelMap.put("result", Constants.ERROR_OK);
		modelMap.put("advertList", returnJsonList);
		return "";
	}

	/**
	 * 公益广告捐款明细
	 * 
	 * @param loginKey
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/publicWelfare/content", method = RequestMethod.GET)
	public String publicWelfareContent(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("advertId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));
		List<RedEnvelope> redList = new ArrayList<>();
		AdvertResult<AdvertStatistics> advertResult = new AdvertResult<>();
		int amount = 0;
		if (user != null) {
			/* 查询公益广告捐款排行 */
			Map<String, Object> map = new HashMap<>();
			map.put("advertId", request.getParameter("advertId"));
			redList = advertService.selectRedEnvelopeList(map);
			if (redList.size() != 0) {
				advertResult = advertService.selectAdvertStatistics(map);
				// 捐款总额
				amount = advertResult.getContent().getSendRedEnvelopeAmount();
			}
		} else {
			modelMap.put("result", 0);
		}
		// 捐款明细列表
		modelMap.put("redList", redList);
		modelMap.put("amount", amount);
		modelMap.put("result", 1);
		return "";
	}

	/**
	 * 公益捐款
	 * 
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/publicWelfare/add", method = RequestMethod.GET)
	public String publicWelfareAdd(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(request.getParameter("advertId"))
				|| StringUtil.isEmpty(request.getParameter("amout"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		String amout = request.getParameter("amout");
		if(Integer.valueOf(amout) <= 0){
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_500009);
			return "";
		}
		String advertId = request.getParameter("advertId");
		int result = 0;
		User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));
		if (user != null) {
			/* 判断用户零钱是否够支付 */
			UserAssets userAssets = userAssetsService.selectUserAssets(user.getId());
			if (userAssets.getRedEnvelopeBalance() >= Integer.parseInt(amout)) {
				/* 查询公益广告 */
				AdvertResult<Advert> advertResult = advertService.selectAdvertById(Long.parseLong(advertId));
				if (advertResult.getState() == 1) {
					/* 扣除用户捐款额 */
					userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() - Integer.parseInt(amout));
					UserResult<Integer> userResult = userAssetsService.updateUserAssets(userAssets);
					if (userResult.getState() == 1) {
						RedEnvelope redEnvelope = new RedEnvelope();
						redEnvelope.setAdvertId(Long.parseLong(advertId));
						redEnvelope.setPublisherId(advertResult.getContent().getUserId());
						redEnvelope.setUserId(user.getId());
						redEnvelope.setAmout(-Integer.parseInt(amout));
						redEnvelope.setUserNickName(user.getNickName());
						redEnvelope.setUserAvatar(user.getAvatar());
						redEnvelope.setUserSex(user.getSex().byteValue());
						redEnvelope.setType(3);
						redEnvelope.setCreateTime(new Date());
						/* 增加捐款记录 */
						AdvertResult<RedEnvelope> redResult = advertService.addRedEnvelope(redEnvelope);
						if (redResult.getState() == 1) {
							Map<String, Object> adsMap = new HashMap<>();
							adsMap.put("advertId", Long.parseLong(advertId));
							/* 查询公益广告状态信息 */
							AdvertResult<AdvertStatistics> adsResult = advertService.selectAdvertStatistics(adsMap);
							/* 捐款成功修改公益广告信息 */
							AdvertStatistics advertStatistics = adsResult.getContent();
							advertStatistics.setSendRedEnvelopeAmount(
									advertStatistics.getSendRedEnvelopeAmount() + Integer.parseInt(amout));
							advertStatistics.setSendRedEnvelopeCount(advertStatistics.getSendRedEnvelopeCount() + 1);
							AdvertResult<Integer> updateResult = advertService.updateAdvertStatistics(advertStatistics);
							result = updateResult.getState();
						}
					}
				} else {
					modelMap.put("errorCode", Constants.ERROR_CODE_500004);
				}
			} else {
				modelMap.put("errorCode", Constants.ERROR_CODE_200502);
			}
		} else {
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 公益首页
	 * 
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/publicWelfare", method = RequestMethod.GET)
	public String publicWelfare(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		int result = 0;
		User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));
		if (user != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("advertType", 10);// 公益广告类型10
			/* 查询所有公益广告 */
			List<Advert> advertList = advertService.selectAllPublicWelFare(map);
			List<AdvertVo> advertVos = new ArrayList<>();
			int cNum = 0;
			int countMoney = 0;// 记录总金额
			for (Advert advert : advertList) {
				AdvertVo advertVo = new AdvertVo();
				BeanUtils.copyProperties(advert, advertVo);
				AdvertResult<AdvertStatistics> advertResult = advertService
						.selectAdvertStatisticsByAdvertId(advertVo.getId());
				if (advertResult.getState() == 1) {
					AdvertStatistics advertStatistics = advertResult.getContent();
					advertVo.setRedEnvelopeAmount(advertStatistics.getSendRedEnvelopeAmount());// 每个公益的捐款总额
					advertVo.setRedEnvelopeCount(advertStatistics.getSendRedEnvelopeCount());// 每个公益的捐款总人数
					if (advertVo.getRedEnvelopeCount() == null) {
						cNum += 0;
					} else {
						cNum += advertVo.getRedEnvelopeCount();
					}
					if (advertVo.getRedEnvelopeAmount() == null) {
						countMoney += 0;
					} else {
						countMoney += advertVo.getRedEnvelopeAmount();
					}
					advertVos.add(advertVo);
				}
			}
			String countNum = String.valueOf(cNum);// 记录总人数
			while (true) {
				if (countNum.length() < 9) {
					countNum = "0" + countNum;
				} else {
					break;
				}
			}
			result = 1;
			modelMap.put("advert", advertVos);
			modelMap.put("countNum", countNum);
			modelMap.put("countMoney", countMoney);
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 领取首页广告奖励
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/getHomePageAward", method = RequestMethod.GET)
	public String getHomePageAward(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String advertId = request.getParameter("advertId").trim();
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Advert advert = advertWebService.selectAdvert(Long.valueOf(advertId));
			if (null == advert || advert.getState() != 1) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500001);
				return "";
			}
			HomePageAwardVo homePageAwardVo = advertWebService.updateHomePageAward(user, advert);
			int errorCode = homePageAwardVo.getErrorCode();
			if (errorCode == (int) Constants.ERROR_CODE_500002) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500002);
				return "";
			}
			if (errorCode == (int) Constants.ERROR_CODE_500005) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500005);
				return "";
			}
			if (errorCode == (int) Constants.ERROR_CODE_900100) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_900100);
				return "";
			}
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("homePageAwardVo", homePageAwardVo);
			/*领取首页福袋任务*/
			try {
				taskHistoryService.insertTask(user.getId(), (long)2, "1");
			} catch (Exception e) {
				logger.error("插入领取首页福袋任务报错");
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 查询运营活动和定时活动(官方)
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	@RequestMapping(value = "/getOperateAndTimerActivity", method = RequestMethod.GET)
	public String getOperateAndTimerActivity(HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		try {
			User user = userWebService.getUserByLoginKey(loginKey);
			if (user != null) {
				Map<String, Object> map = new HashMap<>();
				Date date = new Date();
				Map<String, Object> operateActivity = new HashMap<>(); // 运营活动
				Map<String, Object> timerActivity = new HashMap<>(); // 定时活动(官方)
				/* 查询运营活动信息 */
				map.put("advertType", AdvertHelper.ADVERT_TYPE_7.getMarker());
				map.put("states", "" + AdvertHelper.STATE_0.getMarker() + "," + AdvertHelper.STATE_1.getMarker());
				map.put("rank", 100);
				map.put("showTime", DateUtils.formatDate(date));
				map.put("order", "start_time");
				List<Advert> adverts = advertWebService.selectOperateActivity(map);
				if (null != adverts && adverts.size() > 0) {
					Advert advert = adverts.get(0);
					operateActivity.put("advertId", advert.getId());
					operateActivity.put("advertName", advert.getAdvertName());
					operateActivity.put("preferredPic", advert.getPreferredPic());
				}
				/* 查询定时活动(官方)信息 */
				map.clear();
				map.put("advertType", AdvertHelper.ADVERT_TYPE_8.getMarker());
				map.put("states", "" + AdvertHelper.STATE_0.getMarker() + "," + AdvertHelper.STATE_1.getMarker());
				map.put("startTimegt", date);
				map.put("order", "start_time");
				List<Advert> list = advertWebService.selectListAdvert(map);
				if (null != list && list.size() > 0) {
					Advert advert = list.get(0);
					long secondNum = Math.abs(advert.getStartTime().getTime() - date.getTime()) / 1000;
					timerActivity.put("advertId", advert.getId());
					timerActivity.put("advertName", advert.getAdvertName());
					timerActivity.put("secondNum", secondNum);
				}
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("operateActivity", operateActivity);
				modelMap.put("timerActivity", timerActivity);
			} else {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			}
		} catch (Exception e) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100003);
		}
		return "";
	}
}
