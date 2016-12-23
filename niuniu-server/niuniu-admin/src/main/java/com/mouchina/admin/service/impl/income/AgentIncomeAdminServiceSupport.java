package com.mouchina.admin.service.impl.income;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.income.AgentIncomeAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server_interface.income.AgentIncomeService;
import com.mouchina.moumou_server_interface.view.IncomeResult;

@Service
public class AgentIncomeAdminServiceSupport  implements AgentIncomeAdminService{

	
	@Resource
	AgentIncomeService  agentIncomeService;
	


	@Override
	public int insertCalAgentIncomeDetailByDay(Date date) {
		// TODO Auto-generated method stub
		
		
		
		
		return agentIncomeService.insertCalAgentIncomeDetailByDay(date);
	}
	@Override
	public int insertCalAgentIncomeByMonth(Date date) {
		// TODO Auto-generated method stub
		
		
		try {
			return agentIncomeService.insertCalAgentIncomeByMonth(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public IncomeResult<ListRange<AgentIncome>> selectListAgentIncomePage(Map map){
		return agentIncomeService.selectListAgentIncomePage(map);
	}


	@Override
	public IncomeResult<ListRange<AgentIncomeSum>> selectListAgentIncomeSumPage(Map map){
		return agentIncomeService.selectListAgentIncomeSumPage(map);
	}
	@Override
	public IncomeResult<Integer> updateAgentIncomeSum(AgentIncomeSum agentIncomeSum){
		return agentIncomeService.updateAgentIncomeSum(agentIncomeSum);
	}

	@Override
	public IncomeResult<AgentIncomeSum>  selectAgentIncomeSum(long  agentIncomeSumId){
		return agentIncomeService.selectAgentIncomeSum(agentIncomeSumId);
	}

	
}
