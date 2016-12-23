package com.mouchina.moumou_server.entity.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RelayLuckBagAdvertDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4659813041235062701L;

	/**
	 * 广告类型
	 */
	private Integer advertType;
	/**
	 * 广告发布者的id
	 */
	private Long userId;
	/**
	 * 基础红包的金额
	 */
	private Double redEnvelopeAmount;
	/**
	 * 广告创建的时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 红包的累计金额
	 */
	private Double amount;
	/**
	 * 广告浏览的人数总和
	 */
	private Integer userViewCount;
	/**
	 * 状态 0审核 1已审核 2暂停 3发送完成 4未通过 5超时完成
	 */
	private Integer isPublish;
	/**
	 * 该接力红包是否已经开启
	 */
	private Integer isOpen;
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 开启红包用户的用户名
	 */
	private String userNickName;
	/**
	 * 广告内容【包含图片】
	 */
	private String advertContent;

	public Integer getAdvertType() {
		return advertType;
	}

	public void setAdvertType(Integer advertType) {
		this.advertType = advertType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getRedEnvelopeAmount() {
		return redEnvelopeAmount;
	}

	public void setRedEnvelopeAmount(Double redEnvelopeAmount) {
		this.redEnvelopeAmount = redEnvelopeAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getUserViewCount() {
		return userViewCount;
	}

	public void setUserViewCount(Integer userViewCount) {
		this.userViewCount = userViewCount;
	}

	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

}
