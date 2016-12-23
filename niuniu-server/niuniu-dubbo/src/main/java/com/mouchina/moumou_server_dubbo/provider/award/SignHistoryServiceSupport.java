package com.mouchina.moumou_server_dubbo.provider.award;

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
import com.mouchina.moumou_server.dao.award.SignConfigMapper;
import com.mouchina.moumou_server.dao.award.SignHistoryMapper;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.award.SignConfig;
import com.mouchina.moumou_server.entity.award.SignHistory;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server_interface.award.SignHistoryService;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;

@Service("signHistoryServiceSupport")
public class SignHistoryServiceSupport implements SignHistoryService {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 是否做过操作	1做过		0没有做过
	 */
	private final static Integer STATUS = 1;
	/**
	 * 分隔符
	 */
	private final static String SEPARATOR = "-";
	
	@Resource
	private SignHistoryMapper signHistoryMapper;
	@Resource
	private SignConfigMapper signConfigMapper;
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
	public SignHistory insertUserSign(Long userId,String isSign) {
		SignHistory signHistory = signHistoryMapper.selectByUserId(userId);
		Date date = new Date();
		
		//是否只做查询操作
		if( signHistory != null && "0".equals(isSign) ) {
			//判断当天是否有过签到
			if(sdfDay.format(date).equals(sdfDay.format(signHistory.getSignTime()))) {
				signHistory.setStatus(1);
				return signHistory;
			}
			signHistory.setStatus(0);
			return signHistory;
		} else if(signHistory == null && "0".equals(isSign)) {
			signHistory = new SignHistory();
			signHistory.setStatus(0);
			return signHistory;
		}
		
		if(signHistory == null || ( signHistory.getCount() >= 7 && !sdfDay.format(date).equals(sdfDay.format(signHistory.getSignTime())) ) ) {
			//是否是第一次签到，作为标记是否是使用添加sql还是执行更新sql
			Boolean isFirst = false;
			if(signHistory == null) isFirst = true;

			//如果第一次签到就增加签到
			signHistory = new SignHistory();
			signHistory.setUserId(userId);
			signHistory.setCount((byte)1);
			signHistory.setHistoryTime(sdf.format(date));
			signHistory.setSignTime(date);
			
			SignConfig signConfig = awardType((byte) 1);
			byte awardType = signConfig.getType();
			signHistory.setType(this.byteToChar(awardType));
			//	奖励值
			Integer awardValue = this.getAward((byte)1,awardType).getValue();
			signHistory.setValue(String.valueOf(awardValue));
			//签到之后就不能再次签到
			signHistory.setStatus(1);
			
			//	调用用户增加经验的接口
			this.updateUserAward(userId, awardType, awardValue);
			
			//如果是第一次就添加，如果不是就更新
			int historyValue = isFirst ? signHistoryMapper.insert(signHistory) : signHistoryMapper.updateByUserId(signHistory);
			
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
			
			return signHistory;
		}
		
		//用户是否签到，如果没有签到才可以进行签到，如果签到那么就无法进行签到
		if(sdfDay.format(date).equals(sdfDay.format(signHistory.getSignTime()))) {
			signHistory.setStatus(STATUS);
			return signHistory;
		}
		
		//已经签过到的用户
		byte currentSignDay = (byte)(signHistory.getCount() + 1);
		SignConfig signConfig = this.awardType(currentSignDay);
		byte awardType = signConfig.getType();
		signHistory.setType(signHistory.getType() + SEPARATOR + this.byteToChar(awardType));
		//	奖励值
		Integer awardValue = this.getAward(currentSignDay,awardType).getValue();
		signHistory.setValue(signHistory.getValue() + SEPARATOR +String.valueOf(awardValue));
		
		// 需要调用用户增加经验的接口
		this.updateUserAward(userId, awardType, awardValue);
		
		signHistory.setCount(currentSignDay );
		signHistory.setHistoryTime(signHistory.getHistoryTime() + SEPARATOR + sdf.format(date));
		signHistory.setSignTime(date);
		//签到之后就不能再次签到
		signHistory.setStatus(1);
		
		//signHistory
		// TODO 此处应该增加明细
		signHistoryMapper.updateByPrimaryKey(signHistory);
		
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
		
		return signHistory;
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
			
		} else if(awardType == 1) {
			//如果是红包	增加红包钱数
			userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + awardValue);
		}
		//更新财富值
		userAssetsMapper.updateByUserId(userAssets);
	}
	
	/**
	 * 获取对应天数的奖励值
	 * @param day			签到的天数
	 * @param awardType		奖品的类型
	 * @return
	 */
	private SignConfig getAward(byte day,byte awardType) {
		Map<String,Byte> map = new HashMap<String,Byte>();
		//获取概率
		map.put("account", day);
		map.put("type", awardType);
		return signConfigMapper.selectModel(map);
	}
	
	/**
	 * 奖励类型	0是红包	1是经验
	 * @param day	签到的天数
	 * @return
	 */
	private SignConfig awardType(Byte day) {
		//查询命中率
		List<Double> hitRates = signConfigMapper.selectHitRate();
		Map<String, Object> hitRateMap = PercentUtil.getRandomPercent(hitRates);
		Double hitRate = (Double) hitRateMap.get("percent");
		
		//查询命中几率
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account", day);
		map.put("hitRate", hitRate);
		return signConfigMapper.selectModel(map);
	}
	
	/**
	 * byte转换成字符串
	 * @return
	 */
	public String byteToChar(byte value) {
		return new Byte(value).toString();
	}
	
	/**
	 * 把分割的字符串转换成map
	 * @param awardType		奖励的类型
	 * @param awardValue	奖励的数值
	 * @return
	 */
	public Map<String,String> charToMap(String awardType,String awardValue) {
		Map<String,String> awardTypeValueMap = new HashMap<String,String>();
		String[] awardTypes = awardType.split(SEPARATOR);
		String[] awardValues = awardValue.split(SEPARATOR);
		for(int i=0 ; i<awardTypes.length ; i++) {
			awardTypeValueMap.put(i +"-"+ awardTypes[i], awardValues[i]);
		}
		return awardTypeValueMap;
	}
	
	
	/**
	 * 查询签到信息
	 */
	public void selectSignInfo(Long userId) {
		Map<String,Object> signHistoryMap = new HashMap<String,Object>();
		signHistoryMap.put("userId", userId);
		List<SignHistory> signHistory = signHistoryMapper.selectList(signHistoryMap);
		//使用集合排序
		//signHistory.sort(null);
		
	}
	
	/**
	 * 增加签到记录
	 */
	public void addSignRecord() {
		
	}
	
}
