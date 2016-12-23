package com.mouchina.web.service.impl.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server_interface.member.BusinessService;
import com.mouchina.moumou_server_interface.member.UserAssetsInfo;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.utils.TokenUtil;
import com.mouchina.web.controller.member.UserController;
import com.mouchina.web.service.api.member.UserWebService;

@Service
public class UserWebServiceSupport implements UserWebService {
	@Resource
	UserVerifyService userVerifyService;

	@Resource
	BusinessService businessService;

	@Resource
	UsersRelationService usersRelationService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Override
	public User processUserLogin(UserIdentity userIdentity) {
		// TODO Auto-generated method stub
		User user = null;
		UserResult<User> userResult = userVerifyService.processUserLogin(userIdentity);
		if (userResult.getState() >= 1) {
			user = userResult.getContent();

		}
		return user;
	}

	@Override
	public UserInfo getUserInfo(String loginKey) {

		return null;
	}

	@Override
	public User getUserByLoginKey(String loginKey) {
		String[] loginKeyArray = TokenUtil.split(loginKey);
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setToken(loginKeyArray[0]);
		userIdentity.setTableNum(Integer.valueOf(loginKeyArray[1]));
		User user = userVerifyService.selectUser(userIdentity);
		return user;
	}

	@Override
	public User getUserByPhone(String phone) {
		// TODO Auto-generated method stub
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userVerifyService.selectUser(userIdentity);
		return user;
	}

	public User getUserByThird(String thirdUid) {
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(thirdUid);
		User user = userVerifyService.selectUserByphone(userIdentity);
		return user;
	}

	@Override
	public int updateUser(User user) {
		int result = 0;
		int num = userVerifyService.updateUser(user);
		if (num == 1) {
			Map<String, Object> map = new HashMap<>();
			map.put("relationUserId", user.getId());
			List<UsersRelation> list = usersRelationService.selectListUsersRelation(map);
			if (list.size() > 0 && list != null) {
				// 更新用户关系表数据
				result = usersRelationService.updateTargetRelation(user);
			}
			UserPart userPart = new UserPart();
			userPart.setNickName(user.getNickName());
			userPart.setMapprerValue(user.getId().toString());
			/* 更新总表昵称 */
			UserResult<Integer> userResult = userVerifyService.updateUserPart(userPart);
			result = userResult.getState();
		}
		return result;
	}

	@Override
	public User saveRegister(User user) {
		// TODO Auto-generated method stub
		UserResult<User> userResult = userVerifyService.saveUserRegister(user);

		return userResult.getContent();
	}

	@Override
	public int updatePassWdReset(UserIdentity userIdentity, String passWord) {
		// TODO Auto-generated method stub
		return userVerifyService.updatePassWdReset(userIdentity, passWord);
	}

	@Override
	public int processUserLogOut(UserIdentity userIdentity) {
		// TODO Auto-generated method stub
		return userVerifyService.processUserLogOut(userIdentity);
	}

	@Override
	public UserResult<UserAssetsInfo> getUserAssetsInfo(UserIdentity userIdentity) {
		UserResult<UserAssetsInfo> userResult = userVerifyService.getUserAssetsInfo(userIdentity);

		return userResult;
	}

	@Override
	public UserResult<Business> getBusiness(Long businessId) {
		// TODO Auto-generated method stub

		return businessService.selectBusiness(businessId);
	}

	@Override
	public UserResult<Integer> updateBusiness(Business business) {
		// TODO Auto-generated method stub
		return businessService.updateBusiness(business);
	}

	@Override
	public UserResult<Business> addBusiness(Business business) {
		// TODO Auto-generated method stub
		return businessService.addBusiness(business);
	}

	@Override
	public UserResult<Business> getBusinessByUserId(Long userId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		UserResult<Business> userResult = businessService.selectBusiness(map);
		return userResult;
	}

	@Override
	public UserResult<User> selectUserByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userVerifyService.selectUserByUserId(userId);
	}

	@Override
	public UserResult<Business> updateMultipleAuthentication(Business business) {
		// TODO Auto-generated method stub
		return businessService.updateMultipleAuthentication(business);
	}

	@Override
	public User saveRegisterByThirdUid(User user) {
		// TODO Auto-generated method stub
		UserResult<User> userResult = userVerifyService.saveUserRegisterByThirdUid(user);

		return userResult.getContent();
	}

	@Override
	public User outsideLogin(UserIdentity userIdentity) {
		User user = null;
		UserResult<User> userResult = userVerifyService.processUserLogin(userIdentity);
		if (userResult.getState() >= 1) {
			user = userResult.getContent();

		}
		return user;
	}

	@Override
	public UserResult<BusinessTemp> addBusinessInTemp(BusinessTemp businessTemp) {
		// TODO Auto-generated method stub
		return businessService.addBusinessInTemp(businessTemp);
	}

	@Override
	public UserResult<BusinessTemp> updateBussinessInTemp(BusinessTemp businessTemp) {
		// TODO Auto-generated method stub
		UserResult<BusinessTemp> btTemp = new UserResult<BusinessTemp>();
		businessTemp.setModfiyTime(new Date());
		businessTemp.setCreateTime(new Date());
		int result = businessService.updateBussinessInTemp(businessTemp);
		btTemp.setState(result);
		return btTemp;
	}

	@Override
	public BusinessTemp selectBussinessInTemp(Long id) {
		// TODO Auto-generated method stub
		return businessService.selectBussinessInTemp(id);
	}

	@Override
	public int updateUserBindingPhone(User user) {
		// TODO Auto-generated method stub
		return userVerifyService.updateUser(user);
	}

	@Override
	public User selectByUserNickName(String nickName) {
		// TODO Auto-generated method stub
		return userVerifyService.selectByUserNickName(nickName);
	}

	@Override
	public BusinessTemp selectBussinessTempByUserId(Long userId) {
		// TODO Auto-generated method stub
		return businessService.selectBussinessTempByUserId(userId);
	}

	@Override
	public User getUserByCheckPhone(String phone) {
		// TODO Auto-generated method stub
		return userVerifyService.getUserByCheckPhone(phone);
	}
}
