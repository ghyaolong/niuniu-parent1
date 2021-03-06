package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;

public class UserPart implements Serializable {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column user_part_mapper.phone
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	private String phone;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column user_part_mapper.num
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	private Integer num;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column user_part_mapper.mapprer_value
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	private String mapprerValue;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database table user_part_mapper
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * 第三方
	 */

	private String thirdUid;
	
	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column user_part_mapper.phone
	 *
	 * @return the value of user_part_mapper.phone
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */

	public String getPhone() {
		return phone;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column user_part_mapper.phone
	 *
	 * @param phone
	 *            the value for user_part_mapper.phone
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column user_part_mapper.mapprer_value
	 *
	 * @return the value of user_part_mapper.mapprer_value
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	public String getMapprerValue() {
		return mapprerValue;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column user_part_mapper.mapprer_value
	 *
	 * @param mapprerValue
	 *            the value for user_part_mapper.mapprer_value
	 *
	 * @mbggenerated Mon Jun 15 14:34:52 CST 2015
	 */
	public void setMapprerValue(String mapprerValue) {
		this.mapprerValue = mapprerValue == null ? null : mapprerValue.trim();
	}

	public String getThirdUid() {
		return thirdUid;
	}

	public void setThirdUid(String thirdUid) {
		this.thirdUid = thirdUid == null ? null : thirdUid.trim();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	

}