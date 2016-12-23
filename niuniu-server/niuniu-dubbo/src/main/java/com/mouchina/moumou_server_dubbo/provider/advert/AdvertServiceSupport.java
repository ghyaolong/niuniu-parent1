package com.mouchina.moumou_server_dubbo.provider.advert;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.entity.ReturnEntity;
import com.mouchina.base.exception.BusinessException;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.JsonUtils;
import com.mouchina.base.utils.NumberUtil;
import com.mouchina.base.utils.PercentUtil;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.base.utils.map.GeoUtils;
import com.mouchina.base.utils.map.GpsCorrect;
import com.mouchina.base.utils.map.Point;
import com.mouchina.base.utils.redpacket.UtilsRedPacket;
import com.mouchina.moumou_server.dao.activityConfig.ActivityConfigMapper;
import com.mouchina.moumou_server.dao.advert.AdvertMapper;
import com.mouchina.moumou_server.dao.advert.AdvertStatisticsMapper;
import com.mouchina.moumou_server.dao.advert.AdvertTypeMapper;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.dao.advert.UserDonationRecordMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.pay.UserCashFlowMapper;
import com.mouchina.moumou_server.dao.system.SystemMessageMapper;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.advert.AdvertType;
import com.mouchina.moumou_server.entity.advert.BannerConfig;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeHelper;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.RelayLuckyBagHelper;
import com.mouchina.moumou_server.entity.advert.UserDonationRecord;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.income.AgentIncomeDetail;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.luckybag.LuckyBagHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server.entity.vo.HomePageAwardVo;
import com.mouchina.moumou_server.entity.vo.PublicWelFareAdvertVo;
import com.mouchina.moumou_server.entity.vo.RelayLuckyBagVo;
import com.mouchina.moumou_server.exceptions.AdvertException;
import com.mouchina.moumou_server.exceptions.RedEnvelopeException;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.income.AgentIncomeService;
import com.mouchina.moumou_server_interface.income.UserIncomeService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.moumou_server_interface.view.UserResult;

public class AdvertServiceSupport implements AdvertService {
	
	private final static Logger logger = LoggerFactory.getLogger(AdvertServiceSupport.class);
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

	@Resource
	private AdvertMapper advertMapper;
	@Resource
	private AdvertTypeMapper advertTypeMapper;
	@Resource
	private AdvertStatisticsMapper advertStatisticsMapper;
	@Resource
	private RedEnvelopeMapper redEnvelopeMapper;
	@Resource
	private RedisHelper redisHelper;
	@Resource
	private UsersRelationService usersRelationService;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private RedisLockHandler redisLockHandler;
	@Resource
	private UserCashFlowMapper userCashFlowMapper;
	@Resource
	private UserIncomeService userIncomeService;
	@Resource
	private UserInviteService userInviteService;
	@Resource
	private AgentIncomeService agentIncomeService;
	@Resource
	private UserAgentService userAgentService;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	private SystemService systemService;
	@Autowired
	private UserPartMapper userPartMapper;
	@Autowired
	private UserMapper userMapper;
	@Resource
	private UserLevelConfigService userLevelConfigService;
	@Resource
	private BusinessCouponService businessCouponService;
	@Autowired
	private ActivityConfigMapper activityConfigMapper;
	@Resource
	private SystemMessageMapper systemMessageMapper;
	@Resource
	private UserDonationRecordMapper userDonationRecordMapper;


	private static String USER_ADVERT_REDENVELOPE_LOCK = "User_Advert_RedEnveLope_Lock_";
	private static String USER_ADVERT_FETCH_LOCK = "User_Advert_Fetch_Lock_";
	private static String USER_ADVERT_ADD_LOCK = "User_Advert_Add_Lock_";
	private static String USER_ADVERT_EXPIRE_CLOSE_LOCK = "User_Advert_expire_close_Lock_";

	// 用户拉取广告锁定
	private static String USER_ADVERT_FETCH_FREEZE_LOCK = "User_Advert_Fetch_FREEZE_Lock_";
	// 每天领取广告的最大次数
	private static int USER_DAY_PULL_ADVERT_LIMIT = 100;

	private static int agent_income_1_1 = 12;

	private static int agent_income_2_1 = 10;

	private static int agent_income_3_1 = 7;

	private static int agent_income_1_2_2 = 2;
	private static int agent_income_1_2_3 = 2;

	private static int agent_income_2_2_3 = 3;

	@Override
	public AdvertResult<Advert> selectAdvert(Long advertId) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();

		Advert advert = advertMapper.selectByPrimaryKey(advertId);
		if (advert != null) {
			advertResult.setContent(advert);
			advertResult.setState(1);
		}

