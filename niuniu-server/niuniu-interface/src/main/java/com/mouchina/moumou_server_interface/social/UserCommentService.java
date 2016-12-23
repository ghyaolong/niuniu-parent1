package com.mouchina.moumou_server_interface.social;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server_interface.view.SocialResult;

public interface UserCommentService {

	public SocialResult<UserComment> selectUserComment(Long userCommentId);
	public SocialResult<UserComment> selectUserComment(Map<String,Object> map);

	public SocialResult<UserComment> addUserComment(UserComment userComment);

	public SocialResult<ListRange<UserComment>> selectListUserCommentPage(Map<String,Object> map);

	public SocialResult<Integer> deleteUserComment(Long userCommentId);

	public SocialResult<Integer> updateUserComment(UserComment userComment);
	
	public SocialResult<List<UserComment>> selectListUserComment(Map<String,Object> map);
	/**
	 * 查询用户是否对某广告进行点赞和评论
	 * @param userId
	 * @param advertId
	 * @return map<'点赞或者评论键值(commentFlag、praiseFlag)',true或者false>
	 */
	public Map<String,Boolean> selectUserPraiseAndComment(long userId , long advertId);
	/**
	 * 取消点赞
	 * @param userComment
	 * @return
	 */
	public int deleteUserCommentPraise(UserComment userComment);
	/**
	 * 点赞
	 * @param userComment
	 * @return
	 */
	public int insertUserCommentPraise(UserComment userComment);
	/**
	 * 删除自己的评论
	 * @param userComment
	 * @return
	 */
	public int deleteUserComment(UserComment userComment);
	
	/**
	 * 根据userid获取用户
	 * @param userId
	 * @return
	 */
	public User getUser(Long userId);
	/**
	 * 根据主键查询评论vo
	 * @param id
	 * @return
	 */
	public UserComment getCommentByPK(long id);

}
