package com.mouchina.moumou_server_dubbo.provider.advert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.moumou_server.dao.advert.AdvertMapper;
import com.mouchina.moumou_server.dao.advert.BusinessCouponMapper;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.dao.member.BusinessMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.social.UsersRelationMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.vo.business.coupon.MyBusinessCouponVo;
import com.mouchina.moumou_server.entity.vo.user.particular.UserBusinessCouponParticularVo;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SystemDataVo;

/**
 * 优惠券的业务层
 * @author Administrator
 *
 */
@Service(value="businessCouponServiceSupport")
public class BusinessCouponServiceSupport implements BusinessCouponService {
	
	private final static Logger logger = LoggerFactory.getLogger(BusinessCouponServiceSupport.class);
	
	private static String USER_ADVERT_ADD_LOCK = "User_Advert_Add_Lock_";
	
	@Resource
	private BusinessCouponMapper businessCouponMapper;
	@Autowired
	private BusinessMapper businessMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPartMapper userPartMapper;
	@Autowired
	private AdvertMapper advertMapper;
	@Autowired
	private RedEnvelopeMapper redEnvelopeMapper;
	@Autowired
	private AdvertService advertService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UsersRelationMapper usersRelationMapper;
	@Autowired
	private RedisLockHandler redisLockHandler;
	@Autowired
	private AdvertServiceSupport advertServiceSupport;

	@Override
	public BusinessCoupon addBusinessCoupon(BusinessCoupon businessCoupon) {
		businessCoupon.setCreateTime(new Date());
		businessCouponMapper.insertSelective(businessCoupon);
		return businessCoupon;
	}

	@Override
	public Integer deleteBusinessCoupon(Long id) {
		BusinessCoupon businessCoupon = businessCouponMapper.selectByPrimaryKey(id);
		//如果不存在返回错误码
		if(businessCoupon == null) return 10001;
		//如果存在，进行逻辑删除【设置2就是已经删除】
		businessCoupon.setFlag(2);
		businessCoupon.setModifyTime(new Date());
		businessCouponMapper.updateByPrimaryKey(businessCoupon);
		return 10000;
	}

	@Override
	public BusinessCoupon updateBusinessCoupon(BusinessCoupon businessCoupon) {
		businessCouponMapper.updateByPrimaryKeySelective(businessCoupon);
		return businessCoupon;
	}

	@Override
	public List<BusinessCoupon> selectBusinessCoupon(Map<String,Object> map) {
		List<BusinessCoupon> businessCoupons = businessCouponMapper.selectBusinessCouponByUserId(map);
		if(businessCoupons == null || businessCoupons.size() <= 0) {
			return null;
		}
		return businessCoupons;
	}
	
	@Override
	public ListRange<BusinessCoupon> businessCouponList(Map<String,Object> map) {
		ListRange<BusinessCoupon> listRange = new ListRange<>();
		Page page = (Page) map.get("page");
		int count = businessCouponMapper.selectBusinessCouponCount(map);
		page.setCount(count);
		List<BusinessCoupon> businessCoupons = businessCouponMapper.selectPageList(map);
		listRange.setPage(page);
		listRange.setData(businessCoupons);
		return listRange;
	}
	
	@Override
	public List<BusinessCoupon> selectBusinessCouponManage(Map<String,Object> map) {
		List<BusinessCoupon> businessCoupons = businessCouponMapper.selectBusinessCouponManage(map);
		if(businessCoupons == null || businessCoupons.size() <= 0) {
			return null;
		}
		
		for(BusinessCoupon businessCoupon:businessCoupons) {
			Map<String,Object> businessMap = new HashMap<String,Object>();
			businessMap.put("userId", businessCoupon.getUserId());
			Business business = businessMapper.selectModel(map);
			
			StringBuffer businessAddress = new StringBuffer();
			businessAddress.append(business.getProvince());
			businessAddress.append(business.getCity());
			businessAddress.append(business.getArea());
			businessAddress.append(business.getBusinessAddress());
			
			businessCoupon.setBusinessAddress(businessAddress.toString());
		}
		return businessCoupons;
	}
	
