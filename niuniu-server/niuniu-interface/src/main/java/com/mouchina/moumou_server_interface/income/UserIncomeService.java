package com.mouchina.moumou_server_interface.income;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;
import com.mouchina.moumou_server.entity.vo.user.particular.UserIncomeDo;
import com.mouchina.moumou_server_interface.view.IncomeResult;

public interface UserIncomeService {

	public IncomeResult<UserIncome> selectUserIncome(Long userIncomeId);

	public IncomeResult<UserIncome> selectUserIncome(Map<String, Object> map);

	public IncomeResult<UserIncome> addUserIncome(UserIncome userIncome);

	public IncomeResult<ListRange<UserIncome>> selectListUserIncomePage(Map<String, Object> map);

	public IncomeResult<Integer> deleteUserIncome(Long userIncomeId);

	public IncomeResult<Integer> updateUserIncome(UserIncome userIncome);

	public IncomeResult<List<UserIncome>> selectListUserIncome(Map<String, Object> map);

	public IncomeResult<UserIncomeSum> selectUserIncomeSum(Long userIncomeSumId);

	public IncomeResult<UserIncomeSum> selectUserIncomeSum(Map<String, Object> map);

	public IncomeResult<UserIncomeSum> addUserIncomeSum(UserIncomeSum userIncomeSum);

	public IncomeResult<ListRange<UserIncomeSum>> selectListUserIncomeSumPage(Map<String, Object> map);

	public IncomeResult<Integer> deleteUserIncomeSum(Long userIncomeSumId);

	public IncomeResult<Integer> updateUserIncomeSum(UserIncomeSum userIncomeSum);

	public IncomeResult<List<UserIncomeSum>> selectListUserIncomeSum(Map<String, Object> map);

	public int insertCalUserIncomeByDay(Date date);

	public int insertCalUserInvertAdvertPushlishedByDay(Date date);

	public int insertUserIncomeByDay(UserIncome userIncome);

	public int selectUserIncomeSumCount(Map<String, Object> map);

	/**
	 * 好友帮赚明细列表【3.0】
	 * 
	 * @param map
	 * @return
	 */
	public List<UserIncomeDo> userIncomeDetailList3(Map<String, Object> map);

	/**
	 * 用户每日收益列表【3.0版本】
	 * 
	 * @param userId
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
