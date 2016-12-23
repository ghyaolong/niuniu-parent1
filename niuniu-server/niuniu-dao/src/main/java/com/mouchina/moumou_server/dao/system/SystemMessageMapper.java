package com.mouchina.moumou_server.dao.system;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.system.SystemMessage;

public interface SystemMessageMapper extends BaseMapper<SystemMessage, Long>{
	
	List<SystemMessage> selectMessage(Map map);
	/**
	 * 查询所有系统消息
	 * @return
	 */
	List<SystemMessage> selectAllMessage(Long userId);
	/**
	 * 查询用户已读消息
	 * @param id
	 * @return
	 */
	List<SystemMessage> selectRedMessage(long id);
}