package com.mouchina.moumou_server_interface.member;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;

public interface BusinessShopService {
	
	/**
	 * 查询商户店铺信息(我的相关查询方法)
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result<BusinessShop> queryBusinessShopByMap(Map map);
	
	/**
	 * 查询商户店铺信息
	 * @param map
	 * @return
	 */
	Result<BusinessShop> queryShopByMap(Map<String, Object> map);
	
	/**
	 * 查询商户店铺信息(后台管理系统使用)
	 * @param map
	 * @return
	 */
	ListRange<BusinessShop> queryPageListBusinessShopByMap(Map<String, Object> map);

	/**
	 * 维护商户店铺信息
	 * @param businessShop
	 * @return
	 */
	Result<BusinessShop> saveOrUpdateBusinessShop(BusinessShop businessShop);
	
	/**
	 * 竞价(重新竞价)首页广告
	 * @param advert
	 * @return
	 */
	AdvertResult<Advert> addAdvertBid(Advert advert,User user);

	/**
	 * 预测竞价(重新竞价)排行
	 * @return
	 */
	int predictionAdvertBidTop(Advert advert);
	
	/**
	 * 审核通过商户店铺信息(后台管理系统使用)
	 * @param shopId
	 * @return
	 */
	int updatePassBusinessShop(BusinessShop businessShop);
	
	/**
	 * 审核未通过商户店铺信息(后台管理系统使用)
	 * @param shopId
	 * @return
	 */
	int updateNoPassBusinessShop(BusinessShop businessShop);
	
}
