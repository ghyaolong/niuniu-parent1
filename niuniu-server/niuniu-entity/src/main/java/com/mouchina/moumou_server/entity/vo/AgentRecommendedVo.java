package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;

/**
 * 代理商推荐提成收益vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AgentRecommendedVo implements Serializable{

	private String incomeDate; //日期
	private Long incomeUserId; //用户ID
	private String phone; //手机号码
	private String nickName; //用户昵称
	private Integer starAgentTotal; //推荐星级代理数
	private Double starAgentIncome; //推荐星级代理收益
	private Integer centreAgentTotal; //推荐中心代理数
	private Double centreAgentIncome; //推荐中心代理收益
	private Integer countyAgentTotal; //推荐区县代理数
	private Double countyAgentIncome; //推荐区县代理收益
	private Double promotionAgentIncome; //推荐代理总收益
	
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
	public Integer getStarAgentTotal() {
		return starAgentTotal;
	}
	public void setStarAgentTotal(Integer starAgentTotal) {
		this.starAgentTotal = starAgentTotal;
	}
	public Double getStarAgentIncome() {
		return starAgentIncome;
	}
	public void setStarAgentIncome(Double starAgentIncome) {
		this.starAgentIncome = starAgentIncome;
	}
	public Integer getCentreAgentTotal() {
		return centreAgentTotal;
	}
	public void setCentreAgentTotal(Integer centreAgentTotal) {
		this.centreAgentTotal = centreAgentTotal;
	}
	public Double getCentreAgentIncome() {
		return centreAgentIncome;
	}
	public void setCentreAgentIncome(Double centreAgentIncome) {
		this.centreAgentIncome = centreAgentIncome;
	}
	public Integer getCountyAgentTotal() {
		return countyAgentTotal;
	}
	public void setCountyAgentTotal(Integer countyAgentTotal) {
		this.countyAgentTotal = countyAgentTotal;
	}
	public Double getCountyAgentIncome() {
		return countyAgentIncome;
	}
	public void setCountyAgentIncome(Double countyAgentIncome) {
		this.countyAgentIncome = countyAgentIncome;
	}
	public Double getPromotionAgentIncome() {
		return promotionAgentIncome;
	}
	public void setPromotionAgentIncome(Double promotionAgentIncome) {
		this.promotionAgentIncome = promotionAgentIncome;
	}
	
}
