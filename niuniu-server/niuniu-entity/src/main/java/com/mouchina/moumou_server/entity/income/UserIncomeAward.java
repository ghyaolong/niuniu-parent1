package com.mouchina.moumou_server.entity.income;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户奖励收入信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserIncomeAward implements Serializable {

	private Long id; //主键
	private Long userId; //用户ID
	private Long eventId; //触发事件ID
	private Integer amount; //奖励金额(单位：分)
	private Integer awardType; //活动类型(1:认证返现   2:发广告返现   3:邀请好友返现)
	private Date createTime; //创建日期
	
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
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getAwardType() {
		return awardType;
	}
	public void setAwardType(Integer awardType) {
		this.awardType = awardType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
