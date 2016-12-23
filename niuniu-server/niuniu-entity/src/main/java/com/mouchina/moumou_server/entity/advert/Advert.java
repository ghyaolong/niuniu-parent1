package com.mouchina.moumou_server.entity.advert;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 广告的实体类
 * 
 * @author Administrator
 *
 */
public class Advert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7068864043620532747L;

	private Long id;
	/**
	 * 0普通福袋 1优惠券福袋 2公告福袋 3优惠券圈子 4接力福袋 5首页广告 6定时广告 7运营活动广告(官方)8定时活动广告(官方)9说说(圈子)10公益广告'
	 */
	private Integer advertType;
	/**
	 * 状态 0审核 1已审核 2暂停 3发送完成 4未通过 5超时完成
	 */
	private Byte state;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date modifyTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date endTime;

	private Integer redEnvelopeType;

	private Integer redEnvelopeAmount;

	private Integer redEnvelopeCount;

	private String advertName;

	private String advertContent;

	private String adverConditionCity;

	private String adverConditionArea;

	private String adverConditionProvince;

	private Integer adverCondtionDistance;

	private Integer adverCondtionSex;
	private Integer adverCondtionAgeScope;

	private Integer commentSize;//评论数

	private Integer praisedSize;//点赞数

	private String website;

	private String salaryScope;

	private String timeScope;
	/**
	 * 信息标签 0美食 1服装 2生活 3数码 4信息 5求职 6吐槽 7交友 8跑腿 9求助 10顺风车 11其他
	 */
	private Integer labelType;

	private Byte married;

	private String preferredPic;
	/**
	 * 当前发广告用户所在区域code
	 */
	private String areaCode;
	/**
	 * 活动关联ID
	 */
	private Long relationId;
	/**
	 * 网店名称
	 */
	private String websiteName;
	
	private Integer certificationType; //用户身份类型(0:普通用户 1:个人认证  2:商户认证 3:区县代理商 4:中心代理商 5:星级代理商)

	private String province; //省份
	
	private String city; //市
	
	private String area; //区
	
	private String businessTel; //商户电话
	
	private String businessAddress; //商户详细地址
	
	private String hobby; //爱好
	
	private String work; //职业
	
	private Double longitude; //经度

	private Double latitude; //纬度

	public String getPreferredPic() {
		return preferredPic;
	}

	public void setPreferredPic(String preferredPic) {
		this.preferredPic = preferredPic;
	}

	public Byte getMarried() {
		return married;
	}

	public void setMarried(Byte married) {
		this.married = married;
	}

	public String getTimeScope() {
		return timeScope;
	}

	public void setTimeScope(String timeScope) {
		this.timeScope = timeScope == null ? null : timeScope.trim();
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website == null ? null : website.trim();
	}

	public String getSalaryScope() {
		return salaryScope;
	}

	public void setSalaryScope(String salaryScope) {
		this.salaryScope = salaryScope == null ? null : salaryScope.trim();
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName == null ? null : websiteName.trim();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAdvertType() {
		return advertType;
	}

	public void setAdvertType(Integer advertType) {
		this.advertType = advertType;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getRedEnvelopeType() {
		return redEnvelopeType;
	}

	public void setRedEnvelopeType(Integer redEnvelopeType) {
		this.redEnvelopeType = redEnvelopeType;
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

	public String getAdvertName() {
		return advertName;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName == null ? null : advertName.trim();
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent == null ? null : advertContent.trim();
	}

	public String getAdverConditionCity() {
		return adverConditionCity;
	}

	public void setAdverConditionCity(String adverConditionCity) {
		this.adverConditionCity = adverConditionCity == null ? null : adverConditionCity.trim();
	}

	public String getAdverConditionArea() {
		return adverConditionArea;
	}

	public void setAdverConditionArea(String adverConditionArea) {
		this.adverConditionArea = adverConditionArea == null ? null : adverConditionArea.trim();
	}

	public String getAdverConditionProvince() {
		return adverConditionProvince;
	}

	public void setAdverConditionProvince(String adverConditionProvince) {
		this.adverConditionProvince = adverConditionProvince == null ? null : adverConditionProvince.trim();
	}

	public Integer getAdverCondtionDistance() {
		return adverCondtionDistance;
	}

	public void setAdverCondtionDistance(Integer adverCondtionDistance) {
		this.adverCondtionDistance = adverCondtionDistance;
	}

	public Integer getAdverCondtionSex() {
		return adverCondtionSex;
	}

	public void setAdverCondtionSex(Integer adverCondtionSex) {
		this.adverCondtionSex = adverCondtionSex;
	}

	public Integer getAdverCondtionAgeScope() {
		return adverCondtionAgeScope;
	}

	public void setAdverCondtionAgeScope(Integer adverCondtionAgeScope) {
		this.adverCondtionAgeScope = adverCondtionAgeScope;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getLabelType() {
		return labelType;
	}

	public void setLabelType(Integer labelType) {
		this.labelType = labelType;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public Integer getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(Integer certificationType) {
		this.certificationType = certificationType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBusinessTel() {
		return businessTel;
	}

	public void setBusinessTel(String businessTel) {
		this.businessTel = businessTel;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public Integer getCommentSize() {
		return commentSize;
	}

	public void setCommentSize(Integer commentSize) {
		this.commentSize = commentSize;
	}

	public Integer getPraisedSize() {
		return praisedSize;
	}

	public void setPraisedSize(Integer praisedSize) {
		this.praisedSize = praisedSize;
	}
	
	@Override
	public String toString() {
		return "Advert [id=" + id + ", advertType=" + advertType + ", state=" + state + ", userId=" + userId
				+ ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", redEnvelopeType=" + redEnvelopeType + ", redEnvelopeAmount="
				+ redEnvelopeAmount + ", redEnvelopeCount=" + redEnvelopeCount + ", advertName=" + advertName
				+ ", advertContent=" + advertContent + ", adverConditionCity=" + adverConditionCity
				+ ", adverConditionArea=" + adverConditionArea + ", adverConditionProvince=" + adverConditionProvince
				+ ", salaryScope=" + salaryScope + ", timeScope=" + timeScope + ", married=" + married
				+ ", preferredPic=" + preferredPic + ", areaCode=" + areaCode + ", websiteName=" + websiteName + "]";
	}
	
	private String businessName;//商户名称
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}