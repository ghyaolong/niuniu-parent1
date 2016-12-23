package com.mouchina.moumou_server_interface.member;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 用户授权类 包含 用户登陆 注册 密码找回 角色控制 接口
 * 
 * @author larry 2015年6月15日下午3:27:39
 */

public interface UserVerifyService {
	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return 返回用户所有信息
	 */
	UserResult<User> saveUserRegister(User user);

	/***
	 * 用户登陆
	 * 
	 * @param userIdentity
	 * @return 0用户不存在 1成功 2系统禁用
	 */
	UserResult<User> processUserLogin(UserIdentity userIdentity);

	/**
	 * 用户登录 --web
	 * 
	 * @param userIdentity
	 * @return 0用户不存在 1成功 -1密码错误
	 */
	UserResult<UserInfo> processUserLoginInWeb(UserIdentity userIdentity);

	/***
	 * 用户注销
	 * 
	 * @param userIdentity
	 * @return 0失败 1成功
	 */
	int processUserLogOut(UserIdentity userIdentity);

	/**
	 * 密码重置
	 * 
	 * @param userIdentity
	 * @return 0失败 1成功
	 */
	int updatePassWdReset(UserIdentity userIdentity, String passWord);

	/**
	 * 查询单个用户信息
	 * 
	 * @param userIdentity
	 * @return 用户所有信息
	 */
	UserInfo singleUserSearch(UserIdentity userIdentity);

	/***
	 * 获取用户user
	 * 
	 * @param userIdentity
	 * @return user信息
	 */
	User selectUser(UserIdentity userIdentity);

	/**
	 * 用户信息更新
	 * 
	 * @param User
	 * @return 0 失败 1成功 2用户不存在
	 */
	int updateUser(User user);

	/***
	 * 添加用户分表映映射
	 * 
	 * @param userPart
	 * @return
	 */
	UserResult<UserPart> addUserPart(UserPart userPart);

	/***
	 * 获取用户分表映射 对象
	 * 
	 * @param mapprerValue
	 * @return
	 */
	UserResult<UserPart> selectUserPart(String mapprerValue);

	/***
	 * 按phone获取用户分表映对象列表
	 * 
	 * @param phone
	 * @return
	 */
	UserResult<List<UserPart>> selectListUserPart(String phone);

	/**
	 * 更新用用户分表映射
	 * 
	 * @param userPart
	 * @return
	 */
	UserResult<Integer> updateUserPart(UserPart userPart);

	/**
	 * 删除用户分表映射
	 * 
	 * @param UserPart
	 * @return
	 */
	UserResult<Integer> deleteUserPart(UserPart UserPart);

	/***
	 * 根据userid获取用户对象
	 * 
	 * @param userId
	 * @return
	 */
	UserResult<User> selectUserByUserId(Long userId);

	int userPartTableNum(final String phone);

	/***
	 * 根据mapper返回 UserIdentity 对象
	 * 
	 * @param mapprerValue
	 * @return
	 */
	UserResult<UserIdentity> selectUserPartByUserId(String mapprerValue);

	/**
	 * 根据条件统计user分表的用户数量
	 * @param map
	 * @return
	 */
	public UserResult<Integer> selectCountUserPart(Map<String,Object> map);

	/**
	 * 根据user获取到用户的财富【该接口会自己找到分表的下标】
	 * @param user	用户对象
	 * @return
	 */
	public UserAssets getUserAssetsByUser(User user);

	UserResult<UserAssetsInfo> getUserAssetsInfo(UserIdentity userIdentity);

	UserResult<UserAssets> updateUserAssets(UserAssets userAssets);

	/**
	 * 保存第三方登录用户信息
	 * 
	 * @param user
	 * @return
	 */
	UserResult<User> saveUserRegisterByThirdUid(User user);

	/**
	 * 通过thirdUid查找user
	 * 
	 * @param userIdentity
	 * @return
	 */
	User selectUserByphone(UserIdentity userIdentity);
	/**
	 * 用户昵称排重
	 * @param nickName
	 * @return
	 */
	User selectByUserNickName(String nickName);
	/**
	 * 查找沉默用户
	 * @param map
	 * @return
	 */
	List<User> selectComeBackUser(Map<String,Object> map);
	/**
	 * 查询手机号是否使用
	 * @param phone
	 * @return
	 */
	User getUserByCheckPhone(String phone);
}
