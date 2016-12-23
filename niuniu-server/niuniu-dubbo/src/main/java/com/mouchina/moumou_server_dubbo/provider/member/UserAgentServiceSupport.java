package com.mouchina.moumou_server_dubbo.provider.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.dao.income.AgentIncomeStatisticsMapper;
import com.mouchina.moumou_server.dao.member.UserAgentMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.social.UserInviteMapper;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatistics;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatisticsEnum;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server.entity.vo.AgentPromotionVo;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;
import com.mouchina.moumou_server_interface.member.BusinessService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.moumou_server_interface.view.UserResult;

public class UserAgentServiceSupport implements UserAgentService {
	@Resource
	private UserAgentMapper userAgentMapper;
	
	@Resource
	private AgentIncomeStatisticsMapper agentIncomeStatisticsMapper;
	
	@Resource
	private UserPartMapper userPartMapper;
	
	@Resource
	private UserMapper userMapper;

	@Resource
	UserVerifyService userVerifyService;
	
	@Resource
	RoleService roleService;
	@Resource
	BusinessService businessService;
	@Resource
	private UserInviteMapper userInviteMapper;
	@Resource
	private SystemService systemService;
	
	@Override
	public UserResult<UserAgent> selectUserAgent(Long userAgentId) {
		// TODO Auto-generated method stub

		UserResult<UserAgent> userResult = new UserResult<UserAgent>();

		UserAgent UserAgent = userAgentMapper.selectByPrimaryKey(userAgentId);
		if (UserAgent != null) {
			userResult.setContent(UserAgent);
			userResult.setState(1);
		}

		return userResult;
	}

	@Override
	public UserResult<UserAgent> selectUserAgent(Map map) {
		// TODO Auto-generated method stub
		UserResult<UserAgent> userAgentResult = new UserResult<UserAgent>();

		UserAgent userAgent = userAgentMapper.selectModel(map);
		if (userAgent != null) {
			userAgentResult.setContent(userAgent);
			userAgentResult.setState(1);
		}

		return userAgentResult;
	}

	@Override
	public UserResult<UserAgent> addUserAgent(UserAgent userAgent) {
		// TODO Auto-generated method stub
		UserResult<UserAgent> UserAgentResult = new UserResult<UserAgent>();
		userAgent.setCreateTime(new Date());
		int result = userAgentMapper.insertSelective(userAgent);
		UserAgentResult.setContent(userAgent);
		UserAgentResult.setState(result);
		if (result == 1) {
			User user = userVerifyService.selectUserByUserId(userAgent.getUserId()).getContent();
			StaticUserRole staticUserRole = new StaticUserRole();
			staticUserRole.setUserId(userAgent.getUserId());
			staticUserRole.setUserPhone(user.getPhone());
			if(userAgent.getAgentLevel() != 4){
				staticUserRole.setRoleId((long) 7);
				staticUserRole.setUserRoleNote("代理商");
			}else{
				staticUserRole.setRoleId((long) 13);
				staticUserRole.setUserRoleNote("星级代理商");
			}
			roleService.insertUserRole(staticUserRole);
		}

		return UserAgentResult;
	}

