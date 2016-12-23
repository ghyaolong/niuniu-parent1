package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;

/**
 * 用户的财富
 * 
 * @author Administrator
 *
 */
public class UserAssets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5608955921722612555L;

	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 可用代金券数
	 */
	private Integer cashCouponCount;
	/**
	 * 积分
	 */
	private Integer integral;
	/**
	 * 现金余额
	 */
	private Integer cashBalance;
	/**
	 * 红包余额
	 */
	private Integer redEnvelopeBalance;
	/**
	 * 虚拟金额
	 */
	private Integer virtualBalance;
	/**
	 * 用户对应openId
	 */
	private String openId;
	/**
	 * 经验值（EXP）
	 */
	private Integer growthValue;
	/**
	 * 消费额
	 */
	private Integer priceCount;
	/**
	 * 订单数
	 */
	private Integer orderCount;
	/**
	 * 提现密码
	 */
	private String withdrawPassword;
	/**
	 * 用户财富表的下标【主要是为了找到用户对应的财富表】
	 */
	private Integer tableNum;
	/**
	 * 抢到的红包总数
	 */
	private double redPackCount;

	public double getRedPackCount() {
		return redPackCount;
	}

	public void setRedPackCount(double redPackCount) {
		this.redPackCount = redPackCount;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(Integer cashBalance) {
		this.cashBalance = cashBalance;
	}

	public Integer getRedEnvelopeBalance() {
		return redEnvelopeBalance;
	}

	public void setRedEnvelopeBalance(Integer redEnvelopeBalance) {
		this.redEnvelopeBalance = redEnvelopeBalance;
	}

	public String getWithdrawPassword() {
		return withdrawPassword;
	}

	public void setWithdrawPassword(String withdrawPassword) {
		this.withdrawPassword = withdrawPassword;
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

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

	public Integer getCashCouponCount() {
		return cashCouponCount;
	}

	public void setCashCouponCount(Integer cashCouponCount) {
		this.cashCouponCount = cashCouponCount;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getPriceCount() {
		return priceCount;
	}

	public void setPriceCount(Integer priceCount) {
		this.priceCount = priceCount;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getVirtualBalance() {
		return virtualBalance;
	}

	public void setVirtualBalance(Integer virtualBalance) {
		this.virtualBalance = virtualBalance;
	}

	public Integer getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(Integer growthValue) {
		this.growthValue = growthValue;
	}

}