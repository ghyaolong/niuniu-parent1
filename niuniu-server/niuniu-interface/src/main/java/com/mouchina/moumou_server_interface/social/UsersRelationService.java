package com.mouchina.moumou_server_interface.social;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server_interface.view.SocialResult;

public interface UsersRelationService {
	SocialResult<UsersRelation> selectUsersRelation(Long UsersRelationId);
	SocialResult<UsersRelation> selectUsersRelation(Map map);

	SocialResult<UsersRelation> addUsersRelation(UsersRelation UsersRelation);

	SocialResult<ListRange<UsersRelation>> selectListUsersRelationPage(Map map);

	SocialResult<Integer> deleteUsersRelation(Long UsersRelationId);

	SocialResult<Integer> updateUsersRelation(UsersRelation UsersRelation);
	
	SocialResult<List<UsersRelation>> selectListPageUsersRelation(Map map);
	 int selectListUsersRelationCount(Map  map);
	 /**
	  * A用户是否是B用户的粉丝
	  * @param userA
	  * @param userB
	  * @return
	  */
	Boolean isAFansB(long userA,long userB);
	 
	/**
	 * 修改当前用户关联信息
	 * @param user 当前用户
	 * @return
	 */
	int updateTargetRelation(User user);
	
	List<UsersRelation> selectListUsersRelation(Map<String,Object> map);
	public List<UsersRelation> selectRelation(Map map);
}
