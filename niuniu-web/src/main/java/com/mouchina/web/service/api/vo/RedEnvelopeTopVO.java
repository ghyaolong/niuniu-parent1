package com.mouchina.web.service.api.vo;

import java.io.Serializable;

import com.mouchina.base.entity.BaseEntity;

public class RedEnvelopeTopVO extends BaseEntity{
	private static final long serialVersionUID = 5903780573292960181L;
	/**
	 * 用户名
	 */
	private String userId;
	/**
	 * 用户头像
	 */
	private String userAvatar;
	/**
	 * 用户昵称
	 */
	private String userNickName;
	/**
	 * 领取金额合计
	 */
	private double amountSum;
	/**
	 * 排名
	 */
	private int topNum;
	/**
	 * 领取总数
	 */
	private int total;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public double getAmountSum() {
		return amountSum;
	}
	public void setAmountSum(double amountSum) {
		this.amountSum = amountSum;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTopNum() {
		return topNum;
	}
	public void setTopNum(int topNum) {
		this.topNum = topNum;
	}
}
