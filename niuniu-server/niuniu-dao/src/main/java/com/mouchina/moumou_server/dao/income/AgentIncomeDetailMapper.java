package com.mouchina.moumou_server.dao.income;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeDetail;
import com.mouchina.moumou_server.entity.income.UserIncome;
public interface AgentIncomeDetailMapper extends BaseMapper<AgentIncomeDetail,java.lang.Long> {
	 
	int updateByUserIdAndIncomeDateSelective(Map map);

	List<AgentIncomeDetail> selectIncomePageList(Map map);
	int selectIncomeCount(Map map);
	
	
	
}
