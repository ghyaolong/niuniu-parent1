package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;

public class TaskHistoryVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8774401808169531675L;
	/**
	 * 任务配置id
	 */
    private Long id;
    /**
     * 成长值
     */
    private Integer growthPoint;
    /**
     * 任务名
     */
    private String name;
    /**
     * 任务跳转的路径
     */
    private String uri;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务完成的状态
     */
	private Byte isFinish;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGrowthPoint() {
		return growthPoint;
	}

	public void setGrowthPoint(Integer growthPoint) {
		this.growthPoint = growthPoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Byte isFinish) {
		this.isFinish = isFinish;
	}

}
