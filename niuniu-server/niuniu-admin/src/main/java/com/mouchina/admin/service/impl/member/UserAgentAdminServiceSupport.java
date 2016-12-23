package com.mouchina.admin.service.impl.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.member.UserAgentAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatisticsEnum;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.vo.AgentPromotionVo;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.moumou_server_interface.view.UserResult;

@Service
public class UserAgentAdminServiceSupport implements UserAgentAdminService {

	private Logger logger = Logger.getLogger(UserAgentAdminServiceSupport.class);
	
	@Resource
	private UserAgentService userAgentService;

	@Resource
	private UserAdminService userAdminService;

	@Resource
	RoleService roleService;
	
	@Resource
	UserInviteService userInviteService;
	
	@Resource
	SystemService systemService;

	@Override
	public UserResult<ListRange<UserAgent>> selectListUserAgentPage(Map map) {
		// TODO Auto-generated method stub
		return userAgentService.selectListUserAgentPage(map);
	}

	@Override
	public UserResult<Integer> updateUserAgent(UserAgent userAgent) {
		// 处理修改代理商等级业务
		if(userAgent.getAgentLevel() != userAgent.getAgentLevelOld()){
			//代理商提成
			UserAgentCommission userAgentCommission = getUserAgentCommission(userAgent);
			if(userAgentCommission != null){
				userAgentService.addUserAgentCommission(userAgentCommission);
			}
			userAgent.setParentId((long) 0);
			
			//星级代理商升级代理级别，更改角色权限
			if(userAgent.getAgentLevelOld() == AgentIncomeStatisticsEnum.AGENT_LEVEL_4.getMarker()
					&& userAgent.getAgentLevel() < userAgent.getAgentLevelOld()){
				Map map = new HashMap();
				map.put("userId", userAgent.getUserId());
				StaticUserRole staticUserRole = userAgentService.selectStaticUserRoleByMap(map);
				if(staticUserRole != null){
					map.clear();
					map.put("roleId", 7);
					map.put("id", staticUserRole.getId());
					userAgentService.updateStaticUserRoleByMap(map);
				}
			}
		}else{
			userAgent.setAgentPiont(userAgent.getAgentPiontOld());
		}
		userAgent.setModifyTime(new Date());
		return userAgentService.updateUserAgent(userAgent);
	}

	@Override
	public UserResult<UserAgent> addUserAgent(UserAgent userAgent) {

		UserResult<UserAgent> userResult = userAgentService.addUserAgent(userAgent);

		if (userResult.getState() == 1) {
			
			//代理商提成
			UserAgentCommission userAgentCommission = getUserAgentCommission(userAgent);
			if(userAgentCommission != null){
				userAgentService.addUserAgentCommission(userAgentCommission);
			}
			
			/*User user = userAdminService.selectUserByUserId(userAgent.getUserId());
			StaticUserRole staticUserRole = new StaticUserRole();
			staticUserRole.setRoleId((long) 7);
			staticUserRole.setUserId(userAgent.getUserId());
			staticUserRole.setUserPhone(user.getPhone());
			staticUserRole.setUserRoleNote("代理商");

			roleService.insertUserRole(staticUserRole);*/
		}

		return userResult;
	}

	@Override
	public UserResult<UserAgent> selectUserAgent(Long userAgentId) {
		// TODO Auto-generated method stub
		return userAgentService.selectUserAgent(userAgentId);
	}

	@Override
	public UserResult<UserAgent> selectUserAgent(Map map) {
		// TODO Auto-generated method stub
		return userAgentService.selectUserAgent(map);
	}

	@Override
	public UserResult<User> checkAgent(String phone) {
		// TODO Auto-generated method stub
		//查询当前登录用户信息
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String loginPhone = userDetails.getUsername();
		UserIdentity loginUserIdentity = new UserIdentity();
		loginUserIdentity.setPhone(loginPhone);
		User loginUser = userAdminService.selectUser(loginUserIdentity);
		
		UserResult<User> userResult = new UserResult<User>();
		Integer result = 0;
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);

		if (user != null) {
			result = 1;

			Map map = new HashMap();
			map.put("userId", user.getId());
			UserAgent userAgent = userAgentService.selectUserAgent(map).getContent();

			if (userAgent == null) {
				//查询是否是当前用户的好友，不是当前好友(管理员维护不做限制)则不可维护为代理商(2:不是当前用户好友)
				map.clear();
				map.put("state", AgentIncomeStatisticsEnum.VALID_SIGN.getMarker());
				map.put("userId", loginUser.getId());
				map.put("newUserId", user.getId());
				SocialResult<UserInvite> socialResult = userInviteService.selectUserInvite(map);
				if(socialResult.getState() == 1){
				}else{
					result = 2;
				}
			} else {
				// 查询上级代理商信息
				long parentId = userAgent.getParentId();
				if(parentId > 0){
					map.clear();
					map.put("userId", parentId);
					UserAgent parentUserAgent = userAgentService.selectUserAgent(map).getContent();
					User parentUser = userAdminService.selectUserByUserId(parentId);
					parentUserAgent.setUserName(parentUser.getNickName());
					parentUserAgent.setPhone(parentUser.getPhone());
					userResult.setParentUserAgent(parentUserAgent);
				}
				userResult.setUserAgent(userAgent);
				result = 0;
			}

			userResult.setContent(user);

		} else {
			result = -1;
		}

