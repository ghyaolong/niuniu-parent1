package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mouchina.base.cache.CacheManager;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.Regexp;
import com.mouchina.base.utils.SHA1;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server_interface.member.UserAssetsInfo;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 
 * @author larry 2015年6月15日下午3:56:21
 */

public class UserVerifyServiceSupport implements UserVerifyService {

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserPartMapper userPartMapper;
	@Resource
	UserAssetsMapper userAssetsMapper;
	@Resource
	private RedisHelper redisHelper;
	@Resource
	private CacheManager cacheManager;
	static Logger logger = LogManager.getLogger();
	@Resource
	private RedEnvelopeMapper redEnvelopeMapper;

	@Override
	public UserResult<User> saveUserRegister(User user) {
		// TODO Auto-generated method stub
		UserResult<User> userResult = new UserResult<User>();
		UserPart userPart = new UserPart();
		UserAssets userAssets = new UserAssets();

		Integer tableNum = userPartTableNum(user.getPhone());
		user.setCreateTime(new Date());
		user.setState((byte) 1);
		logger.info("----------11111-------redisHelper : " + redisHelper.getIncr("globllUnique_userid_key"));
		// 保证useId创建唯一
		while (true) {
			Long userId = UUIDGenerator.userIdGenerator(redisHelper);
			Map<String,Object> map = new HashMap<>();
			map.put("mapprerValue", userId.toString());
			UserPart up = userPartMapper.selectModel(map);
			if (up == null) {
				user.setId(userId);
				user.setInventNo(userId + "");
				userAssets.setUserId((long) userId);
				userPart.setMapprerValue(userId + "");
				break;
			}
		}
		user.setPassWord(MD5Util.md5Hex(user.getPassWord()));
		user.setTableNum(tableNum);

		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, 12);// 日期加3个月
		user.setLoginExpireTime(rightNow.getTime());
		user.setToken(SHA1.hex_sha1(UUIDGenerator.getUUID()));
		user.setLoginExpireTime(new Date());
		user.setBindingMobile(user.getPhone());
		userMapper.insertSelective(user);
		//添加用户资产信息
		userAssets.setTableNum(tableNum);
		userAssetsMapper.insertSelective(userAssets);
		//添加用户分表映射信息
		userPart.setPhone(user.getPhone());
		userPart.setNum(tableNum);
		addUserPart(userPart);

