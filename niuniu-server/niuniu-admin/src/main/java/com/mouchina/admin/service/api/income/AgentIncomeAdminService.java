package com.mouchina.admin.service.api.income;

import java.util.Date;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server_interface.view.IncomeResult;

public interface AgentIncomeAdminService {
	int insertCalAgentIncomeDetailByDay(Date date);
	int insertCalAgentIncomeByMonth(Date date);
	
	IncomeResult<ListRange<AgentIncome>> selectListAgentIncomePage(Map map);

	IncomeResult<ListRange<AgentIncomeSum>> selectListAgentIncomeSumPage(Map map);

	IncomeResult<Integer> updateAgentIncomeSum(AgentIncomeSum agentIncomeSum);

	IncomeResult<AgentIncomeSum> selectAgentIncomeSum(long agentIncomeSumId);
}
