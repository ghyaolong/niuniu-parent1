package com.mouchina.moumou_server_interface.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.entity.ReturnEntity;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.AdvertType;
import com.mouchina.moumou_server.entity.advert.BannerConfig;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.BannerConfigVo;
import com.mouchina.moumou_server.entity.vo.HomePageAwardVo;
import com.mouchina.moumou_server.entity.vo.PublicWelFareAdvertVo;
import com.mouchina.moumou_server.entity.vo.RelayLuckyBagVo;
import com.mouchina.moumou_server.exceptions.AdvertException;
import com.mouchina.moumou_server.exceptions.RedEnvelopeException;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;

/**
 * 广告接口
 * 
 * @author Administrator
 *
 */
public interface AdvertService {

	AdvertResult<Advert> selectAdvert(Long advertId);

	AdvertResult<Advert> selectAdvert(Map<String, Object> map);

	/***
	 * 广告发布 state 1 成功 0商家信息审核不通过 2余额不足
	 * (插入广告表advert,更新用户信息（userAssets）中的余额,插入广告明细advertStatistics(advertId、createTime))
	 * 
	 * @param advert
	 * @return
	 */
	public AdvertResult<Advert> addAdvert(Advert advert, User user);

	/**
	 * 发布广告【针对3.0】
	 * 插入广告表advert,更新用户信息（userAssets）中的余额,插入广告明细advertStatistics(advertId、createTime)
	 * 
	 * @param advert
	 *            广告对象
	 * @param user
	 *            用户对象
	 * @return
	 */
	public AdvertResult<Advert> addAdvert3(Advert advert, User user);

	AdvertResult<Integer> updateAdvert(Advert advert);

	AdvertResult<ListRange<Advert>> selectListAdvertPage(Map<String, Object> map);

	AdvertResult<Integer> deleteAdvert(Long advertId);

	AdvertResult<Integer> updatePublishAdvert(Advert advert);

	AdvertResult<AdvertType> selectAdvertType(Integer advertTypeId);

	AdvertResult<AdvertType> selectAdvertType(Map<String, Object> map);

	AdvertResult<AdvertType> addAdvertType(AdvertType advertType);

	AdvertResult<ListRange<AdvertType>> selectListAdvertTypePage(Map<String, Object> map);
	
	AdvertResult<Integer> deleteAdvertType(Integer advertTypeId);

	AdvertResult<Integer> updateAdvertType(AdvertType advertType);

	AdvertResult<AdvertStatistics> selectAdvertStatistics(Long advertStatisticsId);

	AdvertResult<AdvertStatistics> selectAdvertStatisticsByAdvertId(Long advertId);

	List<Advert> selectListAdvert(Map<String, Object> map);

	AdvertResult<AdvertStatistics> selectAdvertStatistics(Map<String, Object> map);

	/**
	 * 给广告统计增加记录
	 * 
	 * @param advertStatistics
	 *            广告列表的对象
	 * @return
	 */
	public AdvertResult<AdvertStatistics> addAdvertStatistics(AdvertStatistics advertStatistics);

	AdvertResult<ListRange<AdvertStatistics>> selectListAdvertStatisticsPage(Map<String, Object> map);

	AdvertResult<Integer> deleteAdvertStatistics(Long advertStatisticsId);

	AdvertResult<Integer> updateAdvertStatistics(AdvertStatistics advertStatistics);

	AdvertResult<RedEnvelope> selectRedEnvelope(Long redEnvelopeId);

	AdvertResult<RedEnvelope> selectRedEnvelope(Map<String, Object> map);

	AdvertResult<RedEnvelope> addRedEnvelope(RedEnvelope redEnvelope);

	/**
	 * 邀请好友排行榜
	 * 
	 * @param map
	 * @return
	 */
	ReturnEntity<RedEnvelopeTop> selectListPageRedEnvelopeTop(UserSearchSO search);

	ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTopNew(Map<String, Object> map);

	/**
	 * 查询全服排行榜前30【适用3.0】
	 * 
	 * @return
	 */
	public ListRange<RedEnvelopeTop> selectProListPageRedEnvelopeTop3(Long userId);

	RedEnvelopeTop selectRedEnvelopeTopInfoMe(Long userId);

	/**
	 * 收入明细【2.0版本】
	 * @param map
	 * @return
	 */
	public AdvertResult<ListRange<RedEnvelope>> selectListRedEnvelopePage(Map<String, Object> map);
	/**
	 * 收入明细【3.0版本】
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectListRedEnvelopePage3(Map<String, Object> map);
	
	AdvertResult<Integer> deleteRedEnvelope(Long redEnvelopeId);

	AdvertResult<Integer> updateRedEnvelope(RedEnvelope redEnvelope);

	int selectAdvertCount(Map<String, Object> map);

	/**
	 * 广告拉取
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<Advert> updateAdvertFetch(User user);

	/**
	 * 广告拉取 & 冻结
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<Advert> updateAdvertFetchFreeze(User user);

	/**
	 * 根据广告类型拉取&冻结
	 * 
	 * @param user
	 * @return
	 */
	AdvertResult<Advert> updateAdvertFetchFreeze3(User user);

