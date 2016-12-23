package com.mouchina.web.service.api.member;

import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.member.UserAssetsInfo;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;
import com.mouchina.moumou_server_interface.view.UserResult;

public interface UserWebService {

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */
	User processUserLogin(UserIdentity userIdentity);

	UserInfo getUserInfo(String loginKey);

	User getUserByLoginKey(String loginKey);

	User getUserByPhone(String phone);

	/***
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	int updateUser(User user);

	/**
	 * 外部用户登录
	 * 
	 * @param userIdentity
	 * @return
	 */
	User outsideLogin(UserIdentity userIdentity);

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	User saveRegister(User user);

	/**
	 * 用户重置密码
	 * 
	 * @param userIdentity
	 * @param passWord
	 * @return
	 */
	int updatePassWdReset(UserIdentity userIdentity, String passWord);

	/**
	 * 用户注销
	 * 
	 * @param userIdentity
	 * @return
	 */
	int processUserLogOut(UserIdentity userIdentity);

	UserResult<UserAssetsInfo> getUserAssetsInfo(UserIdentity userIdentity);

	UserResult<Business> getBusiness(Long businessId);

	UserResult<Business> getBusinessByUserId(Long userId);

	UserResult<Integer> updateBusiness(Business business);

	/**
	 * 添加商户
	 * 
	 * @param business
	 * @return
	 */
	UserResult<Business> addBusiness(Business business);

	UserResult<User> selectUserByUserId(Long userId);

	/**
	 * 认证商户（个人）信息（3.0版本）
	 * 
	 * @param business
	 * @return
	 */
	UserResult<BusinessTemp> addBusinessInTemp(BusinessTemp businessTemp);

	/**
	 * 商户/个人 多次认证(3.0版本)
	 * 
	 * @param business
	 * @return
	 */
	UserResult<BusinessTemp> updateBussinessInTemp(BusinessTemp businessTemp);

	/**
	 * 查询中间表中商户（个人）信息
	 * 
	 * @param business
	 * @return
	 */
	BusinessTemp selectBussinessInTemp(Long id);

	/**
	 * 商户(个人)多次认证
	 * 
	 * @param business
	 * @return
	 */
	UserResult<Business> updateMultipleAuthentication(Business business);

	/**
	 * 第三方注册
	 * 
	 * @param user
	 * @return
	 */
	User saveRegisterByThirdUid(User user);

	/**
	 * 通过第三方唯一码查找用户
	 * 
	 * @param thirdUid
	 * @return
	 */
	User getUserByThird(String thirdUid);

	/**
	 * 绑定手机号码
	 * 
	 * @param user
	 * @return
	 */
	int updateUserBindingPhone(User user);
	/**
	 * 用户昵称排重
	 * @param nickName
	 * @return
	 */
	User selectByUserNickName(String nickName);
	
	BusinessTemp selectBussinessTempByUserId(Long userId);
	/**
	 * 查询手机号是否使用
	 * @param phone
	 * @return
	 */
	User getUserByCheckPhone(String phone);
	
}
