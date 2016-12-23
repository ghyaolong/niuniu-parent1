package com.mouchina.moumou_server_dubbo.provider.social;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.UtilMaskCode;
import com.mouchina.moumou_server.dao.social.UsersRelationMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UsersRelation;
import com.mouchina.moumou_server.exceptions.UserException;
import com.mouchina.moumou_server_interface.social.UsersRelationService;
import com.mouchina.moumou_server_interface.view.SocialResult;

public class UsersRelationServiceSupport implements UsersRelationService {

	@Resource
	UsersRelationMapper usersRelationMapper;

	@Override
	public SocialResult<UsersRelation> selectUsersRelation(Long usersRelationId) {
		// TODO Auto-generated method stub

		SocialResult<UsersRelation> socialResult = new SocialResult<UsersRelation>();

		UsersRelation usersRelation = usersRelationMapper.selectByPrimaryKey(usersRelationId);
		if (usersRelation != null) {
			socialResult.setContent(usersRelation);
			socialResult.setState(1);
		}

		return socialResult;
	}

	@Override
	public SocialResult<UsersRelation> selectUsersRelation(Map map) {
		// TODO Auto-generated method stub
		SocialResult<UsersRelation> usersRelationResult = new SocialResult<UsersRelation>();

		UsersRelation usersRelation = usersRelationMapper.selectModel(map);
		if (usersRelation != null) {
			usersRelationResult.setContent(usersRelation);
			usersRelationResult.setState(1);
		}

		return usersRelationResult;
	}

	@Override
	public SocialResult<UsersRelation> addUsersRelation(UsersRelation usersRelation) {
		// TODO Auto-generated method stub
		SocialResult<UsersRelation> usersRelationResult = new SocialResult<UsersRelation>();
		usersRelation.setCreateTime(new Date());
		int result = usersRelationMapper.insertSelective(usersRelation);
		usersRelationResult.setContent(usersRelation);
		usersRelationResult.setState(result);

		return usersRelationResult;
	}

	@Override
	public int selectListUsersRelationCount(Map map) {
		return usersRelationMapper.selectCount(map);
	}

	@Override
	public SocialResult<ListRange<UsersRelation>> selectListUsersRelationPage(Map map) {
		// TODO Auto-generated method stub
		SocialResult<ListRange<UsersRelation>> usersRelationResult = new SocialResult<ListRange<UsersRelation>>();
		ListRange<UsersRelation> listRange = new ListRange<UsersRelation>();
		int count = selectListUsersRelationCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(usersRelationMapper.selectPageList(map));
		usersRelationResult.setContent(listRange);
		usersRelationResult.setState(1);
		return usersRelationResult;
	}

	@Override
	public SocialResult<Integer> deleteUsersRelation(Long UsersRelationId) {
		// TODO Auto-generated method stub
		SocialResult<Integer> usersRelationResult = new SocialResult<Integer>();

		int result = usersRelationMapper.deleteByPrimaryKey(UsersRelationId);
		usersRelationResult.setState(result);

		return usersRelationResult;
	}

	@Override
	public SocialResult<Integer> updateUsersRelation(UsersRelation usersRelation) {
		// TODO Auto-generated method stub
		SocialResult<Integer> usersRelationResult = new SocialResult<Integer>();
		int result = usersRelationMapper.updateByPrimaryKeySelective(usersRelation);
		usersRelationResult.setState(result);

		return usersRelationResult;
	}

	@Override
	public SocialResult<List<UsersRelation>> selectListPageUsersRelation(Map map) {
		// TODO Auto-generated method stub

		SocialResult<List<UsersRelation>> socialResult = new SocialResult<List<UsersRelation>>();
		socialResult.setContent(usersRelationMapper.selectPageList(map));
		socialResult.setState(1);
		return socialResult;
	}

	@Override
	public int updateTargetRelation(User user) {
		if (user == null || null == user.getId() || 0 == user.getId().longValue()) {
			throw new UserException(false, "用户编号不存在");
		}
		UsersRelation relation = new UsersRelation();
		relation.setRelationUserId(user.getId());
		if(null != user.getSex()){
			relation.setRelationUserSex((byte) user.getSex().intValue());
		}else{
			relation.setRelationUserSex((byte)0);
		}
		

		if (user.getAvatar() != null) {
			relation.setRelationUserAvatar(user.getAvatar());
		}
		relation.setRelationType((byte) 2);
		final String nickName = user.getNickName();
		relation.setRelationUserNickName(nickName);
		if (StringUtils.isBlank(nickName)) {
			relation.setRelationUserNickName(UtilMaskCode.telephone(user.getPhone()));
		}
		return usersRelationMapper.updateTargetRelation(relation);
	}

	@Override
	public List<UsersRelation> selectListUsersRelation(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return usersRelationMapper.selectList(map);
	}

	public List<UsersRelation> selectRelation(Map map) {
		// TODO Auto-generated method stub
		return usersRelationMapper.selectPageList(map);
	}

	/**
	 * 判断用户userIdA是否是userIdB的粉丝
	 * 
	 * @param userIdA
	 * @param userIdB
	 * @return
	 */
	public Boolean isAFansB(long userIdA, long userIdB) {
		Map sqlMap = new HashMap();
		sqlMap.put("userId", userIdB);
		sqlMap.put("relationUserId", userIdA);
		sqlMap.put("relationType", 2);

		List<UsersRelation> relationList = selectRelation(sqlMap);

		if (relationList != null && relationList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
