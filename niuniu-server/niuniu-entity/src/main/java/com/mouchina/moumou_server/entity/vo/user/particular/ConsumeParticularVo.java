package com.mouchina.moumou_server.entity.vo.user.particular;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 消费明细的Vo
 * 
 * @author Administrator
 *
 */
public class ConsumeParticularVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 248337036555732607L;
	/**
	 * 消费的金额
	 */
	private Integer amount;
	/**
	 * 消费的时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date time;
	/**
	 * 消费的事件【2接力福袋3公益 8 提现】
	 */
	private Integer type;
	/**
	 * 月份
	 */
	private String month;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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
