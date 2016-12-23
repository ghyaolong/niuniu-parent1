package com.mouchina.moumou_server.entity.income;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户收益总和
 * 
 * @author Administrator
 *
 */
public class UserIncomeSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7118434201179291027L;

	private Long id;

	private Long userId;
	/**
	 * 好友帮赚的金额
	 */
	private Integer amount;

	private Integer sourceAmout;
	
	/**
	 * 收益的时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date incomeDate;

	/**
	 * 计算收益的时间
	 */
	private Date createTime;

	private Byte state;

	private String descb;

	private Integer incomeSize;

	private Integer aloneUserIncomeSize;
	
	/**
	 * 月份
	 */
	private String month;

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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getSourceAmout() {
		return sourceAmout;
	}

	public void setSourceAmout(Integer sourceAmout) {
		this.sourceAmout = sourceAmout;
	}

	public Date getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getDescb() {
		return descb;
	}

	public void setDescb(String descb) {
		this.descb = descb == null ? null : descb.trim();
	}

	public Integer getIncomeSize() {
		return incomeSize;
	}

	public void setIncomeSize(Integer incomeSize) {
		this.incomeSize = incomeSize;
	}

	public Integer getAloneUserIncomeSize() {
		return aloneUserIncomeSize;
	}

	public void setAloneUserIncomeSize(Integer aloneUserIncomeSize) {
		this.aloneUserIncomeSize = aloneUserIncomeSize;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}