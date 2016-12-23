package com.mouchina.moumou_server_interface.social;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server_interface.view.SocialResult;

public interface UserInviteService {

	
	SocialResult<UserInvite> selectUserInvite(Long userInviteId);
	SocialResult<UserInvite> selectUserInvite(Map map);

	SocialResult<UserInvite> addUserInvite(UserInvite userInvite);

	SocialResult<ListRange<UserInvite>> selectListUserInvitePage(Map map);

	SocialResult<Integer> deleteUserInvite(Long userInviteId);

	SocialResult<Integer> updateUserInvite(UserInvite userInvite);
	
	SocialResult<List<UserInvite>> selectListUserInvite(Map map);
	 int  selectListUserInviteCount(Map map);
	 /**
	  * 查询时间段内用户邀请人数
	  * @param map
	  * @return
	  */
	 int selectUserInviteBetweenTimeAndTime(Map<String, Object> map);
	 /**
	  * 查询用户活动配置
	  * @param map
	  * @return
	  */
	 UserActivityDeploy selectUserActivityDeploy(Map<String, Object> map);
	 /**
	  * 修改用户参与次数
	  * @param userActivityDeploy
	  * @return
	  */
	 int updateUserActivityDeployCount(UserActivityDeploy userActivityDeploy);
	 
	 int addUserActivityDeploy(UserActivityDeploy userActivityDeploy);
}
