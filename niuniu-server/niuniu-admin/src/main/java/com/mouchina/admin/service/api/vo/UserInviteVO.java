package com.mouchina.admin.service.api.vo;
 
import java.io.Serializable;
import java.util.Date;

public class UserInviteVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8488491950860960070L;
	
	private Long id;
	
	private Long userId;
	
	/**
	 * 昵称
	 */
	private String targetNickName;
	
	/**
	 * 红包金额
	 */
	private double targetRedpackMoney;
	/**
	 * 头像
	 */
	private String targetAvatar;
	/**
	 * 性别
	 */
	private Byte targetSex;
	
	/**
	 * 手机规格
	 */
	private String targetPhoneSpecification;
	
	/**
	 * 手机号码
	 */
	private String targetPhone;
	/**
	 * 首次登录时间
	 */
	private Date targetInstallTime;
	/**
	 * 注册时间
	 */
	private Date targetRegisterTime;
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
	public String getTargetNickName() {
		return targetNickName;
	}
	public void setTargetNickName(String targetNickName) {
		this.targetNickName = targetNickName;
	}
	public String getTargetAvatar() {
		return targetAvatar;
	}
	public void setTargetAvatar(String targetAvatar) {
		this.targetAvatar = targetAvatar;
	}
	public Byte getTargetSex() {
		return targetSex;
	}
	public void setTargetSex(Byte targetSex) {
		this.targetSex = targetSex;
	}
	public String getTargetPhoneSpecification() {
		return targetPhoneSpecification;
	}
	public void setTargetPhoneSpecification(String targetPhoneSpecification) {
		this.targetPhoneSpecification = targetPhoneSpecification;
	}
	public String getTargetPhone() {
		return targetPhone;
	}
	public void setTargetPhone(String targetPhone) {
		this.targetPhone = targetPhone;
	}
	public double getTargetRedpackMoney() {
		return targetRedpackMoney;
	}
	public void setTargetRedpackMoney(double targetRedpackMoney) {
		this.targetRedpackMoney = targetRedpackMoney;
	}
	public Date getTargetInstallTime() {
		return targetInstallTime;
	}
	public void setTargetInstallTime(Date targetInstallTime) {
		this.targetInstallTime = targetInstallTime;
	}
	public Date getTargetRegisterTime() {
		return targetRegisterTime;
	}
	public void setTargetRegisterTime(Date targetRegisterTime) {
		this.targetRegisterTime = targetRegisterTime;
	}
}
