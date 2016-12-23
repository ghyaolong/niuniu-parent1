package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.member.UserLevelConfigMapper;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;

public class UserLevelConfigServiceSupport implements UserLevelConfigService {

	@Resource
	private UserLevelConfigMapper userLevelConfigMapper;
	
	@Override
	public UserLevelConfig selectUserLevelConfig(Map map) {
		// TODO Auto-generated method stub
		return userLevelConfigMapper.selectModel(map);
	}

	public List<UserLevelConfig> selectAllList(Map map) {
		// TODO Auto-generated method stub
	
		return userLevelConfigMapper.selectList(map);
	}
}