	@Override
	public Advert addPublishBusinessCoupon(Advert advert,User user) {
		logger.info("请求到方法,参数是:"+advert+"");

		if(user == null) return null;
		
		advert.setState((byte) 3);
		advert.setUserId(user.getId());
		AdvertResult<Advert> advertResult = advertService.addAdvert3(advert, user);

		//如果小于就表示广告没有发布成功
		if(advertResult.getState() < 1) return null;
		
		// 如果为1就表示添加广告成功
		logger.info("更新广告的统计！");
		
		// 广告金额总计 更新SystemData中的全网广告总发布钱数 start
		Map<String, Object> publishAdvertMoneyMap = new HashMap<String, Object>();
		publishAdvertMoneyMap.put("key_name", "publishAdvertMoney");
		SystemDataVo sysDataVo = systemService.selectListSystemData(publishAdvertMoneyMap).get(0);
		SystemData sysData = new SystemData();
		sysData.setId(sysDataVo.getId());
		sysData.setSysValue(sysDataVo.getValue() + advert.getRedEnvelopeAmount());
		systemService.updateSystemData(sysData);
		// 更新SystemData中的全网广告总发布钱数 end

		return advertResult.getContent();
	}

	@Override
	public AdvertResult<RedEnvelope> addRobBusinessCoupon(Long userId, Long advertId) {
		List<String> advertAddLocks = new ArrayList<String>();
		
		AdvertResult<RedEnvelope> advertResult = new AdvertResult<RedEnvelope>();
		try {
			/*//给用户加锁
			String userLock = USER_ADVERT_ADD_LOCK + userId;
			advertAddLocks.add(userLock);
			// 如果已经被锁，就直接返回
			if(!redisLockHandler.tryLock(userLock)) {
				advertResult.setState(0);
				logger.error("用户：" + userId + "被锁住");
				return advertResult;
			}*/
			
			//给广告加锁
			String advertLock = USER_ADVERT_ADD_LOCK + advertId;
			advertAddLocks.add(advertLock);
			// 如果已经被锁，就直接返回
			if(!redisLockHandler.tryLock(advertLock)) {
				advertResult.setState(0);
				logger.info("广告 ：" + advertId + "被锁住");
				return advertResult;
			}
			
			//获取广告信息
			Advert advert = advertMapper.selectAdvertByPrimaryKey(advertId);
			
			//查询用户的权限
			Long advertUserId = advert.getUserId();
			Map<String,Object> usersRelationMap = new HashMap<String,Object>();
			usersRelationMap.put("userId", advertUserId);
			usersRelationMap.put("relationUserId", userId);
			UsersRelation usersRelation = usersRelationMapper.selectModel(usersRelationMap);
			//如果为空就说明用户不是商家的粉丝
			if(usersRelation == null) {
				advertResult.setState(1);
				return advertResult;
			}
			
			//同一个优惠券只能抢一次
			Map<String,Object> redEnvelopeMap = new HashMap<String,Object>();
			redEnvelopeMap.put("advertId", advert.getId());
			redEnvelopeMap.put("userId", userId);
			RedEnvelope re = redEnvelopeMapper.selectModel(redEnvelopeMap);
			if(re != null) {
				//已经抢到过该优惠券，没办法再次抢
				advertResult.setState(2);
				return advertResult;
			}
			
			Map<String,Object> advertMap = new HashMap<String,Object>();
			advertMap.put("id", advert.getId());
			Advert ad = advertMapper.selectModel(advertMap);  //获取广告信息
			
			Integer redEnvelopeCount = ad.getRedEnvelopeCount();
			Map<String,Object> listRedEnvelopeMap = new HashMap<>();
			listRedEnvelopeMap.put("advertId", ad.getId());
			listRedEnvelopeMap.put("type", 1);
			List<RedEnvelope> redEnvelopes = redEnvelopeMapper.selectList(listRedEnvelopeMap);
			if(redEnvelopes.size() >= redEnvelopeCount) {
				//已经抢完，不能再领取
				advertResult.setState(5);
				return advertResult;
			}
			
			
			Long userIdPublish =ad.getUserId();  //用户的ID
			
			//调用领取红包接口
			
			//发布广告的用户
			User userPublish = this.getUserInfo(userIdPublish);
			//领取广告的用户
			User userReceive = this.getUserInfo(userId);
			
			//如果优惠券有限，就需要加锁进行同步
			//抢到的红包和优惠券的明细进行添加
			RedEnvelope redEnvelope = new RedEnvelope();
			redEnvelope.setAdvertId(advertId);
			redEnvelope.setPublisherId(advert.getUserId());
			redEnvelope.setUserId(userId);
			redEnvelope.setAmout( 0);
			redEnvelope.setCreateTime(new Date());
			redEnvelope.setUserAvatar(userReceive.getAvatar());
			redEnvelope.setUserSex((byte)userReceive.getSex().intValue());
			redEnvelope.setUserNickName(userPublish.getNickName());
			redEnvelope.setAdvertLogo("");
			redEnvelope.setAdverName(advert.getAdvertName());
			redEnvelope.setState((byte)1);
			//金额有钱的是红包记录		金额没有钱的是优惠券
			redEnvelope.setOriginalAmout( 0);
			//0是红包  1是优惠券  2接力福袋  3公益
			redEnvelope.setType( 1);
			//领取优惠券在没有使用之前是没有奖励时间的，该奖励时间需要在使用的时候才会生成。用于查询核销人数的
			redEnvelope.setAwardTime(null);
			redEnvelope.setBirthday(userReceive.getBirthday());
			redEnvelopeMapper.insert(redEnvelope);
			
			//每抢一次需要增加广告统计的次数
			advertServiceSupport.updateAdvertStatisticsAwart(advert, 0, userReceive);
			
			advertResult.setContent(redEnvelope);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//释放所有的锁
			for(String lock : advertAddLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return advertResult;
	}

	@Override
	public List<MyBusinessCouponVo> selectUserBusinessCoupon(Map<String,Object> map) {
		List<MyBusinessCouponVo> myBusinessCouponVos = businessCouponMapper.selectUserBusinessCoupon(map);
		for(MyBusinessCouponVo myBusinessCouponVo:myBusinessCouponVos) {
			Long userId = myBusinessCouponVo.getUserId();
			User user = this.getUserInfo(userId);
			if(user == null) {
				logger.info("用户不存在！");
				continue;
			}
			myBusinessCouponVo.setUserAvatar(user.getAvatar());
		}
		return myBusinessCouponVos; 
	}
	
	private User getUserInfo(Long userId) {
		Map<String,String> userPartMap = new HashMap<String,String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
		
		//获取用户信息
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put("tableNum", tableIndex);
		userMap.put("id", userId);
		return userMapper.selectModel(userMap);
	}
	
	public BusinessCoupon selectBusinessCouponById(Long id){
		return businessCouponMapper.selectByPrimaryKey(id);
	}

	@Override
	public UserBusinessCouponParticularVo selectUserBusinessCouponParticular(Long advertId) {
		return businessCouponMapper.selectUserBusinessCouponParticular(advertId);
	}

	@Override
	public BusinessCoupon selectBusinessCouponContent(Long id) {
		return businessCouponMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer deleteUserBusinessCoupon(Long userId,Long advertId) {
		return businessCouponMapper.deleteUserBusinessCoupon(userId, advertId);
	}

	@Override
	public int updateState(long id, int state) {
		BusinessCoupon businessCoupon = businessCouponMapper.selectByPrimaryKey(id);
		businessCoupon.setState(state);
		businessCoupon.setModifyTime(new Date());
		int result = businessCouponMapper.updateByPrimaryKey(businessCoupon);
		return result;
	}

}
