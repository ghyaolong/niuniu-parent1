package com.mouchina.moumou_server_interface.redEnvelope;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.advert.RedEnvelope;

public interface RedEnvelopeService {

	List<RedEnvelope> selectListRedEnvelope(Map<String, Object> map);

	int selectCount(Map<String, Object> map);
	
	RedEnvelope selectModel(Map<String, Object> map);
	
	/**
	 * 返回接力福袋最后若干条抢包信息(用户id,用户昵称，金额，抢包时间),如果传入的number值大于传递次数，则只返回传递次数条信息
	 * @param number 返回最后number条抢包信息
	 * @param advertId 接力红包id
	 * @return
	 */
	List<Map> getLastRedEnvelopeRecords(int number,long advertId);
	/**
	 * 查询幸运转轮中奖人信息
	 * @param advertId
	 * @return
	 */
	List<RedEnvelope> selectLuckyWheelSuccess(Map<String,Object> map);
}
