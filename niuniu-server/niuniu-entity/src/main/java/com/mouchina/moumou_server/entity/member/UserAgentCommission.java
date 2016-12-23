package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商成交金额提成实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserAgentCommission implements Serializable {

	private Long id; //主键
	private Date statDate; //统计日期
	private Date createTime; //创建时间
	private Date modifyTime; //修改时间
	private Long userId; //用户ID
	private String userPhone; //用户手机号码
	private String userNickname; //用户昵称
	private Integer recommendAgentLevel; //被推荐代理类型 2:区县代理 3:中心代理 4:星级代理
	private Long recommendId; //被推荐用户ID
	private String recommendPhone; //被推荐用户手机号码
	private String recommendNickname; //被推荐用户昵称
	private Double commissionAmount; //提成金额(单位:分)
	private Integer settlementState; //结算状态(0:未结算  1:已结算)
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public Integer getRecommendAgentLevel() {
		return recommendAgentLevel;
	}
	public void setRecommendAgentLevel(Integer recommendAgentLevel) {
		this.recommendAgentLevel = recommendAgentLevel;
	}
	public Long getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
	}
	public String getRecommendPhone() {
		return recommendPhone;
	}
	public void setRecommendPhone(String recommendPhone) {
		this.recommendPhone = recommendPhone;
	}
	public String getRecommendNickname() {
		return recommendNickname;
	}
	public void setRecommendNickname(String recommendNickname) {
		this.recommendNickname = recommendNickname;
	}
	public Double getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	public Integer getSettlementState() {
		return settlementState;
	}
	public void setSettlementState(Integer settlementState) {
		this.settlementState = settlementState;
	}
	
}
