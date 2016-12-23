package com.mouchina.moumou_server_dubbo.provider.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.system.SystemMessageMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server_interface.system.SystemMessageService;

public class SystemMessageServiceSupport implements SystemMessageService {
	@Resource
	SystemMessageMapper systemMessageMapper;

	@Override
	public int insertSystemMessage(SystemMessage systemMessage) {
		// TODO Auto-generated method stub
		int result = 0;
		if (systemMessage != null) {
			result = systemMessageMapper.insertSelective(systemMessage);
		}
		return result;
	}

	@Override
	public List<SystemMessage> selectSystemMessageByUserId(SystemMessage systemMessage) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("userId", systemMessage.getUserId());
		map.put("sysId", systemMessage.getSysId());
		List<SystemMessage> ListMessage = systemMessageMapper.selectMessage(map);
		return ListMessage;
	}

	@Override
	public int insertUserReadMessage(SystemMessage systemMessage) {
		// TODO Auto-generated method stub
		return systemMessageMapper.insertSelective(systemMessage);
	}

	@Override
	public List<SystemMessage> selectUserNotReadMessage(User user) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("userId", user.getId());
		return systemMessageMapper.selectList(map);
	}

	@Override
	public List<SystemMessage> selectUserReadMessage(User user) {
		// TODO Auto-generated method stub
		return systemMessageMapper.selectRedMessage(user.getId());
	}

	@Override
	public List<SystemMessage> selectAllMessage(Long userId) {
		// TODO Auto-generated method stub
		return systemMessageMapper.selectAllMessage(userId);
	}

	@Override
	public int updateUserReadMessage(SystemMessage systemMessage) {
		// TODO Auto-generated method stub
		return systemMessageMapper.updateByPrimaryKeySelective(systemMessage);
	}

}
