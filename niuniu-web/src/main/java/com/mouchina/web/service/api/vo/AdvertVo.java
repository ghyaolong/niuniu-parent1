package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server.entity.social.UserComment;

public class AdvertVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6818770600348386495L;

	/**
	 * 广告id
	 */
	@JsonProperty("advertId")
	private Long id;
	/**
	 * 广告类型【0普通福袋 1优惠券福袋 2公告福袋 3优惠券圈子 4接力福袋 5首页广告 6定时广告 7运营活动广告(官方) 8定时活动广告(官方)】
	 */
	private Integer advertType;
	/**
	 * 广告状态【状态 0审核 1已审核 2暂停 3发送完成 4未通过 5超时完成】
	 */
	private Byte state;
	/**
	 * 用户id
	 */
	private Long userId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date modifyTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date endTime;

	/**
	 * 红包的类型【现在只有靠运气 1】
	 */
	private Integer redEnvelopeType;

	/**
	 * 红包总金额
	 */
	private Integer redEnvelopeAmount = 0;

	/**
	 * 红包的数量
	 */
	private Integer redEnvelopeCount = 0;

	/**
	 * 广告名称
	 */
	private String advertName;

	/**
	 * 广告内容
	 */
	private String advertContent;

	/**
	 * 广告精准投放_城市
	 */
	private String adverConditionCity;

	/**
	 * 广告精准投放_区域
	 */
	private String adverConditionArea;

	/**
	 * 广告精准投放区域_省
	 */
	private String adverConditionProvince;

	/**
	 * 广告精准投放_距离
	 */
	private Integer adverCondtionDistance;

	private Integer adverCondtionSex;

	private Integer adverCondtionAgeScope;

	/**
	 * 网店名称
	 */
	private String websiteName;
	/**
	 * 薪水
	 */
	private String salaryScope;

	private String timeScope;
	/**
	 * 婚姻状态 0未婚 1已婚
	 */
	private Byte married;
	/**
	 * 信息标签 0美食 1服装 2生活 3数码 4信息 5求职 6吐槽 7交友 8跑腿 9求助 10顺风车 11其他
	 */
	private Integer labelType;
	/**
	 * 用户身份类型(0:普通用户 1:个人认证  2:商户认证 3:区县代理商 4:中心代理商 5:星级代理商)
	 */
	private Integer certificationType;
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 用户头像
	 */
	private String avatar;
	/**
	 * 活动关联的id
	 */
	private Long relationId;
	
	private String province; //省份
	
	private String city; //市
	
	private String area; //区
	
	private String businessTel; //商户电话
	
	private String businessAddress; //商户详细地址
	
	private Integer viewTimes = 0;// 被浏览次数，即人气
	
	private Integer balance = 0; //单位分
	
	private Integer userLevel; //用户等级
	
	private String shopSign; //店铺门头图片
	
	private String firstPic; //产品简介及链接地址
	
	private String secondPic; //产品简介及链接地址
	
	private String thirdPic; //产品简介及链接地址
	
	private String fourPic; //产品简介及链接地址
	
	private String fivePic; //产品简介及链接地址
	
	private String sixPic; //产品简介及链接地址
	
	private Integer sex; //性别    0 男    1 女
	
	private Integer fansCount; //粉丝数量(自己被关注次数)
	
	private String hobby; //爱好
	
	private String work; //职业
	
	private Double longitude; //经度

	private Double latitude; //纬度
	
	private List<UserCommentVo> likes;  //点赞数据集合
	
	private List<UserCommentVo> comments;  //评论数据集合
	
	private Integer couponType; //优惠券类型(1现金券 2折扣券 3实物券)
	
	private String couponContent; //优惠券内容(1现金券对应money 2折扣券对应discount 3实物券对应physical_volume)
	
	private Integer drawState; //领取状态(0未领取 1已领取)
	
	private Integer followState; //关注状态(0未关注 1已关注)
	
	private String businessName; //商户简称
	
	private long countDownTime; //倒计时(单位:秒)，当福袋为定时福袋时有数据 
	
	private String preferredPic; //首推图片
	
	private String tempContent;//用来存放额外信息
	
	private List<ActivityConfig> activityConfigs; //运营活动奖品信息
	
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
		this.timeScope = timeScope;
	}

	public String getSalaryScope() {
		return salaryScope;
	}

	public void setSalaryScope(String salaryScope) {
		this.salaryScope = salaryScope;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
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

	public Integer getLabelType() {
		return labelType;
	}

	public void setLabelType(Integer labelType) {
		this.labelType = labelType;
	}

	public Integer getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(Integer certificationType) {
		this.certificationType = certificationType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
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

	public Integer getViewTimes() {
		return viewTimes;
	}

	public void setViewTimes(Integer viewTimes) {
		this.viewTimes = viewTimes;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public String getShopSign() {
		return shopSign;
	}

	public void setShopSign(String shopSign) {
		this.shopSign = shopSign;
	}

	public String getFirstPic() {
		return firstPic;
	}

	public void setFirstPic(String firstPic) {
		this.firstPic = firstPic;
	}

	public String getSecondPic() {
		return secondPic;
	}

	public void setSecondPic(String secondPic) {
		this.secondPic = secondPic;
	}

	public String getThirdPic() {
		return thirdPic;
	}

	public void setThirdPic(String thirdPic) {
		this.thirdPic = thirdPic;
	}

	public String getFourPic() {
		return fourPic;
	}

	public void setFourPic(String fourPic) {
		this.fourPic = fourPic;
	}

	public String getFivePic() {
		return fivePic;
	}

	public void setFivePic(String fivePic) {
		this.fivePic = fivePic;
	}

	public String getSixPic() {
		return sixPic;
	}

	public void setSixPic(String sixPic) {
		this.sixPic = sixPic;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getFansCount() {
		return fansCount;
	}

	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
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

	public List<UserCommentVo> getLikes() {
		return likes;
	}

	public void setLikes(List<UserCommentVo> likes) {
		this.likes = likes;
	}

	public List<UserCommentVo> getComments() {
		return comments;
	}

	public void setComments(List<UserCommentVo> comments) {
		this.comments = comments;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public String getCouponContent() {
		return couponContent;
	}

	public void setCouponContent(String couponContent) {
		this.couponContent = couponContent;
	}

	public Integer getDrawState() {
		return drawState;
	}

	public void setDrawState(Integer drawState) {
		this.drawState = drawState;
	}

	public Integer getFollowState() {
		return followState;
	}

	public void setFollowState(Integer followState) {
		this.followState = followState;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public long getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(long countDownTime) {
		this.countDownTime = countDownTime;
	}

	public String getPreferredPic() {
		return preferredPic;
	}

	public void setPreferredPic(String preferredPic) {
		this.preferredPic = preferredPic;
	}

	public List<ActivityConfig> getActivityConfigs() {
		return activityConfigs;
	}

	public void setActivityConfigs(List<ActivityConfig> activityConfigs) {
		this.activityConfigs = activityConfigs;
	}

	public String getTempContent() {
		return tempContent;
	}

	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}

}