	/**
	 * 广告暂停
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<AdvertStatistics> updateStopAdvert(Advert advert);

	AdvertResult<Integer> updateCheckFailAdvert(Advert advert);

	AdvertResult<Integer> updateCheckPassAdvert(Advert advert);

	/**
	 * 广告奖励
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<RedEnvelope> updateAdvertAwart(Advert advert, User user);
	
	/**
	 * 广告奖励3.0
	 * 
	 * @param map
	 * @return
	 */
	AdvertResult<RedEnvelope> updateAdvertAwart3(Advert advert, User user);

	AdvertResult<Integer> updateAdvertExpired(Advert advert);

	/**
	 * 查询待解冻的广告列表
	 * 
	 * @param limit
	 *            获取数量
	 * @return
	 */
	List<RedEnvelope> searchWaitUnFreezeAdvertList(int limit);

	/**
	 * 解冻广告
	 * 
	 * @param envelope
	 */
	void changeAdvertUnFreeze(RedEnvelope envelope);

	/**
	 * 解冻次数自增（不超过10）
	 * 
	 * @param envelope
	 *            阅读广告记录
	 */
	void changeUnFreezeCountAdd(RedEnvelope envelope);

	Map<String, Object> getAdvertSum();

	/**
	 * 返回离当前时间最近的limit条AdvertStatistics组成的List
	 * 
	 * @param limit
	 * @return
	 */
	List<AdvertStatistics> searchAdvertStatisticsList(int limit);

	public void updateAdvertStatisticsAwart(Advert advert, Integer amount, User user);

	/**
	 * 检测红包条件
	 * 
	 * @param advert
	 * @param advertStatistics
	 * @return 0 表示已经发完 1还有可以发的红包数
	 */
	public int checkRedEnvelopeCondition(Advert advert, AdvertStatistics advertStatistics);

	/**
	 * 检查2分钟之内是否有新广告
	 * 
	 * @return
	 */
	public boolean checkIfAdvertsSendedInTwoMinutes();

	/**
	 * 释放超时未领取的红包，并解冻红包对应的广告明细中的冻结数字
	 * 
	 * @param list
	 */
	public void changeRedEnvelopeUnfreeze(List<RedEnvelope> list) throws AdvertException, RedEnvelopeException;

	AdvertResult<ListRange<Advert>> selectListAdvertHomePage(Map<String, Object> map);

	int updateAdvertCommentSize(Advert advert);

	Advert selectByAdvertId(Long id);

	/**
	 * 拆接力福袋
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	public RelayLuckyBagVo updateRelayLuckyBag(User user, Advert advert, int openAmount);

	public AdvertResult<Advert> selectAdvertByMap(Map<String, Object> map);

	public int selectViewTimesCount(Map<String, Object> map);
	
	//查询Banner信息
	public Result<ListRange<BannerConfig>> selectListBannerPage(Map<String,Object> map);
	
	//查询BannerAdvert信息
	public List<BannerConfig> selectListAdvertBannerConfig(Map map);
	
	
	//维护Banner信息
	public int saveOrUpdateBanner(BannerConfig bannerConfig);


	/**
	 * 抢活动福袋
	 * 
	 * @param userId
	 * @param advertId
	 * @return
	 */
	public AdvertResult<RedEnvelope> addFetchActivity(Long userId, Long advertId);
	/**
	 * 公益广告
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectRedEnvelopeList(Map<String,Object> map);
	
	public AdvertResult<Advert> selectAdvertById(Long advertId);
	/**
	 * 查询所有公益广告
	 * @param map
	 * @return
	 */
	public List<Advert> selectAllPublicWelFare(Map<String,Object> map);
	/**
	 * 查询所有公益广告(显示公益广告列表，含捐赠金额)
	 * @param map
	 * @return
	 */
	public ListRange<PublicWelFareAdvertVo> selectPublicWelFareVoList(Map<String,Object> map);
	/**
	 * 添加公益广告信息 增加广告状态信息
	 * @param advert
	 * @return
	 */
	public int addPublicWelfare(Advert advert);
	
	/**
	 * 修改公益广告审核状态
	 * @param advert
	 * @return
	 */
	public int updateStateById(Long id, int state);
	
	public AdvertResult<RedEnvelope> selectRedEnvelopeByMap(Map<String,Object> map);
	
	/**
	 * 领取首页广告福袋
	 * 
	 * @param user
	 * @param advert
	 * @return
	 */
	public HomePageAwardVo updateHomePageAward(User user, Advert advert);
	
	/**
	 * 获取运营活动信息
	 * @param map
	 * @return
	 */
	public List<Advert> selectOperateActivity(Map<String, Object> map);
	/**
	 * 模糊查询广告信息
	 * @param map
	 * @return
	 */
	public ListRange<Advert> selectListRangeAdvertByLike(Map<String, Object> map);
	/**
	 * 查询模糊查询总条数
	 * @param map
	 * @return
	 */
	public int selectCountByLike(Map<String, Object> map);
	/**
	 * 分页查询运营活动
	 * @param map
	 * @return
	 */
	public AdvertResult<ListRange<Advert>> selectListOperateTheActivity(Map<String,Object> map);
	/**
	 * 查询最近一条XX活动
	 * @param map
	 * @return
	 */
	public Advert selectActivity(Map<String,Object> map);
	/**
	 * 幸运转盘抽奖
	 * @param userId
	 * @param advertId
	 * @return
	 */
	AdvertResult<RedEnvelope> addLuckyWheel(Long userId,Long advertId);
}
