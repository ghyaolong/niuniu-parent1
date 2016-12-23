package com.mouchina.moumou_server.dao.award;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.SignHistory;

public interface SignHistoryMapper extends BaseMapper<SignHistory, Long> {
	int deleteByPrimaryKey(Long id);

	int insert(SignHistory record);

	int insertSelective(SignHistory record);

	SignHistory selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SignHistory record);

	int updateByPrimaryKey(SignHistory record);
	
	/**
	 * 根据userid查询
	 * @param userId
	 * @return
	 */
	public SignHistory selectByUserId(Long userId);
	/**
	 * 根据userid更新
	 * @param userId
	 * @return
	 */
	public int updateByUserId(SignHistory record);
}