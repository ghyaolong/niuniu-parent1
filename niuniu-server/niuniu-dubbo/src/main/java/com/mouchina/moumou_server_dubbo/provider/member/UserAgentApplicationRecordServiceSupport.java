package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.member.UserAgentApplicationRecordMapper;
import com.mouchina.moumou_server.entity.member.UserAgentApplicationRecord;
import com.mouchina.moumou_server_interface.member.UserAgentApplicationRecordService;
import com.mouchina.moumou_server_interface.view.Result;

public class UserAgentApplicationRecordServiceSupport implements UserAgentApplicationRecordService {

	@Resource
	private UserAgentApplicationRecordMapper userAgentApplicationRecordMapper;

	@Override
	public Result<UserAgentApplicationRecord> saveOrUpdateAgentApplicationRecord(UserAgentApplicationRecord userAgentApplicationRecord) {
		int result = 0;
		Result<UserAgentApplicationRecord> agentApplicationRecordResult = new Result<>();
		Map<String, Object> map = new HashMap<>();
		Date date = new Date();
		map.put("userId", userAgentApplicationRecord.getUserId());
		UserAgentApplicationRecord agentApplicationRecord = userAgentApplicationRecordMapper.selectModel(map);
		if(null == agentApplicationRecord){
			userAgentApplicationRecord.setCreateTime(date);
			result = userAgentApplicationRecordMapper.insertSelective(userAgentApplicationRecord);
		}else{
			userAgentApplicationRecord.setId(agentApplicationRecord.getId());
			userAgentApplicationRecord.setModifyTime(date);
			result = userAgentApplicationRecordMapper.updateByPrimaryKeySelective(userAgentApplicationRecord);
		}
		if(result > 0){
			agentApplicationRecordResult.setState(1);
		}
		return agentApplicationRecordResult;
	}
	
}
