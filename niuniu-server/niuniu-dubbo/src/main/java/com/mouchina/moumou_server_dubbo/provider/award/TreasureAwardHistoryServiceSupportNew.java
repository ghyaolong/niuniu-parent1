package com.mouchina.moumou_server_dubbo.provider.award;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mouchina.base.utils.PercentUtil;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.dao.award.TreasureAwardHistoryMapper;
import com.mouchina.moumou_server.dao.award.TreasureBoxConfigMapper;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;
import com.mouchina.moumou_server.entity.award.TreasureBoxConfig;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server_interface.award.TreasureAwardHistoryServiceNew;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;

@Service(value="treasureAwardHistoryServiceSupportNew")
public class TreasureAwardHistoryServiceSupportNew implements TreasureAwardHistoryServiceNew {
	
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	
	@Resource
	private TreasureAwardHistoryMapper treasureAwardHistoryMapper;
	@Resource
	private TreasureBoxConfigMapper treasureBoxConfigMapper;
	@Resource
	private UserAssetsMapper userAssetsMapper;
	@Resource
	private UserPartMapper userPartMapper;
	@Resource
	private RedEnvelopeMapper redEnvelopeMapper;
	@Resource
	private UserMapper userMapper;
	@Autowired
	private UserLevelConfigService userLevelConfigService;

	@Override
	public TreasureAwardHistory addOpenUserTreasureAward(Long userId,String isQuery) {
		TreasureAwardHistory treasureAwardHistory = new TreasureAwardHistory();
		
		Map<String,Object> awardStatus = selectAwardStatus(userId);
		//查询	//如果是0只进行查询
		if("0".equals(isQuery)) {
			treasureAwardHistory.setCountDown((long)awardStatus.get("countDown"));
			treasureAwardHistory.setIsClick((String)awardStatus.get("isClick"));
			return treasureAwardHistory;
		}
		//领取奖励
		if("1".equals(isQuery) && "1".equals(awardStatus.get("isClick"))) {
			TreasureBoxConfig treasureBoxConfig = this.getTreasureBoxConfig(sdfTime.format(awardStatus.get("openAwardBoxTime")));
			
			
			treasureAwardHistory.setTreasureBoxConfigId(treasureBoxConfig.getId());
			treasureAwardHistory.setUserId(userId);
			treasureAwardHistory.setCreateTime(new Date());
			treasureAwardHistory.setBoxTimePoint(sdfTime.format(treasureBoxConfig.getOpenTime()));
			treasureAwardHistoryMapper.insert(treasureAwardHistory);
			
			//更新用户的财富
			Byte awardType = treasureBoxConfig.getType();
			Integer awardValue = treasureBoxConfig.getValue();
			this.updateUserAward(userId, awardType, awardValue);
			
			//添加收益明细
			if(awardType == 0) {
				Map<String,String> userPartMap = new HashMap<String,String>();
				userPartMap.put("mapprerValue", String.valueOf(userId));
				Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
				
				Map<String,Object> userMap = new HashMap<String,Object>();
				userMap.put("id", userId);
				userMap.put("tableNum", tableIndex);
				User user = userMapper.selectModel(userMap);
				
				RedEnvelope redEnvelope = new RedEnvelope();
				redEnvelope.setAdvertId(Long.valueOf("0"));
				redEnvelope.setPublisherId(Long.valueOf("0"));
				redEnvelope.setUserId(userId);
				redEnvelope.setAmout(awardValue);
				redEnvelope.setCreateTime(new Date());
				redEnvelope.setUserSex((byte)1);
				redEnvelope.setUserNickName(user.getNickName());
				redEnvelope.setAdverName("任务奖励");
				redEnvelope.setState((byte)1);
				redEnvelope.setOriginalAmout(awardValue);
				redEnvelope.setAwardTime(new Date());
				redEnvelope.setBirthday(user.getBirthday());
				
				redEnvelope.setAwardTime(new Date());
				redEnvelopeMapper.insertSelective(redEnvelope);
			}
			
			treasureAwardHistory.setType(treasureBoxConfig.getType());
			treasureAwardHistory.setValue(treasureBoxConfig.getValue());
			treasureAwardHistory.setCountDown((long)awardStatus.get("countDown"));
			if((Boolean)awardStatus.get("endTimePoint") && "1".equals(isQuery)) {
				treasureAwardHistory.setIsClick("2");
			} else {
				treasureAwardHistory.setIsClick("0");
			}
			return treasureAwardHistory;
		}
		treasureAwardHistory.setCountDown((long)awardStatus.get("countDown"));
		treasureAwardHistory.setIsClick((String)awardStatus.get("isClick"));
		return treasureAwardHistory;
	}
	
