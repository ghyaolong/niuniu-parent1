package com.mouchina.moumou_server.entity.vo.user.particular;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户领取到优惠券的详情
 * 
 * @author Cris
 *
 */
public class UserBusinessCouponParticularVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1921669303091611045L;
	
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 店铺名称
	 */
	private String businessName;
	/**
	 * 优惠券类型 1现金券 2折扣券 3实物券
	 */
	private Integer couponType;
	/**
	 * 现金卷的金额
	 */
	private Double money;
	/**
	 * 折扣卷折扣
	 */
	private Double discount;
	/**
	 * 实物卷的名称
	 */
	private String physicalVolume;
	/**
	 * 优惠券的有效开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 优惠券的有效结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endDate;
	/**
	 * 优惠券的详细内容
	 */
	private String couponContent;
	/**
	 * 商家店铺的地址
	 */
	private String businessAddress;
	/**
	 * 商家店铺的详细地址
	 */
	private String detailAddress;
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

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
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
