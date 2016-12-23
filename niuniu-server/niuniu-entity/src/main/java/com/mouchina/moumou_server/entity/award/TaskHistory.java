package com.mouchina.moumou_server.entity.award;

import java.util.Date;

public class TaskHistory {
	private Long id;

	private Long userId;

	private Long taskConfigId;

	private Byte isFinish;

	private Integer count;
	/**
	 * 完成任务的时间
	 */
	private Date finishTaskDate;

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

	public Long getTaskConfigId() {
		return taskConfigId;
	}

	public void setTaskConfigId(Long taskConfigId) {
		this.taskConfigId = taskConfigId;
	}

	public Byte getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Byte isFinish) {
		this.isFinish = isFinish;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getFinishTaskDate() {
		return finishTaskDate;
	}

	public void setFinishTaskDate(Date finishTaskDate) {
		this.finishTaskDate = finishTaskDate;
	}

}