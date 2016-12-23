package com.mouchina.admin.service.impl.system;

import com.mouchina.admin.service.api.system.CommonAdminService;

import com.mouchina.moumou_server.entity.message.SmsTemplate;

import com.mouchina.moumou_server_interface.message.SmsTemplateService;
import com.mouchina.moumou_server_interface.view.LocationResult;
import com.mouchina.moumou_server_interface.view.MessageResult;
import com.mouchina.moumou_server_interface.view.SkillResult;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class CommonAdminServiceSupport
    implements CommonAdminService
{
    @Resource
    private SmsTemplateService smsTemplateService;


    @Override
    public SmsTemplate selectSmsTemplate( String templateKey )
    {
        // TODO Auto-generated method stub
        MessageResult<SmsTemplate> messageResult = smsTemplateService.selectSmsTemplate( templateKey );
        SmsTemplate smsTemplate = null;

        if ( messageResult.getState(  ) == 1 )
        {
            smsTemplate = messageResult.getContent(  );
        }

        return smsTemplate;
    }

    @Override
    public SmsTemplate addSmsTemplate( SmsTemplate smsTemplate )
    {
        // TODO Auto-generated method stub
        return smsTemplateService.addSmsTemplate( smsTemplate ).getContent(  );
    }

    @Override
    public Integer deleteSmsTemplate( String templateKey )
    {
        // TODO Auto-generated method stub
        return smsTemplateService.deleteSmsTemplate( templateKey ).getState(  );
    }

    @Override
    public Integer updateSmsTemplate( SmsTemplate smsTemplate )
    {
        // TODO Auto-generated method stub
        return smsTemplateService.updateSmsTemplate( smsTemplate ).getState(  );
    }

    @Override
    public List<SmsTemplate> selectListSmsTemplate( Map map )
    {
        // TODO Auto-generated method stub
        return smsTemplateService.selectListSmsTemplate( map ).getContent(  );
    }


}
