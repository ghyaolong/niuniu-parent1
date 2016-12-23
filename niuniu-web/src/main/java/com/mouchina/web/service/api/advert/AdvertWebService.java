package com.mouchina.web.service.api.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.entity.ReturnEntity;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.HomePageAwardVo;
import com.mouchina.moumou_server.entity.vo.RelayLuckyBagVo;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.web.service.api.vo.RedEnvelopeVo;

/**
 * 广告接口
 * @author Administrator
 *
 */
public interface AdvertWebService {

	/**
	 * 获取广告
	 * 
	 * @param advert
	 * @return
	 */
	Advert selectAdvert(Long advertId);

	/**
	 * 添加广告
	 * 
	 * @param advert
	 * @return
	 */
	AdvertResult<Advert> addAdvert(Advert advert, User user);

	/**
	 * 添加广告【针对3.0】
	 * 
	 * @param advert
	 * @param user
	 * @return
	 */
	AdvertResult<Advert> addAdvert3(Advert advert, User user);

	/**
	 * 添加广告
	 * 
	 * @param advert
	 * @return
	 */
	int updateAdvert(Advert advert);

	/**
	 * 添加广告
	 * 
	 * @param advert
	 * @return
	 */
	AdvertResult<AdvertStatistics> updateStopAdvert(Advert advert);

	/**
	 * 获取广告列表
	 * 
	 * @param advert
	 * @return
	 */
	ListRange<Advert> selectListRangeAdvert(Map<String, Object> map);

	/**
	 * 广告拉取
	 * 
	 * @param map
	 * @return
	 */
	Advert updateAdvertFetch(User user);

	/**
	 * 广告拉取并冻结
	 * 
	 * @param user
	 * @return
	 */
	Advert updateAdvertFetchFreeze(User user);

	/**
	 * 根据广告类型拉取&冻结广告 3.0
	 * @param user
	 * @return
	 */
	Advert updateAdvertFetchFreeze3(User user);
	
	/**
	 * 广告奖励
	 * 
	 * @param map
	 * @return
	 */
	RedEnvelope updateAdvertAwart(Advert advert, User user);
	
	/**
	 * 广告奖励3.0
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<RedEnvelope> updateAdvertAwart3(Advert advert, User user);

	/**
	 * 获取红包
	 * 
	 * @param map
	 * @return
	 */
	ListRange<RedEnvelope> selectListRangetRedEnvelope(Map<String, Object> map);

	/**
	 * 获取广告统计
	 * 
	 * @param advert
	 * @return
	 */
	AdvertStatistics selectAdvertStatistics(Map<String, Object> map);

	/**
	 * 更新广告明细
	 * 
	 * @param advertStatistics
	 * @return
	 */
	int updateAdvertStatistics(AdvertStatistics advertStatistics);

	ReturnEntity<RedEnvelopeTop> selectListPageRedEnvelopeTop(UserSearchSO search);

	/**
	 * 获取排行榜信息好友排行信息
	 * 
	 * @param map
	 * @return
	 */
	ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTopNew(Map<String, Object> map);

	/**
	 * 获取排行榜信息个人排行信息
	 * 
	 * @param map
	 * @return
	 */
	RedEnvelopeTop selectRedEnvelopeTopInfoMe(Long userId);

	/**
	 * 获取排行榜信息全服排行信息【3.0版本使用】
	 * 
	 * @return
	 */
	public ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTop3(Long userId);

	/**
	 * 获取广告数量
	 * 
	 * @param map
	 * @return
	 */
	int selectAdvertCount(Map<String, Object> map);

	Map<String, Object> getAdvertSumMap();

	/**
	 * 根据传入的广告对象添加广告人气
	 * 
	 * @param advert
	 * @return 0 操作失败 1操作成功
	 */
	public int addAdvertViewTimes(Advert advert);

	/**
	 * 查看2分钟(当前时间向前2分钟)之内是否有新发布的广告
	 * 
	 * @return
	 */
	public boolean checkNewAdvertInTwoMinutes();
	
	/**
	 * 获取首页广告列表
	 * 
	 * @param advert
	 * @return
	 */
	ListRange<Advert> selectListRangeHomeAdvert(Map<String, Object> map);
	
	/**
	 * 拆接力福袋
	 * @param user
	 * @param advert
	 * @param openAmount
	 * @return
	 */
	public RelayLuckyBagVo updateRelayLuckyBag(User user, Advert advert, int openAmount);

	/**
	 * 获取用户首页广告(按时间排序，取最近一条)
	 * @param map
	 * @return
	 */
	public Advert selectAdvertByMap(Map<String, Object> map);
	
	/**
	 * 获取人气值
	 * @param map
	 * @return
	 */
	int selectViewTimesCount(Map<String, Object> map);  
	
	/**
	 * 领取首页广告福袋
	 * @param user
	 * @param advert
	 * @param openAmount
	 * @return
	 */
	public HomePageAwardVo updateHomePageAward(User user, Advert advert);
	
	/**
	 * 获取广告列表
	 * @param map
	 * @return
	 */
	List<Advert> selectListAdvert(Map<String, Object> map);
	
	/**
	 * 获取运营活动列表
	 * @param map
	 * @return
	 */
	List<Advert> selectOperateActivity(Map<String, Object> map);
	/**
	 * 计算给定的经纬度和广告发布的经纬度的距离
	 * @param advert
	 * @param longitude
	 * @param latitude
	 * @return -1表示传入的经纬度有问题 
	 */
	Double calAdvertDistanceBylongiandlati(Advert advert,Double longitude,Double latitude);
	/**
	 * 查询最近一条XX活动
	 * @return
	 */
	Advert selectActivity(Map<String,Object> map);
}