		return advertResult;
	}

	@Override
	public AdvertResult<Advert> selectAdvert(Map<String, Object> map) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();

		Advert advert = advertMapper.selectModel(map);
		if (advert != null) {
			advertResult.setContent(advert);
			advertResult.setState(1);
		}

		return advertResult;
	}

	/**
	 * 发布广告:插入广告表advert,更新用户信息（userAssets）中的余额,插入广告明细advertStatistics(advertId、createTime)
	 */
	public AdvertResult<Advert> addAdvert(Advert advert, User user) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();

		List<String> advertAddLocks = new ArrayList<String>();
		try {
			String advertLock = USER_ADVERT_ADD_LOCK + user.getId();
			advertAddLocks.add(advertLock);

			if (redisLockHandler.tryLock(advertLock)) {

				UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);

				// 判断发红包兑换的广告币是否足够
				if (advert.getRedEnvelopeAmount() * 100 <= userAssets.getVirtualBalance()) {
					advert.setCreateTime(new Date());
					int result = advertMapper.insertSelective(advert);
					advertResult.setContent(advert);
					advertResult.setState(result);
					if (result == 1) {
						AdvertStatistics advertStatistics = new AdvertStatistics();
						advertStatistics.setAdvertId(advert.getId());
						advertStatistics.setCreateTime(new Date());
						addAdvertStatistics(advertStatistics);
						userAssets.setCashBalance(userAssets.getCashBalance() - advert.getRedEnvelopeAmount());
						userAssets.setTableNum(user.getTableNum());
						userVerifyService.updateUserAssets(userAssets);
						advertResult.setState(1);
					}
				}
			} else {
				advertResult.setState(2);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			for (String lock : advertAddLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}

	@Override
	public AdvertResult<Advert> addAdvert3(Advert advert, User user) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();
		
		List<String> advertAddLocks = new ArrayList<String>();
		
		try {
			String advertLock = USER_ADVERT_ADD_LOCK + user.getId();
			advertAddLocks.add(advertLock);
			
			//如果已经被锁，就直接返回
			if(!redisLockHandler.tryLock(advertLock)) {
				advertResult.setState(0);
				return advertResult; 
			}
			
			// 获取用户财富对象
			UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
			
			//判断广告币是否足够，如果广告币不够是不能发送广告的
			//通过讨论广告币字段延用之前的现金余额字段
			if(advert.getRedEnvelopeAmount() <= userAssets.getCashBalance()) {
				Date date = new Date();
				advert.setRedEnvelopeType(1);
				advert.setCreateTime(date);
				if(null == advert.getStartTime()){
					advert.setStartTime(date);
				}
				//如果广告福袋有经纬度需要做gps纠偏
				Double lng = advert.getLongitude(); //经度
				Double lat = advert.getLatitude(); //纬度
				double[] latlng = new double[2];
				if(lng != null && lat != null && lng.doubleValue() > 0 && lat.doubleValue() > 0){
					GpsCorrect.transform(lat, lng, latlng);
					advert.setLongitude(latlng[1]);
					advert.setLatitude(latlng[0]);
				}
				
				//添加广告记录	返回1就说明添加成功
				int result = advertMapper.insertSelective(advert);
				
				advertResult.setContent(advert);
				advertResult.setState(result);
				
				//如果广告添加成功
				if (result == 1) {
					AdvertStatistics advertStatistics = new AdvertStatistics();
					advertStatistics.setAdvertId(advert.getId());
					advertStatistics.setCreateTime(new Date());
					/*接力福袋则插入基础金额*/
					if(advert.getAdvertType().intValue() == 4){
						advertStatistics.setSendRedEnvelopeAmount(advert.getRedEnvelopeAmount());
					}
					//添加广告统计
					this.addAdvertStatistics(advertStatistics);
					
					userAssets.setTableNum(user.getTableNum());
					//计算账户余额
					userAssets.setCashBalance(userAssets.getCashBalance() - advert.getRedEnvelopeAmount());
					//计算广告币余额
					//userAssets.setVirtualBalance(userAssets.getVirtualBalance() - advert.getRedEnvelopeAmount() * 100);
					userVerifyService.updateUserAssets(userAssets);
					
					advertResult.setState(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			for (String lock : advertAddLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		
		return advertResult;
	}

	@Override
	public int selectAdvertCount(Map<String, Object> map) {
		return advertMapper.selectCount(map);
	}

	@Override
	public List<Advert> selectListAdvert(Map<String, Object> map) {

		return advertMapper.selectPageList(map);
	}

	@Override
	public AdvertResult<ListRange<Advert>> selectListAdvertPage(Map<String, Object> map) {
		AdvertResult<ListRange<Advert>> advertResult = new AdvertResult<ListRange<Advert>>();
		ListRange<Advert> listRange = new ListRange<Advert>();
		int count = selectAdvertCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectListAdvert(map));
		advertResult.setContent(listRange);
		advertResult.setState(1);
		return advertResult;
	}

	@Override
	public AdvertResult<Integer> deleteAdvert(Long advertId) {
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();

		int result = advertMapper.deleteByPrimaryKey(advertId);
		advertResult.setState(result);

		return advertResult;
	}

	@Override
	public AdvertResult<Integer> updatePublishAdvert(Advert advert) {
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();

		if (advert.getState() == 6) {

			AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(
					advert.getId());

			int amoutd = advert.getRedEnvelopeAmount()
					- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeAmount();

			int countd = advert.getRedEnvelopeAmount()
					- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeCount();

			if (amoutd > 0 && countd > 0) {

				advert.setState((byte) 1);

				updateAdvert(advert);
				advertResult.setState(1);
			}
		}
		return advertResult;
	}

	@Override
	public AdvertResult<Integer> updateCheckPassAdvert(Advert advert) {
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();
		if (advert.getState() == 4) {

			AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(
					advert.getId());

			int amoutd = advert.getRedEnvelopeAmount()
					- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeAmount();

			int countd = advert.getRedEnvelopeAmount()
					- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeCount();

			if (amoutd > 0 && countd > 0) {
				advert.setState((byte) 1);
			} else {
				advert.setState((byte) 3);
			}
			updateAdvert(advert);
			advertResult.setState(1);
		}
		return advertResult;
	}

	@Override
	public AdvertResult<Integer> updateAdvert(Advert advert) {
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();
		advert.setModifyTime(new Date());
		int result = advertMapper.updateByPrimaryKeySelective(advert);
		advertResult.setState(result);

		return advertResult;
	}

	@Override
	public AdvertResult<AdvertType> selectAdvertType(Integer AdvertTypeId) {
		AdvertResult<AdvertType> AdvertTypeResult = new AdvertResult<AdvertType>();

		AdvertType AdvertType = advertTypeMapper.selectByPrimaryKey(AdvertTypeId);
		if (AdvertType != null) {
			AdvertTypeResult.setContent(AdvertType);
			AdvertTypeResult.setState(1);
		}

		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<AdvertType> selectAdvertType(Map<String, Object> map) {
		AdvertResult<AdvertType> AdvertTypeResult = new AdvertResult<AdvertType>();

		AdvertType AdvertType = advertTypeMapper.selectModel(map);
		if (AdvertType != null) {
			AdvertTypeResult.setContent(AdvertType);
			AdvertTypeResult.setState(1);
		}

		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<AdvertType> addAdvertType(AdvertType advertType) {
		AdvertResult<AdvertType> AdvertTypeResult = new AdvertResult<AdvertType>();

		int result = advertTypeMapper.insert(advertType);
		AdvertTypeResult.setContent(advertType);
		AdvertTypeResult.setState(result);

		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<ListRange<AdvertType>> selectListAdvertTypePage(Map<String, Object> map) {
		AdvertResult<ListRange<AdvertType>> AdvertTypeResult = new AdvertResult<ListRange<AdvertType>>();
		ListRange<AdvertType> listRange = new ListRange<AdvertType>();
		int count = advertTypeMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(advertTypeMapper.selectPageList(map));
		AdvertTypeResult.setContent(listRange);
		AdvertTypeResult.setState(1);
		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<Integer> deleteAdvertType(Integer advertTypeId) {
		AdvertResult<Integer> AdvertTypeResult = new AdvertResult<Integer>();

		int result = advertTypeMapper.deleteByPrimaryKey(advertTypeId);
		AdvertTypeResult.setState(result);

		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<Integer> updateAdvertType(AdvertType advertType) {
		AdvertResult<Integer> AdvertTypeResult = new AdvertResult<Integer>();

		int result = advertTypeMapper.updateByPrimaryKeySelective(advertType);
		AdvertTypeResult.setState(result);

		return AdvertTypeResult;
	}

	@Override
	public AdvertResult<AdvertStatistics> selectAdvertStatistics(Long advertStatisticsId) {
		AdvertResult<AdvertStatistics> advertStatisticsResult = new AdvertResult<AdvertStatistics>();

		AdvertStatistics advertStatistics = advertStatisticsMapper.selectByPrimaryKey(advertStatisticsId);
		if (advertStatistics != null) {
			advertStatisticsResult.setContent(advertStatistics);
			advertStatisticsResult.setState(1);
		}

		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<AdvertStatistics> selectAdvertStatisticsByAdvertId(Long advertId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("advertId", advertId);
		AdvertResult<AdvertStatistics> advertResult = selectAdvertStatistics(map);
		return advertResult;
	}

	@Override
	public AdvertResult<AdvertStatistics> selectAdvertStatistics(Map<String, Object> map) {
		AdvertResult<AdvertStatistics> advertStatisticsResult = new AdvertResult<AdvertStatistics>();
		AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(map);
		if (advertStatistics != null) {
			advertStatisticsResult.setContent(advertStatistics);
			advertStatisticsResult.setState(1);
		}

		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<AdvertStatistics> addAdvertStatistics(AdvertStatistics advertStatistics) {
		AdvertResult<AdvertStatistics> advertStatisticsResult = new AdvertResult<AdvertStatistics>();

		int result = advertStatisticsMapper.insertSelective(advertStatistics);
		advertStatisticsResult.setContent(advertStatistics);
		advertStatisticsResult.setState(result);

		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<ListRange<AdvertStatistics>> selectListAdvertStatisticsPage(Map<String, Object> map) {
		AdvertResult<ListRange<AdvertStatistics>> advertStatisticsResult = new AdvertResult<ListRange<AdvertStatistics>>();
		ListRange<AdvertStatistics> listRange = new ListRange<AdvertStatistics>();
		int count = advertStatisticsMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(advertStatisticsMapper.selectPageList(map));
		advertStatisticsResult.setContent(listRange);
		advertStatisticsResult.setState(1);
		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<Integer> deleteAdvertStatistics(Long advertStatisticsId) {
		AdvertResult<Integer> advertStatisticsResult = new AdvertResult<Integer>();

		int result = advertStatisticsMapper.deleteByPrimaryKey(advertStatisticsId);
		advertStatisticsResult.setState(result);

		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<Integer> updateAdvertStatistics(AdvertStatistics advertStatistics) {
		AdvertResult<Integer> advertStatisticsResult = new AdvertResult<Integer>();

		int result = advertStatisticsMapper.updateByPrimaryKeySelective(advertStatistics);
		advertStatisticsResult.setState(result);

		return advertStatisticsResult;
	}

	@Override
	public AdvertResult<RedEnvelope> selectRedEnvelope(Long redEnvelopeId) {
		AdvertResult<RedEnvelope> redEnvelopeResult = new AdvertResult<RedEnvelope>();

		RedEnvelope redEnvelope = redEnvelopeMapper.selectByPrimaryKey(redEnvelopeId);
		if (redEnvelope != null) {
			redEnvelopeResult.setContent(redEnvelope);
			redEnvelopeResult.setState(1);
		}

		return redEnvelopeResult;
	}

	@Override
	public AdvertResult<RedEnvelope> selectRedEnvelope(Map<String, Object> map) {
		AdvertResult<RedEnvelope> redEnvelopeResult = new AdvertResult<RedEnvelope>();

		RedEnvelope redEnvelope = redEnvelopeMapper.selectModel(map);
		if (redEnvelope != null) {
			redEnvelopeResult.setContent(redEnvelope);
			redEnvelopeResult.setState(1);
		}

		return redEnvelopeResult;
	}

	@Override
	public AdvertResult<RedEnvelope> addRedEnvelope(RedEnvelope redEnvelope) {
		AdvertResult<RedEnvelope> redEnvelopeResult = new AdvertResult<RedEnvelope>();

		int result = redEnvelopeMapper.insertSelective(redEnvelope);
		redEnvelopeResult.setContent(redEnvelope);
		redEnvelopeResult.setState(result);

		return redEnvelopeResult;
	}

	private int selectRedEnvelopeCount(Map<String, Object> map) {

		return redEnvelopeMapper.selectCount(map);
	}

	private int selectRedEnvelopeCountTop(Map<String, Object> map) {

		return redEnvelopeMapper.selectCountTop(map);
	}

	private List<RedEnvelope> selectRedEnvelopePageList(Map<String, Object> map) {

		return redEnvelopeMapper.selectPageList(map);
	}

	private List<RedEnvelopeTop> selectRedEnvelopeTopPageList(Map<String, Object> map) {

		return redEnvelopeMapper.selectListPageRedEnvelopeTopNew(map);
	}

	private RedEnvelopeTop selectRedEnvelopeTopMe(Long userId) {

		return redEnvelopeMapper.selectRedEnvelopeTopNewMe(userId);
	}

	@Override
	public AdvertResult<ListRange<RedEnvelope>> selectListRedEnvelopePage(Map<String, Object> map) {
		AdvertResult<ListRange<RedEnvelope>> RedEnvelopeResult = new AdvertResult<ListRange<RedEnvelope>>();
		ListRange<RedEnvelope> listRange = new ListRange<RedEnvelope>();
		int count = selectRedEnvelopeCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectRedEnvelopePageList(map));
		RedEnvelopeResult.setContent(listRange);
		RedEnvelopeResult.setState(1);
		return RedEnvelopeResult;
	}
	
	@Override
	public List<RedEnvelope> selectListRedEnvelopePage3(Map<String, Object> map) {
		int count = selectRedEnvelopeCount(map);
		List<RedEnvelope> redEnvelopes = this.selectRedEnvelopePageList(map);
		
		Integer length = redEnvelopes.size();
		//如果只有一条数据，就不用再次比较
		if(length <= 1) return redEnvelopes;
		
		for (int i = 0; i < length; i++) {
			//只有在加载第一页数据的时候才会在顶部加上月份
			if((Integer)map.get("offset") <= 1 && i == 0 ) {
				//给首位加入月份
				String firstTime = sdf.format(redEnvelopes.get(0).getAwardTime());
				RedEnvelope re1 = new RedEnvelope();
				re1.setMonth(firstTime.split("-")[1] + "月");
				redEnvelopes.add(0, re1);
				continue;
			}
			
			//如果时间为null  就不用再次比较了
			if(redEnvelopes.get(i).getAwardTime() == null) continue;
			
			//如果最后一位，就不用再循环
			if(i+1 >= length) return redEnvelopes;
			
			RedEnvelope redEnvelopePrev = redEnvelopes.get(i);
			RedEnvelope redEnvelopeAfter = redEnvelopes.get(i + 1);
			String datePrev = sdf.format(redEnvelopePrev.getAwardTime());
			String dateAfter = sdf.format(redEnvelopeAfter.getAwardTime());
			
			// 如果月份不是一个，那么就出现了跨月份，需要插入月份
			if (!datePrev.equals(dateAfter)) {
				RedEnvelope re2 = new RedEnvelope();
				re2.setMonth(dateAfter.split("-")[1] + "月");
				redEnvelopes.add(i + 1, re2);
				continue;
			}
		}
		
		return redEnvelopes;
	}

	@Override
	public ReturnEntity<RedEnvelopeTop> selectListPageRedEnvelopeTop(UserSearchSO search) {
		return new ReturnEntity<RedEnvelopeTop>(redEnvelopeMapper.selectListPageRedEnvelopeTop(search));
	}

	@Override
	public ListRange<RedEnvelopeTop> selectListPageRedEnvelopeTopNew(Map<String, Object> map) {
		ListRange<RedEnvelopeTop> listRange = new ListRange<RedEnvelopeTop>();
		int count = selectRedEnvelopeCountTop(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectRedEnvelopeTopPageList(map));
		return listRange;
	}

	@Override
	public RedEnvelopeTop selectRedEnvelopeTopInfoMe(Long userId) {
		RedEnvelopeTop redEnvelopeTop = new RedEnvelopeTop();
		redEnvelopeTop = selectRedEnvelopeTopMe(userId);
		return redEnvelopeTop;
	}

	@Override
	public AdvertResult<Integer> deleteRedEnvelope(Long redEnvelopeId) {
		AdvertResult<Integer> RedEnvelopeResult = new AdvertResult<Integer>();

		int result = redEnvelopeMapper.deleteByPrimaryKey(redEnvelopeId);
		RedEnvelopeResult.setState(result);

		return RedEnvelopeResult;
	}

	@Override
	public AdvertResult<Integer> updateRedEnvelope(RedEnvelope redEnvelope) {
		AdvertResult<Integer> redEnvelopeResult = new AdvertResult<Integer>();
		int result = redEnvelopeMapper.updateByPrimaryKeySelective(redEnvelope);
		redEnvelopeResult.setState(result);
		return redEnvelopeResult;
	}

	@Override
	public AdvertResult<Advert> updateAdvertFetch(User user) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();

		// 每个用户每天最多领取红包数量
		int limitDay = 100;
		// String advertIds="";
		Map<String, Object> redLimitMap = new HashMap<String, Object>();
		redLimitMap.put("userId", user.getId());
		redLimitMap.put("crateTimeDay", new Date());
		// 查询 用户当天领取红包数量
		int redLimitCount = selectRedEnvelopeCount(redLimitMap);
		if (redLimitCount > limitDay) {
			return advertResult;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Date now = new Date();
		map.put("startTimelt", now);
		map.put("order", "start_time desc");
		map.put("endTimegt", now);
		map.put("state", 1);
		// if(StringUtil.isNotEmpty(advertIds))
		// map.put("notInIds", advertIds);
		if (user.getAgeGroup() != null && user.getAgeGroup() > 0) {
			map.put("adverCondtionAgeScopes", user.getAgeGroup() + ",");
		}
		if (user.getSalary() != null && user.getSalary() > 0) {
			map.put("salaryScopes", user.getSalary() + ",");
		}

		if (user.getMarried() != null && user.getMarried() > 0) {
			map.put("marrieds", user.getMarried());
		}

		if (user.getSex() != null && user.getSex() > 0) {
			map.put("adverCondtionSexs", user.getSex());
		}

		// int count = selectRedEnvelopeCount(map);

		// if ((count - pageSize) > 0)
		// begin = ((Double) (Math.random() * (count - pageSize))).intValue();

		int pageSize = limitDay * 3;

		map.put("page", new Page(0, pageSize));
		List<String> advertFetchLocks = new ArrayList<String>();
		try {

			String advertLock = USER_ADVERT_FETCH_LOCK + user.getId();
			advertFetchLocks.add(advertLock);

			if (redisLockHandler.tryLock(advertLock)) {
				List<Advert> advertList = selectListAdvert(map);

				for (Advert ad : advertList) {
					Map<String, Object> redMap = new HashMap<String, Object>();
					redMap.put("advertId", ad.getId());
					redMap.put("userId", user.getId());

					int redCount = selectRedEnvelopeCount(redMap);

					logger.debug("----------------AdvertFetch userId=" + user.getId() + ",advertId=" + ad.getId()
							+ ",redCount=" + redCount + ",advertListount=" + advertList.size());
					if (redCount == 0) {

						RedEnvelope redEnvelope = new RedEnvelope();
						redEnvelope.setAdverName(ad.getAdvertName());
						redEnvelope.setAdvertId(ad.getId());
						redEnvelope.setCreateTime(new Date());

						String advertContent = ad.getAdvertContent();
						if (StringUtil.isNotEmpty(advertContent)) {
							Map advertContentMap = JsonUtils.jsonToObject(advertContent, Map.class);
							if (advertContentMap != null && advertContentMap.containsKey("photo")) {

								try {
									List aclist = (List) advertContentMap.get("photo");

									if (aclist != null && aclist.size() > 0) {
										redEnvelope.setAdvertLogo(aclist.get(0).toString());

									}
								} catch (Exception e) {
									logger.error("advertContent=" + advertContent + ",error=" + e.getMessage());
								}
							}

						}
						redEnvelope.setUserId(user.getId());
						redEnvelope.setPublisherId(ad.getUserId());
						redEnvelope.setAgeGroup(user.getAgeGroup());
						redEnvelope.setUserAvatar(user.getAvatar());
						redEnvelope.setUserNickName(user.getNickName());
						redEnvelope.setUserSex((byte)user.getSex().intValue());
						addRedEnvelope(redEnvelope);
						advertResult.setContent(ad);
						advertResult.setState(1);
						break;
					}
				}
			}
		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			for (String lock : advertFetchLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		return advertResult;
	}

	@Override
	public AdvertResult<Advert> updateAdvertFetchFreeze(User user) {
		
		int advertType = 5;//广告类型，前台传入
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();
		// 每个用户每天最多领取红包数量
		// String advertIds="";
		final Integer level = user.getLevel();//用户等级
		Map<String, Object> redLimitMap = new HashMap<String, Object>();
		redLimitMap.put("userId", user.getId());
		redLimitMap.put("crateTimeDay", new Date());
		redLimitMap.put("state", 1);
		// 查询 用户当天领取红包数量
		int redLimitCount = selectRedEnvelopeCount(redLimitMap);//用户当天已领取红包数量
		
		UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(new HashMap<String,Integer>(){{put("level", level);}});
		//当天已经领取数字若大于其用户等级规定的一天可以领取峰值，则直接返回
		if(advertType == 5){
			//领取首页红包要做领取数字限制
			if(userLevelConfig != null){
				if (redLimitCount > userLevelConfig.getHomePageRedEnvelope()) {
					return advertResult;
				}
			}else{
				if (redLimitCount > USER_DAY_PULL_ADVERT_LIMIT) {
					return advertResult;
				}
			}
		}
		

		Map<String, Object> map = new HashMap<String, Object>();
		Date now = new Date();
		map.put("startTimelt", now);
		map.put("order", "start_time desc");
		map.put("endTimegt", now);
		map.put("state", 1);
		
		// if(StringUtil.isNotEmpty(advertIds))
		// map.put("notInIds", advertIds);
		if (user.getAgeGroup() != null && user.getAgeGroup() > 0) {
			map.put("adverCondtionAgeScopes", user.getAgeGroup());
		}
//		if (user.getSalary() != null && user.getSalary() > 0) {
//			map.put("salaryScopes", user.getSalary() + ",");
//		}

		if (user.getMarried() != null && user.getMarried() > 0) {
			map.put("marrieds", user.getMarried());
		}

		if (user.getSex() != null && user.getSex() > 0) {
			map.put("adverCondtionSexs", user.getSex());
		}

		if (user.getWork() != null && user.getWork() > 0) {
			map.put("works", user.getWork());
		}

		if (user.getHobby() != null && !user.getHobby().isEmpty()) {
			map.put("hobbys", user.getHobby());
		}
		
		map.put("adverConditionProvince", user.getProvince());
		map.put("adverConditionCity", user.getCity());
		map.put("adverConditionArea", user.getArea());

		int pageSize = USER_DAY_PULL_ADVERT_LIMIT * 3;

		map.put("page", new Page(0, pageSize));

		List<String> advertFetchLocks = new ArrayList<String>();
		try {

			final String userLock = USER_ADVERT_FETCH_LOCK + user.getId();
			advertFetchLocks.add(userLock);

			if (redisLockHandler.tryLock(userLock)) {
				List<Advert> advertList = selectListAdvert(map);
				
				for (Advert ad : advertList) {
					
					{// 验证该广告是否已被当前用户领取
						RedEnvelope envelope = new RedEnvelope();
						envelope.setUserId(user.getId());
						envelope.setAdvertId(ad.getId());
						int redCount = redEnvelopeMapper.verifyUserReceivedAdvert(envelope);

						logger.info(String.format("----AdvertFetch userId=%s,advertId=%s,redCount=%s,advertListount=%s",
								new Object[] { user.getId(), ad.getId(), redCount, advertList.size() }));
						if (redCount > 0) {
							continue;
						}
					}

					/* 满足条件 则 对当前广告加锁 */
					String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + ad.getId();
					if (redisLockHandler.tryLock(advertLock) == false) {
						// 若当前广告被加锁了，则跳过当前广告
						continue;
					}

					{// 冻结广告：用户浏览广告后冻结
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("advertId", ad.getId());
						AdvertStatistics adStatistics = advertStatisticsMapper.selectModel(paramMap);

						// //add by zhangkun
						// 目前的分布式锁会出现多个用户同时拿到锁对象，故在此数手动执行解冻广告操作 start
						// paramMap.put("state", 0);
						// paramMap.put("expireSeconds",
						// 300);//表示该红包的createTime距离当前时间超过300秒则表示此红包过期无人领取，做解冻处理
						// List<RedEnvelope> list =
						// redEnvelopeMapper.selectPageList(paramMap);//该广告的所有5分钟之内无人领取的红包
						// changeRedEnvelopeUnfreeze(list);
						// //add by zhangkun
						// 目前的分布式锁会出现多个用户同时拿到锁对象，故在此数手动执行解冻广告操作 end

						if (ad.getRedEnvelopeCount() <= adStatistics.getSendRedEnvelopeCount()
								+ adStatistics.getFreezeEnvelopeCount()) {
							// 广告统计： 广告总发行数 = 广告冻结数 + 广告已阅读数 表示该广告已经暂时被抢光
							redisLockHandler.unLock(advertLock);
							continue;
						}
						// 加入锁池
						advertFetchLocks.add(advertLock);
						AdvertStatistics tempStatistice = new AdvertStatistics();
						tempStatistice.setId(adStatistics.getId());
						tempStatistice.setFreezeEnvelopeCount(adStatistics.getFreezeEnvelopeCount() + 1);
						this.updateAdvertStatistics(tempStatistice);
					}

					RedEnvelope redEnvelope = new RedEnvelope();
					redEnvelope.setAdverName(ad.getAdvertName());
					redEnvelope.setAdvertId(ad.getId());
					redEnvelope.setCreateTime(new Date());

					String advertContent = ad.getAdvertContent();
					if (StringUtil.isNotEmpty(advertContent)) {
						Map advertContentMap = JsonUtils.jsonToObject(advertContent, Map.class);
						if (advertContentMap != null && advertContentMap.containsKey("photo")) {
							try {
								List aclist = (List) advertContentMap.get("photo");

								if (aclist != null && aclist.size() > 0) {
									redEnvelope.setAdvertLogo(aclist.get(0).toString());

								}
							} catch (Exception e) {
								logger.error("advertContent=" + advertContent + ",error=" + e.getMessage());
							}
						}
					}
					redEnvelope.setUserId(user.getId());
					redEnvelope.setPublisherId(ad.getUserId());
					redEnvelope.setAgeGroup(user.getAgeGroup());
					redEnvelope.setUserAvatar(user.getAvatar());
					redEnvelope.setUserNickName(user.getNickName());
					redEnvelope.setUserSex((byte)user.getSex().intValue());
					addRedEnvelope(redEnvelope);
					advertResult.setContent(ad);
					advertResult.setState(1);
					return advertResult;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			for (String lock : advertFetchLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}

	
	@Override
	public AdvertResult<Advert> updateAdvertFetchFreeze3(User user) {
		
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();
		// 每个用户每天最多领取红包数量
//		final Integer level = user.getLevel();//用户等级
//		Map<String, Object> redLimitMap = new HashMap<String, Object>();
//		redLimitMap.put("userId", user.getId());
//		redLimitMap.put("crateTimeDay", new Date());
//		redLimitMap.put("state", 1);
//		// 查询 用户当天领取红包数量
//		int redLimitCount = selectRedEnvelopeCount(redLimitMap);//用户当天已领取红包数量
//		
//		UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(new HashMap<String,Integer>(){{put("level", level);}});

		Map<String, Object> map = new HashMap<String, Object>();
		Date now = new Date();
		map.put("startTimelt", now);
		map.put("order", "start_time desc");
		map.put("endTimegt", now);
		map.put("state", 1);
		map.put("advertTypes", "" + AdvertHelper.ADVERT_TYPE_0.getMarker() + "," + 
				AdvertHelper.ADVERT_TYPE_1.getMarker() + "," +
				AdvertHelper.ADVERT_TYPE_2.getMarker() + "," +
				AdvertHelper.ADVERT_TYPE_6.getMarker() + "," +
				AdvertHelper.ADVERT_TYPE_7.getMarker() + "," +
				AdvertHelper.ADVERT_TYPE_8.getMarker());
		if (user.getAgeGroup() != null && user.getAgeGroup() >= 0) {
			map.put("adverCondtionAgeScopes", user.getAgeGroup());
		}

		if (user.getMarried() != null && user.getMarried() >= 0) {
			map.put("marrieds", user.getMarried());
		}

		if (user.getSex() != null && user.getSex() >= 0) {
			map.put("adverCondtionSexs", user.getSex());
		}

		if (user.getWork() != null && user.getWork() >= 0) {
			map.put("works", user.getWork());
		}
		String hobby = user.getHobby();
		if (hobby != null && !hobby.isEmpty()) {
			String[] hobbys = hobby.split(",");
			for(int i=0;i<hobbys.length;i++){
				map.put("hobbys"+i, hobbys[i]);
			}
		}
		
		map.put("adverConditionProvince", user.getProvince());
		map.put("adverConditionCity", user.getCity());
		map.put("adverConditionArea", user.getArea());

		int pageSize = USER_DAY_PULL_ADVERT_LIMIT * 3;

		map.put("page", new Page(0, pageSize));

		List<String> advertFetchLocks = new ArrayList<String>();
		try {

			final String userLock = USER_ADVERT_FETCH_LOCK + user.getId();
			advertFetchLocks.add(userLock);

			if (redisLockHandler.tryLock(userLock)) {
				List<Advert> advertList = selectListAdvert(map);

				for (Advert ad : advertList) {
					
					//距离过滤(查询到的广告福袋有经纬度且不为0则需要过滤)
					double adLng = ad.getLongitude();
					double adLat = ad.getLatitude();
					Integer adDis = ad.getAdverCondtionDistance();
					if(adLng > 0 && adLat > 0 && adDis != null){
						/* 计算距离 */
						Point advertPoint = new Point(ad.getLongitude(), ad.getLatitude());//发布广告坐标点
						Point userPoint = new Point(user.getLongitude(), user.getLatitude());//用户坐标点
						double distance = GeoUtils.getDistance(advertPoint, userPoint);//用户当前定位点距离广告发布点的距离
						double adDistance = 0.0;
						switch (adDis) {
						case 0:
							adDistance = AdvertHelper.DISTANCE_0.getMarker();							
							break;
						case 1:
							adDistance = AdvertHelper.DISTANCE_1.getMarker();						
							break;
						case 2:
							adDistance = AdvertHelper.DISTANCE_2.getMarker();
							break;
						case 3:
							adDistance = AdvertHelper.DISTANCE_3.getMarker();
							break;
						case 4:
							adDistance = AdvertHelper.DISTANCE_4.getMarker();
							break;
						default:
							break;
						}
						//当前用户不在距离范围内，跳过此广告福袋
						if(distance > adDistance){
							continue;
						}
					}
					
					{// 验证该广告是否已被当前用户领取
						RedEnvelope envelope = new RedEnvelope();
						envelope.setUserId(user.getId());
						envelope.setAdvertId(ad.getId());
						int redCount = redEnvelopeMapper.verifyUserReceivedAdvert(envelope);

						logger.info(String.format("----AdvertFetch userId=%s,advertId=%s,redCount=%s,advertListount=%s",
								new Object[] { user.getId(), ad.getId(), redCount, advertList.size() }));
						if (redCount > 0) {
							continue;
						}
					}

					/* 满足条件 则 对当前广告加锁 */
					String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + ad.getId();
					if (redisLockHandler.tryLock(advertLock) == false) {
						// 若当前广告被加锁了，则跳过当前广告
						continue;
					}

					{// 冻结广告：用户浏览广告后冻结
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("advertId", ad.getId());
						AdvertStatistics adStatistics = advertStatisticsMapper.selectModel(paramMap);

						// //add by zhangkun
						// 目前的分布式锁会出现多个用户同时拿到锁对象，故在此数手动执行解冻广告操作 start
						// paramMap.put("state", 0);
						// paramMap.put("expireSeconds",
						// 300);//表示该红包的createTime距离当前时间超过300秒则表示此红包过期无人领取，做解冻处理
						// List<RedEnvelope> list =
						// redEnvelopeMapper.selectPageList(paramMap);//该广告的所有5分钟之内无人领取的红包
						// changeRedEnvelopeUnfreeze(list);
						// //add by zhangkun
						// 目前的分布式锁会出现多个用户同时拿到锁对象，故在此数手动执行解冻广告操作 end
						int advertType = ad.getAdvertType().intValue();
						if(advertType == AdvertHelper.ADVERT_TYPE_2.getMarker().intValue() 
								|| advertType == AdvertHelper.ADVERT_TYPE_7.getMarker().intValue()){
							
						}else{
							if (ad.getRedEnvelopeCount() <= adStatistics.getSendRedEnvelopeCount()
									+ adStatistics.getFreezeEnvelopeCount()) {
								// 广告统计： 广告总发行数 = 广告冻结数 + 广告已阅读数 表示该广告已经暂时被抢光
								redisLockHandler.unLock(advertLock);
								continue;
							}
						}
						//当天已经领取数字若大于其用户等级规定的一天可以领取峰值，则直接返回
//						if(ad.getAdvertType().intValue() == AdvertHelper.ADVERT_TYPE_5.getMarker().intValue()){
//							//领取首页红包要做领取次数限制
//							if(userLevelConfig != null){
//								if (redLimitCount > userLevelConfig.getHomePageRedEnvelope()) {
//									continue;
//								}
//							}else{
//								if (redLimitCount > USER_DAY_PULL_ADVERT_LIMIT) {
//									continue;
//								}
//							}
//						}
						// 加入锁池
						advertFetchLocks.add(advertLock);
						AdvertStatistics tempStatistice = new AdvertStatistics();
						tempStatistice.setId(adStatistics.getId());
						tempStatistice.setFreezeEnvelopeCount(adStatistics.getFreezeEnvelopeCount() + 1);
						this.updateAdvertStatistics(tempStatistice);
					}

					RedEnvelope redEnvelope = new RedEnvelope();
					redEnvelope.setAdverName(ad.getAdvertName());
					redEnvelope.setAdvertId(ad.getId());
					redEnvelope.setCreateTime(new Date());

					String advertContent = ad.getAdvertContent();
					if (StringUtil.isNotEmpty(advertContent)) {
						Map advertContentMap = JsonUtils.jsonToObject(advertContent, Map.class);
						if (advertContentMap != null && advertContentMap.containsKey("photo")) {
							try {
								List aclist = (List) advertContentMap.get("photo");

								if (aclist != null && aclist.size() > 0) {
									redEnvelope.setAdvertLogo(aclist.get(0).toString());

								}
							} catch (Exception e) {
								logger.error("advertContent=" + advertContent + ",error=" + e.getMessage());
							}
						}
					}
					redEnvelope.setUserId(user.getId());
					redEnvelope.setPublisherId(ad.getUserId());
					redEnvelope.setAgeGroup(user.getAgeGroup());
					redEnvelope.setUserAvatar(user.getAvatar());
					redEnvelope.setUserNickName(user.getNickName());
					redEnvelope.setUserSex((byte)user.getSex().intValue());
					int advertType = ad.getAdvertType().intValue();
					if(advertType == AdvertHelper.ADVERT_TYPE_2.getMarker().intValue()){ //如果是公告福袋，插入红包表状态为1
						redEnvelope.setState((byte)AdvertHelper.STATE_1.getMarker().intValue());
						updateAdvertStatisticsAwart3(ad, 0, user);
					}
					if(advertType != AdvertHelper.ADVERT_TYPE_7.getMarker().intValue()){ //如果是运营活动则不插入数据
						addRedEnvelope(redEnvelope);
					}
					advertResult.setContent(ad);
					advertResult.setState(1);
					return advertResult;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			for (String lock : advertFetchLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}
	
	
	/**
	 * 计算红包额
	 * 
	 * @param advert
	 * @param advertStatistics
	 * @return
	 */
	private int calRedEnvelopeAmount(Advert advert, AdvertStatistics advertStatistics) {
		final Integer redType = advert.getRedEnvelopeType();
		if (redType == null) {
			throw new AdvertException(false, "红包类型数据异常");
		}

		final int totalMoney = advert.getRedEnvelopeAmount();
		final int totalNumber = advert.getRedEnvelopeCount();
		if (2 == redType.intValue()) {
			// 定额发广告
			return UtilsRedPacket.meanRedPacket(totalMoney, totalNumber);
		} else if (1 == redType.intValue()) {
			// 拼手气
			final int sendMoney = advertStatistics.getSendRedEnvelopeAmount();
			final int sendNumber = advertStatistics.getSendRedEnvelopeCount();
			return UtilsRedPacket.randomRedPacket(totalMoney, totalMoney - sendMoney, totalNumber,
					totalNumber - sendNumber);
		} else {
			throw new AdvertException(false, String.format("红包类型枚举不存在，信息：redPacketType=%s", redType.intValue()));
		}
	}

	/**
	 * 计算首页红包金额(根据用户等级)
	 * @param advert 广告对象
	 * @param advertStatistics 广告明细对象
	 * @param user 用户对象
	 * @return 单位是分
	 */
	private int calHomePageAdvertRedEnvelopeMoney(Advert advert, AdvertStatistics advertStatistics,User user){
		int money = 0;
		
		final int totalMoney = advert.getRedEnvelopeAmount();//发布总金额
		final int sendMoney = advertStatistics.getSendRedEnvelopeAmount();//已抢总金额
		
		final int level = user.getLevel();//用户等级
		UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(new HashMap<String,Integer>(){{put("level",level);}});
		int amount = (int) (NumberUtil.getHomePageRandomNumber() * userLevelConfig.getLevelRatio() * 100);
		money = amount >= (totalMoney-sendMoney) ? (totalMoney-sendMoney) : amount;
		return money;
	}
	
	/**
	 * 计算普通福袋广告金额(根据用户等级)
	 * @param advert
	 * @param advertStatistics
	 * @param user
	 * @return
	 */
	private int calAdvertRedEnvelopeMoney(Advert advert, AdvertStatistics advertStatistics,User user){
		int money = 0;
		final int totalMoney = advert.getRedEnvelopeAmount();//发布总金额
		final int totalNumber = advert.getRedEnvelopeCount();//发布总数量
		final int sendMoney = advertStatistics.getSendRedEnvelopeAmount();//已抢总金额
		final int sendNumber = advertStatistics.getSendRedEnvelopeCount();//已抢总数量
		final int overPlusMoney = totalMoney - sendMoney;//剩余金额
		final int overPlusCount = totalNumber - sendNumber;//剩余数量
		final int level = user.getLevel();//用户等级
		UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(new HashMap<String,Integer>(){{put("level",level);}});
		int amount = (int) (NumberUtil.getCommonAdvertRandomNumber() * (overPlusMoney/overPlusCount) * userLevelConfig.getLevelRatio());
		money = amount >= overPlusMoney ? overPlusMoney : amount;
		
		return money;
	}
	
	private int calRedEnvelopeAmount3(Advert advert, AdvertStatistics advertStatistics,User user) {
		final Integer advertType = advert.getAdvertType();//广告类型
		int amount = 0;
		
		switch(advertType){
			case 0 : 
				//普通福袋
				amount = calAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
			case 1 : 
				//优惠券福袋
				amount = calAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
			case 3 : 
				//圈子优惠券
				amount = calAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
			case 4 : 
				//接力福袋
			break;
			case 5 : 
				//首页广告
				amount = calHomePageAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
			case 6 : 
				//定时广告
				amount = calAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
			case 7 : 
				//运营活动广告
			break;
			case 8 : 
				//定时活动广告
				amount = calAdvertRedEnvelopeMoney(advert, advertStatistics, user);
			break;
		}
		return amount;
	}
	
	/**
	 * 计算红包2.0.6老方法
	 * @param advert
	 * @param user
	 * @return
	 */
	public int calRedEnvelope(Advert advert, User user) {
		int redEnvelopeAmout = 0;
		AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(advert.getId());
		if (advertAdvertStatisticsResult.getState() == 1) {
			int checkRed = checkRedEnvelopeCondition(advert, advertAdvertStatisticsResult.getContent());
			if (checkRed == 1) {
//				int amount = calRedEnvelopeAmount(advert, advertAdvertStatisticsResult.getContent());
				int amount = calRedEnvelopeAmount3(advert, advertAdvertStatisticsResult.getContent(),user);
				if (amount > 0) {
					redEnvelopeAmout = amount;
				}
			}
		}
		return redEnvelopeAmout;
	}

	/**
	 * 计算红包3.0
	 * @param advert
	 * @param user
	 * @return
	 */
	public int calRedEnvelope3(Advert advert, User user) {
		int redEnvelopeAmout = 0;
		AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(advert.getId());
		if (advertAdvertStatisticsResult.getState() == 1) {
			int checkRed = checkRedEnvelopeCondition(advert, advertAdvertStatisticsResult.getContent());
			if (checkRed == 1) {
				int amount = calRedEnvelopeAmount3(advert, advertAdvertStatisticsResult.getContent(),user);
				if (amount > 0) {
					redEnvelopeAmout = amount;
				}
			}
		}
		return redEnvelopeAmout;
	}
	
	/**
	 * 检查是否在2分钟之内有新广告发布
	 * 
	 * @return
	 */
	public boolean checkIfAdvertsSendedInTwoMinutes() {
		boolean checkFlag = false;// 默认没有

		// 获取福袋排序时长配置参数
		Map<String,Object> sortMap = new HashMap<String,Object>();
		sortMap.put("configKey", "client.advert.sort.duration"); // 只查询福袋排序时长配置参数
		sortMap.put("configGroup", "client");
		sortMap.put("state", "1");
		int interval = 30;
		SystemResult<List<SystemGlobalConfig>> systemGlobalConfigSystemResult = systemService
				.selectListSystemGlobalConfig(sortMap);
		List<SystemGlobalConfig> globalConfigs = systemGlobalConfigSystemResult.getContent();
		if (globalConfigs != null && globalConfigs.size() > 0) {
			interval = Integer.valueOf(globalConfigs.get(0).getConfigContent());
		}
		Date end = new Date();// 当前时间
		Date start = DateUtils.getSeveralMinutesAgoDate(end, interval);// interval分钟之前
		logger.error("广告范围在 start : " + start + " end : " + end);
		// 查询是否含有创建时间create_time在start、end中间的广告
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("createTimelt", end);// <if test="startTimegt != null" >and
										// ad.start_time >=
										// #{startTimegt,jdbcType=TIMESTAMP}</if>
		map.put("order", "create_time desc");
		map.put("createTimegt", start);
		map.put("states", "1,3");

		List<Advert> advertList = advertMapper.selectList(map);
		logger.info("twoMinutes adverts : " + advertList);
		if (advertList != null) {
			if (advertList.size() > 0) {
				checkFlag = true;
			} else {
				checkFlag = false;
			}
		} else {
			checkFlag = false;
		}
		logger.info(interval + "分钟之内检测广告的标志位 : " + checkFlag);
		return checkFlag;
	}

	/**
	 * 广告统计递增
	 * 
	 * @param advert
	 * @param amount
	 * @param user
	 */
	// private void updateAdvertStatisticsAwart(Advert advert, int amount,
	// User user) {
	//
	// AdvertResult<AdvertStatistics> advertAdvertStatisticsResult =
	// selectAdvertStatisticsByAdvertId(advert.getId());
	// AdvertStatistics aAdvertStatistics =
	// advertAdvertStatisticsResult.getContent();
	//
	// aAdvertStatistics.setSendRedEnvelopeAmount(aAdvertStatistics.getSendRedEnvelopeAmount()
	// + amount);
	//
	// aAdvertStatistics.setSendRedEnvelopeCount(aAdvertStatistics.getSendRedEnvelopeCount()
	// + 1);
	//
	// // 领取广告，广告冻结数 递减
	// aAdvertStatistics.setFreezeEnvelopeCount(aAdvertStatistics.getFreezeEnvelopeCount()
	// - 1);
	//
	// if (advert.getRedEnvelopeCount() ==
	// aAdvertStatistics.getSendRedEnvelopeCount()) {
	// // 更新广告状态为：已完成
	// advert.setState((byte) 3);
	// updateAdvert(advert);
	//
	// /** 发布广告添加收益 **/
	// updateCalPublishIncome(advert,advertAdvertStatisticsResult.getContent(),
	// user.getId());
	// }
	// updateAdvertStatistics(aAdvertStatistics);
	// }

	private UserResult<UserAgent> selectUserUpAgent(Long userId) {
		UserResult<UserAgent> userResult = new UserResult<UserAgent>();

		Long tempUserId = userId;
		int limitFinUPdAgent = 10;

		for (int i = limitFinUPdAgent; i > 0; i--) {

			Map<String,Object> inviteMap = new HashMap<String,Object>();
			inviteMap.put("newUserId", tempUserId);
			inviteMap.put("state", 1);
			SocialResult<UserInvite> socialInviteResult = userInviteService.selectUserInvite(inviteMap);
			if (socialInviteResult.getState() == 1) {

				Map<String,Object> agentMap = new HashMap<String,Object>();
				agentMap.put("userId", socialInviteResult.getContent().getUserId());
				agentMap.put("state", "1");
				UserResult<UserAgent> userAgentResult = userAgentService.selectUserAgent(agentMap);
				if (userAgentResult.getState() == 1) {

					userResult = userAgentResult;
					break;
				} else {
					tempUserId = socialInviteResult.getContent().getUserId();
				}
			} else {

				break;
			}

		}

		return userResult;

	}

	private void updateAgentCalPublishIncome(Advert advert, long userAgentId, AdvertStatistics advertStatistics,
			Long userId) {

		UserResult<UserAgent> userAgentResult = selectUserUpAgent(advert.getUserId());
		;

		if (userAgentResult.getState() == 1 && userAgentResult.getContent().getAgentLevel() != 4) {
			int sourceAgentLevel = userAgentResult.getContent().getAgentLevel();
			// 省级
			if (sourceAgentLevel == 1) {

				AgentIncomeDetail agentIncomeDetail = new AgentIncomeDetail();
				agentIncomeDetail.setAmout(advertStatistics.getSendRedEnvelopeAmount() * agent_income_1_1 / 100);
				agentIncomeDetail.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
				agentIncomeDetail.setEnventName("代理商发布广告收益");
				agentIncomeDetail.setEnventId(advert.getId());
				agentIncomeDetail.setUserId(userAgentResult.getContent().getUserId());
				agentIncomeDetail.setEventUserId(advert.getUserId());
				agentIncomeDetail.setIncomeType((byte) 1);
				agentIncomeDetail.setIncomeDate(new Date());
				agentIncomeDetail.setCreateTime(new Date());
				agentIncomeService.addAgentIncomeDetail(agentIncomeDetail);

			}

			// 市级

			if (sourceAgentLevel == 2) {

				AgentIncomeDetail agentIncomeDetail = new AgentIncomeDetail();
				agentIncomeDetail.setAmout(advertStatistics.getSendRedEnvelopeAmount() * agent_income_2_1 / 100);
				agentIncomeDetail.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
				agentIncomeDetail.setEnventName("代理商发布广告收益");
				agentIncomeDetail.setUserId(userAgentResult.getContent().getUserId());
				agentIncomeDetail.setEnventId(advert.getId());
				agentIncomeDetail.setEventUserId(advert.getUserId());
				agentIncomeDetail.setIncomeType((byte) 1);
				agentIncomeDetail.setIncomeDate(new Date());
				agentIncomeDetail.setCreateTime(new Date());
				agentIncomeService.addAgentIncomeDetail(agentIncomeDetail);

				if (userAgentResult.getContent().getParentId() > 0) {

					Map<String,Object> agentMapLevel1 = new HashMap<String,Object>();
					agentMapLevel1.put("userId", userAgentResult.getContent().getParentId());
					agentMapLevel1.put("state", 1);
					UserResult<UserAgent> userAgentLevel1 = userAgentService.selectUserAgent(agentMapLevel1);

					if (userAgentLevel1.getState() == 1 && userAgentLevel1.getContent().getAgentLevel() == 1) {
						AgentIncomeDetail agentIncomeDetailLevel1 = new AgentIncomeDetail();
						agentIncomeDetailLevel1
								.setAmout(advertStatistics.getSendRedEnvelopeAmount() * agent_income_1_2_2 / 100);
						agentIncomeDetailLevel1.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
						agentIncomeDetailLevel1.setEnventName("代理商发布广告收益");
						agentIncomeDetailLevel1.setEnventId(advert.getId());
						agentIncomeDetailLevel1.setEventUserId(advert.getUserId());
						agentIncomeDetailLevel1.setUserId(userAgentLevel1.getContent().getUserId());
						agentIncomeDetailLevel1.setIncomeType((byte) 1);
						agentIncomeDetailLevel1.setIncomeDate(new Date());
						agentIncomeDetailLevel1.setCreateTime(new Date());
						agentIncomeService.addAgentIncomeDetail(agentIncomeDetailLevel1);
					}
				}

			}

			// 县级
			if (sourceAgentLevel == 3) {

				AgentIncomeDetail agentIncomeDetail = new AgentIncomeDetail();
				agentIncomeDetail.setAmout(advertStatistics.getSendRedEnvelopeAmount() * agent_income_3_1 / 100);
				agentIncomeDetail.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
				agentIncomeDetail.setEnventName("代理商发布广告收益");
				agentIncomeDetail.setEnventId(advert.getId());
				agentIncomeDetail.setEventUserId(advert.getUserId());
				agentIncomeDetail.setUserId(advert.getUserId());
				agentIncomeDetail.setIncomeType((byte) 1);
				agentIncomeDetail.setUserId(userAgentResult.getContent().getUserId());
				agentIncomeDetail.setIncomeDate(new Date());
				agentIncomeDetail.setCreateTime(new Date());
				agentIncomeService.addAgentIncomeDetail(agentIncomeDetail);
				if (userAgentResult.getContent().getParentId() > 0) {

					Map<String,Object> agentMapLevel2 = new HashMap<String,Object>();
					agentMapLevel2.put("userId", userAgentResult.getContent().getParentId());
					agentMapLevel2.put("state", 1);
					UserResult<UserAgent> userAgentLevel2 = userAgentService.selectUserAgent(agentMapLevel2);

					if (userAgentLevel2.getState() == 1 && userAgentLevel2.getContent().getAgentLevel() == 2) {
						AgentIncomeDetail agentIncomeDetailLevel2 = new AgentIncomeDetail();
						agentIncomeDetailLevel2
								.setAmout(advertStatistics.getSendRedEnvelopeAmount() * agent_income_2_2_3 / 100);
						agentIncomeDetailLevel2.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
						agentIncomeDetailLevel2.setEnventName("代理商发布广告收益");
						agentIncomeDetailLevel2.setEnventId(advert.getId());
						agentIncomeDetailLevel2.setUserId(userAgentLevel2.getContent().getUserId());
						agentIncomeDetailLevel2.setEventUserId(advert.getUserId());
						agentIncomeDetailLevel2.setIncomeType((byte) 1);
						agentIncomeDetailLevel2.setIncomeDate(new Date());
						agentIncomeDetailLevel2.setCreateTime(new Date());
						agentIncomeService.addAgentIncomeDetail(agentIncomeDetailLevel2);

						if (userAgentLevel2.getContent().getParentId() > 0) {

							Map<String,Object> agentMapLevel1 = new HashMap<String,Object>();
							agentMapLevel1.put("userId", userAgentLevel2.getContent().getParentId());
							agentMapLevel1.put("state", 1);
							UserResult<UserAgent> userAgentLevel1 = userAgentService.selectUserAgent(agentMapLevel1);

							if (userAgentLevel1.getState() == 1 && userAgentLevel1.getContent().getAgentLevel() == 1) {
								AgentIncomeDetail agentIncomeDetailLevel1 = new AgentIncomeDetail();
								agentIncomeDetailLevel1.setAmout(
										advertStatistics.getSendRedEnvelopeAmount() * agent_income_1_2_3 / 100);
								agentIncomeDetailLevel1.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
								agentIncomeDetailLevel1.setEnventName("代理商发布广告收益");
								agentIncomeDetailLevel1.setEnventId(advert.getId());
								agentIncomeDetailLevel1.setEventUserId(advert.getUserId());
								agentIncomeDetailLevel1.setUserId(userAgentLevel1.getContent().getUserId());
								agentIncomeDetailLevel1.setIncomeType((byte) 1);
								agentIncomeDetailLevel1.setIncomeDate(new Date());
								agentIncomeDetailLevel1.setCreateTime(new Date());
								agentIncomeService.addAgentIncomeDetail(agentIncomeDetailLevel1);
							}

						}
					} else if (userAgentLevel2.getState() == 1 && userAgentLevel2.getContent().getAgentLevel() == 1) {

						AgentIncomeDetail agentIncomeDetailLevel1 = new AgentIncomeDetail();
						agentIncomeDetailLevel1.setAmout(advertStatistics.getSendRedEnvelopeAmount()
								* (agent_income_1_2_3 + agent_income_2_2_3) / 100);
						agentIncomeDetailLevel1.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
						agentIncomeDetailLevel1.setEnventName("代理商发布广告收益");
						agentIncomeDetailLevel1.setEnventId(advert.getId());
						agentIncomeDetailLevel1.setEventUserId(advert.getUserId());
						agentIncomeDetailLevel1.setUserId(userAgentLevel2.getContent().getUserId());
						agentIncomeDetailLevel1.setIncomeType((byte) 1);
						agentIncomeDetailLevel1.setIncomeDate(new Date());
						agentIncomeDetailLevel1.setCreateTime(new Date());
						agentIncomeService.addAgentIncomeDetail(agentIncomeDetailLevel1);
					}
				}

			}

		}
	}

	private void updateCalPublishIncome(Advert advert, AdvertStatistics advertStatistics, Long userId) {

		Map<String,Object> inviteMap = new HashMap<String,Object>();
		inviteMap.put("newUserId", advert.getUserId());
		inviteMap.put("state", 1);
		SocialResult<UserInvite> socialInviteResult = userInviteService.selectUserInvite(inviteMap);
		if (socialInviteResult.getState() == 1) {

			Map<String,Object> agentMap = new HashMap<String,Object>();
			agentMap.put("userId", socialInviteResult.getContent().getUserId());
			agentMap.put("state", "1");

			UserResult<UserAgent> userAgentResult = userAgentService.selectUserAgent(agentMap);

			if (userAgentResult.getState() == 1 && userAgentResult.getContent().getAgentLevel() != 4) {
				/** 发布广告代理添加收益 **/
				updateAgentCalPublishIncome(advert, socialInviteResult.getContent().getUserId(), advertStatistics,
						advert.getUserId());

			} else {

				UserResult<User> userResult = userVerifyService
						.selectUserByUserId(socialInviteResult.getContent().getUserId());
				if (userResult.getState() == 1 && userResult.getContent().getIncomeLevel() == 2) {
					UserIncome userIncome = new UserIncome();
					userIncome.setAmout((int) (advertStatistics.getSendRedEnvelopeAmount() * 0.05));
					userIncome.setSourceAmount(advertStatistics.getSendRedEnvelopeAmount());
					userIncome.setEnventId(advert.getId());
					userIncome.setEnventName("邀请好友发布广告收益");
					userIncome.setEventUserId(userId);
					userIncome.setUserId(socialInviteResult.getContent().getUserId());
					userIncome.setIncomeType((byte) 2);
					userIncome.setIncomeDate(new Date());
					userIncomeService.addUserIncome(userIncome);
				}
			}
		}
	}

	@Override
	public AdvertResult<RedEnvelope> updateAdvertAwart(Advert advert, User user) {
		List<String> advertLocks = new ArrayList<String>();
		AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
		try {
			// String advertLock = USER_ADVERT_REDENVELOPE_LOCK +
			// advert.getId();
			String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + advert.getId();// modify
																				// by
																				// zhangkun
																				// 应该锁定广告而不是针对红包
			advertLocks.add(advertLock);
			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {
				Map<String,Object> redMap = new HashMap<String,Object>();
				redMap.put("advertId", advert.getId());
				redMap.put("userId", user.getId());
				redMap.put("state", "0");
				AdvertResult<RedEnvelope> advertRedResult = selectRedEnvelope(redMap);
				if (null == advertRedResult) {
					throw new AdvertException(false,
							String.format("用户(userId=%s) 没有领取该广告(advertId=%s)！ ", user.getId(), advert.getId()));
				}
				RedEnvelope redEnvelope = advertRedResult.getContent();
				/* debug */
				if (redEnvelope != null) {
					logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>message { " + "redEnvelopeId : "
							+ redEnvelope.getId() + "amount : " + redEnvelope.getAmout());
				}
				if (advertRedResult.getState() == 1 && redEnvelope.getState() == 0) {
					RedEnvelope tempRedEnvelope = new RedEnvelope();
					tempRedEnvelope.setId(redEnvelope.getId());
					tempRedEnvelope.setAwardTime(new Date());
					// 红包金额计算
					int amount = calRedEnvelope(advert, user);
					if (amount > 0) {
						tempRedEnvelope.setOriginalAmout(amount);
						tempRedEnvelope.setAmout((int) (amount * 0.8));
						tempRedEnvelope.setState((byte) 1);
						updateRedEnvelope(tempRedEnvelope);

						updateAdvertStatisticsAwart(advert, amount, user);

						UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
						// userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance()
						// + redEnvelope.getAmout());
						userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + (int) (amount * 0.8));
						userAssets.setTableNum(user.getTableNum());
						userVerifyService.updateUserAssets(userAssets);

						// 更新SystemData中的全网抢到红包总人数 加1 start
						Map<String,Object> systemData = new HashMap<String,Object>();
						systemData.put("key_name", "redEnvelopePersons");
						SystemDataVo sysDataVo = systemService.selectListSystemData(systemData).get(0);
						
						SystemData sysData = new SystemData();
						sysData.setId(sysDataVo.getId());
						sysData.setSysValue(sysDataVo.getValue() + 1);
						systemService.updateSystemData(sysData);
						// 更新SystemData中的全网抢到红包总人数 加1 end

						advertResult.setContent(selectRedEnvelope(redEnvelope.getId()).getContent());
						// advertResult.setContent(redEnvelopeMapper.selectByPrimaryKey(redEnvelope.getId()));
						advertResult.setState(1);

						/** 接收广告添加收益 **/
						Map<String,Object> inviteMap = new HashMap<String,Object>();
						inviteMap.put("newUserId", user.getId());
						inviteMap.put("state", 1);
						
						//接收优惠券start
						int advertType = advert.getAdvertType();
						if(advertType == 1 || advertType == 3){
							businessCouponService.addRobBusinessCoupon(user.getId(), advert.getId());
						}
						//接收优惠券end
						
						// 查询邀请我的用户
						SocialResult<UserInvite> socialInviteResult = userInviteService.selectUserInvite(inviteMap);
						if (socialInviteResult.getState() == 1) {
							UserResult<User> userResult = userVerifyService
									.selectUserByUserId(socialInviteResult.getContent().getUserId());
							if (userResult.getState() == 1 && (userResult.getContent().getIncomeLevel() == 1
									|| userResult.getContent().getIncomeLevel() == 2)) {
								UserIncome userIncome = new UserIncome();
								// userIncome.setAmout((int)
								// (redEnvelope.getOriginalAmout() * 0.05));
								userIncome.setAmout((int) (amount * 0.05));
								// userIncome.setSourceAmount(redEnvelope.getOriginalAmout());
								userIncome.setSourceAmount(amount);
								userIncome.setEnventId(redEnvelope.getId());
								userIncome.setEventUserId(user.getId());
								userIncome.setEnventName("邀请好有领取红包收益");
								userIncome.setUserId(socialInviteResult.getContent().getUserId());
								userIncome.setIncomeType((byte) 1);
								userIncome.setIncomeDate(new Date());
								userIncomeService.addUserIncome(userIncome);
							}
						}
					} else {
						redEnvelope.setState((byte) 2);
						logger.debug("领取的红包金额异常");
					}
				}
			}
		} catch (RedEnvelopeException redEx) {
			logger.error("修改红包额和状态发生异常");
			throw redEx;
		} catch (AdvertException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new AdvertException(false, ex.getMessage(), ex);
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}
	
	@Override
	public AdvertResult<RedEnvelope> updateAdvertAwart3(Advert advert, User user) {
		List<String> advertLocks = new ArrayList<String>();
		AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
		try {
			String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + advert.getId();// modify
																				// by
																				// zhangkun
																				// 应该锁定广告而不是针对红包
			advertLocks.add(advertLock);
			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {
				advert = advertMapper.selectByPrimaryKey(advert.getId());
				Map<String,Object> redMap = new HashMap<String,Object>();
				redMap.put("advertId", advert.getId());
				redMap.put("userId", user.getId());
				redMap.put("state", AdvertHelper.RESULT_STATE_0.getMarker());
				AdvertResult<RedEnvelope> advertRedResult = selectRedEnvelope(redMap);
				//广告福袋类型
				int advertType = advert.getAdvertType().intValue();
				if(advertType == AdvertHelper.ADVERT_TYPE_11.getMarker().intValue()){
					//调用拆运营活动福袋方法
					return this.addFetchActivity(user.getId(),advert.getId());
				}else{
					if (null == advertRedResult.getContent()) {
						throw new AdvertException(false,
								String.format("用户(userId=%s) 没有领取该广告(advertId=%s)！ ", user.getId(), advert.getId()));
					}
					RedEnvelope redEnvelope = advertRedResult.getContent();
					/* debug */
					if (redEnvelope != null) {
						logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>message { " + "redEnvelopeId : "
								+ redEnvelope.getId() + "amount : " + redEnvelope.getAmout());
					}
					if (advertRedResult.getState() == 1 && redEnvelope.getState() == 0) {
						RedEnvelope tempRedEnvelope = new RedEnvelope();
						tempRedEnvelope.setId(redEnvelope.getId());
						tempRedEnvelope.setAwardTime(new Date());
						// 红包金额计算
						int amount = calRedEnvelope3(advert, user);
						if (amount > 0) {
							
							// 增加捐赠扣除功能，当抢到红包金额大于1分时扣除1分钱。
							if(amount > 1 && user.getDonateFlag().intValue() == 1){
								UserDonationRecord userDonationRecord = new UserDonationRecord();
								userDonationRecord.setUserId(user.getId());
								userDonationRecord.setMoney(1);
								userDonationRecord.setRelationId(advert.getId());
								userDonationRecord.setCreateTime(new Date());
								userDonationRecordMapper.insertSelective(userDonationRecord);
								amount = amount - 1;
							}
							
							tempRedEnvelope.setOriginalAmout(amount);
							tempRedEnvelope.setAmout((int) (amount));
							tempRedEnvelope.setState((byte) 1);
							updateRedEnvelope(tempRedEnvelope);

							updateAdvertStatisticsAwart3(advert, amount, user);

							UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
							userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + amount);
							userAssets.setTableNum(user.getTableNum());
							userVerifyService.updateUserAssets(userAssets);

							// 更新SystemData中的全网抢到红包总人数 加1 start
							Map<String,Object> systemData = new HashMap<String,Object>();
							systemData.put("key_name", "redEnvelopePersons");
							SystemDataVo sysDataVo = systemService.selectListSystemData(systemData).get(0);
							
							SystemData sysData = new SystemData();
							sysData.setId(sysDataVo.getId());
							sysData.setSysValue(sysDataVo.getValue() + 1);
							systemService.updateSystemData(sysData);
							// 更新SystemData中的全网抢到红包总人数 加1 end

							advertResult.setContent(selectRedEnvelope(redEnvelope.getId()).getContent());
							advertResult.setState(1);

							/** 接收广告添加收益 **/
							Map<String,Object> inviteMap = new HashMap<String,Object>();
							inviteMap.put("newUserId", user.getId());
							inviteMap.put("state", 1);
							
							//接收优惠券start
							if(advertType == 1){
								RedEnvelope redEnvelopeCoupon = new RedEnvelope();
								redEnvelopeCoupon.setAdvertId(advert.getId());
								redEnvelopeCoupon.setPublisherId(advert.getUserId());
								redEnvelopeCoupon.setUserId(user.getId());
								redEnvelopeCoupon.setAmout( 0);
								redEnvelopeCoupon.setCreateTime(new Date());
								redEnvelopeCoupon.setUserAvatar(user.getAvatar());
								redEnvelopeCoupon.setUserSex((byte)user.getSex().intValue());
								redEnvelopeCoupon.setUserNickName(user.getNickName());
								redEnvelopeCoupon.setAdvertLogo("");
								redEnvelopeCoupon.setAdverName(advert.getAdvertName());
								redEnvelopeCoupon.setState((byte)1);
								//金额有钱的是红包记录		金额没有钱的是优惠券
								redEnvelopeCoupon.setOriginalAmout( 0);
								//0是红包  1是优惠券  2接力福袋  3公益
								redEnvelopeCoupon.setType( 1);
								//领取优惠券在没有使用之前是没有奖励时间的，该奖励时间需要在使用的时候才会生成。用于查询核销人数的
								redEnvelopeCoupon.setAwardTime(null);
								redEnvelopeCoupon.setBirthday(user.getBirthday());
								redEnvelopeMapper.insert(redEnvelopeCoupon);
							}
							//接收优惠券end
							
							// 查询邀请我的用户
							SocialResult<UserInvite> socialInviteResult = userInviteService.selectUserInvite(inviteMap);
							if (socialInviteResult.getState() == 1) {
								UserResult<User> userResult = userVerifyService
										.selectUserByUserId(socialInviteResult.getContent().getUserId());
								if (userResult.getState() == 1 && (userResult.getContent().getIncomeLevel() == 1
										|| userResult.getContent().getIncomeLevel() == 2)) {
									UserIncome userIncome = new UserIncome();
									if(amount >= 10000){ //抢到金额大于等于100元时，好友获得同等金额收益。适用于3.0版本
										userIncome.setAmout((int) (amount));
										//写系统消息
										String msg = "号外、号外，您邀请的好友"+user.getNickName()+"获得了" + amount/100 +"元福袋，为了感谢您的支持，您也将获得" + amount/100 +"元福袋，快去您的零钱里面看看吧。";
										SystemMessage systemMessage = new SystemMessage();
										systemMessage.setContent(msg);
										systemMessage.setUserId(socialInviteResult.getContent().getUserId());
										systemMessage.setCreateTime(new Date());
										systemMessageMapper.insertSelective(systemMessage);
									}else{
										userIncome.setAmout((int) (amount * 0.05));
									}
									userIncome.setSourceAmount(amount);
									userIncome.setEnventId(redEnvelope.getId());
									userIncome.setEventUserId(user.getId());
									userIncome.setEnventName("邀请好有领取红包收益");
									userIncome.setUserId(socialInviteResult.getContent().getUserId());
									userIncome.setIncomeType((byte) 1);
									userIncome.setIncomeDate(new Date());
									userIncomeService.addUserIncome(userIncome);
								}
							}
						} else {
							redEnvelope.setState((byte) 2);
							logger.debug("领取的红包金额异常");
						}
					}
				}
			}
		} catch (RedEnvelopeException redEx) {
			logger.error("修改红包额和状态发生异常");
			throw redEx;
		} catch (AdvertException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new AdvertException(false, ex.getMessage(), ex);
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}

	@Override
	public AdvertResult<Integer> updateAdvertExpired(Advert advert) {
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();
		List<String> advertLocks = new ArrayList<String>();
		// 3600*48*1000-5*1000 48小时
		if (advert.getEndTime().compareTo(new Date(new Date().getTime() - 60 * 1000)) >= 0) {

			advertResult.setState(2);
			return advertResult;
		}

		String advertLock = USER_ADVERT_EXPIRE_CLOSE_LOCK + advert.getId();
		advertLocks.add(advertLock);
		try {
			if (redisLockHandler.tryLock(advertLock)) {

				if (advert.getState() != 1) {

					advertResult.setState(3);
					return advertResult;
				}

				logger.info("updateAdvertExpired  1-----advertAdvertStatisticsResult advertId=" + advert.getId());
				AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(
						advert.getId());

				int redec = advert.getRedEnvelopeAmount()
						- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeAmount();
				int redcount = advert.getRedEnvelopeCount()
						- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeCount();
				if (redec > 0 && redcount > 0) {

					UserResult<User> userResult = userVerifyService.selectUserByUserId(advert.getUserId());
					UserAssets userAssets = userVerifyService.getUserAssetsByUser(userResult.getContent());
					userAssets.setCashBalance(userAssets.getCashBalance() + redec);
					userAssets.setTableNum(userVerifyService.userPartTableNum(userResult.getContent().getPhone()));

					logger.info("updateAdvertExpired  2-----advertAdvertStatisticsResult advertId=" + advert.getId());
					userVerifyService.updateUserAssets(userAssets);

					UserCashFlow userCashFlow = new UserCashFlow();

					userCashFlow.setCashFlowTitle(advert.getAdvertName());

					userCashFlow.setPrice(redec);
					String tableNum = DateUtils.getDateStringYYYY(new Date());
					userCashFlow.setTableNum(tableNum);
					userCashFlow.setBalance(userAssets.getCashBalance());
					userCashFlow.setUserId(advert.getUserId());
					userCashFlow.setTriggerNo(advert.getId() + "");

					logger.info("updateAdvertExpired  3----advertAdvertStatisticsResult advertId=" + advert.getId());
					userCashFlowMapper.insertSelective(userCashFlow);

					logger.info("updateAdvertExpired  4-----advertAdvertStatisticsResult advertId=" + advert.getId());
					advert.setState((byte) 5);
					updateAdvert(advert);

					/** 接收广告添加收益 **/
					updateCalPublishIncome(advert, advertAdvertStatisticsResult.getContent(), advert.getUserId());

				}
				if (redec == 0 && redcount == 0) {
					advert.setState((byte) 3);
					updateAdvert(advert);
					/** 接收广告添加收益 **/
					updateCalPublishIncome(advert, advertAdvertStatisticsResult.getContent(), advert.getUserId());

				}
				advertResult.setState(1);

			}
		} catch (Exception e) {

			logger.equals("updateAdvertexpired advertId=" + advert.getId() + ",e=" + e);
			throw e;
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		return advertResult;
	}

	@Override
	public AdvertResult<Integer> updateCheckFailAdvert(Advert advert) {
		List<String> advertLocks = new ArrayList<String>();
		AdvertResult<Integer> advertResult = new AdvertResult<Integer>();
		try {

			String advertLock = USER_ADVERT_REDENVELOPE_LOCK + advert.getId();
			advertLocks.add(advertLock);

			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {

				AdvertResult<Advert> advertResult2 = selectAdvert(advert.getId());
				AdvertStatistics advertStatistics = new AdvertStatistics();
				if (advertResult2.getContent().getState() == 1) {

					AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(
							advert.getId());

					int amoutd = advertResult2.getContent().getRedEnvelopeAmount()
							- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeAmount();

					int countd = advertResult2.getContent().getRedEnvelopeAmount()
							- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeCount();
					if (amoutd > 0 && countd > 0) {

						advertStatistics.setSendRedEnvelopeAmount(amoutd);
						advertStatistics.setSendRedEnvelopeCount(countd);
						advertResult.setContent(1);
						advert.setState((byte) 4);
						updateAdvert(advert);
					}
					if(advert.getAdvertType().intValue() == 9){
						advertResult.setContent(1);
						advert.setState((byte) 4);
						updateAdvert(advert);
					}
					advertResult.setContent(0);
					advertResult.setState(advertResult2.getContent().getState());

				} else if (advertResult2.getContent().getState() == 3) {
					advert.setState((byte) 4);
					updateAdvert(advert);
					advertResult.setContent(1);
					advertResult.setState(1);
				}

			}
		} catch (Exception e) {

			logger.equals("updateAdvertexpired advertId=" + advert.getId() + ",e=" + e);
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		return advertResult;
	}

	@Override
	public AdvertResult<AdvertStatistics> updateStopAdvert(Advert advert) {
		List<String> advertLocks = new ArrayList<String>();
		AdvertResult<AdvertStatistics> advertResult = new AdvertResult<AdvertStatistics>();
		try {

			String advertLock = USER_ADVERT_REDENVELOPE_LOCK + advert.getId();
			advertLocks.add(advertLock);

			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {

				AdvertResult<Advert> advertResult2 = selectAdvert(advert.getId());
				AdvertStatistics advertStatistics = new AdvertStatistics();
				if (advertResult2.getContent().getState() == 1) {

					AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(
							advert.getId());

					int amoutd = advertResult2.getContent().getRedEnvelopeAmount()
							- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeAmount();

					int countd = advertResult2.getContent().getRedEnvelopeAmount()
							- advertAdvertStatisticsResult.getContent().getSendRedEnvelopeCount();

					if (amoutd > 0 && countd > 0) {

						advertStatistics.setSendRedEnvelopeAmount(amoutd);
						advertStatistics.setSendRedEnvelopeCount(countd);
						advertResult.setContent(advertStatistics);
						advert.setState((byte) 6);
						updateAdvert(advert);
					}
					advertResult.setState(advertResult2.getContent().getState());

				}

			}
		} catch (Exception e) {

			logger.equals("updateAdvertexpired advertId=" + advert.getId() + ",e=" + e);
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		return advertResult;
	}

	@Override
	public List<RedEnvelope> searchWaitUnFreezeAdvertList(int limit) {
		if (limit > 1000) {
			limit = 1000;
		}
		return redEnvelopeMapper.searchWaitUnFreezeAdvertList(limit);
	}

	@Override
	public List<AdvertStatistics> searchAdvertStatisticsList(int limit) {
		if (limit > 1000) {
			limit = 1000;
		}
		return null;
	}

	@Override
	public void changeRedEnvelopeUnfreeze(List<RedEnvelope> list) throws AdvertException, RedEnvelopeException {
		// TODO Auto-generated method stub
		if (list != null) {
			logger.info("--------------解冻广告并释放未领取的红包----start");
			for (RedEnvelope envelope : list) {
				// 释放红包对象start
				RedEnvelope temp = new RedEnvelope();
				temp.setId(envelope.getId());
				temp.setState((byte) -1); // 系统解冻
				// 广告解冻次数自增
				temp.setAdvertUnfreezeCount(envelope.getAdvertUnfreezeCount() + 1);
				if (redEnvelopeMapper.updateByPrimaryKeySelective(temp) < 0) {
					// RedEnvelope temp = new RedEnvelope();
					temp.setId(envelope.getId());
					// 广告解冻次数自增
					temp.setAdvertUnfreezeCount(envelope.getAdvertUnfreezeCount() + 1);
					if (redEnvelopeMapper.updateByPrimaryKeySelective(temp) < 0) {
						throw new RedEnvelopeException(false, String.format("释放广告(%s)的红包失败", envelope.getAdvertName()));
					}
				}

				// 释放红包对象end

				// 广告统计start
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("advertId", envelope.getAdvertId());
				AdvertStatistics statistics = advertStatisticsMapper.selectModel(paramMap);
				if (statistics == null) {
					throw new AdvertException(false, String.format("待解冻广告(%s)统计数据不存在", envelope.getAdvertId()));
				}
				AdvertStatistics tempSta = new AdvertStatistics();
				tempSta.setId(statistics.getId());
				tempSta.setFreezeEnvelopeCount(statistics.getFreezeEnvelopeCount() - 1);

				if (advertStatisticsMapper.updateByPrimaryKeySelective(tempSta) < 0) {
					throw new AdvertException(false,
							String.format("解冻广告(%s)失败,广告id(%s)", envelope.getAdvertName(), envelope.getAdvertId()));
				}
				// 广告统计end
			}
			logger.info("--------------解冻广告并释放未领取的红包----end");
		}
	}

	@Override
	public void changeAdvertUnFreeze(RedEnvelope envelope) {
		if (envelope == null || envelope.getId() == null) {
			throw new AdvertException(false, "待解冻的广告不存在");
		}

		// add by zhangkun start
		// List<String> advertFetchLocks = new
		// ArrayList<String>();//存储所有加锁的String拼接值，保证最后可以逐一释放

		String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + envelope.getAdvertId();// 此字符串拼接值一定要和用户抢广告时候加锁的值一样
		if (redisLockHandler.tryLock(advertLock) == false) {
			// 若当前广告被加锁了，证明有用户正在看广告，
			// 此时不能对这条广告对应的数据进行修改(即解冻)否则存在并发问题(导致advertStatistics的freezeEnvelopeCount的值不稳定，
			// 进而导致产生的红包数会大于原本广告的红包数)，
			// 则跳过当前广告
			logger.info("广告 : " + envelope.getAdvertName() + " 已被其他用户锁定，不能进行解冻操作！");
			return;
		}

		// advertFetchLocks.add(advertLock);//加入锁池
		// add by zhangkun end

		{// 修改广告阅读表
			RedEnvelope temp = new RedEnvelope();
			temp.setId(envelope.getId());
			temp.setState((byte) -1); // 系统解冻
			// 广告解冻次数自增
			temp.setAdvertUnfreezeCount(envelope.getAdvertUnfreezeCount() + 1);
			redEnvelopeMapper.updateByPrimaryKeySelective(temp);
		}
		{// 修改广告统计表
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("advertId", envelope.getAdvertId());

			// 获取广告统计
			AdvertStatistics statistics = advertStatisticsMapper.selectModel(paramMap);

			if (statistics == null) {
				throw new AdvertException(false, String.format("待解冻广告(%s)统计数据不存在", envelope.getAdvertId()));
			}

			// 释放广告统计：减一
			AdvertStatistics temp = new AdvertStatistics();
			temp.setId(statistics.getId());
			temp.setFreezeEnvelopeCount(statistics.getFreezeEnvelopeCount() - 1);
			advertStatisticsMapper.updateByPrimaryKeySelective(temp);
		}

		/* add by zhangkun start 20160922 */
		// 释放所有锁池中的锁
		// for (String lock : advertFetchLocks) {
		// redisLockHandler.unLock(lock);
		// }
		redisLockHandler.unLock(advertLock);
		/* add by zhangkun end 20160922 */
	}

	@Override
	public void changeUnFreezeCountAdd(RedEnvelope envelope) {
		if (envelope == null || envelope.getId() == null) {
			throw new AdvertException(false, "阅读广告记录不存在");
		}
		RedEnvelope temp = new RedEnvelope();
		temp.setId(envelope.getId());
		// 广告解冻次数自增
		temp.setAdvertUnfreezeCount(envelope.getAdvertUnfreezeCount() + 1);
		redEnvelopeMapper.updateByPrimaryKeySelective(temp);
	}

	@Override
	public Map<String, Object> getAdvertSum() {
		return advertMapper.getAdvertSumInfo();
	}

	/**
	 * 广告统计递增
	 * 
	 * @param advert
	 * @param amount
	 * @param user
	 */
	@Override
	public void updateAdvertStatisticsAwart(Advert advert, Integer amount, User user) {
		AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(advert.getId());
		AdvertStatistics aAdvertStatistics = advertAdvertStatisticsResult.getContent();

		aAdvertStatistics.setSendRedEnvelopeAmount(aAdvertStatistics.getSendRedEnvelopeAmount() + amount);

		aAdvertStatistics.setSendRedEnvelopeCount(aAdvertStatistics.getSendRedEnvelopeCount() + 1);

		// 领取广告，广告冻结数 递减
		aAdvertStatistics.setFreezeEnvelopeCount(aAdvertStatistics.getFreezeEnvelopeCount() - 1);

		if (advert.getRedEnvelopeCount() == aAdvertStatistics.getSendRedEnvelopeCount()) {
			// 更新广告状态为：已完成
			advert.setState((byte) 3);
			updateAdvert(advert);

			/** 发布广告添加收益 **/
			updateCalPublishIncome(advert, advertAdvertStatisticsResult.getContent(), user.getId());
		}
		updateAdvertStatistics(aAdvertStatistics);
	}
	
	/**
	 * 广告统计递增 3.0
	 * 
	 * @param advert
	 * @param amount
	 * @param user
	 */
	private void updateAdvertStatisticsAwart3(Advert advert, Integer amount, User user) {
		AdvertResult<AdvertStatistics> advertAdvertStatisticsResult = selectAdvertStatisticsByAdvertId(advert.getId());
		AdvertStatistics aAdvertStatistics = advertAdvertStatisticsResult.getContent();

		aAdvertStatistics.setSendRedEnvelopeAmount(aAdvertStatistics.getSendRedEnvelopeAmount() + amount);

		aAdvertStatistics.setSendRedEnvelopeCount(aAdvertStatistics.getSendRedEnvelopeCount() + 1);

		// 领取广告，广告冻结数 递减
		aAdvertStatistics.setFreezeEnvelopeCount(aAdvertStatistics.getFreezeEnvelopeCount() - 1);
		//0普通福袋 1优惠券福袋 3优惠券圈子  6定时广告  8定时活动广告(官方)，这些福袋类型在抢完红包后需要更新福袋状态
		int advetType = advert.getAdvertType().intValue();
		if(advetType == 0 || advetType == 1 || advetType == 3
				|| advetType == 6 || advetType == 8){
			if (aAdvertStatistics.getSendRedEnvelopeAmount() >= advert.getRedEnvelopeAmount() 
					|| aAdvertStatistics.getSendRedEnvelopeCount() >= advert.getRedEnvelopeCount()) {
				// 更新广告状态为：已完成
				advert.setState((byte) 3);
				updateAdvert(advert);
			}
		}
		updateAdvertStatistics(aAdvertStatistics);
	}

	@Override
	public int checkRedEnvelopeCondition(Advert advert, AdvertStatistics advertStatistics) {
		int check = 0;
		/*if (advert.getState() != 1 || advertStatistics.getSendRedEnvelopeAmount() >= advert.getRedEnvelopeAmount()) {
			return check;
		}*/
		if (advertStatistics.getSendRedEnvelopeAmount() >= advert.getRedEnvelopeAmount()) {
			return check;
		}
		check = 1;
		return check;
	}
	
	@Override
	public AdvertResult<ListRange<Advert>> selectListAdvertHomePage(Map<String, Object> map) {
		AdvertResult<ListRange<Advert>> advertResult = new AdvertResult<ListRange<Advert>>();
		ListRange<Advert> listRange = new ListRange<Advert>();
		int count = advertMapper.selectHomeCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(advertMapper.selectHomePageList(map));
		advertResult.setContent(listRange);
		advertResult.setState(1);
		return advertResult;
	}

	@Override
	public ListRange<RedEnvelopeTop> selectProListPageRedEnvelopeTop3(Long userId) {
		ListRange<RedEnvelopeTop> listRange = new ListRange<RedEnvelopeTop>();
		
		RedEnvelopeTop envelopeTop = null;
//		List<RedEnvelopeTop> redEnvelopeTops = redEnvelopeMapper.selectRedEnvelopeTop3();
		List<RedEnvelopeTop> redEnvelopeTops = redEnvelopeMapper.selectTopRed50Procedure();
		RedEnvelopeTop redEnvelopeTopOneself = redEnvelopeMapper.selectRedEnvelopeOneself(userId);
		
		List<RedEnvelopeTop> res = new ArrayList<RedEnvelopeTop>();
		if(redEnvelopeTopOneself == null) {
			redEnvelopeTopOneself = new RedEnvelopeTop();
			redEnvelopeTopOneself.setUserId(String.valueOf(userId));
			redEnvelopeTopOneself.setAmountSum(0);
			
			redEnvelopeTopOneself.setTopNum(100000);
		}
		/*else if((redEnvelopeTops == null || redEnvelopeTops.size() <= 0) && redEnvelopeTopOneself != null) {
			
		}*/
		
		for(RedEnvelopeTop redEnvelopeTop:redEnvelopeTops) {
			
			logger.info("查询排行榜的用户：---------->"+redEnvelopeTop);
			logger.info("user data field is : ------------>"+redEnvelopeTop.toString());
			
			Map<String, String> userPartMap = new HashMap<String, String>();
			userPartMap.put("mapprerValue", String.valueOf(redEnvelopeTop.getUserId()));
			Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
			
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("userId", redEnvelopeTop.getUserId());
			userMap.put("tableNum", tableIndex);
			envelopeTop = userMapper.getUserRankInfo(userMap);
			envelopeTop.setTopNum(redEnvelopeTop.getTopNum());
			envelopeTop.setAmountSum(redEnvelopeTop.getAmountSum());
			res.add(envelopeTop);
		}
		
		
		//查询自己的信息
		//envelopeTop = new RedEnvelopeTop();
		
		Map<String, String> userPartMap = new HashMap<String, String>();
		userPartMap.put("mapprerValue", String.valueOf(redEnvelopeTopOneself.getUserId()));
		UserPart userPart = userPartMapper.selectModel(userPartMap);
		if(userPart == null) {
			logger.info("用户根本就不存在!");
			return null;
		}
		Integer tableIndex = userPart.getNum();
		
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put("userId", redEnvelopeTopOneself.getUserId());
		userMap.put("tableNum", tableIndex);
		envelopeTop = userMapper.getUserRankInfo(userMap);
		
		if(envelopeTop == null) {
			envelopeTop = new RedEnvelopeTop();
		}
		
		envelopeTop.setTopNum(redEnvelopeTopOneself.getTopNum());
		envelopeTop.setAmountSum(redEnvelopeTopOneself.getAmountSum());
		res.add(envelopeTop);
		
		listRange.setData(res);
		return listRange;
	}

	@Override

	public int updateAdvertCommentSize(Advert advert) {
		// TODO Auto-generated method stub
		if(advert.getCommentSize() != null){
			advert.setCommentSize(advert.getCommentSize() + 1);
		}else{
			advert.setCommentSize(1);
		}
		
		return advertMapper.updateByPrimaryKeySelective(advert);
	}

	@Override
	public Advert selectByAdvertId(Long id) {
		// TODO Auto-generated method stub
		return advertMapper.selectByAdvertId(id);
	}

	public RelayLuckyBagVo updateRelayLuckyBag(User user, Advert advert, int openAmount) {
		List<String> advertLocks = new ArrayList<String>();
		RelayLuckyBagVo relayLuckyBagVo = new RelayLuckyBagVo();
		try {
			String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + advert.getId();
			advertLocks.add(advertLock);
			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {
				advert = advertMapper.selectByPrimaryKey(advert.getId());
				/*查询当前用户是否已开过此接力福袋*/
				Map<String,Object> redMap = new HashMap<String,Object>();
				redMap.put("advertId", advert.getId());
				redMap.put("userId", user.getId());
				AdvertResult<RedEnvelope> advertRedResult = selectRedEnvelope(redMap);
				if (null != advertRedResult && advertRedResult.getContent() != null) {
					relayLuckyBagVo.setErrorCode(LuckyBagHelper.ERROR_CODE_500002.getMarker());
				}else{
					boolean flag = openRelayLuckyBag(advert);
					RedEnvelope redEnvelope = getRedEnvelope(advert, user, AdvertHelper.RED_ENVELOPE_TYPE_2.getMarker());
					Map<String, Object> map = new HashMap<>();
					map.put("advertId", advert.getId());
					AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(map);
					/*查询个人账户信息*/
					UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
					if(userAssets.getRedEnvelopeBalance().intValue() < openAmount){
						relayLuckyBagVo.setErrorCode(LuckyBagHelper.ERROR_CODE_500006.getMarker());
						return relayLuckyBagVo;
					}
					//开启成功
					if(flag){
						if(advertStatistics != null){
							int amount = advertStatistics.getSendRedEnvelopeAmount(); //累积金额
							int count = advertStatistics.getSendRedEnvelopeCount(); //参数人数
							/*插入红包信息*/
							redEnvelope.setAmout(amount);
							redEnvelope.setOriginalAmout(advert.getRedEnvelopeAmount());
							addRedEnvelope(redEnvelope);
							/*更新广告明细 */
							advertStatistics.setSendRedEnvelopeAmount(amount);
							advertStatistics.setSendRedEnvelopeCount(count + 1);
							advertStatisticsMapper.updateByPrimaryKeySelective(advertStatistics);
							/*更新个人账户信息*/
							userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + amount);
							userAssets.setTableNum(user.getTableNum());
							userVerifyService.updateUserAssets(userAssets);
							/*更新广告福袋为已完成，状态代码：3*/
							advert.setModifyTime(new Date());
							advert.setState((byte)LuckyBagHelper.ADVERT_STATE_3.getMarker().intValue());
							advertMapper.updateByPrimaryKeySelective(advert);
							/*返回app数据vo*/
							relayLuckyBagVo.setAmount(amount);
							relayLuckyBagVo.setNickName(user.getNickName());
							relayLuckyBagVo.setNum(count + 1);
							relayLuckyBagVo.setState(LuckyBagHelper.ADVERT_STATE_3.getMarker());
							relayLuckyBagVo.setOpenFlag(RelayLuckyBagHelper.OPEN_STATE_1.getMarker().intValue());
							relayLuckyBagVo.setErrorCode(1);
						}
					}else{ //开启失败
						//1插入红包信息 2更新广告明细 3更新个人账户信息
						if(advertStatistics != null){
							int amount = advertStatistics.getSendRedEnvelopeAmount(); //累积金额
							int count = advertStatistics.getSendRedEnvelopeCount(); //参数人数
							int newAmount = amount + openAmount;
							/*插入红包信息*/
							redEnvelope.setAmout(0 - openAmount);
							redEnvelope.setOriginalAmout(advert.getRedEnvelopeAmount());
							addRedEnvelope(redEnvelope);
							/*更新广告明细 */
							advertStatistics.setSendRedEnvelopeAmount(newAmount);
							advertStatistics.setSendRedEnvelopeCount(count + 1);
							advertStatisticsMapper.updateByPrimaryKeySelective(advertStatistics);
							/*更新个人账户信息*/
							userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() - openAmount);
							userAssets.setTableNum(user.getTableNum());
							userVerifyService.updateUserAssets(userAssets);
							/*返回app数据vo*/
							relayLuckyBagVo.setAmount(newAmount);
							relayLuckyBagVo.setNickName(user.getNickName());
							relayLuckyBagVo.setNum(count + 1);
							relayLuckyBagVo.setState(LuckyBagHelper.ADVERT_STATE_1.getMarker());
							relayLuckyBagVo.setOpenFlag(RelayLuckyBagHelper.OPEN_STATE_0.getMarker().intValue());
							relayLuckyBagVo.setErrorCode(1);
						}
					}
					
					// 更新SystemData中的全网抢到红包总人数 加1 start
					Map<String,Object> systemData = new HashMap<String,Object>();
					systemData.put("key_name", "redEnvelopePersons");
					SystemDataVo sysDataVo = systemService.selectListSystemData(systemData).get(0);
					
					SystemData sysData = new SystemData();
					sysData.setId(sysDataVo.getId());
					sysData.setSysValue(sysDataVo.getValue() + 1);
					systemService.updateSystemData(sysData);
					// 更新SystemData中的全网抢到红包总人数 加1 end
				}
			}
		} catch (Exception ex) {
			relayLuckyBagVo.setErrorCode(LuckyBagHelper.ERROR_CODE_900100.getMarker());
			ex.printStackTrace();
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return relayLuckyBagVo;
	}
	
	//获取红包明细对象
	private RedEnvelope getRedEnvelope(Advert advert, User user, int redType){
		Date date = new Date();
		RedEnvelope redEnvelope = new RedEnvelope();
		redEnvelope.setAdverName(advert.getAdvertName());
		redEnvelope.setAdvertId(advert.getId());
		redEnvelope.setCreateTime(date);
		redEnvelope.setUserId(user.getId());
		redEnvelope.setPublisherId(advert.getUserId());
		redEnvelope.setAgeGroup(user.getAgeGroup());
		redEnvelope.setUserAvatar(user.getAvatar());
		redEnvelope.setUserNickName(user.getNickName());
		redEnvelope.setUserSex((byte)user.getSex().intValue());
		redEnvelope.setState((byte) 1);
		redEnvelope.setType(redType);
		redEnvelope.setAwardTime(date);
		return redEnvelope;
	}
	
	//计算是否开启成功(接力福袋)
	@SuppressWarnings("unused")
	private boolean openRelayLuckyBag(Advert advert){
		boolean flag = false;
		Map<String, Object> map = new HashMap<>();
		map.put("advertId", advert.getId());
		AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(map);
		if(advertStatistics != null){
			int openNum = advertStatistics.getSendRedEnvelopeCount(); //参与人数
			BigDecimal baseAmount = new BigDecimal(advert.getRedEnvelopeAmount()).divide(new BigDecimal(100)); //基础金额（单位:元）
			//BigDecimal amount = new BigDecimal(advertStatistics.getSendRedEnvelopeAmount()); //累积金额
			BigDecimal parameterOne = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_1.getMarker()); //开启参数一
			BigDecimal parameterTwo = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_2.getMarker(),  MathContext.DECIMAL32); //开启参数二
			BigDecimal parameterThree = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_3.getMarker()); //开启参数三
			BigDecimal parameterFour = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_4.getMarker()); //开启参数四
			BigDecimal parameterFive = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_5.getMarker()); //开启参数五
			BigDecimal parameterSix = new BigDecimal(RelayLuckyBagHelper.OPEN_PARAMETER_6.getMarker()); //开启参数六
			int fistNum = baseAmount.multiply(parameterFive).multiply(parameterTwo).intValue();
			int secondNum = baseAmount.multiply(parameterFive).intValue();
			double probabilityOne = parameterThree.divide(new BigDecimal(secondNum), 2, RoundingMode.DOWN).doubleValue(); //开启概率一
			double probabilityTwo = parameterFour.divide(new BigDecimal(secondNum), 2, RoundingMode.DOWN).doubleValue(); //开启概率二
			double probabilityThree = parameterSix.divide(new BigDecimal(secondNum), 2, RoundingMode.DOWN).doubleValue(); //开启概率三
			if(openNum < fistNum){
				List<Double> list = new ArrayList<Double>();
				list.add(probabilityOne);
				list.add(parameterThree.subtract(new BigDecimal(probabilityOne)).doubleValue());
				map.clear();
				map = PercentUtil.getRandomPercent(list);
				if((int)map.get("index") == 0){
					flag = true;
				}
			}else if ( openNum >= fistNum && openNum <= secondNum){
				List<Double> list = new ArrayList<Double>();
				list.add(probabilityTwo);
				list.add(parameterThree.subtract(new BigDecimal(probabilityTwo)).doubleValue());
				map.clear();
				map = PercentUtil.getRandomPercent(list);
				if((int)map.get("index") == 0){
					flag = true;
				}
			}else{
				List<Double> list = new ArrayList<Double>();
				list.add(probabilityThree);
				list.add(parameterThree.subtract(new BigDecimal(probabilityThree)).doubleValue());
				map.clear();
				map = PercentUtil.getRandomPercent(list);
				if((int)map.get("index") == 0){
					flag = true;
				}
			}
		}
		return flag;
	}

	@Override
	public AdvertResult<Advert> selectAdvertByMap(Map<String, Object> map) {
		AdvertResult<Advert> advertResult = new AdvertResult<Advert>();

		List<Advert> adverts = advertMapper.selectList(map);
		if (adverts != null && adverts.size() > 0) {
			advertResult.setContent(adverts.get(0));
			advertResult.setState(1);
		}

		return advertResult;
	}

	@Override
	public int selectViewTimesCount(Map<String, Object> map) {
		return advertMapper.selectViewTimesCount(map);
	}

	@Override
	public Result<ListRange<BannerConfig>> selectListBannerPage(Map<String, Object> map) {
		Result<ListRange<BannerConfig>> advertResult = new Result<ListRange<BannerConfig>>();
		ListRange<BannerConfig> listRange = new ListRange<BannerConfig>();
		int count = advertMapper.selectBannerCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(advertMapper.selectListBanner(map));
		advertResult.setContent(listRange);
		advertResult.setState(1);
		return advertResult;
	}

	@Override
	public List<BannerConfig> selectListAdvertBannerConfig(Map map) {
		// TODO Auto-generated method stub
		return advertMapper.selectListBanner(map);
	}
	
	public int saveOrUpdateBanner(BannerConfig bannerConfig) {
		int result = 0;
		Date date = new Date();
		Map<String, Object> map = new HashMap<>();
		map.put("bannerType", 1);
		map.put("flag", 1);
		List<BannerConfig> configs = advertMapper.selectListBanner(map);
		int size = configs.size();
		if(bannerConfig.getId() > 0){
			bannerConfig.setModifyTime(date);
			result = advertMapper.updateBannerByPrimaryKey(bannerConfig);
		}else{
			bannerConfig.setCreateTime(date);
			bannerConfig.setPicIndex(size + 1);
			result = advertMapper.insertBannerSelective(bannerConfig);
		}
		return result;
	}

	public AdvertResult<RedEnvelope> addFetchActivity(Long userId,Long advertId) {
		//List<String> advertAddLocks = new ArrayList<String>();
		try {
			AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
			
			/*//给用户加锁
			String userLock = USER_ADVERT_ADD_LOCK + userId;
			advertAddLocks.add(userLock);
			// 如果已经被锁，就直接返回
			if(!redisLockHandler.tryLock(userLock)) {
				advertResult.setState(0);
				logger.error("用户：" + userId + "被锁住");
				return advertResult;
			}
			
			//给广告加锁
			String advertLock = USER_ADVERT_ADD_LOCK + advertId;
			advertAddLocks.add(advertLock);
			// 如果已经被锁，就直接返回
			if(!redisLockHandler.tryLock(advertLock)) {
				advertResult.setState(0);
				logger.info("广告 ：" + advertId + "被锁住");
				return advertResult;
			}*/
			
			Map<String,Object> userPartMap = new HashMap<String,Object>();
			userPartMap.put("mapprerValue", userId);
			Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("id", userId);
			userMap.put("tableNum", tableIndex);
			User user = userMapper.selectModel(userMap);
			if(user == null) {
				logger.error("用户不存在!");
				return advertResult;
			}
			
			Map<String,Object> reMap = new HashMap<String,Object>();
			reMap.put("userId", userId);
			reMap.put("advertId", advertId);
			reMap.put("amout", 0);
			RedEnvelope ren = redEnvelopeMapper.selectModel(reMap);
			/*if(ren != null) {
				logger.error("userId ：" + userId + "用户已经抢过红包!");
				return advertResult;
			}*/
			
			Advert advert = advertMapper.selectAdvertByPrimaryKey(advertId);
			if(advert == null) {
				logger.error("广告不存在!");
				return advertResult;
			}
			
			//抢广告的明细
			/*Map<String,Object> redEnvelopeMap = new HashMap<String,Object>();
			redEnvelopeMap.put("userId", userId);
			List<RedEnvelope> redEnvelopes = redEnvelopeMapper.selectList(redEnvelopeMap);
			if(redEnvelopes == null || redEnvelopes.size() <= 0) {
				//如果没有抢过红包，就要加0.04块钱
				RedEnvelope re = new RedEnvelope();
				re.setAdvertId(advert.getId());
				re.setAdverName(advert.getAdvertName());
				re.setAmout(4);
				//不是奖品，只是红包
				re.setOriginalAmout(-1);
				re.setPublisherId(advert.getUserId());
				re.setAdverName(advert.getAdvertName());
				re.setAwardTime(new Date());
				re.setCrateTime(new Date());
				re.setUserId(user.getId());
				re.setUserAvatar(user.getAvatar());
				re.setUserSex(user.getAgeGroup());
				re.setUserNickName(user.getNickName());
				re.setState((byte)1);
				re.setBirthday(user.getBirthday());
				re.setAgeGroup(user.getAgeGroup());
				re.setType(0);
				redEnvelopeMapper.insert(re);
				
				//用户财富
				UserAssets userAssets = userAssetsService.selectUserAssets(userId);
				if(userAssets == null) {
					userAssets = new UserAssets();
					userAssets.setTableNum(tableIndex);
					userAssets.setUserId(userId);
					userAssets.setRedEnvelopeBalance(4);
					userAssetsService.addUserAssets(userAssets);
				} else {
					userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + 4);
					userAssetsService.updateUserAssets(userAssets);
				}
			}*/
			
			//抢iPhone7
			//活动配置
			Map<String,Object> activityConfigMap = new HashMap<String,Object>();
			activityConfigMap.put("advertId", advertId);
			activityConfigMap.put("order", "rank DESC");
			List<ActivityConfig> activityConfigs = activityConfigMapper.selectList(activityConfigMap);
			
			Map<Integer,String> rankActivityConfigMap = new HashMap<Integer,String>();
			for(ActivityConfig activityConfig:activityConfigs) {
				//奖品在领取完的条件是，有奖励时间
				rankActivityConfigMap.put(activityConfig.getRank(), activityConfig.getProbability());
			}
			return this.awardRankHandle(advert,user, rankActivityConfigMap);
		} catch (Exception e) {
			logger.info("抢iPhone7出现服务器异常!");
			e.printStackTrace();
			throw new BusinessException("10000", e.getMessage(), e);
		}
		/*finally {
			//释放所有的锁
			for(String lock : advertAddLocks) {
				redisLockHandler.unLock(lock);
			}
		}*/
		
	}
	/**
	 * 抢活动的记录
	 * @param rankActivityConfigMap		等级好奖励的目标点对应起来的
	 * @param fetchRedEnvelopeCount		当前抢红包的人数
	 * @param advert					广告
	 */
	private AdvertResult<RedEnvelope> fetchActiveRecord(Map<Integer,String> rankActivityConfigMap,Integer fetchRedEnvelopeCount,Advert advert,User user) {
		AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
		
		//是否中奖的标志【如果为true就表示中奖】
		Boolean isWin = false;
		//奖品等级【如果是-1就表示没有中奖】
		Integer rank = -1;
		String fetchRedEnvelopeCountString = String.valueOf(fetchRedEnvelopeCount);
		Set<Integer> rankActivityConfigKeys = rankActivityConfigMap.keySet();
		for(Integer rankActivityConfigKey:rankActivityConfigKeys ) {
			if(fetchRedEnvelopeCount <= 3) break;
			
			String[] awardPoints = rankActivityConfigMap.get(rankActivityConfigKey).split(",|~");
			List<String> aps = Arrays.asList(awardPoints);
			//如果抢iPhone7的人数和中奖点完全一样，就表示抽奖用户中奖
			if(aps.contains(fetchRedEnvelopeCountString)) {
				isWin = true;
				rank = rankActivityConfigKey;
				break;
			}
			
			/*for(String awardPoint:awardPoints) {
				if(fetchRedEnvelopeCountString.equals(awardPoint)) {
					isWin = true;
					rank = rankActivityConfigKey;
					break;
				}
			}*/
			
			//如果中奖就不用再循环了
			//if(isWin) break;
		}
		
		//增加红包记录
		if(isWin) {
			//增加抢iPhone7的记录
			RedEnvelope re = new RedEnvelope();
			re.setAdvertId(advert.getId());
			re.setAmout(0);
			//存储奖品的等级【4,3,2,1,100】
			re.setOriginalAmout(rank);
			re.setPublisherId(advert.getUserId());
			re.setAdverName(advert.getAdvertName());
			//抢到奖品就有奖励时间
			re.setAwardTime(new Date());
			re.setCreateTime(new Date());
			re.setUserId(user.getId());
			re.setUserAvatar(user.getAvatar());
			re.setUserSex(user.getAgeGroup());
			re.setUserNickName(user.getNickName());
			re.setState((byte)1);
			re.setBirthday(user.getBirthday());
			re.setAgeGroup(user.getAgeGroup());
			re.setType(0);
			
			redEnvelopeMapper.insert(re);
			
			//如果中奖了，需要返回中奖的信息
			Map<String,Object> awardInfoMap = new HashMap<>();
			awardInfoMap.put("rank", rank);
			advertResult.setContent(re);
			advertResult.setData(awardInfoMap);
			
			return advertResult;
		} else {
			//增加抢iPhone7的记录
			RedEnvelope re = new RedEnvelope();
			re.setAdvertId(advert.getId());
			re.setAmout(0);
			//存储奖品的等级【4,3,2,1,100】		存储奖品的等级【如果是-1024就表示没有中奖】
			re.setOriginalAmout(-1024);
			re.setPublisherId(advert.getUserId());
			re.setAdverName(advert.getAdvertName());
			//re.setAwardTime(new Date());
			re.setCreateTime(new Date());
			re.setUserId(user.getId());
			re.setUserAvatar(user.getAvatar());
			re.setUserSex(user.getAgeGroup());
			re.setUserNickName(user.getNickName());
			re.setState((byte)1);
			re.setBirthday(user.getBirthday());
			re.setAgeGroup(user.getAgeGroup());
			re.setType(0);
			redEnvelopeMapper.insert(re);
			advertResult.setContent(re);
			return advertResult;
		}
	}
	/**
	 * 广告明细
	 * @param advert			活动的广告
	 * @param activityConfig	活动配置
	 */
	private void advertDetail(Advert advert) {
		//操作广告明细表
		Map<String,Object> advertStatisticsMap = new HashMap<String,Object>();
		advertStatisticsMap.put("advertId",advert.getId());
		//advertStatisticsMap.put("sendRedEnvelopeAmount", activityConfig.getRank());
		AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(advertStatisticsMap);
		if(advertStatistics == null) {
			advertStatistics = new AdvertStatistics();
			advertStatistics.setAdvertId(advert.getId());
			advertStatistics.setCreateTime(new Date());
			advertStatistics.setSendRedEnvelopeAmount(0);
			//保存有多少人抢过广告
			advertStatistics.setSendRedEnvelopeCount(1);
			advertStatistics.setViewTimes((long)1);
			advertStatistics.setIsAward(0);
			advertStatisticsMapper.insert(advertStatistics);
		} else {
			advertStatistics.setAdvertId(advert.getId());
			advertStatistics.setSendRedEnvelopeCount(advertStatistics.getSendRedEnvelopeCount() + 1);
			advertStatistics.setViewTimes(advertStatistics.getViewTimes() + 1);
			advertStatisticsMapper.updateByPrimaryKey(advertStatistics);
		}
	}
	/**
	 * 奖励等级的处理
	 * @param advert			广告
	 * @param activityConfig	活动的配置
	 * @param isSkipRank		是否跳过该等级的活动	
	 */
	private AdvertResult<RedEnvelope> awardRankHandle(Advert advert,User user,Map<Integer,String> rankActivityConfigMap) {
		//更新明细表
		this.advertDetail(advert);
		
		//当前抢红包的人数
		Map<String,Object> advertStatisticsMap = new HashMap<String,Object>();
		advertStatisticsMap.put("advertId", advert.getId());
		Integer fetchRedEnvelopeCount = advertStatisticsMapper.selectModel(advertStatisticsMap).getSendRedEnvelopeCount();
		return this.fetchActiveRecord(rankActivityConfigMap, fetchRedEnvelopeCount, advert,user);
	}

	@Override
	public List<RedEnvelope> selectRedEnvelopeList(Map<String, Object> map) {
		List<RedEnvelope> redList = redEnvelopeMapper.selectPublicWelfareRanking(map);
		return redList;
	}

	@Override
	public AdvertResult<Advert> selectAdvertById(Long advertId) {
		AdvertResult<Advert> advertResult = new AdvertResult<>();
		Advert advert = advertMapper.selectAdvertByPrimaryKey(advertId);
		if(advert != null){
			advertResult.setContent(advert);
			advertResult.setState(1);
		}
		return advertResult;
	}

	@Override
	public List<Advert> selectAllPublicWelFare(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Advert> list = advertMapper.selectAllPublicWelFare(map);
		return list;
	}
	
	@Override
	public ListRange<PublicWelFareAdvertVo> selectPublicWelFareVoList(Map<String, Object> map) {
		int count = advertMapper.selectCountByType(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		List<PublicWelFareAdvertVo> list = advertMapper.selectPublicWelFareVoList(map);
		ListRange<PublicWelFareAdvertVo> listRange = new ListRange<>();
		listRange.setPage(page);
		listRange.setData(list);
		return listRange;
	}

	@Override
	public int addPublicWelfare(Advert advert) {
		// TODO Auto-generated method stub
		int result = advertMapper.insertSelective(advert);
		if(result == 1){
			AdvertStatistics advertStatistics = new AdvertStatistics();
			advertStatistics.setAdvertId(advert.getId());
			advertStatistics.setFreezeEnvelopeCount(0);
			advertStatistics.setSendRedEnvelopeAmount(0);
			advertStatistics.setCreateTime(new Date());
			result = advertStatisticsMapper.insertSelective(advertStatistics);
		}
		return result;
	}

	@Override
	public AdvertResult<RedEnvelope> selectRedEnvelopeByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AdvertResult<RedEnvelope> redEnvelopeResult = new AdvertResult<RedEnvelope>();
		RedEnvelope redEnvelope = redEnvelopeMapper.selectRedEnvelopeByMap(map);
		if(redEnvelope != null){
			redEnvelopeResult.setContent(redEnvelope);
			redEnvelopeResult.setState(1);
		}
		return redEnvelopeResult;
	}
	
	public HomePageAwardVo updateHomePageAward(User user, Advert advert) {
		List<String> advertLocks = new ArrayList<String>();
		HomePageAwardVo homePageAwardVo = new HomePageAwardVo();
		try {
			String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + advert.getId();
			advertLocks.add(advertLock);
			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {
				advert = advertMapper.selectByPrimaryKey(advert.getId()); //防止首页福袋领取总金额大于发布总金额问题
				Date date = new Date();
				/*查询当前用户当天是否已领取此首页广告*/
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("advertId", advert.getId());
				map.put("userId", user.getId());
				map.put("crateTimeDay", date);
				AdvertResult<RedEnvelope> advertRedResult = selectRedEnvelope(map);
				if (null != advertRedResult && advertRedResult.getContent() != null) {
					homePageAwardVo.setErrorCode(AdvertHelper.ERROR_CODE_500002.getMarker());
					return homePageAwardVo;
				}
				/*查询当前用户当天领取首页广告次数*/
				map.clear();
				map.put("userId", user.getId());
				map.put("crateTimeDay", date);
				map.put("type", RedEnvelopeHelper.REDENVELOPE_TYPE_4.getMarker());
				int count = selectRedEnvelopeCount(map);
				/*查询用户等级配置信息*/
				map.clear();
				map.put("level", user.getLevel());
				UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(map);
				if (null != userLevelConfig && count >= userLevelConfig.getHomePageRedEnvelope().intValue()) {
					homePageAwardVo.setErrorCode(AdvertHelper.ERROR_CODE_500005.getMarker());
					return homePageAwardVo;
				}
				RedEnvelope redEnvelope = getRedEnvelope(advert, user, AdvertHelper.RED_ENVELOPE_TYPE_4.getMarker());
				map.clear();
				map.put("advertId", advert.getId());
				AdvertStatistics advertStatistics = advertStatisticsMapper.selectModel(map);
				if(advertStatistics != null){
					int amount = calHomePageAdvertRedEnvelopeMoney(advert, advertStatistics, user); //计算领取首页广告福袋金额
					/*插入红包信息*/
					redEnvelope.setAmout(amount);
					redEnvelope.setOriginalAmout(advert.getRedEnvelopeAmount());
					addRedEnvelope(redEnvelope);
					/*更新广告明细 */
					advertStatistics.setSendRedEnvelopeAmount(advertStatistics.getSendRedEnvelopeAmount() + amount);
					advertStatistics.setSendRedEnvelopeCount(advertStatistics.getSendRedEnvelopeCount() + 1);
					advertStatisticsMapper.updateByPrimaryKeySelective(advertStatistics);
					/*更新广告*/
					if (advertStatistics.getSendRedEnvelopeAmount() >= advert.getRedEnvelopeAmount()) {
						// 更新广告状态为：已完成
						advert.setState((byte) 3);
						updateAdvert(advert);
					}
					/*更新个人账户信息*/
					UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
					userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + amount);
					userAssets.setTableNum(user.getTableNum());
					userVerifyService.updateUserAssets(userAssets);
					/*返回app数据vo*/
					homePageAwardVo.setAmount(amount);
					homePageAwardVo.setErrorCode(1);
				}else{
					homePageAwardVo.setErrorCode(AdvertHelper.ERROR_CODE_900100.getMarker());
				}
				
				// 更新SystemData中的全网抢到红包总人数 加1 start
				Map<String,Object> systemData = new HashMap<String,Object>();
				systemData.put("key_name", "redEnvelopePersons");
				SystemDataVo sysDataVo = systemService.selectListSystemData(systemData).get(0);
				
				SystemData sysData = new SystemData();
				sysData.setId(sysDataVo.getId());
				sysData.setSysValue(sysDataVo.getValue() + 1);
				systemService.updateSystemData(sysData);
				// 更新SystemData中的全网抢到红包总人数 加1 end
			}
		} catch (Exception ex) {
			homePageAwardVo.setErrorCode(LuckyBagHelper.ERROR_CODE_900100.getMarker());
			ex.printStackTrace();
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return homePageAwardVo;
	}

	@Override
	public List<Advert> selectOperateActivity(Map<String, Object> map) {
		return advertMapper.selectOperateActivity(map);
	}

	@Override
	public ListRange<Advert> selectListRangeAdvertByLike(Map<String, Object> map) {
		// TODO Auto-generated method stub
		ListRange<Advert> listRange = new ListRange<>();
		listRange.setData(advertMapper.selectListRangeAdvertByLike(map));
		int count = selectCountByLike(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		return listRange;
	}
	@Override
	public int selectCountByLike(Map<String, Object> map) {
		return advertMapper.selectCountByLike(map);
	}

	@Override
	public int updateStateById(Long id, int state) {
		Advert advert = advertMapper.selectByPrimaryKey(id);
		int result = 0;
		if (advert != null) {
			advert.setModifyTime(new Date());
			advert.setState((byte) state);
			result = advertMapper.updateByPrimaryKey(advert);
		}
		return result;
	}

	@Override
	public AdvertResult<ListRange<Advert>> selectListOperateTheActivity(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Page page = (Page)map.get("page");
		int count = advertMapper.selectCountByType(map);
		page.setCount(count);
		AdvertResult<ListRange<Advert>> advertResult = new AdvertResult<>();
		List<Advert> list = advertMapper.selectListOperateTheActivity(map);
		if(list.size()>0){
			ListRange<Advert> listRange = new ListRange<>();
			listRange.setPage(page);
			listRange.setData(list);
			advertResult.setContent(listRange);
			advertResult.setState(1);
		}
		return advertResult;
	}

	@Override
	public Advert selectActivity(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return advertMapper.selectActivity(map);
	}
	/**
	 * 幸运转盘抽奖
	 * @param userId
	 * @param advertId
	 * @return
	 */
	public AdvertResult<RedEnvelope> addLuckyWheel(Long userId,Long advertId) {
		List<String> advertLocks = new ArrayList<String>();
		AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
		try {
			String advertLock = USER_ADVERT_FETCH_FREEZE_LOCK + advertId;
			advertLocks.add(advertLock);
			if (redisLockHandler.tryLock(advertLock, 10000, 500)) {
			Map<String,Object> userPartMap = new HashMap<String,Object>();
			userPartMap.put("mapprerValue", userId);
			Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("id", userId);
			userMap.put("tableNum", tableIndex);
			User user = userMapper.selectModel(userMap);
			if(user == null) {
				logger.error("用户不存在!");
				return advertResult;
			}
			
			Advert advert = advertMapper.selectAdvertByPrimaryKey(advertId);
			if(advert == null) {
				logger.error("广告不存在!");
				return advertResult;
			}
			
			//活动配置
			Map<String,Object> activityConfigMap = new HashMap<String,Object>();
			activityConfigMap.put("advertId", advertId);
			activityConfigMap.put("order", "rank DESC");
			List<ActivityConfig> activityConfigs = activityConfigMapper.selectList(activityConfigMap);
			
			Map<Integer,String> rankActivityConfigMap = new HashMap<Integer,String>();
			for(ActivityConfig activityConfig:activityConfigs) {
				//奖品在领取完的条件是，有奖励时间
				rankActivityConfigMap.put(activityConfig.getRank(), activityConfig.getProbability());
			}
			return this.awardRankHandle(advert,user, rankActivityConfigMap);
			}
		} catch (Exception ex) {
			advertResult.getContent().setAmout((LuckyBagHelper.ERROR_CODE_900100.getMarker()));
			ex.printStackTrace();
		} finally {
			for (String lock : advertLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}
}
