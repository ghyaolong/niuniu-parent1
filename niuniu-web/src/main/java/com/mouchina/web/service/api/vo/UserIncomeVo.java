package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserIncomeVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8186020735051355746L;

	private Long id;

	private Long userId;

	private String userName;

	private Long enventId;

	private Integer amout;

	private Integer sourceAmount;

	private Byte state;

	private String enventName;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private Long eventUserId;

	private Byte incomeType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date incomeDate;

	public Date getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
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

	public Long getEnventId() {
		return enventId;
	}

	public void setEnventId(Long enventId) {
		this.enventId = enventId;
	}

	public Integer getAmout() {
		return amout;
	}

	public void setAmout(Integer amout) {
		this.amout = amout;
	}

	public Integer getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(Integer sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getEnventName() {
		return enventName;
	}

	public void setEnventName(String enventName) {
		this.enventName = enventName == null ? null : enventName.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getEventUserId() {
		return eventUserId;
	}

	public void setEventUserId(Long eventUserId) {
		this.eventUserId = eventUserId;
	}

	public Byte getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(Byte incomeType) {
		this.incomeType = incomeType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}