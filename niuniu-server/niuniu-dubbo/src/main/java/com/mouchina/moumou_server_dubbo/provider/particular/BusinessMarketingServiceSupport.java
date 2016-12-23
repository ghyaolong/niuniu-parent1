package com.mouchina.moumou_server_dubbo.provider.particular;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.dao.advert.AdvertMapper;
import com.mouchina.moumou_server.dao.advert.AdvertStatisticsMapper;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.social.UsersRelationMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.marketing.AdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.CouponAdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.HomePageAdvertDo;
import com.mouchina.moumou_server.entity.vo.marketing.MarketDetailVo;
import com.mouchina.moumou_server.entity.vo.marketing.RelayLuckBagAdvertDo;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.particular.BusinessMarketingService;

@Service(value = "businessMarketingServiceSupport")
public class BusinessMarketingServiceSupport implements BusinessMarketingService {

	@Autowired
	private AdvertMapper advertMapper;
	@Autowired
	private RedEnvelopeMapper redEnvelopeMapper;
	@Autowired
	private UserAgentService userAgentService;
	@Autowired
	private UserPartMapper userPartMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UsersRelationMapper usersRelationMapper;
	@Autowired
	private AdvertStatisticsMapper advertStatisticsMapper;

	@Override
	public List<HomePageAdvertDo> homePageAdvert(Map<String, Object> map) {
		Long userId = (Long) map.get("userId");
		List<HomePageAdvertDo> dates = redEnvelopeMapper.selectRedEnvAllDate(userId);
		for(HomePageAdvertDo date:dates) {
			Long advertId = date.getAdvertId();
			Date createDate = date.getDate();
			
			Map<String,Object> usersRelationMap = new HashMap<String,Object>();
			usersRelationMap.put("userId", userId);
			usersRelationMap.put("createDate", createDate);
			Integer fans = usersRelationMapper.selectFansCount(usersRelationMap);
			date.setFans(fans);
			
			Map<String,Object> redEnvelopeMap = new HashMap<String,Object>();
			redEnvelopeMap.put("userId", userId);
			redEnvelopeMap.put("createDate", createDate);
			Integer homeLuckyBagBalance = redEnvelopeMapper.selectHomeLuckyBagBalance(redEnvelopeMap);
			date.setOverPlusMoney(date.getRedEnvelopeAmount() - homeLuckyBagBalance.doubleValue());
			
			Map<String,Object> advertStatisticsMap = new HashMap<String,Object>();
			advertStatisticsMap.put("advertId", advertId);
			AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(advertStatisticsMap);
			date.setViewCount(advertStatistics.getViewTimes().intValue());
		}
		
		return dates;
	}

	@Override
	public List<RelayLuckBagAdvertDo> relayLuckBagAdvert(Map<String, Object> map) {
		List<RelayLuckBagAdvertDo> relayLuckBagAdvertDos = advertMapper.relayLuckBagAdvert(map);
		return relayLuckBagAdvertDos;
	}

	@Override
	public List<AdvertVo> selectGeneralAdvert(Map<String, Object> map) {
		List<AdvertVo> advertVos = advertMapper.selectGeneralAdvert(map);
		return advertVos;
	}

	@Override
	public List<CouponAdvertVo> couponAdvert(Map<String, Object> map) {
		List<CouponAdvertVo> couponAdvertVos = advertMapper.couponAdvert(map);
		return couponAdvertVos;
	}

	@Override
	public List<MarketDetailVo> selectMarketDetail(Map<String, Object> map) {
		Long advertId = (Long) map.get("advertId");
		
		Map<String,Object> advertMap = new HashMap<>();
		advertMap.put("id", advertId);
		Advert advert = advertMapper.selectModel(advertMap);
		Boolean businessCoupon = false;
		if(advert.getAdvertType() == 0) {
			//普通福袋
			map.put("type", 1);
		} else if(advert.getAdvertType() == 1 || advert.getAdvertType() == 3) {
			//优惠券
			map.put("type", 2);
			businessCoupon = true;
		} else if(advert.getAdvertType() == 4) {
			//接力福袋
			map.put("type", 3);
		} else if(advert.getAdvertType() == 5) {
			//首页福袋
			map.put("type", 5);
		}
		
		List<MarketDetailVo> marketDetailVos = null;
		if(businessCoupon) {
			// 优惠券详情
			marketDetailVos = redEnvelopeMapper.selectBusinessCouponMarketDetail(map);
		} else {
			// 其它详情
			marketDetailVos = redEnvelopeMapper.selectMarketDetail(map);
		}
		
		for(MarketDetailVo marketDetailVo:marketDetailVos) {
			Long userId = marketDetailVo.getUserId();
			User user = this.getUser(userId);
			marketDetailVo.setPublishUserCertificate(userAgentService.selectUserCertificateLevel(userId));
			marketDetailVo.setUserNickName(user.getNickName());
			marketDetailVo.setUserAvatar(user.getAvatar());
		}
		return marketDetailVos;
	}
	
	/**
	 * 获取用户id
	 * @param userId
	 * @return
	 */
	private User getUser(Long userId) {
		Map<String,String> userPartMap = new HashMap<String,String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
		
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put("id", userId);
		userMap.put("tableNum", tableIndex);
		return userMapper.selectModel(userMap);
	}

}
