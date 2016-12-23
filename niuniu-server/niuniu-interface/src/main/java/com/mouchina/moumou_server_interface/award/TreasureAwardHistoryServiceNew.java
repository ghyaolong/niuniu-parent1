package com.mouchina.moumou_server_interface.award;

import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;

/**
 * 宝箱的历史记录
 * @author Administrator
 *
 */
public interface TreasureAwardHistoryServiceNew {
	
	/**
	 * 增加用户的寻宝记录
	 * @param userId	用户id
	 * @param isQuery	是否查询
	 * @return
	 */
	public TreasureAwardHistory addOpenUserTreasureAward(Long userId,String isQuery);
	
}
