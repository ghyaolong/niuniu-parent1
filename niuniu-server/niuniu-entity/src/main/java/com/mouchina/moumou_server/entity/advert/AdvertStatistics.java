package com.mouchina.moumou_server.entity.advert;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 广告统计
 * 
 * @author Administrator
 *
 */
public class AdvertStatistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5514839328555703300L;

	private Long id;
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 已经领取红包金额
	 */
	private Integer sendRedEnvelopeAmount;
	/**
	 * 已经领取红包数
	 */
	private Integer sendRedEnvelopeCount;
	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 广告冻结数
	 */
	private Integer freezeEnvelopeCount;
	/**
	 * 广告浏览次数统计
	 */
	private Long viewTimes;
	/**
	 * 是否已奖励(返现 : 0为未返现、1为已返现)
	 */
	private Integer isAward;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public Integer getSendRedEnvelopeAmount() {
		return sendRedEnvelopeAmount;
	}

	public void setSendRedEnvelopeAmount(Integer sendRedEnvelopeAmount) {
		this.sendRedEnvelopeAmount = sendRedEnvelopeAmount;
	}

	public Integer getSendRedEnvelopeCount() {
		return sendRedEnvelopeCount;
	}

	public void setSendRedEnvelopeCount(Integer sendRedEnvelopeCount) {
		this.sendRedEnvelopeCount = sendRedEnvelopeCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getFreezeEnvelopeCount() {
		return freezeEnvelopeCount;
	}

	public void setFreezeEnvelopeCount(Integer freezeEnvelopeCount) {
		this.freezeEnvelopeCount = freezeEnvelopeCount < 0 ? 0 : freezeEnvelopeCount;
	}

	public Long getViewTimes() {
		return viewTimes;
	}

	public void setViewTimes(Long viewTimes) {
		this.viewTimes = viewTimes;
	}

	public Integer getIsAward() {
		return isAward;
	}

	public void setIsAward(Integer isAward) {
		this.isAward = isAward;
	}

}