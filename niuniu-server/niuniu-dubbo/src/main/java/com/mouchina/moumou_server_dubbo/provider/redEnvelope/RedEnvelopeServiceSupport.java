package com.mouchina.moumou_server_dubbo.provider.redEnvelope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server_interface.redEnvelope.RedEnvelopeService;

@Service(value = "redEnvelopeServiceSupport")
public class RedEnvelopeServiceSupport implements RedEnvelopeService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private RedEnvelopeMapper redEnvelopeMapper;

	@Override
	public List<RedEnvelope> selectListRedEnvelope(Map<String, Object> map) {
		return redEnvelopeMapper.selectList(map);
	}

	@Override
	public int selectCount(Map<String,Object> map) {
		return redEnvelopeMapper.selectCount(map);
	}

	@Override
	public RedEnvelope selectModel(Map<String, Object> map) {
		return redEnvelopeMapper.selectModel(map);
	}

	@Override
	public List<Map> getLastRedEnvelopeRecords(int number, long advertId) {
		Map sqlMap = new HashMap<>();
		sqlMap.put("advertId", advertId);
		sqlMap.put("order", "create_time desc");
		List<RedEnvelope> redEnvelopeList = selectListRedEnvelope(sqlMap);
		
		if(redEnvelopeList != null && redEnvelopeList.size() > 0){
			List<Map> returnList = new ArrayList<Map>();
			
			
			int redEnvelopeSize = redEnvelopeList.size();//当前接力福袋广告传递次数
			int loopNum = number <= redEnvelopeSize ? number : redEnvelopeSize;//返回的记录条数
			
			for(int i = 0; i < loopNum; i++){
				Map perRedEnvelopeInfoMap = new HashMap<>();
				RedEnvelope currentRedVo = redEnvelopeList.get(i);
				long userId = currentRedVo.getUserId();//抢包用户id
				String nickName = currentRedVo.getUserNickName();//抢包人昵称
				int awardAmount = currentRedVo.getAmout();//抢包人抢到金额（若为负表示未中奖）
				long time = currentRedVo.getCreateTime().getTime();//参与时间
				perRedEnvelopeInfoMap.put("partakeUserId", userId);
				perRedEnvelopeInfoMap.put("partakeUserNickName", nickName);
				perRedEnvelopeInfoMap.put("partakeUserAmount", awardAmount);
				perRedEnvelopeInfoMap.put("partakeTime", time);
				
				returnList.add(perRedEnvelopeInfoMap);
			}
			
			return returnList;
		}else{
			return null;
		}
		
	}

	@Override
	public List<RedEnvelope> selectLuckyWheelSuccess(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return redEnvelopeMapper.selectLuckyWheelList(map);
	}
}
