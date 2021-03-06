package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;

/**
 * 代理商推广收益vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AgentPromotionVo implements Serializable{

	private String incomeDate; //日期
	private Long incomeUserId; //用户ID
	private String phone; //手机号码
	private String nickName; //用户昵称
	private String agentLevel; //代理级别
	private Integer businessTotal; //商家数量
	private Double businessTotallIncome; //商家总收益
	private Integer oneLevelBusinessTotal; //一级商家数量
	private Double oneLevelBusinessIncome; //一级商家收益
	private Integer twoLevelBusinessTotal; //二级商家数量
	private Double twoLevelBusinessIncome; //二级商家收益
	private Integer threeLevelBusinessTotal; //三级商家数量
	private Double threeLevelBusinessIncome; //三级商家收益
	private String agentArea; //代理区域
	private Double areaIncome; //区域收益
	private Double totalIncome; //总收益
	
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	public Long getIncomeUserId() {
		return incomeUserId;
	}
	public void setIncomeUserId(Long incomeUserId) {
		this.incomeUserId = incomeUserId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public Integer getBusinessTotal() {
		return businessTotal;
	}
	public void setBusinessTotal(Integer businessTotal) {
		this.businessTotal = businessTotal;
	}
	public Double getBusinessTotallIncome() {
		return businessTotallIncome;
	}
	public void setBusinessTotallIncome(Double businessTotallIncome) {
		this.businessTotallIncome = businessTotallIncome;
	}
	public Integer getOneLevelBusinessTotal() {
		return oneLevelBusinessTotal;
	}
	public void setOneLevelBusinessTotal(Integer oneLevelBusinessTotal) {
		this.oneLevelBusinessTotal = oneLevelBusinessTotal;
	}
	public Double getOneLevelBusinessIncome() {
		return oneLevelBusinessIncome;
	}
	public void setOneLevelBusinessIncome(Double oneLevelBusinessIncome) {
		this.oneLevelBusinessIncome = oneLevelBusinessIncome;
	}
	public Integer getTwoLevelBusinessTotal() {
		return twoLevelBusinessTotal;
	}
	public void setTwoLevelBusinessTotal(Integer twoLevelBusinessTotal) {
		this.twoLevelBusinessTotal = twoLevelBusinessTotal;
	}
	public Double getTwoLevelBusinessIncome() {
		return twoLevelBusinessIncome;
	}
	public void setTwoLevelBusinessIncome(Double twoLevelBusinessIncome) {
		this.twoLevelBusinessIncome = twoLevelBusinessIncome;
	}
	public Integer getThreeLevelBusinessTotal() {
		return threeLevelBusinessTotal;
	}
	public void setThreeLevelBusinessTotal(Integer threeLevelBusinessTotal) {
		this.threeLevelBusinessTotal = threeLevelBusinessTotal;
	}
	public Double getThreeLevelBusinessIncome() {
		return threeLevelBusinessIncome;
	}
	public void setThreeLevelBusinessIncome(Double threeLevelBusinessIncome) {
		this.threeLevelBusinessIncome = threeLevelBusinessIncome;
	}
	public String getAgentArea() {
		return agentArea;
	}
	public void setAgentArea(String agentArea) {
		this.agentArea = agentArea;
	}
	public Double getAreaIncome() {
		return areaIncome;
	}
	public void setAreaIncome(Double areaIncome) {
		this.areaIncome = areaIncome;
	}
	public Double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}
	
}