	/**
	 * 更新财富值
	 * @param userId		用户id
	 * @param awardType		财富类型
	 * @param awardValue	财富值
	 */
	private void updateUserAward(Long userId,byte awardType,Integer awardValue) {
		Map<String,String> userPartMap = new HashMap<String,String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
		
		//查询用户的财富
		Map<String,Object> userAssetsMap = new HashMap<String,Object>();
		userAssetsMap.put("userId", userId);
		userAssetsMap.put("tableNum", tableIndex);
		UserAssets userAssets = userAssetsMapper.selectModel(userAssetsMap);
		
		userAssets.setTableNum(tableIndex);
		if(awardType == 1) {
			//如果是经验	增加经验值
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("id", userId);
			userMap.put("tableNum", tableIndex);
			User user = userMapper.selectModel(userMap);
			Integer userLevel = user.getLevel();
			Integer userGrowthValue = userAssets.getGrowthValue();
			
			Map<String,Object> userLevelConfigMap = new HashMap<String,Object>();
			userLevelConfigMap.put("level", userLevel);
			UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(userLevelConfigMap);
			
			Integer levelGrowthPoint = userLevelConfig.getGrowthPoint();
			Integer currentGrowthPoint = userGrowthValue + awardValue;
			//用户升级
			if(currentGrowthPoint >= levelGrowthPoint) {
				Integer temGrowthPoint = currentGrowthPoint - levelGrowthPoint;
				userAssets.setGrowthValue(temGrowthPoint);
				user.setLevel(userLevel + 1);
				//更新用户等级
				userMapper.updateByPrimaryKey(user);
			} else {
				userAssets.setGrowthValue(userGrowthValue + awardValue);
			}
			
		} else if(awardType == 0) {
			//如果是红包	增加红包钱数
			userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + awardValue);
		}
		//更新财富值
		userAssetsMapper.updateByUserId(userAssets);
	}
	
	/**
	 * 获取宝箱的类型和财富值
	 * @param openTreasureTime	几点的宝箱
	 * @return
	 */
	private TreasureBoxConfig getTreasureBoxConfig(String openTreasureTime) {
		//查询命中率
		List<Double> hitRates = treasureBoxConfigMapper.selectHitRate();
		Map<String, Object> hitRateMap = PercentUtil.getRandomPercent(hitRates);
		Double hitRate = (Double) hitRateMap.get("percent");
		
		//查询命中几率
		Map<String,Object> map = new HashMap<String,Object>();
		//时间格式是09:00:00
		map.put("openTime", openTreasureTime);
		map.put("hitRate", hitRate.intValue());
		return treasureBoxConfigMapper.selectUserHitAward(map);
	}
	
	/**
	 * 查询宝箱当前状态是否可以领取宝箱
	 * @param userId
	 */
	public Map<String,Object> selectAwardStatus(Long userId) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			//endTimePoint判断是否是最后的时间点
			map.put("endTimePoint", false);
			
			Date currentDate = new Date();
			currentDate = sdfTime.parse(sdfTime.format(currentDate));
			//获取所有宝箱的时间点
			List<Date> treasureBoxTimes = treasureBoxConfigMapper.getTreasureBoxTimes();
			Integer timePoints = treasureBoxTimes.size();
			
			for(int i=0 ;i<timePoints;i++) {
				
				//当前时间在9点之前，直接return
				if(currentDate.before(treasureBoxTimes.get(i))) {
					//0不能领取
					map.put("isClick", "0");
					//打开宝箱的时间
					map.put("openAwardBoxTime", treasureBoxTimes.get(i));
					//倒计时
					map.put("countDown", treasureBoxTimes.get(i).getTime() - currentDate.getTime());
					return map;
				}
				
				//当前时间在23:59:59之前
				Date lastTimePoint = treasureBoxTimes.get(timePoints-1);
				if(currentDate.after(lastTimePoint) && currentDate.before(sdfTime.parse("23:59:59"))) {
					//是否已经领取奖励
					if(this.selectTreasureBoxIsOpen(userId, lastTimePoint)) {
						//如果开启就直接返回
						//2	今日已领完
						map.put("isClick", "2");
						//打开宝箱的时间
						map.put("openAwardBoxTime", lastTimePoint);
						//倒计时
						map.put("countDown", lastTimePoint.getTime() - currentDate.getTime());
						map.put("endTimePoint", true);
						return map;
					} else {
						//1能领取
						map.put("isClick", "1");
						//打开宝箱的时间
						map.put("openAwardBoxTime", lastTimePoint);
						//倒计时
						map.put("countDown", lastTimePoint.getTime() - currentDate.getTime());
						map.put("endTimePoint", true);
						
						return map;
					}
					
				}
				
				//当前时间在9点之后并且在12点之前
				if(currentDate.after(treasureBoxTimes.get(i)) && currentDate.before(treasureBoxTimes.get(i+1))) {
					//是否已经领取奖励
					Date timePoint = treasureBoxTimes.get(i);
					if(this.selectTreasureBoxIsOpen(userId, timePoint)) {
						//如果开启就直接返回
						//0不能领取
						map.put("isClick", "0");
						//打开宝箱的时间
						map.put("openAwardBoxTime", treasureBoxTimes.get(i));
						//倒计时
						map.put("countDown", treasureBoxTimes.get(i+1).getTime() - currentDate.getTime());
						return map;
					} else {
						//1能领取
						map.put("isClick", "1");
						//打开宝箱的时间
						map.put("openAwardBoxTime", treasureBoxTimes.get(i));
						//倒计时
						map.put("countDown", treasureBoxTimes.get(i+1).getTime() - currentDate.getTime());
						return map;
					}
				}
				
			}
			
			return map;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Boolean selectTreasureBoxIsOpen(Long userId,Date boxTimePoint) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("boxTimePoint", sdfTime.format(boxTimePoint));
		map.put("createTime", sdfDate.format(new Date()));
		TreasureAwardHistory treasureAwardHistory = treasureAwardHistoryMapper.selectTreasureBoxIsOpen(map);
		return treasureAwardHistory == null ? false :true ;
	}
	
}
