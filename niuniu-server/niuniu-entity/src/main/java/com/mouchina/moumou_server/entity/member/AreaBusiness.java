package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;
import java.util.Date;

/**
 * 区域代理商统计实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AreaBusiness implements Serializable {
	
	private Long id; //主键
	private Date statDate; //统计日期
	private Long userId; //用户id
	private Integer advertCount; //发广告次数
	private String countyCode; //区域
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getAdvertCount() {
		return advertCount;
	}
	public void setAdvertCount(Integer advertCount) {
		this.advertCount = advertCount;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
}
