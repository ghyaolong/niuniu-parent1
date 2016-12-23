package com.mouchina.web.service.api.social;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.web.service.api.vo.CommentVo;
import com.mouchina.web.service.api.vo.UserRedEnvelope;

public interface SocialWebService {

	
	/***
	 * 获取用户关系分页
	 * @param map
	 * @return
	 */
	ListRange<UsersRelation> selectListRangeUsersRelation(Map map);
	int selectListUsersRelationCount(Map map);
	
	/**
	 * 添加用户关系
	 * @param usersRelation
	 * @return
	 */
	UsersRelation  addUsersRelation(UsersRelation usersRelation);
	
	
	/**
	 * 修改用户关系
	 * @param usersRelation
	 * @return
	 */
	int  updateUsersRelation(UsersRelation usersRelation);
	
	
	/**
	 * 移除用户关系
	 * @param usersRelation
	 * @return
	 */
	int removeUsersRelation(UsersRelation usersRelation);
	
	
	
	/***
	 * 获取用户邀请列表分页
	 * @param map
	 * @return
	 */
	ListRange<UserInvite> selectListRangeUserInvite(Map map);
	int selectListUserInviteCount(Map map);
	/***
	 * 获取用户邀请列表
	 * @param map
	 * @return
	 */
	List<UserInvite> selectListUserInvite(Map map);
	
	UserInvite  addUserInvite(UserInvite userInvite);
	
	
	 ListRange<UserComment> selectCommentListRange(Map<String, Object> map);
	
	 ListRange<CommentVo> getListRangeCommentVo(Map map, String contentKey);
	 ListRange<CommentVo> getListRangeCommentVoNoCache(Map map);
	 /**
	  * 获取某个时间段内用户邀请列表
	  * @param map
	  * @return
	  */
	 int selectUserInviteBetweenTimeAndTime(Map<String,Object> map);
	 
	 UserActivityDeploy selectUserActivityDeploy(Map<String,Object> map);
	 
	 int updateUserActivityDeploy(UserActivityDeploy userActivityDeploy);
	 
	 int addUserActivityDeploy(UserActivityDeploy userActivityDeploy);
	 
}
