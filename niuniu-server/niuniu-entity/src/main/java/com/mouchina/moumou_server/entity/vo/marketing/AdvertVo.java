package com.mouchina.moumou_server.entity.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AdvertVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5057935222171504195L;

	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 金额
	 */
	private Double amount;
	/**
	 * 广告创建的时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 浏览次数
	 */
	private Integer viewCount;
	/**
	 * 广告当前的状态 0审核 1已审核 2暂停 3发送完成 4未通过 5超时完成
	 */
	private Integer state;
	/**
	 * 覆盖人数
	 */
	private Integer getRedEnvelopeCount;
	/**
	 * 广告的内容，json中包含广告的图片
	 */
	private String advertContent;
	/**
	 * 已经领取红包金额
	 */
	private Integer redEnvelopeAmount;
	/**
	 * 已经领取红包数
	 */
	private Integer redEnvelopeCount;

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getGetRedEnvelopeCount() {
		return getRedEnvelopeCount;
	}

	public void setGetRedEnvelopeCount(Integer getRedEnvelopeCount) {
		this.getRedEnvelopeCount = getRedEnvelopeCount;
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

	public Integer getRedEnvelopeAmount() {
		return redEnvelopeAmount;
	}

	public void setRedEnvelopeAmount(Integer redEnvelopeAmount) {
		this.redEnvelopeAmount = redEnvelopeAmount;
	}

	public Integer getRedEnvelopeCount() {
		return redEnvelopeCount;
	}

	public void setRedEnvelopeCount(Integer redEnvelopeCount) {
		this.redEnvelopeCount = redEnvelopeCount;
	}
	
	
}
