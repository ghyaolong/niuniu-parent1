package com.mouchina.moumou_server.entity.advert;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RedEnvelope implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5631794690377132814L;

	private Long id;
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 发布广告用户的id
	 */
	private Long publisherId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户得到金额
	 */
	private Integer amout;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String userAvatar;

	private Byte userSex;

	private String userNickName;

	private String advertLogo;

	private String advertName;

	private Byte state;
	/**
	 * 原始金额
	 */
	private Integer originalAmout;
	/**
	 * 奖励时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date awardTime;

	private Byte ageGroup;
	/**
	 * 生日
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date birthday;
	/**
	 * 广告解冻次数
	 */
	private int advertUnfreezeCount;
	/**
	 * 0红包，1优惠券,2接力福袋3公益 4首页广告福袋
	 */
	private Integer type;
	/**
	 * 月份
	 */
	private String month;

	public Byte getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(Byte ageGroup) {
		this.ageGroup = ageGroup;
	}

	public String getAdvertLogo() {
		return advertLogo;
	}

	public void setAdvertLogo(String advertLogo) {
		this.advertLogo = advertLogo;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}

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

	public Integer getAmout() {
		return amout;
	}

	public void setAmout(Integer amout) {
		this.amout = amout;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar == null ? null : userAvatar.trim();
	}

	public Byte getUserSex() {
		return userSex;
	}

	public void setUserSex(Byte userSex) {
		this.userSex = userSex;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName == null ? null : userNickName.trim();
	}

	public String getAdvertName() {
		return advertName;
	}

	public void setAdverName(String advertName) {
		this.advertName = advertName == null ? null : advertName.trim();
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Integer getOriginalAmout() {
		return originalAmout;
	}

	public void setOriginalAmout(Integer originalAmout) {
		this.originalAmout = originalAmout;
	}

	public Date getAwardTime() {
		return awardTime;
	}

	public void setAwardTime(Date awardTime) {
		this.awardTime = awardTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAdvertUnfreezeCount() {
		return advertUnfreezeCount;
	}

	public void setAdvertUnfreezeCount(int advertUnfreezeCount) {
		this.advertUnfreezeCount = advertUnfreezeCount > 10 ? 10 : advertUnfreezeCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}