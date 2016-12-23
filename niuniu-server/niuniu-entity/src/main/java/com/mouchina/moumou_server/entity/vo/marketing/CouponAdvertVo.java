package com.mouchina.moumou_server.entity.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CouponAdvertVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1209904658275573884L;
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 广告内容
	 */
	private String advertContent;
	/**
	 * 状态 0待审核 1审核通过 2暂停 3已下线 4未通过 5超时完成
	 */
	private Integer state;
	/**
	 * 广告类型	0普通福袋 1优惠券福袋 2公告 3优惠券圈子 4接力福袋(圈子) 5首页广告(首页） 6定时广告 7运营活动广告(官方) 8定时活动广告(官方) 9说说(圈子)
	 */
	private Integer advertType;
	/**
	 * 优惠券模板id
	 */
	private Long relationId;
	/**
	 * 红包数量【也是优惠券的数量】
	 */
	private Integer redEnvelopeCount;
	/**
	 * 优惠券类型 1现金券 2折扣券 3实物券
	 */
	private Integer couponType;
	/**
	 * 优惠券的开始有效期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 优惠券的结束有效期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endDate;
	/**
	 * 使用优惠券的人数【核销人数】
	 */
	private Integer useRedCount;
	/**
	 * 领取优惠券的人数
	 */
	private Integer getRedCount;
	/**
	 * 现金券的金额
	 */
	private Double money;
	/**
	 * 折扣卷的折扣
	 */
	private Double discount;
	/**
	 * 实物卷对应的商品名称
	 */
	private String physicalVolume;
	/**
	 * 优惠券领取时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createDate;

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAdvertType() {
		return advertType;
	}

	public void setAdvertType(Integer advertType) {
		this.advertType = advertType;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public Integer getRedEnvelopeCount() {
		return redEnvelopeCount;
	}

	public void setRedEnvelopeCount(Integer redEnvelopeCount) {
		this.redEnvelopeCount = redEnvelopeCount;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getUseRedCount() {
		return useRedCount;
	}

	public void setUseRedCount(Integer useRedCount) {
		this.useRedCount = useRedCount;
	}

	public Integer getGetRedCount() {
		return getRedCount;
	}

	public void setGetRedCount(Integer getRedCount) {
		this.getRedCount = getRedCount;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getPhysicalVolume() {
		return physicalVolume;
	}

	public void setPhysicalVolume(String physicalVolume) {
		this.physicalVolume = physicalVolume;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
