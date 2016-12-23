package com.mouchina.moumou_server.entity.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 营销数据详情
 * 
 * @author Administrator
 *
 */
public class MarketDetailVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8279159351930376676L;

	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 发布者的id
	 */
	private Long publisherId;
	/**
	 * 抢红包用户的id
	 */
	private Long userId;
	/**
	 * 抢到的金额
	 */
	private Integer amount;
	/**
	 * 抢红包的时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 用户的性别【0男 1女 2不限】
	 */
	private Integer userSex;
	/**
	 * 用户的昵称
	 */
	private String userNickName;
	/**
	 * 发布用户认证信息(0普通用户1个人认证2商户认证3区县4中心5星级)
	 */
	private Integer publishUserCertificate;
	/**
	 * 用户头像
	 */
	private String userAvatar;
	/**
	 * 优惠券专用【做是否核销使用】
	 */
	private String isCancel;

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public Integer getPublishUserCertificate() {
		return publishUserCertificate;
	}

	public void setPublishUserCertificate(Integer publishUserCertificate) {
		this.publishUserCertificate = publishUserCertificate;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

}
