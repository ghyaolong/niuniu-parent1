package com.mouchina.moumou_server.dao.member;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;

public interface UserActivityDeployMapper extends BaseMapper<UserActivityDeploy, Long> {
	
	int updateActivityCount(UserActivityDeploy userActivityDeploy);
}