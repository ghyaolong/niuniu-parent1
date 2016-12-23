package com.mouchina.moumou_server.dao.member;


import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.AreaBusiness;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.vo.AgentPromotionVo;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;

public interface UserAgentMapper extends BaseMapper<UserAgent, Long> {
	
	int insertUserAgentCommission(UserAgentCommission userAgentCommission);
	
	int updateUserAgentCommissionByMap(Map map);
	
	UserAgentCommission selectCommissionModel(Map map);
	
	List<UserAgentCommission> selectListUserAgentCommissionPage(Map map);
	
	List<UserAgentCommissionVo> exportUserAgentCommission(Map map);
	
	int selectCommissionCount(Map map);
	
	AreaBusiness selectAreaBusiness(Map map);
	
	int updateAreaBusinessByMap(Map map);
	
	int insertAreaBusiness(AreaBusiness areaBusiness);
	
	List<AgentRecommendedVo> selectListUserAgentRecommendedPage(Map map);
	
	int selectRecommendedCount(Map map);
	
	List<AgentRecommendedVo> exportUserAgentRecommended(Map map);
	
	List<AgentPromotionVo> selectListUserAgentPromotionPage(Map map);
	
	int selectPromotionCount(Map map);
	
	List<AgentPromotionVo> exportUserAgentPromotion(Map map);

	int selectOneLevelBusinessCount(Map map);
	
	int selectTwoLevelBusinessCount(Map map);
	
	int selectThreeLevelBusinessCount(Map map);
	
	String selectAreaName(Map map);

	int updateStaticUserRoleByMap(Map map);

}