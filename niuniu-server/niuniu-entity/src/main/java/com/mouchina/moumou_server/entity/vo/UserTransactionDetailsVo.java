package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户交易明细导出实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserTransactionDetailsVo implements Serializable {
	
	private Long userId; //用户标识
	
	private String phoneNum; //用户手机号
	
	private String userName; //用户名称
	
	private String tradeTime; //交易时间
	
	private String tradeType; //交易类型(1:充值    2:提现)
	
	private Double tradeAmount; //交易金额
	
	private String tradeNo; //交易号
	
	private String payType; //支付类型(1:支付宝  2:微信)
	
	private Double purseBalance; //钱袋余额(单位:分)
	
	private Double luckyBagBalance; //福袋余额(单位:分)
	
	private String tradeState; //交易状态
	
	private Double currentPurseBalance; //钱袋余额(单位:分)
	
	private Double currentuckyBagBalance; //福袋余额(单位:分)

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Double getPurseBalance() {
		return purseBalance;
	}

	public void setPurseBalance(Double purseBalance) {
		this.purseBalance = purseBalance;
	}

	public Double getLuckyBagBalance() {
		return luckyBagBalance;
	}

	public void setLuckyBagBalance(Double luckyBagBalance) {
		this.luckyBagBalance = luckyBagBalance;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public Double getCurrentPurseBalance() {
		return currentPurseBalance;
	}

	public void setCurrentPurseBalance(Double currentPurseBalance) {
		this.currentPurseBalance = currentPurseBalance;
	}

	public Double getCurrentuckyBagBalance() {
		return currentuckyBagBalance;
	}

	public void setCurrentuckyBagBalance(Double currentuckyBagBalance) {
		this.currentuckyBagBalance = currentuckyBagBalance;
	}
	
}
