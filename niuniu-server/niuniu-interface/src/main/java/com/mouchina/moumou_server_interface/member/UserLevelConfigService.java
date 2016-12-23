package com.mouchina.moumou_server_interface.member;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.member.UserLevelConfig;

public interface UserLevelConfigService {
	/**
	 * 查询用户等级配置信息
	 * @param map
	 * @return
	 */
	UserLevelConfig selectUserLevelConfig(Map map);
	
	/**
	 * 查询所有记录
	 * @param map
	 * @return
	 */
	public List<UserLevelConfig> selectAllList(Map map);
}
