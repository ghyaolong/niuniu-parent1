package com.mouchina.web.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.utils.AdvertContentUtil;
import com.mouchina.base.utils.JsonUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.BusinessShopHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserHelper;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server_interface.member.BusinessShopService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;

/**
 * 商户店铺controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/businessShop")
public class BusinessShopController extends BaseController {

	@Resource
	BusinessShopService businessShopService;
	
	@Resource
	UserWebService userWebService;
	
	@Resource
	SystemService systemService;
	
	/**查询商户店铺信息**/
	@RequestMapping(value = "/query", method = RequestMethod.GET )
	public String queryBusinessShop(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		Map<String, Object> map = new HashMap<>();
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			map.put("userId", user.getId());
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			return "";
		}
		Result<BusinessShop> result = businessShopService.queryBusinessShopByMap(map);
		if (result.getState() == Constants.ERROR_OK) {
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("businessShop", result.getContent());
		} else {
			modelMap.put("result", Constants.ERROR_NO_DATA);
			modelMap.put("errorCode", Constants.ERROR_CODE_400001);
		}
		return "";
	}
	
	
	/**维护商户店铺信息**/
	@RequestMapping(value = "/preserve", method = RequestMethod.GET )
	public String preserveBusinessShop(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, BusinessShop businessShop)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)
				|| StringUtil.isEmpty(businessShop.getShopSign())
				|| StringUtil.isEmpty(businessShop.getFirstPic())
				|| StringUtil.isEmpty(businessShop.getSecondPic())
				|| StringUtil.isEmpty(businessShop.getThirdPic())) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			businessShop.setUserId(user.getId());
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			return "";
		}
		/* 查询认证信息 */
		Business business = userWebService.getBusinessByUserId(user.getId()).getContent();
		if (null == business) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_300100);
			return "";
		}
		businessShop.setBusinessId(business.getId());
		Result<BusinessShop> result = businessShopService.saveOrUpdateBusinessShop(businessShop);
		if (result.getState() == Constants.ERROR_OK) {
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("businessShop", result.getContent());
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_400002);
		}
		return "";
	}
	
	/**
	 * 首页广告竞价(重新竞价)
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param advert
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = "/bid", method = RequestMethod.GET)
	public String advertBid(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Advert advert)
			throws IOException {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("redEnvelopeAmount"))) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.remove("advert");
			return "";
		}

		String loginKey = request.getParameter("loginKey");
		User user = userWebService.getUserByLoginKey(loginKey);
		
		if (user != null) {
			/* 查询认证信息 */
			Business business = userWebService.getBusinessByUserId(user.getId()).getContent();
			if (null == business) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_300100);
				modelMap.remove("advert");
				return "";
			}
			/* 是否是认证商户 */
			if(business.getCertificationType().intValue() != UserHelper.USER_IDENTITY_2.getMarker().intValue()){
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_300101);
				modelMap.remove("advert");
				return "";
			}
			/* 发布首页广告金额错误 */
			if (advert.getRedEnvelopeAmount().intValue() < BusinessShopHelper.ADVERT_MIN_AMOUNT.getMarker().intValue()) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_400003);
				modelMap.remove("advert");
				return "";
			}
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getId());
			Result<BusinessShop> result = businessShopService.queryShopByMap(map);
			if (result.getState() == Constants.ERROR_OK) {
				advert.setRelationId(result.getContent().getId());
			} else {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_400001);
				modelMap.remove("advert");
				return "";
			}
			advert.setAdvertType(BusinessShopHelper.ADVERT_TYPE_5.getMarker());
			advert.setUserId(user.getId());
			advert.setRedEnvelopeType(BusinessShopHelper.RED_ENVELOPE_TYPE_1.getMarker());
			advert.setRedEnvelopeCount(BusinessShopHelper.RED_ENVELOPE_COUNT_0.getMarker());
			advert.setAdvertName(BusinessShopHelper.ADVERT_TYPE_5.getDisplay());
			List<String> photo = new ArrayList<>();
			photo.add(result.getContent().getShopSign());
			String content = result.getContent().getAbout();
			AdvertContentUtil advertContentUtil = new AdvertContentUtil(photo, content);
			advert.setAdvertContent(JsonUtils.javaToString(advertContentUtil));
			AdvertResult<Advert> advertResult = businessShopService.addAdvertBid(advert, user);
			if (advertResult.getState() == BusinessShopHelper.STATE_1.getMarker().intValue()) {
				//更新SystemData中的全网广告总发布钱数-----start
				SystemDataVo sysDataVo = systemService.selectListSystemData(new HashMap<String,Object>(){{put("key_name", "publishAdvertMoney");}}).get(0);
				SystemData sysData = new SystemData();
				sysData.setId(sysDataVo.getId());
				sysData.setSysValue(sysDataVo.getValue() + advert.getRedEnvelopeAmount()/100);
				systemService.updateSystemData(sysData);
				//更新SystemData中的全网广告总发布钱数-----end
				
				modelMap.put("result", Constants.ERROR_OK);
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
	
	/**
	 * 首页广告竞价(重新竞价)排名预测
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param advert
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/bidTop", method = RequestMethod.GET)
	public String queryAdvertBidTop(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Advert advert)
			throws IOException {
		if (StringUtil.isEmpty(request.getParameter("loginKey"))
				|| StringUtil.isEmpty(request.getParameter("redEnvelopeAmount"))) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		String loginKey = request.getParameter("loginKey");
		User user = userWebService.getUserByLoginKey(loginKey);
		
		if (user != null) {
			if (advert.getRedEnvelopeAmount() < BusinessShopHelper.ADVERT_MIN_AMOUNT.getMarker()) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_400003);
				return "";
			}
			advert.setUserId(user.getId());
			int topNum = businessShopService.predictionAdvertBidTop(advert);
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("topNum", topNum);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("advert");
		return "";
	}
	
}
