package com.mouchina.web.service.impl.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.mouchina.base.cache.CacheManager;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.CommentVo;
import com.mouchina.web.service.api.vo.UserRedEnvelope;

@Service
public class SocialWebServiceSupport implements SocialWebService {

	@Resource
	UsersRelationService usersRelationService;
	@Resource
	UserInviteService userInviteService;
	
	@Resource
	UserCommentService userCommentService;
	@Resource
	AdvertService advertService;
	@Resource
	UserVerifyService userVerifyService;
	@Resource
	CacheManager cacheManager;
	@Override
	public ListRange<UsersRelation> selectListRangeUsersRelation(Map map) {
		// TODO Auto-generated method stub
		return usersRelationService.selectListUsersRelationPage(map).getContent();
	}
	
	@Override
	public int selectListUsersRelationCount(Map map) {
		// TODO Auto-generated method stub
		return usersRelationService.selectListUsersRelationCount(map);
	}

	@Override
	public UsersRelation addUsersRelation(UsersRelation usersRelation) {
		// TODO Auto-generated method stub
		return usersRelationService.addUsersRelation(usersRelation).getContent();
	}

	@Override
	public int updateUsersRelation(UsersRelation usersRelation) {
		// TODO Auto-generated method stub
		return usersRelationService.updateUsersRelation(usersRelation).getContent();
	}

	@Override
	public int removeUsersRelation(UsersRelation usersRelation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListRange<UserInvite> selectListRangeUserInvite(Map map) {
		// TODO Auto-generated method stub
		return userInviteService.selectListUserInvitePage(map).getContent();
	}
	
	@Override
	public int selectListUserInviteCount(Map map) {
		// TODO Auto-generated method stub
		return userInviteService.selectListUserInviteCount(map);
	}
	

	@Override
	public List<UserInvite> selectListUserInvite(Map map) {
		// TODO Auto-generated method stub
		return userInviteService.selectListUserInvite(map).getContent();
	}

	@Override
	public UserInvite addUserInvite(UserInvite userInvite) {
		// TODO Auto-generated method stub
		return userInviteService.addUserInvite(userInvite).getContent();
	}
	@Override
	public ListRange<UserComment> selectCommentListRange(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return userCommentService.selectListUserCommentPage(map).getContent();
	}
	
	@Override
	public ListRange<CommentVo> getListRangeCommentVo(Map map, String contentKey) {
		// TODO Auto-generated method stub

		ListRange<CommentVo> menuListVo = cacheManager.getListRangeByMap(
				CommentVo.class, "getListRangeCommentVo_" + contentKey, map);

		if (menuListVo == null) {
			ListRange<UserComment> menuList = selectCommentListRange(map);
			List<CommentVo> listVo = new ArrayList<CommentVo>();
			menuListVo = new ListRange<CommentVo>();
			menuListVo.setPage(menuList.getPage());
			menuListVo.setData(listVo);
			if (menuList != null && menuList.getData().size() > 0) {
				
				
				
				for (UserComment UserComment : menuList.getData()) {
					CommentVo commentVo = new CommentVo();
					BeanUtils.copyProperties(UserComment, commentVo);
					listVo.add(commentVo);
				}

				menuListVo.setData(listVo);

				cacheManager.addListRangeByMap(CommentVo.class,
						"getListRangeCommentVo_" + contentKey, map,
						menuListVo, 3 );
			}else{
				
				
			}
		}

		return menuListVo;
	}
	
	@Override
	public ListRange<CommentVo> getListRangeCommentVoNoCache(Map map) {
		// TODO Auto-generated method stub

		ListRange<CommentVo> menuListVo = null;

		if (menuListVo == null) {
			List<CommentVo> listVo = new ArrayList<CommentVo>();
			ListRange<UserComment> menuList = selectCommentListRange(map);
			if (menuList != null && menuList.getData()!=null) {
				menuListVo = new ListRange<CommentVo>();
				for (UserComment UserComment : menuList.getData()) {
					CommentVo commentVo = new CommentVo();
					BeanUtils.copyProperties(UserComment, commentVo);
					listVo.add(commentVo);
				}
			
			}
			menuListVo.setPage(menuList.getPage());
			menuListVo.setData(listVo);
		}

		return menuListVo;
	}

	@Override
	public int selectUserInviteBetweenTimeAndTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userInviteService.selectUserInviteBetweenTimeAndTime(map);
	}

	@Override
	public UserActivityDeploy selectUserActivityDeploy(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userInviteService.selectUserActivityDeploy(map);
	}

	@Override
	public int updateUserActivityDeploy(UserActivityDeploy userActivityDeploy) {
		// TODO Auto-generated method stub
		return userInviteService.updateUserActivityDeployCount(userActivityDeploy);
	}

	@Override
	public int addUserActivityDeploy(UserActivityDeploy userActivityDeploy) {
		// TODO Auto-generated method stub
		return userInviteService.addUserActivityDeploy(userActivityDeploy);
	}
	
	
}
