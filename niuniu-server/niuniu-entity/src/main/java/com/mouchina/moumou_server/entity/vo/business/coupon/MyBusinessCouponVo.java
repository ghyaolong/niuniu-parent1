package com.mouchina.moumou_server.entity.vo.business.coupon;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 我的优惠券
 * @author Cris
 *
 */
public class MyBusinessCouponVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -20305411982200079L;
	
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 有效标志 0失效 1有效 2删除
	 */
	private Integer flag;
	/**
	 * 状态 0审核 1已审核 2未通过
	 */
	private Integer state;
	/**
	 * 优惠券类型
	 */
	private Integer couponType;
	/**
	 * 优惠券金额
	 */
	private Double money;
	/**
	 * 折扣卷折扣
	 */
	private Double discount;
	/**
	 * 实物卷名称
	 */
	private String physicalVolume;
	/**
	 * 优惠券的开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 优惠券的结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endDate;
	/**
	 * 优惠券内容
	 */
	private String couponContent;
	/**
	 * 优惠券名称
	 */
	private String businessName;
	/**
	 * 优惠券地址
	 */
	private String businessAddress;
	/**
	 * 优惠券详细地址
	 */
	private String detailAddress;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户头像
	 */
	private String userAvatar;
	/**
	 * 是否使用	0未使用	1使用
	 */
	private Integer type;
	/**
	 * 距离
	 */
	private Double distance;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
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

	public String getCouponContent() {
		return couponContent;
	}

	public void setCouponContent(String couponContent) {
		this.couponContent = couponContent;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
