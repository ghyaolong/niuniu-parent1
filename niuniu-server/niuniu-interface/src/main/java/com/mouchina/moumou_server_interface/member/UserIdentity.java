package com.mouchina.moumou_server_interface.member;

import java.io.Serializable;

/**
 * 用户权限身份类
 *
 * @author larry 2015年6月15日下午3:21:20
 */

public class UserIdentity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6534431831366049610L;
	private Long userId;


	/**
	 * 用户手机号
	 */
	private String phone;
	/**
	 * 访问token
	 */
	private String token;
	/**
	 * 登录密码
	 */
	private String pwd;
	/**
	 * 登录手机规格
	 */
	private String phoneSpecification;
	/***
	 * 用户表编号
	 */
	private Integer tableNum;
	/***
	 * 第三方标识
	 */
	private String thirdUid;
	
	private String bindingMobile;//绑定手机号
	
	
	public String getBindingMobile() {
		return bindingMobile;
	}

	public void setBindingMobile(String bindingMobile) {
		this.bindingMobile = bindingMobile;
	}

	public String getThirdUid() {
		return thirdUid;
	}

	public void setThirdUid(String thirdUid) {
		this.thirdUid = thirdUid;
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public int getUserSate() {
		return userSate;
	}

	public void setUserSate(int userSate) {
		this.userSate = userSate;
	}

	private int userSate;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhoneSpecification() {
		return phoneSpecification;
	}

	public void setPhoneSpecification(String phoneSpecification) {
		this.phoneSpecification = phoneSpecification;
	}
}
