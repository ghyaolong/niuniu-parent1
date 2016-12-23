package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.advert.AdvertMapper;
import com.mouchina.moumou_server.dao.advert.AdvertStatisticsMapper;
import com.mouchina.moumou_server.dao.member.BusinessShopMapper;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.system.SystemMessageMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.BusinessShopHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server_interface.member.BusinessShopService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;
import com.mouchina.moumou_server_interface.view.ResultHelper;

public class BusinessShopServiceSupport implements BusinessShopService {

	@Resource
	BusinessShopMapper businessShopMapper;
	
	@Resource
	AdvertMapper advertMapper;
	
	@Resource
	AdvertStatisticsMapper advertStatisticsMapper;
	
	@Resource
	UserVerifyService userVerifyService;
	
	@Resource
	UserAssetsMapper userAssetsMapper;
	
	@Resource
	SystemMessageMapper systemMessageMapper;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Result<BusinessShop> queryBusinessShopByMap(Map map) {
		Result<BusinessShop> result = new Result<BusinessShop>();
		result.setState(ResultHelper.STATE_1.getMarker());
		BusinessShop shopTemp = businessShopMapper.selectModelTemp(map);
		if(shopTemp != null){
			if(shopTemp.getState() == BusinessShopHelper.AUDIT_STATE_0.getMarker()){
				result.setContent(shopTemp);
			}else{
				BusinessShop shop = businessShopMapper.selectModel(map);
				if(shop != null){
					result.setContent(shop);
				}else{
					result.setState(ResultHelper.STATE_0.getMarker());
				}
			}
		}else{
			BusinessShop shop = businessShopMapper.selectModel(map);
			if(shop != null){
				result.setContent(shop);
			}else{
				result.setState(ResultHelper.STATE_0.getMarker());
			}
		}
		return result;
	}
	
	@Override
	public Result<BusinessShop> saveOrUpdateBusinessShop(BusinessShop businessShop) {
		Result<BusinessShop> result = new Result<BusinessShop>();
		Date date = new Date();
		int id = 0;
		result.setState(ResultHelper.STATE_1.getMarker());
		BusinessShop shop = businessShopMapper.selectByBusinessIdTemp(businessShop.getBusinessId());
		if(shop != null){
			businessShop.setModifyTime(date);
			businessShop.setState(BusinessShopHelper.AUDIT_STATE_0.getMarker());
			businessShop.setRemark("");
			id = businessShopMapper.updateByBusinessIdTemp(businessShop);
		}else{
			businessShop.setCreateTime(date);
			id = businessShopMapper.insertSelectiveTemp(businessShop);
		}
		if(id > 0){
			result.setContent(businessShopMapper.selectByPrimaryKeyTemp((long)id));
		}else{
			result.setState(ResultHelper.STATE_0.getMarker());
		}
		return result;
	}
	
	/**
	 * 发布首页广告:插入广告表advert,更新用户信息（userAssets）中的余额,插入广告明细advertStatistics(advertId、createTime)
	 */
	public AdvertResult<Advert> addAdvertBid(Advert advert, User user) {
		
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();
		
		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("tableNum", user.getTableNum());
		UserAssets userAssets = userAssetsMapper.selectModel(map);

		if (advert.getRedEnvelopeAmount() <= (userAssets
				.getCashBalance())) {
			int result = 1;
			//判断当前用户首页广告余额是否已被领完，如果没有领完则更新首页广告，否则插入
			map.clear();
			map.put("userId", user.getId());
			map.put("advertType", BusinessShopHelper.ADVERT_TYPE_5.getMarker());
			map.put("state", BusinessShopHelper.AUDIT_STATE_1.getMarker());
			map.put("order", "create_time desc");
			List<Advert> homePageAdvert = advertMapper.selectList(map);
			boolean flag = homePageAdvert != null && homePageAdvert.size() > 0;
			if(flag){
				Advert upAdvert = homePageAdvert.get(0);
				upAdvert.setModifyTime(new Date());
				upAdvert.setRedEnvelopeAmount(upAdvert.getRedEnvelopeAmount() + advert.getRedEnvelopeAmount());
				result = advertMapper.updateByPrimaryKeySelective(upAdvert);
			}else{
				advert.setCreateTime(new Date());
				result = advertMapper.insertSelective(advert);
			}
			advertResult.setContent(advert);
			advertResult.setState(result);
			if (result == BusinessShopHelper.STATE_1.getMarker()) {
				AdvertStatistics advertStatistics = new AdvertStatistics();
				advertStatistics.setAdvertId(advert.getId());
				advertStatistics.setCreateTime(new Date());
				if(!flag){
					addAdvertStatistics(advertStatistics);
				}
				userAssets.setCashBalance(userAssets.getCashBalance()
						- advert.getRedEnvelopeAmount());
				userAssets.setTableNum(user.getTableNum());
				userAssetsMapper.updateByPrimaryKeySelective(userAssets);
				advertResult.setState(BusinessShopHelper.STATE_1.getMarker());
			}
		}else{
			advertResult.setState(BusinessShopHelper.STATE_2.getMarker());
		}
		return advertResult;
	}

