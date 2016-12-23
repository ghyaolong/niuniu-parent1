package com.mouchina.moumou_server_interface.income;


import java.util.Map;

import com.mouchina.moumou_server.entity.income.UserIncomeAward;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 活动奖励收入service
 * @author Administrator
 *
 */
public interface UserIncomeAwardService {

	/**
	 * 插入奖励信息
	 * @param award
	 * @return
	 */
	UserResult<UserIncomeAward> addIncomeAward(UserIncomeAward award);
	
	/**
	 * 查询奖励信息
	 * @param map
	 * @return
	 */
	UserIncomeAward selectUserIncomeAward(Map map);
	
}
