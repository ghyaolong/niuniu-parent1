package com.mouchina.admin.service.api.member;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.vo.AgentIncomeStatisticsVo;
import com.mouchina.moumou_server.entity.vo.AgentPromotionVo;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;
import com.mouchina.moumou_server_interface.view.UserResult;

public interface UserAgentAdminService  {

	
	UserResult<ListRange<UserAgent>> selectListUserAgentPage(Map map);
	
	UserResult<Integer> updateUserAgent(UserAgent userAgent);
	
	UserResult<UserAgent> addUserAgent(UserAgent userAgent);
	
	UserResult<UserAgent> selectUserAgent(Long userAgentId);
	UserResult<UserAgent> selectUserAgent(Map map);
	UserResult<UserAgent> selectUserAgentByUserId(Long userId);
	/**
	 * 查询代理统计
	 * @param phone
	 * @return
	 */
	UserResult<UserAgent> selectUserAgentStatistics(Map map);
	 /***
     * 0 代理商已经存在  1 可以添加    2 代理商用户不可添加  －1 用户不存在请先注册
     * @param phone
     * @return
     */
	UserResult<User> checkAgent(String phone);
	
	/**
	 * 查询代理商成交金额提成信息
	 * @param map
	 * @return
	 */
	ListRange<UserAgentCommission> selectListUserAgentCommissionPage(Map map);
	
	/**
	 * 导出代理商成交金额提成信息
	 * @param map
	 * @return
	 */
	List<UserAgentCommissionVo> exportUserAgentCommission(Map map);
	
	int updateUserAgentCommissionByMap(Map map);
	
	/**
	 * 查询代理商推荐提成收益信息
	 * @param map
	 * @return
	 */
	ListRange<AgentRecommendedVo> selectListUserAgentRecommendedPage(Map map);
	
	/**
	 * 导出代理商推荐提成收益信息
	 * @param map
	 * @return
	 */
	List<AgentRecommendedVo> exportUserAgentRecommended(Map map);
	
	//查询用户信息
	User selectUserInfo(String phone);
	
	/**
	 * 查询代理商推广收益信息
	 * @param map
	 * @return
	 */
	ListRange<AgentPromotionVo> selectListUserAgentPromotionPage(Map map);
	
	/**
	 * 导出代理商推广收益信息
	 * @param map
	 * @return
	 */
	List<AgentPromotionVo> exportUserAgentPromotion(Map map);
}
