package com.mouchina.moumou_server.entity.vo.user.particular;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserIncomeDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9137164530741313900L;
	/**
	 * 明细的id
	 */
	private Long id;
	/**
	 * 收益的用户id
	 */
	private Long incomeUserId;
	/**
	 * 收益的日期
	 */
	@DateTimeFormat(pattern = "MM月dd日")
	@JsonFormat(pattern = "MM月dd日", timezone = "GMT+8")
	private Date date;
	/**
	 * 收益的具体时间
	 */
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date time;
	/**
	 * 触发收益的用户id【就是用户的下线】
	 */
	private Long eventUserId;
	/**
	 * 收益的金额
	 */
	private Integer amount;
	/**
	 * 用户昵称【下线的昵称】
	 */
	private String userNickName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIncomeUserId() {
		return incomeUserId;
	}

	public void setIncomeUserId(Long incomeUserId) {
		this.incomeUserId = incomeUserId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getEventUserId() {
		return eventUserId;
	}

	public void setEventUserId(Long eventUserId) {
		this.eventUserId = eventUserId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
}