		userResult.setContent(user);
		userResult.setState(1);
		return userResult;

	}

	@Override
	public UserResult<User> processUserLogin(UserIdentity userIdentity) {

		UserResult<User> userResult = new UserResult<User>();
		// TODO Auto-generated method stub
		int loginResult = 0;
		User user = null;
		if (userIdentity != null) {

			user = selectUser(userIdentity);

			if (user != null) {

				if (user.getState() == 0)

				{
					loginResult = -1;
				} else {
					loginResult = 2;

					if (StringUtil.isNotEmpty(userIdentity.getPhone())
							|| StringUtil.isNotEmpty(userIdentity.getThirdUid())) {
						Calendar rightNow = Calendar.getInstance();
						rightNow.setTime(new Date());
						rightNow.add(Calendar.MONTH, 12);// 日期加3个月
						user.setLoginExpireTime(rightNow.getTime());
						user.setToken(SHA1.hex_sha1(UUIDGenerator.getUUID()));
						user.setLastLoginTime(new Date());
						user.setPassWord(null);
						user.setPhoneSpecification(userIdentity.getPhoneSpecification());
						User cuser = selectUser(userIdentity);
						if (cuser != null) {
							user.setId(cuser.getId());
							userMapper.updateByPrimaryKeySelective(user);
							loginResult = 1;
						}
					}
				}
			}
		}
		userResult.setState(loginResult);
		userResult.setContent(user);
		return userResult;

	}

	@Override
	public UserResult<UserInfo> processUserLoginInWeb(UserIdentity userIdentity) {

		UserResult<UserInfo> userResult = new UserResult<UserInfo>();
		UserInfo userInfo = new UserInfo();
		// TODO Auto-generated method stub
		int loginResult = 0;
		if (userIdentity != null) {
			User user = selectUser(userIdentity);
			if (user != null) {
				if (user.getPassWord().equals(MD5Util.md5Hex(userIdentity.getPwd()))) {
					userInfo.setUser(user);
					loginResult = 1;
				} else
					loginResult = -1;// 密码错误
			}
		}

		userResult.setState(loginResult);
		userResult.setUserInfo(userInfo);
		return userResult;
	}

	@Override
	public int processUserLogOut(UserIdentity userIdentity) {
		// TODO Auto-generated method stub
		int result = 0;

		logger.debug(" 0--------selectUser  userIdentity=" + userIdentity.getToken());
		User user = selectUser(userIdentity);
		logger.debug(" 1--------selectUser  user=" + user + ",result=" + result);
		if (user != null) {
			User tempUser = new User();
			tempUser.setId(user.getId());
			tempUser.setTableNum(user.getTableNum());
			tempUser.setToken("");
			tempUser.setModifyTime(new Date());
			userMapper.updateByPrimaryKeySelective(tempUser);
			result = 1;

		}
		logger.debug(" 3--------result=" + result);
		return result;

	}

	@Override
	public int updatePassWdReset(UserIdentity userIdentity, String passWord) {
		// TODO Auto-generated method stub
		int result = 0;

		User user = selectUser(userIdentity);
		if(user != null){
			user.setPassWord(MD5Util.md5Hex(passWord));
			result = updateUser(user);
		}
		

		return result;

	}

	/**
	 * 查询单个用户信息
	 * 
	 * @param userIdentity
	 * @return
	 */
	@Override
	public UserInfo singleUserSearch(UserIdentity userIdentity) {
		UserInfo userInfo = null;
		User user = selectUser(userIdentity);
		if (user != null) {
			userInfo = new UserInfo();
			userInfo.setUser(user);
//			Map<String,Object> map = new HashMap<>();
//			map.put("tableNum", user.getTableNum());
//			map.put("userId", user.getId());
		}

		return userInfo;
	}

	/**
	 * 根据手机号确定分表num
	 * 
	 * @param phone
	 * @return
	 */
	@Override
	public int userPartTableNum(final String phone) {

		String md5String = MD5Util.md5Octal(phone);
		int num = Integer.parseInt(md5String.substring(0, 1), 8);
		return num;
	}

	@Override
	public User selectUser(UserIdentity userIdentity) {
		User user = null;
		Map<String,Object> userMap = new HashMap<>();
		if (userIdentity != null) {
			if (StringUtil.isNotEmpty(userIdentity.getPhone())) {
				userMap.put("phone", userIdentity.getPhone());

				Integer tableNum = userPartTableNum(userIdentity.getPhone());

				userMap.put("tableNum", tableNum);

				if (StringUtil.isNotEmpty(userIdentity.getPwd())) {
					userMap.put("passWord", MD5Util.md5Hex(userIdentity.getPwd()));
				}

			} else if (StringUtil.isNotEmpty(userIdentity.getToken()) && userIdentity.getTableNum() >= 0) {
				userMap.put("token", userIdentity.getToken());
				userMap.put("tableNum", userIdentity.getTableNum());

			} else if (StringUtil.isNotEmpty(userIdentity.getThirdUid()) && userIdentity.getTableNum() >= 0) {
				userMap.put("thirdUid", userIdentity.getThirdUid());
				userMap.put("tableNum", userIdentity.getTableNum());
			}

		}
		user = userMapper.selectModel(userMap);

		return user;
	}

	@Override
	public int updateUser(User user) {
		// TODO Auto-generated method stub
		int result = 0;
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setToken(user.getToken());
		userIdentity.setTableNum(user.getTableNum());
		userIdentity.setPhone(user.getPhone());
		if (user.getBindingMobile() != null) {
			userIdentity.setBindingMobile(user.getBindingMobile());
		}

		User cuser = selectUser(userIdentity);

		if (cuser != null) {

			user.setId(cuser.getId());
			user.setTableNum(cuser.getTableNum());
			user.setLastLoginTime(new Date());
			result = userMapper.updateByPrimaryKeySelective(user);
		} else {

			result = -1;
		}

		return result;

	}

	@Override
	public UserResult<UserPart> addUserPart(UserPart userPart) {
		// TODO Auto-generated method stub
		UserResult<UserPart> userResult = new UserResult<UserPart>();
		userResult.setState(userPartMapper.insertSelective(userPart));
		userResult.setContent(userPart);
		return userResult;

	}

	@Override
	public UserResult<UserIdentity> selectUserPartByUserId(String mapprerValue) {
		UserResult<UserIdentity> userResult = new UserResult<UserIdentity>();
		UserResult<UserPart> userUserPartResult = selectUserPart(mapprerValue);
		if (userUserPartResult.getState() == 1) {
			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setUserId(Long.valueOf(mapprerValue));
			userIdentity.setTableNum(userUserPartResult.getContent().getNum());
			userIdentity.setPhone(userUserPartResult.getContent().getPhone());
			userResult.setState(1);
			userResult.setContent(userIdentity);
		}
		return userResult;
	}

	@Override
	public UserResult<UserPart> selectUserPart(String mapprerValue) {

		UserResult<UserPart> userResult = new UserResult<UserPart>();

		Map<String,Object> map = new HashMap<>();
		map.put("mapprerValue", mapprerValue);
		UserPart usrePart = cacheManager.getEntityByKey(UserPart.class, mapprerValue);
		if (usrePart == null) {
			usrePart = userPartMapper.selectModel(map);
			if (usrePart != null) {
				userResult.setContent(usrePart);
				userResult.setState(1);
				cacheManager.addEntityByKey(mapprerValue, UserPart.class);
			}
		}
		return userResult;

	}

	@Override
	public UserResult<List<UserPart>> selectListUserPart(String phone) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public UserResult<Integer> updateUserPart(UserPart userPart) {
		// TODO Auto-generated method stub
		UserResult<Integer> userResult = new UserResult<Integer>();
		cacheManager.deleteEntityByKey(UserPart.class, userPart.getMapprerValue());
		userResult.setState(userPartMapper.updateByPrimaryKeySelective(userPart));

		return userResult;

	}

	@Override
	public UserResult<Integer> deleteUserPart(UserPart userPart) {
		// TODO Auto-generated method stub
		UserResult<Integer> userResult = new UserResult<Integer>();
		userResult.setState(userPartMapper.deleteByPrimaryKey(userPart.getPhone()));
		return userResult;

	}

	@Override
	public UserResult<User> selectUserByUserId(Long userId) {
		// TODO Auto-generated method stub
		UserResult<User> uerModelResult = new UserResult<User>();
		UserResult<UserPart> userResult = selectUserPart(userId + "");
		if (userResult.getState() == 1) {
			String phone = userResult.getContent().getPhone();
			UserIdentity userIdentity = new UserIdentity();
			if (!Regexp.checkPhone(phone)) {
				userIdentity.setThirdUid(phone);
				userIdentity.setTableNum(userResult.getContent().getNum());
			}else{
				userIdentity.setPhone(phone);
			}
			User user = selectUser(userIdentity);
			if (user != null) {
				uerModelResult.setContent(user);
				uerModelResult.setState(1);
			}
		}
		return uerModelResult;
	}

	@Override
	public UserResult<Integer> selectCountUserPart(Map<String, Object> map) {
		UserResult<Integer> userResult = new UserResult<Integer>();
		userResult.setContent(userMapper.selectCount(map));
		return userResult;
	}

	@Override
	public UserAssets getUserAssetsByUser(User user) {
		UserAssets userAssets = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("tableNum", user.getTableNum());
		userAssets = userAssetsMapper.selectModel(map);
		if(userAssets != null){
			userAssets.setTableNum(user.getTableNum());
		}
		return userAssets;
	}

	@Override
	public UserResult<UserAssetsInfo> getUserAssetsInfo(UserIdentity userIdentity) {
		UserResult<UserAssetsInfo> userResult = new UserResult<UserAssetsInfo>();

		User user = selectUser(userIdentity);
		if (user != null) {
			UserAssetsInfo userAssetsInfo = new UserAssetsInfo();
			userAssetsInfo.setUser(user);
			UserAssets userAssets = getUserAssetsByUser(user);
			// 查询用户抢到的所有红包金额
			RedEnvelopeTop redEnvelopeTop = redEnvelopeMapper.selectRedEnvelopeOneself(user.getId());
			if(redEnvelopeTop == null){
				userAssets.setRedPackCount(0);
			}else{
				userAssets.setRedPackCount(redEnvelopeTop.getAmountSum());
			}
			userAssetsInfo.setUserAssets(userAssets);
			userResult.setContent(userAssetsInfo);
			userResult.setState(1);
		}
		return userResult;
	}

	@Override
	public UserResult<UserAssets> updateUserAssets(UserAssets userAssets) {
		UserResult<UserAssets> userResult = new UserResult<UserAssets>();
		userResult.setState(userAssetsMapper.updateByPrimaryKeySelective(userAssets));
		return userResult;
	}

	@Override
	public UserResult<User> saveUserRegisterByThirdUid(User user) {

		UserResult<User> userResult = new UserResult<User>();
		UserPart userPart = new UserPart();
		UserAssets userAssets = new UserAssets();

		// logger.info("-----------------redisHelper : " +
		// redisHelper.getIncr("globllUnique_userid_key"));
		User cuser = new User();
		// 保证userId唯一
		while (true) {
			Long userId = UUIDGenerator.userIdGenerator(redisHelper);
			Map<String,Object> map = new HashMap<>();
			map.put("mapprerValue", userId.toString());
			UserPart up = userPartMapper.selectModel(map);
			if (up == null) {
				cuser.setId(userId);
				userAssets.setUserId((long) userId);
				userPart.setMapprerValue(userId + "");
				break;
			}
		}
		Integer tableNum = new Random().nextInt(8);// 产生0-7的随机数
		cuser.setTableNum(tableNum);
		// cuser.setPhone("");
		cuser.setCreateTime(new Date());
		cuser.setState((byte) 1);

		String thirdUid = user.getThirdUid();
		int type = Integer.valueOf(thirdUid.substring(thirdUid.length() - 1));
		cuser.setType((byte) type);

		cuser.setThirdUid(user.getThirdUid().substring(0, user.getThirdUid().length() - 2));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, 12);// 日期加3个月
		cuser.setBindingMobile("");
		cuser.setLoginExpireTime(rightNow.getTime());
		logger.info("设置的token:" + SHA1.hex_sha1(UUIDGenerator.getUUID()));
		cuser.setToken(SHA1.hex_sha1(UUIDGenerator.getUUID()));
		cuser.setLastLoginTime(new Date());
		userMapper.insertSelective(cuser);
		// 添加用户资产信息
		userAssets.setTableNum(tableNum);
		userAssetsMapper.insertSelective(userAssets);
		// 添加分表映射
		logger.info("thirdUid=" + cuser.getThirdUid());
		userPart.setPhone(cuser.getThirdUid());
		userPart.setNum(tableNum);
		userPart.setNickName("");
		addUserPart(userPart);

		userResult.setContent(cuser);
		userResult.setState(1);

		return userResult;

	}

	@Override
	public User selectUserByphone(UserIdentity userIdentity) {
		// TODO Auto-generated method stub
		User user = null;
		Map<String,Object> userMap = new HashMap<>();

		String phone = userIdentity.getPhone().substring(0, userIdentity.getPhone().length() - 2);
		Map<String,Object> map = new HashMap<>();
		map.put("phone", phone);
		user = userMapper.selectByThirdUid(map);
		if (user != null) {
			userMap.put("thirdUid", user.getPhone());
			userMap.put("id", user.getMapprervalue());
			userMap.put("tableNum", user.getNum());
			user = userMapper.selectModel(userMap);
		}

		return user;
	}

	@Override
	public User selectByUserNickName(String nickName) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("nickName", nickName);
		return userMapper.selectByThirdUid(map);
	}

	@Override
	public List<User> selectComeBackUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userMapper.selectComeBackUser(map);
	}

	@Override
	public User getUserByCheckPhone(String phone) {
		Map<String,Object> map = new HashMap<>();
		map.put("phone", phone);
		UserPart usrePart = userPartMapper.selectModel(map);
		User user = new User();
		if (usrePart == null) {
			Map<String,Object> userMap = new HashMap<>();
			userMap.put("bindingMobile", phone);
			for(int i = 0;i<=7;i++){
				userMap.put("tableNum", i);
				user = userMapper.selectUser(userMap);
				if(user != null ){
					return user;
				}
			}
		}else{
			map.put("tableNum", usrePart.getNum());
			map.put("id", usrePart.getMapprerValue());
			user = userMapper.selectModel(map);
		}
		return user;
	}
}
