package com.mouchina.moumou_server_interface.member;

import com.mouchina.moumou_server.entity.member.UserAgentApplicationRecord;
import com.mouchina.moumou_server_interface.view.Result;

public interface UserAgentApplicationRecordService {
	
	public Result<UserAgentApplicationRecord> saveOrUpdateAgentApplicationRecord(UserAgentApplicationRecord userAgentApplicationRecord);

}
