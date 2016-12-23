package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户福袋信息导出实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserLuckyBagDetailsVo implements Serializable {

	private Long userId; //用户标识
	
	private String phoneNum; //用户手机号
	
	private String issueTime; //发布时间
	
	private Double luckyBagAmount; //福袋总额(单位:分)
	
	private Integer luckyBagNumber; //福袋数量
	
	private Double purseBalance; //钱袋余额(单位:分)
	
	private Double refundAmount; //退还金额(单位:分)
	
	private String state; //是否领完(1:已领完  2:未领完)
	
	private Double currentuckyPurseBalance; //钱袋余额(单位:分)
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public Double getLuckyBagAmount() {
		return luckyBagAmount;
	}

	public void setLuckyBagAmount(Double luckyBagAmount) {
		this.luckyBagAmount = luckyBagAmount;
	}

	public Integer getLuckyBagNumber() {
		return luckyBagNumber;
	}

	public void setLuckyBagNumber(Integer luckyBagNumber) {
		this.luckyBagNumber = luckyBagNumber;
	}

	public Double getPurseBalance() {
		return purseBalance;
	}

	public void setPurseBalance(Double purseBalance) {
		this.purseBalance = purseBalance;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getCurrentuckyPurseBalance() {
		return currentuckyPurseBalance;
	}

	public void setCurrentuckyPurseBalance(Double currentuckyPurseBalance) {
		this.currentuckyPurseBalance = currentuckyPurseBalance;
	}
	
}
