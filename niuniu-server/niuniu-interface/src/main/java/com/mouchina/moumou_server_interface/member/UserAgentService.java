package com.mouchina.moumou_server_interface.member;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.AgentIncomeStatistics;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server.entity.vo.AgentPromotionVo;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;
import com.mouchina.moumou_server_interface.view.UserResult;

public interface UserAgentService {

	
	UserResult<UserAgent> selectUserAgent(Long userAgentId);
	UserResult<UserAgent> selectUserAgent(Map map);

	UserResult<UserAgent> addUserAgent(UserAgent userAgent);

	UserResult<ListRange<UserAgent>> selectListUserAgentPage(Map map);

	UserResult<Integer> deleteUserAgent(Long userAgentId);

	UserResult<Integer> updateUserAgent(UserAgent userAgent);
	
	//保存代理商成交金额提成信息
	int addUserAgentCommission(UserAgentCommission userAgentCommission);
	
	//更新代理商提成信息结算标志
	int updateUserAgentCommissionByMap(Map map);
	
	//获取代理商提成信息
	UserAgentCommission selectUserAgentCommission(Map map);
	
	//分页查询代理商成交金额提成信息
	ListRange<UserAgentCommission> selectListUserAgentCommissionPage(Map map);
	
	//导出代理商成交金额提成信息
	List<UserAgentCommissionVo> exportUserAgentCommission(Map map);
	
	//查询代理商信息
	List<UserAgent> selectUserAgentByMap(Map map);
	
	//保存代理商收益信息
	int addAgentIncome(AgentIncomeStatistics agentIncomeStatistics);
	
	//删除代理商收益信息
	int deleteAgentIncomeByMap(Map map);
	
	//分页查询代理商推荐提成信息
	ListRange<AgentRecommendedVo> selectListUserAgentRecommendedPage(Map map);
	
	//导出代理商推荐提成信息
	List<AgentRecommendedVo> exportUserAgentRecommended(Map map);
	
	User selectUserInfo(String phone);
	
	//分页查询代理商推广收益信息
	ListRange<AgentPromotionVo> selectListUserAgentPromotionPage(Map map);
	
	//导出代理商推广收益信息
	List<AgentPromotionVo> exportUserAgentPromotion(Map map);
	
	//查询用户角色信息
	StaticUserRole selectStaticUserRoleByMap(Map map);
	
	//修改用户角色信息
	int updateStaticUserRoleByMap(Map map);
	
	/**
	 * 查询用户认证信息 0普通用户 1个人认证 2商户认证 3区县4中心 5星级
	 * @param userId
	 * @return
	 */
	int selectUserCertificateLevel(Long userId);
	
	/**
	 * 线上维护为星级代理商方法
	 * @param userAgent(必填属性：userId 当前用户ID，agentLevel 代理商等级，值为4  agentPiont 代理商点数，值为0  applyAmount 成为代理商金额，单位：分)
	 * @return
	 */
	UserResult<UserAgent> addOnLineUserAgent(UserAgent userAgent);
}
