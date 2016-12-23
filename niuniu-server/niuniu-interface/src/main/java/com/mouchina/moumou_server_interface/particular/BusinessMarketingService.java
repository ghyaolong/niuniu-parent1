package com.mouchina.moumou_server_interface.particular;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.vo.marketing.AdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.CouponAdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.HomePageAdvertDo;
import com.mouchina.moumou_server.entity.vo.marketing.MarketDetailVo;
import com.mouchina.moumou_server.entity.vo.marketing.RelayLuckBagAdvertDo;

public interface BusinessMarketingService {

	/**
	 * 首页福袋营销
	 * 
	 * @param userId
	 * @return
	 */
	public List<HomePageAdvertDo> homePageAdvert(Map<String,Object> map);

	/**
	 * 接力福袋营销
	 * 
	 * @param userId
	 */
	public List<RelayLuckBagAdvertDo> relayLuckBagAdvert(Map<String,Object> map);

	/**
	 * 普通福袋营销
	 * 
	 * @param userId
	 */
	public List<AdvertVo> selectGeneralAdvert(Map<String,Object> map);

	/**
	 * 优惠券营销
	 * 
	 * @param userId
	 */
	public List<CouponAdvertVo> couponAdvert(Map<String,Object> map);
	/**
	 * 查询营销数据明细
	 * @param map
	 * @return
	 */
	public List<MarketDetailVo> selectMarketDetail(Map<String,Object> map);
	
}
