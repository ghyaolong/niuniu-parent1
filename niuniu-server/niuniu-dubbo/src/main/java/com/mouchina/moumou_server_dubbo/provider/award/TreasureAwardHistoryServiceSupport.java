package com.mouchina.moumou_server_dubbo.provider.award;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.mouchina.moumou_server_interface.award.TreasureAwardHistoryService;

@Service(value="treasureAwardHistoryServiceSupport")
public class TreasureAwardHistoryServiceSupport implements TreasureAwardHistoryService {
	
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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

	@Override
	public TreasureAwardHistory addOpenUserTreasureAward(Long userId,String isQuery) {
		try {
			TreasureAwardHistory treasureAwardHistory = treasureAwardHistoryMapper.selectByUserId(userId);
			
			//第一次领取宝箱
			if(treasureAwardHistory == null) {
				treasureAwardHistory = new TreasureAwardHistory();
				//获取宝箱的奖励值
				Map<String,Object> map = this.openAwardBox(null,isQuery);
				TreasureBoxConfig treasureBoxConfig = this.getTreasureBoxConfig(sdfTime.format(map.get("openAwardBoxTime")));
				treasureAwardHistory.setUserId(userId);
				treasureAwardHistory.setTreasureBoxConfigId(treasureBoxConfig.getId());
				treasureAwardHistory.setCreateTime(new Date());
				treasureAwardHistory.setCountDown((Long) map.get("countDown"));
				treasureAwardHistory.setIsClick((String) map.get("isClick"));
				
				//如果是0只进行查询
				if("1".equals(isQuery)) {
					treasureAwardHistoryMapper.insert(treasureAwardHistory);
					//更新用户的财富
					Byte awardType = treasureBoxConfig.getType();
					Integer awardValue = treasureBoxConfig.getValue();
					this.updateUserAward(userId, awardType, awardValue);
					
					//添加收益明细
					if(awardType == 1) {
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
				}
				return treasureAwardHistory;
			}
			
			//是否已经开启过宝箱
			Date openTreasureTime = treasureAwardHistory.getCreateTime();
			
			Map<String,Object> map = this.openAwardBox(openTreasureTime,isQuery);
			TreasureBoxConfig treasureBoxConfig = this.getTreasureBoxConfig(sdfTime.format(map.get("openAwardBoxTime")));
			treasureAwardHistory.setUserId(userId);
			treasureAwardHistory.setTreasureBoxConfigId(treasureBoxConfig.getId());
			treasureAwardHistory.setCreateTime(new Date());
			treasureAwardHistory.setCountDown((Long) map.get("countDown"));
			treasureAwardHistory.setIsClick((String) map.get("isClick"));
			if("1".equals(isQuery)) {
				treasureAwardHistoryMapper.updateByPrimaryKey(treasureAwardHistory);
				//更新用户的财富
				Byte awardType = treasureBoxConfig.getType();
				Integer awardValue = treasureBoxConfig.getValue();
				this.updateUserAward(userId, awardType, awardValue);
				
				//添加收益明细
				if(awardType == 1) {
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
			}
			
			return treasureAwardHistory;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 可以开启几点的宝箱
	 * @param openTreasureTime		上次开宝时间
	 * @return
	 * @throws ParseException
	 */
	public Map<String,Object> openAwardBox(Date openTreasureTime,String isQuery) throws ParseException {
		if(openTreasureTime != null) openTreasureTime = sdfTime.parse(sdfTime.format(openTreasureTime));
		
		Date currentTime = sdfTime.parse(sdfTime.format(new Date()));
		List<Date> treasureBoxTimes = treasureBoxConfigMapper.getTreasureBoxTimes();
		
		//如果在最开始的时间之前，是不能领取宝箱的
		Map<String,Object> map = new HashMap<String,Object>();
		if(currentTime.before(treasureBoxTimes.get(0) )) {
			map.put("isClick", "0");
			map.put("openAwardBoxTime", treasureBoxTimes.get(0));
			map.put("countDown", treasureBoxTimes.get(0).getTime() - currentTime.getTime());
			return map;
		}
		
		for(int i=0; i < treasureBoxTimes.size() ; i++) {
			//只有在9点到12点之间才能开启9点的宝箱
			if(currentTime.before(treasureBoxTimes.get(i))) {
				if( openTreasureTime == null || !(openTreasureTime.after(treasureBoxTimes.get(i-1)) && openTreasureTime.before(treasureBoxTimes.get(i)) ) ) {
					//只有	没有打开宝箱的用户	或者	没有在该时间段之内开启过宝箱的用户		才能签到
					/*map.put("isClick", "1");
					map.put("openAwardBoxTime", treasureBoxTimes.get(i-1));
					map.put("countDown", treasureBoxTimes.get(i).getTime() - currentTime.getTime());*/
					if("1".equals(isQuery)) {
						map.put("isClick", "0");
						map.put("openAwardBoxTime", treasureBoxTimes.get(treasureBoxTimes.size()-1));
						map.put("countDown", treasureBoxTimes.get(i).getTime() - currentTime.getTime());
					} else if("0".equals(isQuery)) {
						map.put("isClick", "1");
						map.put("openAwardBoxTime", treasureBoxTimes.get(treasureBoxTimes.size()-1));
						map.put("countDown", treasureBoxTimes.get(i).getTime() - currentTime.getTime());
					}
					return map;
				} else if(openTreasureTime.after(treasureBoxTimes.get(i-1)) && openTreasureTime.before(treasureBoxTimes.get(i)) ) {
					//在9点到12点之间已经签过到，不能再次签到
					map.put("isClick", "0");
					map.put("openAwardBoxTime", treasureBoxTimes.get(i));
					map.put("countDown", treasureBoxTimes.get(i).getTime() - currentTime.getTime());
					return map;
				}
			}
		}
		
		// 凌晨24点之前可以领取20的宝箱
		if(currentTime.before(sdfTime.parse("23:59:59"))) {
			//开启时间如果在20点之后已经开启过，那么就不能再次开启
			if(openTreasureTime == null || openTreasureTime.before(treasureBoxTimes.get(treasureBoxTimes.size()-1)) ) {
				//领取宝箱
				if("1".equals(isQuery)) {
					map.put("isClick", "0");
					map.put("openAwardBoxTime", treasureBoxTimes.get(treasureBoxTimes.size()-1));
				} else if("0".equals(isQuery)) {
					map.put("isClick", "1");
					map.put("openAwardBoxTime", treasureBoxTimes.get(treasureBoxTimes.size()-1));
				}
				return map;
			}
			map.put("isClick", "0");
			map.put("openAwardBoxTime", treasureBoxTimes.get(treasureBoxTimes.size()-1));
			return map;
		}
		return null;
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
		if(awardType == 0) {
			//如果是经验	增加经验值
			userAssets.setGrowthValue(userAssets.getGrowthValue() + awardValue);
		} else if(awardType == 1) {
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
		map.put("openTime", "1970-01-01 " + openTreasureTime);
		map.put("hitRate", hitRate);
		return treasureBoxConfigMapper.selectModel(map);
	}
	
	/**
	 * byte转换成字符串
	 * @return
	 */
	public String byteToChar(byte value) {
		return new Byte(value).toString();
	}
	
	
	
	
	
	/**
	 * 查询宝箱当前状态
	 * @param userId
	 */
	public void selectAwardStatus(Long userId) {
		Map<String,Object> treasureAwardHistoryMap = new HashMap<String,Object>();
		treasureAwardHistoryMap.put("userId", userId);
		List<TreasureAwardHistory> treasureAwardHistorys = treasureAwardHistoryMapper.selectList(treasureAwardHistoryMap);
		
		Map<String,Object> treasureBoxConfigMap = new HashMap<String,Object>();
		List<TreasureBoxConfig> treasureBoxConfigs = treasureBoxConfigMapper.selectList(treasureBoxConfigMap);
		if(treasureAwardHistorys == null) {
			for(TreasureBoxConfig treasureBoxConfig:treasureBoxConfigs) {
				//treasureAwardHistoryMapper.insert(record);
			}
			
		}
		for(TreasureAwardHistory treasureAwardHistory:treasureAwardHistorys) {
			
		}
	}
	/**
	 * 获取一天宝箱的数量
	 */
	public Integer getAwardNum() {
		return 4;
	}
	/**
	 * 获取宝箱的种类
	 */
	public Integer getAwardKind() {
		//获取当前宝箱的种类
		return 2;
	}
	/**
	 * 开启宝箱的id
	 */
	public void openAwardId() {
		
	}
	/**
	 * 用户可以开启宝箱的时间
	 */
	public void userOpenAwardTime() {
		
	}
	
	
	public Object insertOpenUserTreasureAward(Long userId,String isQuery) {
		
		if("0".equals(isQuery)) {
			//查询
			//this.isMayOpenAwardBox(currentDate, userId, treasureBoxConfigId);
			Map<String,Object> treasureAwardHistoryMap = new HashMap<String,Object>();
			treasureAwardHistoryMap.put("userId", userId);
			TreasureAwardHistory  treasureAwardHistory = treasureAwardHistoryMapper.selectModel(treasureAwardHistoryMap);
			//如果没有领取过宝箱，那么进行初始化
			List<Date> openAwardBoxDates = treasureBoxConfigMapper.getTreasureBoxTimes();
			if(treasureAwardHistory == null) {
				//treasureAwardHistoryMapper.insert(record);
			}
			
		} else if("1".equals(isQuery)) {
			//领取奖励
			
		}
		return null;
	}
}
