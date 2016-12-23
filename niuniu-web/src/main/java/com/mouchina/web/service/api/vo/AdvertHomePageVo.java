package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdvertHomePageVo implements Serializable {
	@JsonProperty("advertId")
	private Long id;
	private Long businessId;// 该广告对应的商户id
	private Byte isCollectByLoginUser = 0;// 是否被当前用户收藏
	private Long viewTimes = (long) 0;// 被浏览次数，即人气
	private String businessLogo;// 商户logo(单独设置(存在于Business))
	private String businessName;// 商户名称(单独设置(存在于Business))
	private Integer agentLevel; // 商户等级  2 区县 3中心 4星级	Int
	private Long userId;// 用户标识
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;// 发布时间
	private Integer redEnvelopeAmount;// 总金额
	private String advertName;// 广告名称
	private String advertContent;// 广告内容

	private Integer sendRedEnvelopeCount;// 已领取红包人数(存在于AdvertStatistic)
	private String website;

	public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessLogo() {
		return businessLogo;
	}

	public void setBusinessLogo(String businessLogo) {
		this.businessLogo = businessLogo;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getRedEnvelopeAmount() {
		return redEnvelopeAmount;
	}

	public void setRedEnvelopeAmount(Integer redEnvelopeAmount) {
		this.redEnvelopeAmount = redEnvelopeAmount;
	}

	public String getAdvertName() {
		return advertName;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

	public Integer getSendRedEnvelopeCount() {
		return sendRedEnvelopeCount;
	}

	public void setSendRedEnvelopeCount(Integer sendRedEnvelopeCount) {
		this.sendRedEnvelopeCount = sendRedEnvelopeCount;
	}

	public Byte getIsCollectByLoginUser() {
		return isCollectByLoginUser;
	}

	public void setIsCollectByLoginUser(Byte isCollectByLoginUser) {
		this.isCollectByLoginUser = isCollectByLoginUser;
	}

	public Long getViewTimes() {
		return viewTimes;
	}

	public void setViewTimes(Long viewTimes) {
		this.viewTimes = viewTimes;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

}
