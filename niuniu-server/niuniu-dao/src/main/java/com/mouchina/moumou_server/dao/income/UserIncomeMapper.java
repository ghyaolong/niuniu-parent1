package com.mouchina.moumou_server.dao.income;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.vo.user.particular.UserIncomeDo;

/**
 * 好友帮赚明细
 * 
 * @author Administrator
 *
 */
public interface UserIncomeMapper extends BaseMapper<UserIncome, Long> {

	public int selectIncomeCount(Map<String, Object> map);

	public List<UserIncome> selectIncomePageList(Map<String, Object> map);

	public int updateByUserIdAndIncomeDateSelective(Map<String, Object> map);

	/**
	 * 好友帮赚明细
	 * 
	 * @param map
	 * @return
	 */
	public List<UserIncomeDo> userIncomeDetailList3(Map<String, Object> map);
}