		userResult.setState(result);

		return userResult;
	}

	@Override
	public UserResult<UserAgent> selectUserAgentByUserId(Long userId) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("userId", userId);
		UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);

		return userResult;
	}

	public UserResult<UserAgent> selectUserAgentStatistics(Map map) {
		UserResult<UserAgent> userResult = userAgentService.selectUserAgent(map);
		return userResult;
	}
	
	//代理商成交金额提成业务
	private UserAgentCommission getUserAgentCommission(UserAgent userAgent){
		UserAgentCommission userAgentCommission = null;
		Map map = new HashMap();
		map.put("state", 1);
		map.put("newUserId", userAgent.getUserId());
		SocialResult<UserInvite> result = userInviteService.selectUserInvite(map);
		if(result.getState() == 1){
			UserInvite userInvite = result.getContent();
			//校验被维护代理商的推荐人是否已获取提成
			map.clear();
			map.put("userId", userInvite.getUserId());
			map.put("recommendId", userAgent.getUserId());
			UserAgentCommission agentCommission = userAgentService.selectUserAgentCommission(map);
			if(agentCommission == null){
				User parentUser = userAdminService.selectUserByUserId(userInvite.getUserId());
				User childUser = userAdminService.selectUserByUserId(userAgent.getUserId());
				userAgentCommission = new UserAgentCommission();
				userAgentCommission.setStatDate(DateUtils.getDate(new Date()));
				userAgentCommission.setCreateTime(new Date());
				userAgentCommission.setUserId(userInvite.getUserId());
				userAgentCommission.setUserPhone(parentUser.getPhone());
				userAgentCommission.setUserNickname(parentUser.getNickName());
				userAgentCommission.setRecommendAgentLevel(userAgent.getAgentLevel());
				userAgentCommission.setRecommendId(userAgent.getUserId());
				userAgentCommission.setRecommendPhone(childUser.getPhone());
				userAgentCommission.setRecommendNickname(childUser.getNickName());
				
				//获取代理商成交金额提成比例参数
				int commission = 0;
				map.clear();
				switch (userAgent.getAgentLevel()) {
				case 2:
					map.put("configKey", "system.commission.income.county");
					break;
				case 3:
					map.put("configKey", "system.commission.income.centre");
					break;
				case 4:
					map.put("configKey", "system.commission.income.starlevel");
					break;
				default:
					map.put("configKey", "-1");
					break;
				}
				map.put("configGroup", "system");
				map.put("state", "1");
				SystemResult<List<SystemGlobalConfig>> commissionResult = systemService
						.selectListSystemGlobalConfig(map);
				List<SystemGlobalConfig> commissionConfigs = commissionResult.getContent();
				if (commissionConfigs != null && commissionConfigs.size() > 0) {
					commission = Integer.valueOf(commissionConfigs.get(0).getConfigContent());
				}
				//获取代理商收费标准参数
				int toll = 0;
				map.clear();
				switch (userAgent.getAgentLevel()) {
				case 2:
					map.put("configKey", "system.toll.norm.county");
					break;
				case 3:
					map.put("configKey", "system.toll.norm.centre");
					break;
				case 4:
					map.put("configKey", "system.toll.norm.starlevel");
					break;
				default:
					map.put("configKey", "-1");
					break;
				}
				map.put("configGroup", "system");
				map.put("state", "1");
				SystemResult<List<SystemGlobalConfig>> tollResult = systemService
						.selectListSystemGlobalConfig(map);
				List<SystemGlobalConfig> tollConfigs = tollResult.getContent();
				if (tollConfigs != null && tollConfigs.size() > 0) {
					toll = Integer.valueOf(tollConfigs.get(0).getConfigContent());
				}
				BigDecimal bigDecimal = new BigDecimal(0.01);
				BigDecimal bigDecimalCommission = new BigDecimal(commission);
				BigDecimal bigDecimalToll = new BigDecimal(toll);
				userAgentCommission.setCommissionAmount(bigDecimalToll.multiply(bigDecimal).multiply(bigDecimalCommission).doubleValue());
			}
		}
		return userAgentCommission;
	}

	@Override
	public ListRange<UserAgentCommission> selectListUserAgentCommissionPage(Map map) {
		return userAgentService.selectListUserAgentCommissionPage(map);
	}

	@Override
	public List<UserAgentCommissionVo> exportUserAgentCommission(Map map) {
		return userAgentService.exportUserAgentCommission(map);
	}

	@Override
	public int updateUserAgentCommissionByMap(Map map) {
		return userAgentService.updateUserAgentCommissionByMap(map);
	}

	@Override
	public ListRange<AgentRecommendedVo> selectListUserAgentRecommendedPage(Map map) {
		return userAgentService.selectListUserAgentRecommendedPage(map);
	}

	@Override
	public List<AgentRecommendedVo> exportUserAgentRecommended(Map map) {
		return userAgentService.exportUserAgentRecommended(map);
	}

	@Override
	public User selectUserInfo(String phone) {
		return userAgentService.selectUserInfo(phone);
	}

	@Override
	public ListRange<AgentPromotionVo> selectListUserAgentPromotionPage(Map map) {
		return userAgentService.selectListUserAgentPromotionPage(map);
	}

	@Override
	public List<AgentPromotionVo> exportUserAgentPromotion(Map map) {
		return userAgentService.exportUserAgentPromotion(map);
	}

}
