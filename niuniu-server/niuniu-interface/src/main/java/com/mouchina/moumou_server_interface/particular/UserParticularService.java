package com.mouchina.moumou_server_interface.particular;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.vo.user.particular.ConsumeParticularVo;

/**
 * 用户明细
 * 
 * @author Administrator
 *
 */
public interface UserParticularService {

	/**
	 * 收入明细
	 * 
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectEarnParticular(Map<String, Object> map);

	/**
	 * 消费明细
	 * 
	 * @param map
	 */
	public List<ConsumeParticularVo> selectConsumeParticular(Map<String, Object> map);
}
