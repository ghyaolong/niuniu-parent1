package com.mouchina.moumou_server.entity.advert;

import com.mouchina.base.entity.BaseEntity;

public class RedEnvelopeTop extends BaseEntity {
	private static final long serialVersionUID = -3240062075157543952L;

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
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 性别:0男1女
	 */
	private Byte sex;
	/**
	 * 用户地址
	 */
	private String address;
	/**
	 * 用户身份类型：0:普通用户 1:个人认证  2:商户认证 3:区县代理商 4:中心代理商 5:星级代理商
	 */
	private Integer certificationType;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(Integer certificationType) {
		this.certificationType = certificationType;
	}

	@Override
	public String toString() {
		return "RedEnvelopeTop [userId=" + userId + ", userAvatar=" + userAvatar + ", userNickName=" + userNickName
				+ ", amountSum=" + amountSum + ", topNum=" + topNum + ", total=" + total + ", phone=" + phone + ", sex="
				+ sex + ", address=" + address + ", certificationType=" + certificationType + "]";
	}

}
