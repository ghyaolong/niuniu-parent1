package com.mouchina.moumou_server_dubbo.provider.social;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.member.UserActivityDeployMapper;
import com.mouchina.moumou_server.dao.social.UserInviteMapper;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.view.SocialResult;

public class UserInviteServiceSupport implements UserInviteService {

	@Resource
	UserInviteMapper  userInviteMapper;
	@Resource
	UserActivityDeployMapper userActivityDeployMapper;
	@Override
	public SocialResult<UserInvite> selectUserInvite(Long UserInviteId) {
		// TODO Auto-generated method stub

		SocialResult<UserInvite> socialResult = new SocialResult<UserInvite>();

		UserInvite userInvite = userInviteMapper.selectByPrimaryKey(UserInviteId);
		if (userInvite != null) {
			socialResult.setContent(userInvite);
			socialResult.setState(1);
		}

		return socialResult;
	}

	@Override
	public SocialResult<UserInvite> selectUserInvite(Map map) {
		// TODO Auto-generated method stub
		SocialResult<UserInvite> userInviteResult = new SocialResult<UserInvite>();

		UserInvite userInvite = userInviteMapper.selectModel(map);
		if (userInvite != null) {
			userInviteResult.setContent(userInvite);
			userInviteResult.setState(1);
		}

		return userInviteResult;
	}

	@Override
	public SocialResult<UserInvite> addUserInvite(UserInvite userInvite) {
		// TODO Auto-generated method stub
		SocialResult<UserInvite> userInviteResult = new SocialResult<UserInvite>();
		userInvite.setCreateTime(new Date());
		int result = userInviteMapper.insertSelective(userInvite);
		userInviteResult.setContent(userInvite);
		userInviteResult.setState(result);

		return userInviteResult;
	}

	@Override
	public int  selectListUserInviteCount(Map map){
		
		
		return  userInviteMapper.selectCount(map);
	}
	private List<UserInvite>  selectPageListUserInvite(Map map){
		
		
		return  userInviteMapper.selectPageList(map);
	}
	@Override
	public SocialResult<ListRange<UserInvite>> selectListUserInvitePage(Map map) {
		// TODO Auto-generated method stub
		SocialResult<ListRange<UserInvite>> userInviteResult = new SocialResult<ListRange<UserInvite>>();
		ListRange<UserInvite> listRange = new ListRange<UserInvite>();
		int count = selectListUserInviteCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListUserInvite(map));
		userInviteResult.setContent(listRange);
		userInviteResult.setState(1);
		return userInviteResult;
	}

	@Override
	public SocialResult<Integer> deleteUserInvite(Long UserInviteId) {
		// TODO Auto-generated method stub
		SocialResult<Integer> userInviteResult = new SocialResult<Integer>();

		int result = userInviteMapper.deleteByPrimaryKey(UserInviteId);
		userInviteResult.setState(result);

		return userInviteResult;
	}

	@Override
	public SocialResult<Integer> updateUserInvite(UserInvite userInvite) {
		// TODO Auto-generated method stub
		SocialResult<Integer> userInviteResult = new SocialResult<Integer>();
		int result = userInviteMapper.updateByPrimaryKeySelective(userInvite);
		userInviteResult.setState(result);

		return userInviteResult;
	}

	@Override
	public SocialResult<List<UserInvite>> selectListUserInvite(Map map) {
		// TODO Auto-generated method stub
		 SocialResult<List<UserInvite>> socialResult=new SocialResult<List<UserInvite>>();
		 
		 List<UserInvite>  list=selectPageListUserInvite(map);
		 socialResult.setContent(list);
		 
		return socialResult;
	}

	@Override
	public int selectUserInviteBetweenTimeAndTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userInviteMapper.selectUserInviteBetweenTime(map);
	}

	@Override
	public UserActivityDeploy selectUserActivityDeploy(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userActivityDeployMapper.selectModel(map);
	}

	@Override
	public int updateUserActivityDeployCount(UserActivityDeploy userActivityDeploy) {
		// TODO Auto-generated method stub
		return userActivityDeployMapper.updateActivityCount(userActivityDeploy);
	}

	@Override
	public int addUserActivityDeploy(UserActivityDeploy userActivityDeploy) {
		// TODO Auto-generated method stub
		return userActivityDeployMapper.insertSelective(userActivityDeploy);
	}
	
}
