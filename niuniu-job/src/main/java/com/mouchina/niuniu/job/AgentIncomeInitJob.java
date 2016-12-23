package com.mouchina.niuniu.job;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatistics;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatisticsEnum;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server_interface.member.UserAgentService;

/**
 * 代理商收益初始化job
 * 每天凌晨执行一次
 * @author Administrator
 *
 */
@Component
public class AgentIncomeInitJob extends ABaseJob {

	@Resource
	private UserAgentService userAgentService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute() {
		try {
			Map map = new HashMap();
			map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
			List<UserAgent> agents = userAgentService.selectUserAgentByMap(map);
			if(agents != null && agents.size() > 0){
				map.clear();
				map.put("incomeDate", DateUtils.formatDate(new Date()));
				map.put("incomeType", AgentIncomeStatisticsEnum.INCOME_TYPE_0.getMarker());
				userAgentService.deleteAgentIncomeByMap(map);
				long beginTime = System.currentTimeMillis();
				log.info(String.format("[任务：代理商收益初始化] 时间：%s 执行开始...", beginTime));
				for(UserAgent agent : agents){
					AgentIncomeStatistics agentIncomeStatistics = getAis(agent);
					userAgentService.addAgentIncome(agentIncomeStatistics);
				}
				long endTime = System.currentTimeMillis();
				log.info(String.format("[任务：代理商收益初始化] 时间：%s 执行结束，耗时：%s 秒", endTime, (endTime-beginTime)/1000));
			}
		} catch (Exception e) {
			log.error("代理商收益初始化job错误信息:" + ExceptionUtils.getStackTrace(e));
		}
	}
	
	private AgentIncomeStatistics getAis(UserAgent userAgent){
		AgentIncomeStatistics incomeStatistics = new AgentIncomeStatistics();
		incomeStatistics.setUserId((long)0);
		incomeStatistics.setIncomeUserId(userAgent.getUserId());
		incomeStatistics.setAgentLevel(userAgent.getAgentLevel());
		incomeStatistics.setIncomeType(AgentIncomeStatisticsEnum.INCOME_TYPE_0.getMarker());
		incomeStatistics.setIncomeAmount(new BigDecimal(0));
		incomeStatistics.setIncomeDate(new Date());
		incomeStatistics.setCreateTime(new Date());
		return incomeStatistics;
	}
}
