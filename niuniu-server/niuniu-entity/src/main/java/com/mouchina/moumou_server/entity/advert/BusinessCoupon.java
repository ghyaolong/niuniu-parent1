package com.mouchina.moumou_server.entity.advert;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 优惠券
 * 
 * @author Administrator
 *
 */
public class BusinessCoupon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7361259655227515496L;

	private Long id;
	/**
	 * 优惠券类型 1现金券 2折扣券 3实物券
	 */
	private Integer couponType;
	/**
	 * 状态 0审核 1已审核 2未通过
	 */
	private Integer state;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 优惠券标题(现金券对应数字,折扣券、实物券对应不多于8个中文字符)
	 */
	private String couponTitle;
	/**
	 * 优惠券内容
	 */
	private String couponContent;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 开始日期(有效期)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 结束日期(有效期)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endDate;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	/**
	 * 有效标志 0失效 1有效
	 */
	private Integer flag;
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
	 * 是否投放广告【 0审核 1已审核 2暂停 3发送完成 4未通过 5超时完成】
	 */
	private Integer isThrowIn;
	/**
	 * 用户头像
	 */
	private String userAvatar;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 广告id
	 */
	private Long advertId;
	/**
	 * 商户的详细地址
	 */
	private String businessAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle == null ? null : couponTitle.trim();
	}

	public String getCouponContent() {
		return couponContent;
	}

	public void setCouponContent(String couponContent) {
		this.couponContent = couponContent == null ? null : couponContent.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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

	public Integer getIsThrowIn() {
		return isThrowIn;
	}

	public void setIsThrowIn(Integer isThrowIn) {
		this.isThrowIn = isThrowIn;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
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

	public Long getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Long advertId) {
		this.advertId = advertId;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
}