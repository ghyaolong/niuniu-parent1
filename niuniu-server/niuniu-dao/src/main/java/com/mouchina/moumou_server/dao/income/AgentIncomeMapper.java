package com.mouchina.moumou_server.dao.income;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.income.AgentIncome;
import com.mouchina.moumou_server.entity.vo.AgentStatisticsVo;

public interface AgentIncomeMapper extends BaseMapper<AgentIncome, java.lang.Long> {

	int updateByUserIdAndIncomeDateSelective(Map map);

	List<AgentIncome> selectIncomePageList(Map map);

	int selectIncomeCount(Map map);

	List<AgentIncome> selectMonthIncomePageList(Map map);

	int selectMonthIncomeCount(Map map);

	String selectSubAgent(Map<Object, Object> map);

	List<AgentStatisticsVo> selectStatisticsAgent(Map<Object, Object> map);

	int selectStatisticsListCount(Map<Object, Object> map);
}
