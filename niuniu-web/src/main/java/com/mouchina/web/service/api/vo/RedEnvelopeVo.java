package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RedEnvelopeVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7360125437186089446L;

	@JsonIgnore
	private Long id;

	private Long advertId;

	private Long publisherId;

	private Long userId;

	private Integer amout;

	@JsonIgnore
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String userAvatar;

	private Byte userSex;

	private String userNickName;

	private String advertLogo;

	private String advertName;

	private Byte state;

	@JsonIgnore
	private Integer originalAmout;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date awardTime;

	private Byte ageGroup;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 奖品等级
	 */
	private Integer awardRank;
	/**
	 * 奖品名称
	 */
	private String prize;
	/**
	 * 奖品图片
	 */
	private String prizePic;

	public Byte getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(Byte ageGroup) {
		this.ageGroup = ageGroup;
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

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
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

	public String getAdvertLogo() {
		return advertLogo;
	}

	public void setAdvertLogo(String advertLogo) {
		this.advertLogo = advertLogo;
	}

	public String getAdvertName() {
		return advertName;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}

	public void setAwardTime(Date awardTime) {
		this.awardTime = awardTime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getAwardRank() {
		return awardRank;
	}

	public void setAwardRank(Integer awardRank) {
		this.awardRank = awardRank;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getPrizePic() {
		return prizePic;
	}

	public void setPrizePic(String prizePic) {
		this.prizePic = prizePic;
	}
}
