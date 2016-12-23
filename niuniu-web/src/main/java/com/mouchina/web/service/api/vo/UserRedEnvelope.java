package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserRedEnvelope  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userAvatar;
	
	private String UserNickName;
	
	private int sumAmount;
	
	private int count;

	private long userId;
	
	private String prizeName;//奖品名称
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date prizeDate;//中奖时间
	
	private String prizeUrl;//奖品图片
	
	private int prizeCount;//参与活动次数
	
	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}


	public int getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(int sumAmount) {
		this.sumAmount = sumAmount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public Date getPrizeDate() {
		return prizeDate;
	}

	public void setPrizeDate(Date prizeDate) {
		this.prizeDate = prizeDate;
	}

	public String getPrizeUrl() {
		return prizeUrl;
	}

	public void setPrizeUrl(String prizeUrl) {
		this.prizeUrl = prizeUrl;
	}

	public int getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(int prizeCount) {
		this.prizeCount = prizeCount;
	}
	
	
	

}