	@Override
	public UserResult<ListRange<UserAgent>> selectListUserAgentPage(Map map) {
		// TODO Auto-generated method stub
		UserResult<ListRange<UserAgent>> userAgentResult = new UserResult<ListRange<UserAgent>>();
		ListRange<UserAgent> listRange = new ListRange<UserAgent>();
		int count = userAgentMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userAgentMapper.selectPageList(map));
		userAgentResult.setContent(listRange);
		userAgentResult.setState(1);
		return userAgentResult;
	}

	@Override
	public UserResult<Integer> deleteUserAgent(Long userAgentId) {
		// TODO Auto-generated method stub
		UserResult<Integer> userAgentResult = new UserResult<Integer>();

		int result = userAgentMapper.deleteByPrimaryKey(userAgentId);
		userAgentResult.setState(result);

		return userAgentResult;
	}

	@Override
	public UserResult<Integer> updateUserAgent(UserAgent userAgent) {
		// TODO Auto-generated method stub
		UserResult<Integer> userAgentResult = new UserResult<Integer>();

		int result = userAgentMapper.updateByPrimaryKeySelective(userAgent);
		userAgentResult.setState(result);

		if (result == 1) {

			UserResult<User> userResult = userVerifyService
					.selectUserByUserId(userAgent.getUserId());
			if (userResult.getState() == 1) {
				User user = userResult.getContent();
				user.setIncomeLevel(userAgent.getAgentLevel() == 4 ? (byte) 2
						: (byte) 1);
				userVerifyService.updateUser(user);
			}

		}

		return userAgentResult;
	}

	@Override
	public int addUserAgentCommission(UserAgentCommission userAgentCommission) {
		return userAgentMapper.insertUserAgentCommission(userAgentCommission);
	}

	@Override
	public UserAgentCommission selectUserAgentCommission(Map map) {
		UserAgentCommission userAgentCommission = userAgentMapper.selectCommissionModel(map);
		return userAgentCommission != null ? userAgentCommission : null;
	}

	@Override
	public int updateUserAgentCommissionByMap(Map map) {
		return userAgentMapper.updateUserAgentCommissionByMap(map);
	}

	@Override
	public ListRange<UserAgentCommission> selectListUserAgentCommissionPage(Map map) {
		ListRange<UserAgentCommission> listRange = new ListRange<UserAgentCommission>();
		int count = userAgentMapper.selectCommissionCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userAgentMapper.selectListUserAgentCommissionPage(map));
		return listRange;
	}

	@Override
	public List<UserAgentCommissionVo> exportUserAgentCommission(Map map) {
		return userAgentMapper.exportUserAgentCommission(map);
	}

	@Override
	public List<UserAgent> selectUserAgentByMap(Map map) {
		return userAgentMapper.selectList(map);
	}

	@Override
	public int addAgentIncome(AgentIncomeStatistics agentIncomeStatistics) {
		return agentIncomeStatisticsMapper.insertSelective(agentIncomeStatistics);
	}

	@Override
	public int deleteAgentIncomeByMap(Map map) {
		return agentIncomeStatisticsMapper.deleteAgentIncomeByMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListRange<AgentRecommendedVo> selectListUserAgentRecommendedPage(Map map) {
		ListRange<AgentRecommendedVo> listRange = new ListRange<AgentRecommendedVo>();
		int count = userAgentMapper.selectRecommendedCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<AgentRecommendedVo> agentRecommendedVos = userAgentMapper.selectListUserAgentRecommendedPage(map);
		if(agentRecommendedVos != null && agentRecommendedVos.size() > 0){
			for(AgentRecommendedVo recommendedVo : agentRecommendedVos){
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_4.getMarker());
				recommendedVo.setStarAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_3.getMarker());
				recommendedVo.setCentreAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker());
				recommendedVo.setCountyAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				
				User user = getUser(recommendedVo.getIncomeUserId());
        		if(user != null){
        			recommendedVo.setPhone(user.getPhone());
        			recommendedVo.setNickName(user.getNickName());
        		}
			}
		}
		listRange.setData(agentRecommendedVos);
		return listRange;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentRecommendedVo> exportUserAgentRecommended(Map map) {
		List<AgentRecommendedVo> agentRecommendedVos = userAgentMapper.exportUserAgentRecommended(map);
		if(agentRecommendedVos != null && agentRecommendedVos.size() > 0){
			for(AgentRecommendedVo recommendedVo : agentRecommendedVos){
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_4.getMarker());
				recommendedVo.setStarAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_3.getMarker());
				recommendedVo.setCentreAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				map.clear();
				map.put("incomeUserId", recommendedVo.getIncomeUserId());
				map.put("incomeDate", recommendedVo.getIncomeDate());
				map.put("agentLevel", AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getMarker());
				recommendedVo.setCountyAgentTotal(agentIncomeStatisticsMapper.selectRecommendedCount(map));
				
				User user = getUser(recommendedVo.getIncomeUserId());
        		if(user != null){
        			recommendedVo.setPhone(user.getPhone());
        			recommendedVo.setNickName(user.getNickName());
        		}
			}
		}
		return agentRecommendedVos;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public User getUser(Long userId){
		User user = new User();
		Map map = new HashMap();
		map.put("mapprerValue", userId);
		UserPart userPart = userPartMapper.selectModel(map);
		if(userPart != null){
			map.clear();
			map.put("phone", userPart.getPhone());
			map.put("tableNum", userPart.getNum());
			user = userMapper.selectModel(map);
		}
		return user != null ? user : null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public User selectUserInfo(String phone) {
		Map map = new HashMap();
		User user = new User();
		map.put("phone", phone);
		UserPart userPart = userPartMapper.selectModel(map);
		if(userPart != null){
			map.clear();
			map.put("phone", phone);
			map.put("tableNum", userPart.getNum());
			user = userMapper.selectModel(map);
		}
		return user != null ? user : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListRange<AgentPromotionVo> selectListUserAgentPromotionPage(Map map) {
		ListRange<AgentPromotionVo> listRange = new ListRange<AgentPromotionVo>();
		int count = userAgentMapper.selectPromotionCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<AgentPromotionVo> agentPromotionVos = userAgentMapper.selectListUserAgentPromotionPage(map);
		if(agentPromotionVos != null && agentPromotionVos.size() > 0){
			for(AgentPromotionVo agentPromotionVo : agentPromotionVos){
				User user = getUser(agentPromotionVo.getIncomeUserId());
        		if(user != null){
        			agentPromotionVo.setPhone(user.getPhone());
        			agentPromotionVo.setNickName(user.getNickName());
        		}
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setOneLevelBusinessTotal(userAgentMapper.selectOneLevelBusinessCount(map));
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setTwoLevelBusinessTotal(userAgentMapper.selectTwoLevelBusinessCount(map));
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setThreeLevelBusinessTotal(userAgentMapper.selectThreeLevelBusinessCount(map));
				
				agentPromotionVo.setBusinessTotal(agentPromotionVo.getOneLevelBusinessTotal() + agentPromotionVo.getTwoLevelBusinessTotal() + agentPromotionVo.getThreeLevelBusinessTotal());
				
				if(agentPromotionVo.getAgentLevel().equals(AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getDisplay())){
					map.clear();
					map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
					String areaName = userAgentMapper.selectAreaName(map);
					agentPromotionVo.setAgentArea(areaName != null ? areaName : ""); 
				}else{
					agentPromotionVo.setAgentArea(null); 
				}
				
			}
		}
		listRange.setData(agentPromotionVos);
		return listRange;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentPromotionVo> exportUserAgentPromotion(Map map) {
		List<AgentPromotionVo> agentPromotionVos = userAgentMapper.exportUserAgentPromotion(map);
		if(agentPromotionVos != null && agentPromotionVos.size() > 0){
			for(AgentPromotionVo agentPromotionVo : agentPromotionVos){
				User user = getUser(agentPromotionVo.getIncomeUserId());
        		if(user != null){
        			agentPromotionVo.setPhone(user.getPhone());
        			agentPromotionVo.setNickName(user.getNickName());
        		}
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setOneLevelBusinessTotal(userAgentMapper.selectOneLevelBusinessCount(map));
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setTwoLevelBusinessTotal(userAgentMapper.selectTwoLevelBusinessCount(map));
				map.clear();
				map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
				map.put("incomeDate", agentPromotionVo.getIncomeDate());
				agentPromotionVo.setThreeLevelBusinessTotal(userAgentMapper.selectThreeLevelBusinessCount(map));

				agentPromotionVo.setBusinessTotal(agentPromotionVo.getOneLevelBusinessTotal() + agentPromotionVo.getTwoLevelBusinessTotal() + agentPromotionVo.getThreeLevelBusinessTotal());
				
				if(agentPromotionVo.getAgentLevel().equals(AgentIncomeStatisticsEnum.AGENT_LEVEL_2.getDisplay())){
					map.clear();
					map.put("incomeUserId", agentPromotionVo.getIncomeUserId());
					String areaName = userAgentMapper.selectAreaName(map);
					agentPromotionVo.setAgentArea(areaName != null ? areaName : ""); 
				}else{
					agentPromotionVo.setAgentArea(""); 
				}
				
			}
		}
		return agentPromotionVos;
	}


	@Override
	public StaticUserRole selectStaticUserRoleByMap(Map map) {
		return roleService.selectStaticUserRoleByMap(map);
	}

	@Override
	public int updateStaticUserRoleByMap(Map map) {
		return userAgentMapper.updateStaticUserRoleByMap(map);
	}

	@Override
	public int selectUserCertificateLevel(Long userId) {
		// TODO Auto-generated method stub
		int publishUserCertificateType = 0;
		int agentLevel=0;//代理商级别
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		UserResult<Business> businessResult = businessService.selectBusiness(map);
		
		map.put("userId", userId);
		map.put("state", 1);
		List<UserAgent> userAgentList = userAgentMapper.selectList(map);
		if(userAgentList.size() > 0){
			agentLevel = userAgentList.get(0).getAgentLevel();
		}
		
		if(businessResult.getState() != 1){
			//说明未进行认证
			publishUserCertificateType = (agentLevel == 0 || agentLevel == 1) ? 0 : agentLevel + 1;
		}else{
			//进行了认证
			if(agentLevel != 0 && agentLevel != 1){
				publishUserCertificateType = agentLevel + 1;
			}else{
				publishUserCertificateType = businessResult.getContent().getCertificationType();
			}
		}
		return publishUserCertificateType;
	}

	@Override
	public UserResult<UserAgent> addOnLineUserAgent(UserAgent userAgent) {
		UserResult<UserAgent> userResult = addUserAgent(userAgent);
		if(userResult.getState() == 1){
			//代理商提成
			UserAgentCommission userAgentCommission = getUserAgentCommission(userAgent);
			if(userAgentCommission != null){
				addUserAgentCommission(userAgentCommission);
			}
		}
		return userResult;
	}
	
	//代理商成交金额提成业务
	private UserAgentCommission getUserAgentCommission(UserAgent userAgent){
		UserAgentCommission userAgentCommission = null;
		Map map = new HashMap();
		map.put("state", 1);
		map.put("newUserId", userAgent.getUserId());
		UserInvite userInvite = userInviteMapper.selectModel(map);
		if(userInvite != null){
			//校验被维护代理商的推荐人是否已获取提成
			map.clear();
			map.put("userId", userInvite.getUserId());
			map.put("recommendId", userAgent.getUserId());
			UserAgentCommission agentCommission = selectUserAgentCommission(map);
			if(agentCommission == null){
				User parentUser = userVerifyService.selectUserByUserId(userInvite.getUserId()).getContent();
				User childUser = userVerifyService.selectUserByUserId(userAgent.getUserId()).getContent();
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
}
