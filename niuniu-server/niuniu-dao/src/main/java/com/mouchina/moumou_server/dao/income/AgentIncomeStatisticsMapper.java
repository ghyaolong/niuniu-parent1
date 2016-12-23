package com.mouchina.moumou_server.dao.income;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatistics;
import com.mouchina.moumou_server.entity.vo.AgentIncomeStatisticsVo;

public interface AgentIncomeStatisticsMapper extends BaseMapper<AgentIncomeStatistics, java.lang.Long> {

	@SuppressWarnings("rawtypes")
	List<AgentIncomeStatisticsVo> selectAgentIncomeStatisticsPageList(Map map);
	
	int selectAgentIncomeStatisticsCount(Map map);
	
	int selectBusinessCount(Map map);
	
	int selectAreaAdvertCount(Map map);
	
	int selectExpandAgentCount(Map map);
	
	int deleteAgentIncomeByMap(Map map);
	
	int selectRecommendedCount(Map map);
}
