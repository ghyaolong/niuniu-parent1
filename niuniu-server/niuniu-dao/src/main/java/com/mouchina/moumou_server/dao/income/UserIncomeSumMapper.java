package com.mouchina.moumou_server.dao.income;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;

/**
 * 好友帮赚列表
 * 
 * @author Administrator
 *
 */
public interface UserIncomeSumMapper extends BaseMapper<UserIncomeSum, Long> {

	public int selectUserIncomeSumCount(Map<String, Object> map);

	/**
	 * 用户每日收益列表【3.0版本】
	 * 
	 * @param map
	 * @return
	 */
	public List<UserIncomeSum> selectIncomeList3(Map<String, Object> map);

	/**
	 * 获取好友帮赚的总额
	 * 
	 * @param userId
	 * @return
	 */
	public Integer selectIncomeCount(Long userId);
}
