package com.mouchina.web.controller.particular;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.marketing.AdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.CouponAdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.HomePageAdvertDo;
import com.mouchina.moumou_server.entity.vo.marketing.MarketDetailVo;
import com.mouchina.moumou_server.entity.vo.marketing.RelayLuckBagAdvertDo;
import com.mouchina.moumou_server_interface.particular.BusinessMarketingService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.BaseResultVo;

/**
 * 营销数据
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/business/marketing")
public class BusinessMarketingController {
	
	@Autowired
	private BusinessMarketingService businessMarketingService;
	@Resource
	private UserWebService userWebService;

	/**
	 * 首页福袋
	 * @param loginKey
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/homePageAdvert")
	public @ResponseBody BaseResultVo<List<HomePageAdvertDo>> homePageAdvert(String loginKey,Integer begin) {
		BaseResultVo<List<HomePageAdvertDo>> baseResultVo = new BaseResultVo<List<HomePageAdvertDo>>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		Map<String,Object> homePageAdvertMap = this.setPage(begin == null ? 0 : begin);
		homePageAdvertMap.put("userId",userId);
		List<HomePageAdvertDo> homePageAdvertDos = businessMarketingService.homePageAdvert(homePageAdvertMap);
		if(homePageAdvertDos == null || homePageAdvertDos.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("营销数据不存在!");
			baseResultVo.setData(homePageAdvertDos);
			return baseResultVo;
		}
		
		baseResultVo.setResult("1");
		baseResultVo.setOffset(begin);
		baseResultVo.setErrorMsg("请求成功!");
		baseResultVo.setData(homePageAdvertDos);
		return baseResultVo;
	}
	
	/**
	 * 接力福袋营销
	 * @param loginKey
	 * @param offset
	 * @return
	 */
	@RequestMapping(value="/relayLuckBagAdvert")
	public @ResponseBody BaseResultVo<List<RelayLuckBagAdvertDo>> relayLuckBagAdvert(String loginKey,Integer begin) {
		BaseResultVo<List<RelayLuckBagAdvertDo>> baseResultVo = new BaseResultVo<List<RelayLuckBagAdvertDo>>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		Map<String,Object> relayLuckBagAdvertMap = this.setPage(begin);
		relayLuckBagAdvertMap.put("userId",userId);
		List<RelayLuckBagAdvertDo> relayLuckBagAdvertDos = businessMarketingService.relayLuckBagAdvert(relayLuckBagAdvertMap);
		
		if(relayLuckBagAdvertDos == null || relayLuckBagAdvertDos.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("数据为空！");
			return baseResultVo;
		}
		
		baseResultVo.setResult("1");
		baseResultVo.setErrorMsg("请求成功!");
		baseResultVo.setOffset(begin);
		baseResultVo.setData(relayLuckBagAdvertDos);
		return baseResultVo;
	}
	
	/**
	 * 营销数据普通福袋
	 * @param loginKey
	 * @param offset
	 * @return
	 */
	@RequestMapping(value="/selectGeneralAdvert")
	public @ResponseBody BaseResultVo<List<AdvertVo>> selectGeneralAdvert(String loginKey,Integer begin) {
		BaseResultVo<List<AdvertVo>> baseResultVo = new BaseResultVo<List<AdvertVo>>();

		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		Map<String,Object> generalAdvertMap = this.setPage(begin);
		generalAdvertMap.put("userId",userId);
		List<AdvertVo> advertVos = businessMarketingService.selectGeneralAdvert(generalAdvertMap);
		
		if(advertVos == null || advertVos.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("数据为空！");
			return baseResultVo;
		}
		
		baseResultVo.setResult("1");
		baseResultVo.setOffset(begin);
		baseResultVo.setData(advertVos);
		return baseResultVo;
	}
	
	/**
	 * 优惠券福袋
	 * @param loginKey
	 * @param offset
	 * @return
	 */
	@RequestMapping(value="/couponAdvert")
	public @ResponseBody BaseResultVo<List<CouponAdvertVo>> couponAdvert(String loginKey,Integer begin) {
		BaseResultVo<List<CouponAdvertVo>> baseResultVo = new BaseResultVo<List<CouponAdvertVo>>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		Map<String,Object> couponAdvertMap = this.setPage(begin);
		couponAdvertMap.put("userId",userId);
		List<CouponAdvertVo> couponAdvertVos = businessMarketingService.couponAdvert(couponAdvertMap);
		
		if(couponAdvertVos == null || couponAdvertVos.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("数据为空！");
			return baseResultVo;
		}
		
		baseResultVo.setResult("1");
		baseResultVo.setOffset(begin);
		baseResultVo.setData(couponAdvertVos);
		return baseResultVo;
	}
	
	/**
	 * 设置分页
	 * @param offset
	 * @return
	 */
	private Map<String,Object> setPage(Integer offset) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("offset", offset);
		map.put("pagesize", 10);
		return map;
	}
	
	/**
	 * 查询营销数据的明细
	 * @param loginKey
	 * @param queryDate
	 * @param offset
	 * @param awardId
	 * @return
	 */
	@RequestMapping(value="/selectMarketDetail")
	public @ResponseBody BaseResultVo<List<MarketDetailVo>> selectMarketDetail(String loginKey,String queryDate,Integer begin,Long advertId) {
		BaseResultVo<List<MarketDetailVo>> baseResultVo = new BaseResultVo<List<MarketDetailVo>>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		Map<String,Object> selectMarketDetailMap = this.setPage(begin);
		selectMarketDetailMap.put("userId", userId);
		selectMarketDetailMap.put("advertId", advertId);
		selectMarketDetailMap.put("createTime", queryDate);
		List<MarketDetailVo> marketDetailVos = businessMarketingService.selectMarketDetail(selectMarketDetailMap);
		
		if(marketDetailVos == null || marketDetailVos.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("数据为空！");
			return baseResultVo;
		}
		
		baseResultVo.setResult("1");
		baseResultVo.setData(marketDetailVos);
		return baseResultVo;
	}

}
