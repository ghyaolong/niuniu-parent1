package com.mouchina.moumou_server.dao.social;

import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.social.UserInvite;

public interface UserInviteMapper  extends BaseMapper<UserInvite,Long> {
	/**
	 * 查询时间段内用户邀请列表
	 * @param map
	 * @return
	 */
	int selectUserInviteBetweenTime(Map<String, Object> map);
}