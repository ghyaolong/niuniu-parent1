package com.mouchina.web.controller.advert;

import java.io.IOException;
import java.util.ArrayList;
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

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.BannerConfig;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserHelper;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.exceptions.AdvertException;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.income.AgentIncomeService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.config.Env;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.advert.AdvertWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.AdvertShowVo;
import com.mouchina.web.service.api.vo.AdvertStatisticsVo;
import com.mouchina.web.service.api.vo.AdvertVo;
import com.mouchina.web.service.api.vo.RedEnvelopeVo;

@Controller
@RequestMapping("/business/advert")
public class BusinessAdvertController {

	private final Logger logger = Logger.getLogger(getClass());
	
	@Resource
	AdvertService advertService;
	
	@Resource
	AdvertWebService advertWebService;
	@Resource
	UserWebService userWebService;
	@Resource
	SystemService systemService;
	@Resource
	Env env;
	@Resource
	SocialWebService socialWebService;
	@Resource
	AgentIncomeService agentIncomeService;
	
	
	@RequestMapping(value = "/bannerAdvert", method = { RequestMethod.GET, RequestMethod.POST })
	public String bannerList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		if(StringUtil.isEmpty(loginKey)){
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user !=null){
			Map<String,Object> sqlMap = new HashMap<String,Object>();
			sqlMap.put("bannerType", 1);
			sqlMap.put("flag", 1);
			List<BannerConfig> list =advertService.selectListAdvertBannerConfig(sqlMap);
			//定义一个空集合
			List listBanner =new ArrayList();
			if(list != null && list.size()>0){
				Map<String,Object> mapBanner;
				for(BannerConfig bannerVo:list){
					mapBanner =new HashMap<String,Object>();
					String picurl =bannerVo.getPicUrl();  //banner图片url
					String secondLevelUrl =bannerVo.getSecondLevelUrl(); //二级链接url
					Integer picIndex =bannerVo.getPicIndex(); //图片序号
					mapBanner.put("picurl", picurl);
					mapBanner.put("secondLevelurl",secondLevelUrl);
					mapBanner.put("picIndex", picIndex);
					listBanner.add(mapBanner);
				}
				Map<String,Object> publishAdvertMoney =new HashMap<String,Object>();
				Map<String, Object> systemMap = new HashMap<String, Object>();
				List<SystemDataVo> sysDataVo = systemService.selectListSystemData(systemMap);
				SystemDataVo vo = sysDataVo.get(0);
				String key = vo.getName();
				String val = vo.getValue().toString();
				publishAdvertMoney.put(key, "");  //广告发布总金额 publishAdvertMoney key
				publishAdvertMoney.put("listBanner", listBanner);  
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("advertList", publishAdvertMoney);
			}
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String advertList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {

		String loginKey = request.getParameter("loginKey");
		String state = request.getParameter("state");
		String states = request.getParameter("states");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		int begin = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		User user = userWebService.getUserByLoginKey(loginKey);
		if (state != null && !state.isEmpty()) {
			map.put("state", state);
		}
		if (states != null && !states.isEmpty()) {
			map.put("states", states);
		}

		if (user != null) {
			modelMap.put("result", "1");
			map.put("userId", user.getId());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			ListRange<Advert> listRange = advertWebService.selectListRangeAdvert(map);
			List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
			for (Advert advert : listRange.getData()) {
				AdvertVo advertVo = new AdvertVo();
				BeanUtils.copyProperties(advert, advertVo);

				advertVoList.add(advertVo);
			}
			modelMap.put("advertList", advertVoList);
			modelMap.put("page", listRange.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	@RequestMapping(value = "/searchAdvertById", method = RequestMethod.GET)
	public String info(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {

		response.addHeader("Access-Control-Allow-Origin", "*");

		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		long userId = 0;
		if (request.getParameter("loginKey") != null) {
			User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));

			if (user != null) {
				userId = user.getId();
			}
		}

		if (userId > 0) {
			modelMap.put("result", "1");
			Long advertId = Long.valueOf(request.getParameter("advertId"));

			Advert advert = advertWebService.selectAdvert(advertId);
			AdvertShowVo advertVo = new AdvertShowVo();
			BeanUtils.copyProperties(advert, advertVo);
			if (advert != null) {

				if (userId > 0) {
					Map<String, Object> fansMap = new HashMap<String, Object>();
					fansMap.put("userId", advert.getUserId());
					fansMap.put("relationUserId", userId);
					fansMap.put("relationType", 2);
					int count = socialWebService.selectListUsersRelationCount(fansMap);
					advertVo.setIsFans(count > 0 ? 1 : 0);

					Map<String, Object> collectMap = new HashMap<String, Object>();
					collectMap.put("eventId", advert.getId());
					collectMap.put("relationUserId", userId);
					collectMap.put("relationType", 1);
					int collectCount = socialWebService.selectListUsersRelationCount(collectMap);
					advertVo.setIsCollect(collectCount > 0 ? 1 : 0);
				}
				advertWebService.addAdvertViewTimes(advert);// 添加广告人气 加1
			}
			modelMap.put("advert", advertVo);
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	@RequestMapping(value = "/list/show", method = RequestMethod.GET)
	public String advertListVo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Long businessId) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();

		UserResult<Business> userBusinessResult = userWebService.getBusiness(businessId);

		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		long userId = 0;
		if (request.getParameter("loginKey") != null) {
			User user = userWebService.getUserByLoginKey(request.getParameter("loginKey"));

			if (user != null) {
				userId = user.getId();
			}
		}

		if (userBusinessResult.getState() == 1 && userBusinessResult.getContent() != null) {
			modelMap.put("result", "1");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userBusinessResult.getContent().getUserId());
			map.put("order", "create_time desc");
			map.put("states", "2,3,5");
			map.put("page", new Page(begin, 10));
			ListRange<Advert> listRange = advertWebService.selectListRangeAdvert(map);
			List<AdvertShowVo> advertVoList = new ArrayList<AdvertShowVo>();
			for (Advert advert : listRange.getData()) {
				AdvertShowVo advertVo = new AdvertShowVo();
				BeanUtils.copyProperties(advert, advertVo);

				if (userId > 0) {
					Map<String, Object> fansMap = new HashMap<String, Object>();
					fansMap.put("userId", advert.getUserId());
					fansMap.put("relationUserId", userId);
					fansMap.put("relationType", 2);
					int count = socialWebService.selectListUsersRelationCount(fansMap);
					advertVo.setIsFans(count > 0 ? 1 : 0);

					Map<String, Object> collectMap = new HashMap<String, Object>();
					collectMap.put("eventId", advert.getId());
					collectMap.put("relationUserId", userId);
					collectMap.put("relationType", 1);
					int collectCount = socialWebService.selectListUsersRelationCount(collectMap);
					advertVo.setIsCollect(collectCount > 0 ? 1 : 0);
				}

				advertVoList.add(advertVo);
			}
			modelMap.put("advertList", advertVoList);
			modelMap.put("page", listRange.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	/**
	 * 发布广告
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param advert
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public String advertPublish(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Advert advert) throws IOException {

		if (StringUtil.isEmpty(request.getParameter("loginKey")) || StringUtil.isEmpty(advert.getAdvertName())
				|| StringUtil.isEmpty(request.getParameter("endTime"))
				|| StringUtil.isEmpty(request.getParameter("startTime"))
				|| StringUtil.isEmpty(request.getParameter("redEnvelopeType"))
				|| StringUtil.isEmpty(request.getParameter("redEnvelopeAmount"))
				|| StringUtil.isEmpty(request.getParameter("redEnvelopeCount"))
				|| StringUtil.isEmpty(request.getParameter("advertContent"))
				|| StringUtil.isEmpty(request.getParameter("advertType")) || advert.getRedEnvelopeAmount() < 1
				|| advert.getRedEnvelopeCount() < 1) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String loginKey = request.getParameter("loginKey");
		User user = userWebService.getUserByLoginKey(loginKey);

		String provinceCode = request.getParameter("provinceCode");
		String cityCode = request.getParameter("cityCode");
		String countyCode = request.getParameter("countyCode");

		advert.setAdverConditionArea(countyCode != null && !countyCode.isEmpty() ? countyCode : "all");
		advert.setAdverConditionCity(cityCode != null && !cityCode.isEmpty() ? cityCode : "all");
		advert.setAdverConditionProvince(provinceCode != null && !provinceCode.isEmpty() ? provinceCode : "all");

		if (user != null) {
			/* 这是两个条件过滤 start */
			if (advert.getRedEnvelopeType() == 2) {// 红包类型2代表 定额红包
				if (advert.getRedEnvelopeAmount() % advert.getRedEnvelopeCount() != 0
						|| advert.getRedEnvelopeAmount() / advert.getRedEnvelopeCount() < 20) {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_200100);
				}
			}
			if (advert.getRedEnvelopeType() == 1) {// 红包类型1代表 拼手气
				if (advert.getRedEnvelopeAmount() / advert.getRedEnvelopeCount() < 20) {

					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_200100);
				}
			}
			/* 这是两个条件过滤 end */

			advert.setUserId(user.getId());

			/*
			 * if (StringUtil.isEmpty(advert.getAdverCondtionAgeScope())) {
			 * advert.setAdverCondtionAgeScope("0,");
			 * 
			 * }
			 */
			AdvertResult<Advert> advertResult = advertWebService.addAdvert(advert, user);

			if (advertResult.getState() == 1) {

				// 更新SystemData中的全网广告总发布钱数 start
				Map<String, Object> systemData = new HashMap<String, Object>();
				systemData.put("key_name", "publishAdvertMoney");
				SystemDataVo sysDataVo = systemService.selectListSystemData(systemData).get(0);

				SystemData sysData = new SystemData();
				sysData.setId(sysDataVo.getId());
				sysData.setSysValue(sysDataVo.getValue() + advert.getRedEnvelopeAmount() / 100);
				systemService.updateSystemData(sysData);

				// 更新SystemData中的全网广告总发布钱数 end
				modelMap.put("result", "1");
				modelMap.put("advertId", advertResult.getContent().getId());
				if (advert.getRedEnvelopeAmount() > 0) {
					// 计算代理商收益线程
					new AgentIncomeThread(agentIncomeService, advert).start();
				}
			} else {
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_200101);
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		modelMap.remove("advert");
		return "";
	}

	/**
	 * 发布广告【3.0版本】
	 * 
	 * @param publishAdvertVo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/advertPublish3" )
	public String advertPublish3(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Advert advert) throws IOException {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.remove("advert");
			return "";
		}
		String loginKey = request.getParameter("loginKey");

		User user = userWebService.getUserByLoginKey(loginKey);

		String provinceCode = advert.getAdverConditionProvince();
		String cityCode = advert.getAdverConditionCity();
		String countyCode = advert.getAdverConditionArea();

		advert.setAdverConditionArea(countyCode != null && !countyCode.isEmpty() ? countyCode : "all");
		advert.setAdverConditionCity(cityCode != null && !cityCode.isEmpty() ? cityCode : "all");
		advert.setAdverConditionProvince(provinceCode != null && !provinceCode.isEmpty() ? provinceCode : "all");

		Map<String, Object> map = new HashMap<>();
		
		if(user != null) {
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(user.getId()).getContent();
			if (null == business) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_300100);
				modelMap.remove("advert");
				return "";
			}
			int advertType = advert.getAdvertType();
			/* 发布优惠券(圈子)权限，每天只可发布一条，且只有认证商户才可以发  
			 * 发布优惠券(福袋),只有认证商户才可以发   */
			if(advertType == AdvertHelper.ADVERT_TYPE_1.getMarker().intValue()
					|| advertType == AdvertHelper.ADVERT_TYPE_3.getMarker().intValue()){
				if(business.getCertificationType().intValue() != UserHelper.USER_IDENTITY_2.getMarker().intValue()){
					modelMap.put("result", Constants.ERROR_ERROR);
					modelMap.put("errorCode", Constants.ERROR_CODE_300101);
					modelMap.remove("advert");
					return "";
				}
				/*查询当天是否发布过优惠券(圈子)，如果发布过，则当天不可再发布*/
				if(advertType == AdvertHelper.ADVERT_TYPE_3.getMarker().intValue()){
					map.clear();
					map.put("userId", user.getId());
					map.put("advertType", advertType);
					map.put("crateTimeDay", new Date());
					int count = advertWebService.selectAdvertCount(map);
					if(count > 0){
						modelMap.put("result", Constants.ERROR_ERROR);
						modelMap.put("errorCode", Constants.ERROR_CODE_300102);
						modelMap.remove("advert");
						return "";
					}
				}
			}
			/* 发布说说权限，每天只可发布一条  */
			if(advertType == AdvertHelper.ADVERT_TYPE_9.getMarker().intValue()){
				/*查询当天是否发布过说说，如果发布过，则当天不可再发布*/
				map.clear();
				map.put("userId", user.getId());
				map.put("advertType", advertType);
				map.put("crateTimeDay", new Date());
				int count = advertWebService.selectAdvertCount(map);
				if(count > 0){
					modelMap.put("result", Constants.ERROR_ERROR);
					modelMap.put("errorCode", Constants.ERROR_CODE_300102);
					modelMap.remove("advert");
					return "";
				}
			}
			
			if (advertType == AdvertHelper.ADVERT_TYPE_2.getMarker().intValue() 
					|| advertType == AdvertHelper.ADVERT_TYPE_6.getMarker().intValue()
					|| advertType == AdvertHelper.ADVERT_TYPE_8.getMarker().intValue()) {
				advert.setState((byte) AdvertHelper.STATE_0.getMarker().intValue());
			}else{
				advert.setState((byte) AdvertHelper.STATE_1.getMarker().intValue());
			}
			advert.setUserId(user.getId());
			if(advert.getAdvertName() == null || advert.getAdvertName().isEmpty()) {
				switch (advertType) {
				case 0:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_0.getDisplay());
					break;
				case 1:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_1.getDisplay());
					break;
				case 2:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_2.getDisplay());
					break;
				case 3:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_3.getDisplay());
					break;
				case 4:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_4.getDisplay());
					break;
				case 5:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_5.getDisplay());
					break;
				case 6:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_6.getDisplay());
					break;
				case 7:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_7.getDisplay());
					break;
				case 8:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_8.getDisplay());
					break;
				case 9:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_9.getDisplay());
					break;
				case 10:
					advert.setAdvertName(AdvertHelper.ADVERT_TYPE_10.getDisplay());
					break;
				default:
					break;
				}
			}
			if(advert.getRedEnvelopeAmount() == null) {
				advert.setRedEnvelopeAmount(0);
			}
			if(advert.getRedEnvelopeCount() == null) {
				advert.setRedEnvelopeCount(0);
			}
			AdvertResult<Advert> advertResult = advertWebService.addAdvert3(advert, user);

			// 如果为1就表示添加广告成功
			if(advertResult.getState() == 1) {
				logger.info("更新广告的统计！");

				// 广告金额总计 更新SystemData中的全网广告总发布钱数 start
				Map<String, Object> publishAdvertMoneyMap = new HashMap<String, Object>();
				publishAdvertMoneyMap.put("key_name", "publishAdvertMoney");
				SystemDataVo sysDataVo = systemService.selectListSystemData(publishAdvertMoneyMap).get(0);
				SystemData sysData = new SystemData();
				sysData.setId(sysDataVo.getId());
				sysData.setSysValue(sysDataVo.getValue() + advert.getRedEnvelopeAmount());
				systemService.updateSystemData(sysData);
				// 更新SystemData中的全网广告总发布钱数 end

				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("advertId", advertResult.getContent().getId());
				
				
				// 计算代理商收益线程 如果红包金额小于零，就不用计算收益,只针对普通福袋、优惠券福袋、定时广告
				if (advert.getRedEnvelopeAmount() > 0 && 
						(advert.getAdvertType() == 0 
						|| advert.getAdvertType() == 1
						|| advert.getAdvertType() == 6)) {
					logger.info("计算代理商收益!");
					new AgentIncomeThread(agentIncomeService, advert).start();
				}
			} else {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_200101);
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("advert");
		return "";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String advertEdit(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Advert advert)
			throws IOException {

		String loginKey = request.getParameter("loginKey");

		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("advertId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		Long advertId = Long.valueOf(request.getParameter("advertId"));

		User user = userWebService.getUserByLoginKey(loginKey);

		Advert selectAdvert = advertWebService.selectAdvert(advertId);
		if (user != null && selectAdvert != null && selectAdvert.getState() == 6
				&& user.getId().longValue() == selectAdvert.getUserId().longValue()) {
			advert.setId(selectAdvert.getId());

			int result = advertWebService.updateAdvert(advert);
			modelMap.put("result", result);

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("advert");
		return "";
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	public String advertStop(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, Advert advert)
			throws IOException {

		String loginKey = request.getParameter("loginKey");

		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("advertId"))) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		Long advertId = Long.valueOf(request.getParameter("advertId"));

		User user = userWebService.getUserByLoginKey(loginKey);

		Advert selectAdvert = advertWebService.selectAdvert(advertId);
		if (user != null && selectAdvert != null && selectAdvert.getState() == 1
				&& user.getId().longValue() == selectAdvert.getUserId().longValue()) {
			advert.setId(selectAdvert.getId());

			AdvertResult<AdvertStatistics> advertResult = advertWebService.updateStopAdvert(advert);
			if (advertResult.getState() == 1) {

				modelMap.put("remainRedEnvelopeAmount", advertResult.getContent().getSendRedEnvelopeAmount());
				modelMap.put("remainRedEnvelopeCount", advertResult.getContent().getSendRedEnvelopeCount());

				modelMap.put("result", "1");
			}

			if (advertResult.getState() == 3 || advertResult.getState() == 5) {
				modelMap.put("result", "2");
			}

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("advert");
		return "";
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String statistics(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		try {
			final String loginKey = request.getParameter("loginKey");
			final String advertId = request.getParameter("advertId");
			if (StringUtils.isBlank(loginKey) || StringUtils.isBlank(advertId)) {
				throw new AdvertException(false, "参数(loginKey or advertId)为空");
			}

			User user = userWebService.getUserByLoginKey(loginKey);
			if (null == user || null == user.getId()) {
				throw new AdvertException(false, String.format("用户不存在，参数：loginKey=%s", loginKey));
			}

			Long advertIdLong = Long.valueOf(advertId.trim());
			Advert advert = advertWebService.selectAdvert(advertIdLong);
			if (null == advert || null == advert.getUserId()) {
				throw new AdvertException(false, String.format("广告不存在，参数：advertId=%s", advertId));
			}

			if (user.getId().longValue() != advert.getUserId().longValue()) {
				throw new AdvertException(false,
						String.format("当前用户没有此广告，参数：userId=%s,advertId=%s", user.getId(), advertId));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("advertId", advert.getId());
			map.put("order", "create_time desc");
			AdvertStatistics advertStatistics = advertWebService.selectAdvertStatistics(map);

			AdvertStatisticsVo advertStatisticsVo = new AdvertStatisticsVo();

			BeanUtils.copyProperties(advertStatistics, advertStatisticsVo);
			// // 已发出广告 = 真实领取 + 冻结广告
			// advertStatisticsVo.setSendRedEnvelopeCount(
			// advertStatistics.getSendRedEnvelopeCount()+advertStatistics.getFreezeEnvelopeCount()
			// );
			modelMap.put("advertStatistics", advertStatisticsVo);
			modelMap.put("result", "1");
		} catch (AdvertException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 商家广告明细统计
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/statistics/detail/list", method = RequestMethod.GET)
	public String statisticsDetailList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		try {
			final String loginKey = request.getParameter("loginKey");
			final String advertId = request.getParameter("advertId");
			if (StringUtils.isBlank(loginKey) || StringUtils.isBlank(advertId)) {
				throw new AdvertException(false, "参数(loginKey or advertId)为空");
			}

			User user = userWebService.getUserByLoginKey(loginKey);
			if (null == user || null == user.getId()) {
				throw new AdvertException(false, String.format("用户不存在，参数：loginKey=%s", loginKey));
			}

			Long advertIdLong = Long.valueOf(advertId.trim());
			Advert advert = advertWebService.selectAdvert(advertIdLong);
			if (null == advert || null == advert.getUserId()) {
				throw new AdvertException(false, String.format("广告不存在，参数：advertId=%s", advertId));
			}

			if (user.getId().longValue() != advert.getUserId().longValue()) {
				throw new AdvertException(false,
						String.format("当前用户没有此广告，参数：userId=%s,advertId=%s", user.getId(), advertId));
			}

			final String begin = request.getParameter("begin");
			final int beginInt;
			if (StringUtils.isBlank(begin)) {
				beginInt = 0;
			} else {
				beginInt = Integer.valueOf(begin.trim()).intValue();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("publisherId", user.getId());
			map.put("order", "create_time desc");
			map.put("advertId", advert.getId());
			map.put("page", new Page(beginInt, 10));
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
			modelMap.put("result", "1");

		} catch (AdvertException ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			modelMap.put("errorMsg", ex.getMessage());
		} catch (Exception ex) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			modelMap.put("errorMsg", ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

}

// 计算代理商收益线程(使用线程，为了防止影响app发布广告接口响应速度)
class AgentIncomeThread extends Thread {

	private AgentIncomeService agentIncomeService; // 代理商收益service
	private Advert advert; // 广告实体

	public AgentIncomeThread(AgentIncomeService agentIncomeService, Advert advert) {
		super();
		this.agentIncomeService = agentIncomeService;
		this.advert = advert;
	}

	@Override
	public void run() {
		agentIncomeService.processComputeAgentIncome(advert);
	}
}
