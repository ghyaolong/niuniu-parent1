package com.mouchina.moumou_server.dao.social;

import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.social.UsersRelation;

public interface UsersRelationMapper  extends BaseMapper<UsersRelation,Long>{
	/**
	 * 更新目标关系用户信息
	 * @param record 记录
	 * @return
	 */
	int updateTargetRelation(UsersRelation record);
	/**
	 * 查询当天fans的数量
	 * @param map
	 * @return
	 */
	public Integer selectFansCount(Map<String,Object> map);
}