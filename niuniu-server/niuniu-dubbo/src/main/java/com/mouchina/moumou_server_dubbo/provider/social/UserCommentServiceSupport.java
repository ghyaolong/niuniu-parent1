package com.mouchina.moumou_server_dubbo.provider.social;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.advert.AdvertMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.social.UserCommentMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.view.SocialResult;

public class UserCommentServiceSupport implements UserCommentService {
	@Resource
	UserCommentMapper userCommentMapper;
	@Resource
	AdvertMapper advertMapper;
	@Resource
	UserPartMapper userPartMapper;
	@Resource
	UserMapper userMapper;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public SocialResult<UserComment> selectUserComment(Long userCommentId) {
		SocialResult<UserComment> socialResult = new SocialResult<UserComment>();

		UserComment userComment = userCommentMapper.selectByPrimaryKey(userCommentId);
		if (userComment != null) {
			socialResult.setContent(userComment);
			socialResult.setState(1);
		}

		return socialResult;
	}

	@Override
	public SocialResult<UserComment> selectUserComment(Map<String, Object> map) {
		SocialResult<UserComment> userCommentResult = new SocialResult<UserComment>();

		UserComment userComment = userCommentMapper.selectModel(map);
		if (userComment != null) {
			userCommentResult.setContent(userComment);
			userCommentResult.setState(1);
		}

		return userCommentResult;
	}

	@Override
	public SocialResult<UserComment> addUserComment(UserComment userComment) {
		SocialResult<UserComment> userCommentResult = new SocialResult<UserComment>();
		userComment.setCreateTime(new Date());
		int result = userCommentMapper.insertSelective(userComment);
		userCommentResult.setContent(userComment);
		userCommentResult.setState(result);

		return userCommentResult;
	}

	private int selectListUserCommentCount(Map<String, Object> map) {

		return userCommentMapper.selectCount(map);
	}

	private List<UserComment> selectPageListUserComment(Map<String, Object> map) {

		return userCommentMapper.selectPageList(map);
	}

	@Override
	public SocialResult<ListRange<UserComment>> selectListUserCommentPage(Map<String, Object> map) {
		SocialResult<ListRange<UserComment>> userCommentResult = new SocialResult<ListRange<UserComment>>();
		ListRange<UserComment> listRange = new ListRange<UserComment>();
		int count = selectListUserCommentCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(selectPageListUserComment(map));
		userCommentResult.setContent(listRange);
		userCommentResult.setState(1);
		return userCommentResult;
	}

	@Override
	public SocialResult<Integer> deleteUserComment(Long userCommentId) {
		SocialResult<Integer> userCommentResult = new SocialResult<Integer>();

		int result = userCommentMapper.deleteByPrimaryKey(userCommentId);
		userCommentResult.setState(result);

		return userCommentResult;
	}

	@Override
	public SocialResult<Integer> updateUserComment(UserComment userComment) {
		SocialResult<Integer> userCommentResult = new SocialResult<Integer>();
		int result = userCommentMapper.updateByPrimaryKeySelective(userComment);
		userCommentResult.setState(result);
		return userCommentResult;
	}

	@Override
	public SocialResult<List<UserComment>> selectListUserComment(Map<String, Object> map) {
		SocialResult<List<UserComment>> socialResult = new SocialResult<List<UserComment>>();
		List<UserComment> list = selectPageListUserComment(map);
		socialResult.setContent(list);

		return socialResult;
	}

	@Override
	public Map<String, Boolean> selectUserPraiseAndComment(long userId, long advertId) {
		// SocialResult<UserComment> userCommentResult = new
		// SocialResult<UserComment>();
		Map<String, Boolean> returnMap = new HashMap<String, Boolean>();

		Map<String, Object> sqlmap = new HashMap<>();
		sqlmap.put("userId", userId);
		sqlmap.put("commentType", 1);
		sqlmap.put("sourceId", advertId);
		List<UserComment> userCommentList = userCommentMapper.selectList(sqlmap);
		if (userCommentList != null && userCommentList.size() > 0) {
			returnMap.put("commentFlag", true);
		} else {
			returnMap.put("commentFlag", false);
		}

		sqlmap.put("commentType", 2);
		List<UserComment> userCommentPraiseList = userCommentMapper.selectList(sqlmap);
		if (userCommentPraiseList != null && userCommentPraiseList.size() > 0) {
			returnMap.put("praiseFlag", true);
		} else {
			returnMap.put("praiseFlag", false);
		}

		return returnMap;
	}

	@Override
	public int deleteUserCommentPraise(UserComment userComment) {
		int result = 0;
		long sourceId = userComment.getSourceId();// 广告id

		int num = userCommentMapper.deleteUserCommentPraise(userComment);
		if (num == 1) {
			// 删除成功修改广告表点赞数
			Advert advert = advertMapper.selectByAdvertId(sourceId);
			if (advert.getPraisedSize() != null) {
				advert.setPraisedSize(advert.getPraisedSize() - 1);
				result = advertMapper.updateByPrimaryKeySelective(advert);
			}
		}
		return result;
	}

	@Override
	public int insertUserCommentPraise(UserComment userComment) {
		int result = 0;
		// 点赞父id等于广告id
		userComment.setParentId(userComment.getSourceId());
		userComment.setCreateTime(new Date());
		int num = userCommentMapper.insertSelective(userComment);
		if (num == 1) {
			// 点赞成功修改广告表点赞数
			Advert advert = advertMapper.selectByAdvertId(userComment.getSourceId());
			if (advert.getPraisedSize() == null) {
				advert.setPraisedSize(1);
				result = advertMapper.updateByPrimaryKeySelective(advert);
			} else {
				advert.setPraisedSize(advert.getPraisedSize() + 1);
				result = advertMapper.updateByPrimaryKeySelective(advert);
			}

		}
		return result;
	}

	@Override
	public int deleteUserComment(UserComment userComment) {
		Map<String, Object> map = new HashMap<>();
		int result = 0;
		Long id = userComment.getSourceId();
		map.put("id", userComment.getId());
		map.put("commentType", userComment.getCommentType());
		map.put("userId", userComment.getUserId());
		UserComment uc = userCommentMapper.selectModel(map);
		if (uc != null) {
			result = userCommentMapper.deleteByPrimaryKey(userComment.getId());
			if (result == 1) {
				Advert advert = advertMapper.selectAdvertByPrimaryKey(id);
				if (advert.getCommentSize() != null && advert.getCommentSize() > 0) {
					advert.setCommentSize(advert.getCommentSize() - 1);
					result = advertMapper.updateByPrimaryKeySelective(advert);
				}
			}
		}
		return result;
	}

	@Override
	public User getUser(Long userId) {
		Map<String, String> userPartMap = new HashMap<String, String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();

		// 查询用户的财富
		Map<String, Object> userAssetsMap = new HashMap<String, Object>();
		userAssetsMap.put("userId", userId);
		userAssetsMap.put("tableNum", tableIndex);
		User user = userMapper.selectModel(userAssetsMap);

		user.setTableNum(tableIndex);
		return user;
	}

	@Override
	public UserComment getCommentByPK(long id) {
		// TODO Auto-generated method stub
		return userCommentMapper.selectByPrimaryKey(id);
	}
}
