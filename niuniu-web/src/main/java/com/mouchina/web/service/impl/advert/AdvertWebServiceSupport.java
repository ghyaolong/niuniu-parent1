package com.mouchina.web.service.impl.advert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.entity.ReturnEntity;
import com.mouchina.base.utils.map.GeoUtils;
import com.mouchina.base.utils.map.GpsCorrect;
import com.mouchina.base.utils.map.Point;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.vo.HomePageAwardVo;
import com.mouchina.moumou_server.entity.vo.RelayLuckyBagVo;
import com.mouchina.moumou_server.exceptions.AdvertException;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.web.service.api.advert.AdvertWebService;

@Service
public class AdvertWebServiceSupport implements AdvertWebService {

	private Logger logger = Logger.getLogger(getClass());

	@Resource
	private AdvertService advertService;
	@Resource
	private SystemService systemService;

	public AdvertResult<Advert> addAdvert(Advert advert, User user) {
		AdvertResult<Advert> advertResult = advertService.addAdvert(advert, user);
		return advertResult;
	}

	public AdvertResult<Advert> addAdvert3(Advert advert, User user) {
		logger.info("传入的参数有:"+advert+"----"+user+"");
		AdvertResult<Advert> advertResult = advertService.addAdvert3(advert, user);
		return advertResult;
	}

	@Override
	public int updateAdvert(Advert advert) {
		return advertService.updatePublishAdvert(advert).getState();
	}

	@Override
	public AdvertResult<AdvertStatistics> updateStopAdvert(Advert advert) {
		return advertService.updateStopAdvert(advert);
	}

	@Override
	public ListRange<Advert> selectListRangeAdvert(Map<String, Object> map) {
		return advertService.selectListAdvertPage(map).getContent();
	}

	@Override
	public Advert updateAdvertFetch(User user) {
		return advertService.updateAdvertFetch(user).getContent();
	}

	@Override
	public Advert updateAdvertFetchFreeze(User user) {
		return advertService.updateAdvertFetchFreeze(user).getContent();
	}

	@Override
	public Advert updateAdvertFetchFreeze3(User user) {
		return advertService.updateAdvertFetchFreeze3(user).getContent();
	}
	@Override
	public RedEnvelope updateAdvertAwart(Advert advert, User user) {
		return advertService.updateAdvertAwart(advert, user).getContent();
	}
	@Override
	public AdvertResult<RedEnvelope> updateAdvertAwart3(Advert advert, User user) {
		return advertService.updateAdvertAwart3(advert, user);
	}

	@Override
	public ListRange<RedEnvelope> selectListRangetRedEnvelope(Map<String, Object> map) {
		return advertService.selectListRedEnvelopePage(map).getContent();
	}

	@Override
	public AdvertStatistics selectAdvertStatistics(Map<String, Object> map) {
		return advertService.selectAdvertStatistics(map).getContent();
	}

	@Override
	public Advert selectAdvert(Long advertId) {
		return advertService.selectAdvert(advertId).getContent();
	}

	@Override
	public ReturnEntity<RedEnvelopeTop> selectListPageRedEnvelopeTop(UserSearchSO search) {
		return advertService.selectListPageRedEnvelopeTop(search);
	}

	@Override
	public ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTopNew(Map<String, Object> map) {
		return advertService.selectListPageRedEnvelopeTopNew(map);
	}

	@Override
	public RedEnvelopeTop selectRedEnvelopeTopInfoMe(Long userId) {
		return advertService.selectRedEnvelopeTopInfoMe(userId);
	}

	@Override
	public int updateAdvertStatistics(AdvertStatistics advertStatistics) {
		return advertService.updateAdvertStatistics(advertStatistics).getState();
	}

	@Override
	public int selectAdvertCount(Map<String, Object> map) {
		return advertService.selectAdvertCount(map);
	}

	@Override
	public Map<String, Object> getAdvertSumMap() {
		return advertService.getAdvertSum();
	}

