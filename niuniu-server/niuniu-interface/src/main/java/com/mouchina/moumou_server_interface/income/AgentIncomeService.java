package com.mouchina.moumou_server_interface.income;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.income.AgentIncomeDetail;
import com.mouchina.moumou_server.entity.income.AgentIncomeSum;
import com.mouchina.moumou_server.entity.vo.AgentIncomeStatisticsVo;
import com.mouchina.moumou_server.entity.vo.AgentStatisticsVo;
import com.mouchina.moumou_server_interface.view.IncomeResult;

public interface AgentIncomeService {

	IncomeResult<AgentIncome> selectAgentIncome(Long AgentInocme);

	IncomeResult<AgentIncome> selectAgentIncome(Map map);

	IncomeResult<AgentIncome> addAgentIncome(AgentIncome agentIncome);

	IncomeResult<ListRange<AgentIncome>> selectListAgentIncomePage(Map map);

	IncomeResult<Integer> deleteAgentIncome(Long AgentIncomeId);

	IncomeResult<Integer> updateAgentIncome(AgentIncome AgentIncome);

	IncomeResult<List<AgentIncome>> selectListAgentIncome(Map map);

	IncomeResult<AgentIncomeSum> selectAgentIncomeSum(Long agentIncomeSumId);

	IncomeResult<AgentIncomeSum> selectAgentIncomeSum(Map map);

	IncomeResult<AgentIncomeSum> addAgentIncomeSum(AgentIncomeSum agentIncomeSum);

	IncomeResult<ListRange<AgentIncomeSum>> selectListAgentIncomeSumPage(Map map);

	IncomeResult<Integer> deleteAgentIncomeSum(Long agentIncomeSumId);

	IncomeResult<Integer> updateAgentIncomeSum(AgentIncomeSum agentIncomeSum);

	IncomeResult<List<AgentIncomeSum>> selectListAgentIncomeSum(Map map);

	int insertCalAgentIncomeByMonth(Date date) throws Exception;

	int insertAgentIncomeByMonth(AgentIncome agentIncome, Date startDay, Date endDay);

	IncomeResult<ListRange<AgentIncome>> selectListAgentDayIncomePage(Map map);

	IncomeResult<AgentIncomeDetail> selectAgentIncomeDetail(Long agentIncomeDetailId);

	IncomeResult<AgentIncomeDetail> selectAgentIncomeDetail(Map map);

	IncomeResult<AgentIncomeDetail> addAgentIncomeDetail(AgentIncomeDetail agentIncomeDetail);

	IncomeResult<ListRange<AgentIncomeDetail>> selectListAgentIncomeDetailPage(Map map);

	IncomeResult<Integer> deleteAgentIncomeDetail(Long AgentIncomeDetailId);

	IncomeResult<Integer> updateAgentIncomeDetail(AgentIncomeDetail agentIncomeDetail);

	IncomeResult<List<AgentIncomeDetail>> selectListAgentIncomeDetail(Map map);

	int insertAgentIncomeDetailByDay(AgentIncomeDetail agentIncomeDetail);

	int insertCalAgentIncomeDetailByDay(Date date);

	IncomeResult<ListRange<AgentStatisticsVo>> selectStatisticsPageList(Map<Object, Object> map);
	
	/*计算代理商收益总入口*/
	void processComputeAgentIncome(Advert advert);
	
	ListRange<AgentIncomeStatisticsVo> selectAgentIncomeStatisticsPageList(Map map);
}
