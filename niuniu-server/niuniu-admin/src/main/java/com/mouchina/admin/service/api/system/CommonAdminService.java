package com.mouchina.admin.service.api.system;

import com.mouchina.moumou_server.entity.message.SmsTemplate;

import java.util.List;
import java.util.Map;

public interface CommonAdminService
{

    SmsTemplate selectSmsTemplate(String templateKey);

    SmsTemplate addSmsTemplate(SmsTemplate smsTemplate);

    Integer deleteSmsTemplate(String templateKey);

    Integer updateSmsTemplate(SmsTemplate smsTemplate);

    List<SmsTemplate> selectListSmsTemplate(Map map);


}
