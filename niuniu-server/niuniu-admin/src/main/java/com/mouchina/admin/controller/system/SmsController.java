package com.mouchina.admin.controller.system;

import com.mouchina.admin.service.api.system.CommonAdminService;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.message.SmsTemplate;
import com.mouchina.moumou_server_interface.message.SmsTemplateService;
import com.mouchina.moumou_server_interface.view.MessageResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( "/system/sms" )
public class SmsController
{
    @Resource
    private CommonAdminService commonAdminService;
    @Resource
    private SmsTemplateService smsTemplateService;

    @RequestMapping( value = "/template/new", method = RequestMethod.GET )
    private String smsTemplateNew( ModelMap modelMap )
    {
        return "system/sms/template/new";
    }

    @RequestMapping( value = "/template/add", method = RequestMethod.POST )
    private String smsTemplateAdd(SmsTemplate smsTemplate, ModelMap modelMap ) {

        MessageResult<SmsTemplate> smsTemplateMessageResult= smsTemplateService.addSmsTemplate(smsTemplate);
        modelMap.put("result",smsTemplateMessageResult.getState());
        return "redirect:list.html";
    }

    @RequestMapping( value = "/template/show", method = RequestMethod.GET )
    private String smsTemplateShow( ModelMap modelMap )
    {
        return "system/sms/template/show";
    }

    @RequestMapping( value = "/template/list", method = RequestMethod.GET )
    private String smsTemplateList( ModelMap modelMap )
    {
        return "system/sms/template/list";
    }

    @RequestMapping( value = "/template/list/query", method = RequestMethod.GET )
    private String smsTemplateQuery( HttpServletRequest request, ModelMap modelMap )
    {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null )
        {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }

        page.setBegin( begin );
        map.put( "page", page );

        List<SmsTemplate> smsTemplates = commonAdminService.selectListSmsTemplate( map );
        modelMap.put( "smsTemplates", smsTemplates );

        return "";
    }
}