	@Override
	public int predictionAdvertBidTop(Advert advert) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", advert.getUserId());
		map.put("amount", advert.getRedEnvelopeAmount());
		map.put("advertType", BusinessShopHelper.ADVERT_TYPE_5.getMarker());
		map.put("state", BusinessShopHelper.AUDIT_STATE_1.getMarker());
		return advertMapper.selectAdvertBidTop(map);
	}
	
	private AdvertResult<AdvertStatistics> addAdvertStatistics(AdvertStatistics advertStatistics) {
		AdvertResult<AdvertStatistics> advertStatisticsResult = new AdvertResult<AdvertStatistics>();
		int result = advertStatisticsMapper.insertSelective(advertStatistics);
		advertStatisticsResult.setContent(advertStatistics);
		advertStatisticsResult.setState(result);
		return advertStatisticsResult;
	}

	@Override
	public ListRange<BusinessShop> queryPageListBusinessShopByMap(Map<String,Object> map) {
		ListRange<BusinessShop> listRange = new ListRange<BusinessShop>();
		int count = businessShopMapper.selectCountTemp(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(businessShopMapper.selectPageListTemp(map));
		return listRange;
	}

	@Override
	public Result<BusinessShop> queryShopByMap(Map<String, Object> map) {
		Result<BusinessShop> result = new Result<BusinessShop>();
		result.setState(ResultHelper.STATE_1.getMarker());
		BusinessShop shop = businessShopMapper.selectModel(map);
		if(shop != null){
			result.setContent(shop);
		}else{
			result.setState(ResultHelper.STATE_0.getMarker());
		}
		return result;
	}

	@Override
	public int updatePassBusinessShop(BusinessShop businessShop) {
		int result = 0;
		Date date = new Date();
		Map<String, Object> map = new HashMap<>();
		businessShop.setModifyTime(date);
		result = businessShopMapper.updateByPrimaryKeySelectiveTemp(businessShop);
		BusinessShop saveOrUpdateShop = businessShopMapper.selectByPrimaryKeyTemp(businessShop.getId());
		if(result > 0){
			saveOrUpdateShop.setState(businessShop.getState());
			saveOrUpdateShop.setRemark(businessShop.getRemark());
			saveOrUpdateShop.setModifyTime(date);
			map.put("businessId", saveOrUpdateShop.getBusinessId());
			map.put("userId", saveOrUpdateShop.getUserId());
			BusinessShop shop = businessShopMapper.selectModel(map);
			if(shop != null){
				result = businessShopMapper.updateByBusinessId(saveOrUpdateShop);
			}else{
				result = businessShopMapper.insertSelective(saveOrUpdateShop);
			}
			//写系统消息
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setContent(businessShop.getRemark());
			systemMessage.setUserId(saveOrUpdateShop.getUserId());
			systemMessage.setCreateTime(new Date());
			systemMessageMapper.insertSelective(systemMessage);
		}
		return result;
	}
	
	@Override
	public int updateNoPassBusinessShop(BusinessShop businessShop) {
		int result = 0;
		businessShop.setModifyTime(new Date());
		result = businessShopMapper.updateByPrimaryKeySelectiveTemp(businessShop);
		if(result > 0){
			BusinessShop saveOrUpdateShop = businessShopMapper.selectByPrimaryKeyTemp(businessShop.getId());
			//写系统消息
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setContent(businessShop.getRemark());
			systemMessage.setUserId(saveOrUpdateShop.getUserId());
			systemMessage.setCreateTime(new Date());
			systemMessageMapper.insertSelective(systemMessage);
		}
		return result;
	}

}