	/**
	 * 根据传入的广告对象添加广告人气
	 * 
	 * @param advert
	 * @return 0 操作失败 1操作成功
	 * 
	 */
	public int addAdvertViewTimes(Advert advert) {
		if (advert.getId() == null) {
			throw new AdvertException(false, "广告数据不存在");
		} else {
			Map<String, Object> advertStatisSqlMap = new HashMap<String, Object>();
			advertStatisSqlMap.put("advertId", advert.getId());
			AdvertStatistics advertStatistics = selectAdvertStatistics(advertStatisSqlMap);

			AdvertStatistics toUpdateAdvertStatistics = new AdvertStatistics();
			if (advertStatistics != null) {

				if (advertStatistics.getViewTimes() != null) {
					toUpdateAdvertStatistics.setId(advertStatistics.getId());
					toUpdateAdvertStatistics.setViewTimes(advertStatistics.getViewTimes() + 1);
				} else {
					advertStatistics.setViewTimes((long) 0);
				}

				// 更新SystemData中的全网广告浏览量 加1 start
				Map<String,Object> systemDataMap = new HashMap<String, Object>();
				systemDataMap.put("key_name", "advertScanCount");
				SystemDataVo sysDataVo = systemService.selectListSystemData(systemDataMap).get(0);
				
				SystemData sysData = new SystemData();
				sysData.setId(sysDataVo.getId());
				sysData.setSysValue(sysDataVo.getValue() + 1);
				systemService.updateSystemData(sysData);
				// 更新SystemData中的全网广告浏览量 加1 end
			}
			// log.info("当前人气值advertStatistics.viewTimes<未存入数据库之前>的值是 : " +
			// toUpdateAdvertStatistics.getViewTimes());
			int addFlag = updateAdvertStatistics(toUpdateAdvertStatistics);

			return addFlag;
		}
	}

	/**
	 * 查看2分钟(当前时间向前2分钟)之内是否有新发布的广告
	 * 
	 * @return
	 */
	public boolean checkNewAdvertInTwoMinutes() {
		return advertService.checkIfAdvertsSendedInTwoMinutes();
	}

	@Override
	public ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTop3(Long userId) {
		return advertService.selectProListPageRedEnvelopeTop3(userId);
	}

	@Override
	public ListRange<Advert> selectListRangeHomeAdvert(Map<String, Object> map) {
		return advertService.selectListAdvertHomePage(map).getContent();
	}

	@Override
	public RelayLuckyBagVo updateRelayLuckyBag(User user, Advert advert, int openAmount) {
		return advertService.updateRelayLuckyBag(user, advert, openAmount);
	}

	@Override
	public Advert selectAdvertByMap(Map<String, Object> map) {
		return advertService.selectAdvertByMap(map).getContent();
	}

	@Override
	public int selectViewTimesCount(Map<String, Object> map) {
		return advertService.selectViewTimesCount(map);
	}

	@Override
	public HomePageAwardVo updateHomePageAward(User user, Advert advert) {
		return advertService.updateHomePageAward(user, advert) ;
	}

	@Override
	public List<Advert> selectListAdvert(Map<String, Object> map) {
		return advertService.selectListAdvert(map);
	}

	@Override
	public List<Advert> selectOperateActivity(Map<String, Object> map) {
		return advertService.selectOperateActivity(map);
	}

	@Override
	public Double calAdvertDistanceBylongiandlati(Advert advert, Double longitude, Double latitude) {
		if(longitude == null || latitude == null){
			return (double)-1;
		}
		Double lng = longitude; // 经度
		Double lat = latitude; // 纬度
		double[] latlng = new double[2];
		if (lng != null && lat != null && lng.doubleValue() > 0 && lat.doubleValue() > 0) {
			GpsCorrect.transform(lat, lng, latlng);
			lng = latlng[1];
			lat = latlng[0];
		}
		Point advertPoint = new Point(advert.getLongitude(), advert.getLatitude());// 发布广告坐标点
		GpsCorrect.transform(lat, lng, latlng);// 纠偏
		Point userPoint = new Point(lng, lat);// 用户坐标点
		double distance = GeoUtils.getDistance(advertPoint, userPoint);// 用户当前定位点距离广告发布点的距离
		return distance;
	}

	@Override
	public Advert selectActivity(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return advertService.selectActivity(map);
	}
	
}
