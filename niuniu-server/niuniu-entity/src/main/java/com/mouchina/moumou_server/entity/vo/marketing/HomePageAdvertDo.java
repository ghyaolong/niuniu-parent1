package com.mouchina.moumou_server.entity.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HomePageAdvertDo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2793919776751162778L;
	
	/**
	 * 广告发布者的id
	 */
	private Long userId;
	/**
	 * 广告
	 */
	private Long advertId;
	/**
	 * 广告内容
	 */
	private String advertContent;
	/**
	 * 剩余金额
	 */
	private Double overPlusMoney;
	/**
	 * 访问人数
	 */
	private Integer viewCount;
	/**
	 * 总金额
	 */
	private Double sumAmount;
	/**
	 * 广告总金额
	 */
	private Double redEnvelopeAmount;
	/**
	 * 关注人数
	 */
	private Integer fans;
	/**
	 * 日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

	public Double getOverPlusMoney() {
		return overPlusMoney;
	}

	public void setOverPlusMoney(Double overPlusMoney) {
		this.overPlusMoney = overPlusMoney;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Double getRedEnvelopeAmount() {
		return redEnvelopeAmount;
	}

	public void setRedEnvelopeAmount(Double redEnvelopeAmount) {
		this.redEnvelopeAmount = redEnvelopeAmount;
	}

}
