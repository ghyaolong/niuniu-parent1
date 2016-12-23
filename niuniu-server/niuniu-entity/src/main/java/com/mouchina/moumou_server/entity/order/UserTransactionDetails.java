package com.mouchina.moumou_server.entity.order;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户交易明细实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserTransactionDetails implements Serializable {
	
	private Long userId; //用户标识
	
	private String phoneNum; //用户手机号
	
	private String userName; //用户名称
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date tradeTime; //交易时间
	
	private Integer tradeType; //交易类型(1:充值    2:提现)
	
	private Double tradeAmount; //交易金额
	
	private String tradeNo; //交易号
	
	private Integer payType; //支付类型(1:支付宝  2:微信)
	
	private Double purseBalance; //钱袋余额(单位:分)
	
	private Double luckyBagBalance; //福袋余额(单位:分)
	
	private String tradeState; //交易状态

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

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
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

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
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
	
}
