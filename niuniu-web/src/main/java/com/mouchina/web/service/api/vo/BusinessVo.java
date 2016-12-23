package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -856290775777340518L;

	@JsonProperty("businessId")
	private Long id;

	private Long userId;

	private String businessName;

	private String businessAddress;

	private String businessTel;
	
	private String businessSite;

	private String businessQq;

	private String businessWeixin;

	private String businessWeibo;

	private String businssCredentialsUp;

	private String businessCredentialsDown;

	private String businessCredentialsFace;

	private String descb;

	private Integer fansSize;
	
	private Integer inviteSize;

	private Integer isFans;

	private String businessAbbreviation; // 商户简称

	private String registrationNumber; // 注册号

	private String applyName; // 申请人姓名

	private String identityCard; // 身份证号码

	private Integer certificationType; // 认证类型(1 个人认证 2 商户认证)
	
	private Integer agentLevel; // 商户等级 2 区县 3中心 4星级
	
	private Integer advertCount; // 广告数
	
	private Byte state;

	private String businessIndustry;

	private String businessLogo;

	private String detailAddress;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modfiyTime;

	public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}

	public Integer getIsFans() {
		return isFans;
	}

	public void setIsFans(Integer isFans) {
		this.isFans = isFans;
	}

	public Integer getFansSize() {
		return fansSize;
	}

	public void setFansSize(Integer fansSize) {
		this.fansSize = fansSize;
	}

	public Integer getInviteSize() {
		return inviteSize;
	}

	public void setInviteSize(Integer inviteSize) {
		this.inviteSize = inviteSize;
	}

	public String getDescb() {
		return descb;
	}

	public void setDescb(String descb) {
		this.descb = descb;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getBusinessLogo() {
		return businessLogo;
	}

	public void setBusinessLogo(String businessLogo) {
		this.businessLogo = businessLogo;
	}

	public String getBusinessIndustry() {
		return businessIndustry;
	}

	public void setBusinessIndustry(String businessIndustry) {
		this.businessIndustry = businessIndustry;
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

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessTel() {
		return businessTel;
	}

	public void setBusinessTel(String businessTel) {
		this.businessTel = businessTel;
	}

	public String getBusinessSite() {
		return businessSite;
	}

	public void setBusinessSite(String businessSite) {
		this.businessSite = businessSite;
	}

	public String getBusinessQq() {
		return businessQq;
	}

	public void setBusinessQq(String businessQq) {
		this.businessQq = businessQq;
	}

	public String getBusinessWeixin() {
		return businessWeixin;
	}

	public void setBusinessWeixin(String businessWeixin) {
		this.businessWeixin = businessWeixin;
	}

	public String getBusinessWeibo() {
		return businessWeibo;
	}

	public void setBusinessWeibo(String businessWeibo) {
		this.businessWeibo = businessWeibo;
	}

	public String getBusinssCredentialsUp() {
		return businssCredentialsUp;
	}

	public void setBusinssCredentialsUp(String businssCredentialsUp) {
		this.businssCredentialsUp = businssCredentialsUp;
	}

	public String getBusinessCredentialsDown() {
		return businessCredentialsDown;
	}

	public void setBusinessCredentialsDown(String businessCredentialsDown) {
		this.businessCredentialsDown = businessCredentialsDown;
	}

	public String getBusinessCredentialsFace() {
		return businessCredentialsFace;
	}

	public void setBusinessCredentialsFace(String businessCredentialsFace) {
		this.businessCredentialsFace = businessCredentialsFace;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModfiyTime() {
		return modfiyTime;
	}

	public void setModfiyTime(Date modfiyTime) {
		this.modfiyTime = modfiyTime;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getBusinessAbbreviation() {
		return businessAbbreviation;
	}

	public void setBusinessAbbreviation(String businessAbbreviation) {
		this.businessAbbreviation = businessAbbreviation;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public Integer getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(Integer certificationType) {
		this.certificationType = certificationType;
	}

	public Integer getAdvertCount() {
		return advertCount;
	}

	public void setAdvertCount(Integer advertCount) {
		this.advertCount = advertCount;
	}

}
