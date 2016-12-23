package com.mouchina.moumou_server.entity.income;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商收益统计实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AgentIncomeStatistics implements Serializable {
	
	private Long id; //主键
	private Long userId; //发布广告用户id
	private Long incomeUserId; //收益用户id
	private Integer agentLevel; //代理商等级(2:区县代理商 3:中心代理商 4:星级代理商)
	private Integer incomeType; //收益类型 (1:一级商家收益 2:二级商家收益 3:三级商家收益 4:区域收益 5:推荐区县收益 6:推荐中心收益 7:推荐星级收益)
	private BigDecimal incomeAmount; //收益额(单位:分)
	private Date incomeDate; //收益日期
	private Date createTime; //创建时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getIncomeUserId() {
		return incomeUserId;
	}
	public void setIncomeUserId(Long incomeUserId) {
		this.incomeUserId = incomeUserId;
	}
	public Integer getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}
	public Integer getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(Integer incomeType) {
		this.incomeType = incomeType;
	}
	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}
	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	public Date getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
