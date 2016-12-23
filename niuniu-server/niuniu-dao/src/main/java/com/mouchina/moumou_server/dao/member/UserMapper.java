package com.mouchina.moumou_server.dao.member;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.member.User;

public interface UserMapper  extends BaseMapper<User,Integer>{

	public User selectByThirdUid(Map<String,Object> map);

	public User selectUserInfoNotNull(Map<String,Object> map);
	/**
	 * 获取用户排行榜信息
	 * @return	map是userID和table下标
	 */
	public RedEnvelopeTop getUserRankInfo(Map<String,Object> map);
	
	/**
	 * 根据userID查询user
	 * @param userId
	 * @param tableNum
	 * @return
	 */
	public User findByUserId(Long userId,Integer tableNum);
	/**
	 * 查找沉默用户
	 * @param map
	 * @return
	 */
	List<User> selectComeBackUser(Map<String, Object> map);
	
	User selectUser(Map<String, Object> map);
}