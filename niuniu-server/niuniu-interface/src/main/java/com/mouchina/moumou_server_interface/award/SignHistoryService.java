package com.mouchina.moumou_server_interface.award;

import com.mouchina.moumou_server.entity.award.SignHistory;

/**
 * 签到历史
 * @author Administrator
 *
 */
public interface SignHistoryService {

	/**
	 * 用户签到
	 * @param userId	用户id
	 * @return
	 */
	public SignHistory insertUserSign(Long userId,String isSign);
	
}
