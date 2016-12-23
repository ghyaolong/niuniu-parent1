package com.mouchina.admin.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.member.UserCashFlowService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;

@Controller
@RequestMapping( "/members/user" )
public class UserController
{
    @Resource
    private UserAdminService userAdminService;
    @Resource
    private UserCashFlowService userCashFlowService;

    @RequestMapping( value = "/new", method = RequestMethod.GET )
    private String userNew( ModelMap modelMap )
    {
        return "members/user/new";
    }

    @RequestMapping( value = "/check/phone/{phone}" )
    private String userCheckPhone( @PathVariable
    String phone, ModelMap modelMap )
    {
        int result = userAdminService.checkUser( phone );
        modelMap.put( "result", result );

        return "";
    }

    @RequestMapping( value = "/delete/{phone}", method = RequestMethod.POST )
    private String userDelte( @PathVariable
    String phone, ModelMap modelMap )
    {
        return "";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST )
    private String userAdd( @ModelAttribute
    User user, ModelMap modelMap )
    {
        userAdminService.addUser( user );

        return "redirect:show/" + user.getPhone(  ) + ".html";
    }

    @RequestMapping( value = "/update", method = RequestMethod.POST )
    private String userUpdate( User user, ModelMap modelMap )
    {
        userAdminService.updateUser( user );

        return "redirect:show/" + user.getPhone(  ) + ".html";
    }

    @RequestMapping( value = "/show/{phone}", method = RequestMethod.GET )
    private String showUser( @PathVariable
    String phone, ModelMap modelMap )
    {
        modelMap.put( "userInfo",
                      userAdminService.user( phone ) );

        Map map=new HashMap();
        map.put("userPhone",phone);
        return "members/user/show";
    }
    @RequestMapping( value = "/show/cashflow", method = RequestMethod.GET )
    private String showCashFlow(HttpServletRequest _req,ModelMap modelMap) {

        Page page = new Page(0, 10);
        int begin = 0;
        if (_req.getParameter("begin") != null) {
            begin = Integer.valueOf(_req.getParameter("begin").toString()).intValue();
        }
        String userId= StringUtils.isBlank(_req.getParameter("userId"))?null:_req.getParameter("userId");
        String year=StringUtils.isBlank(_req.getParameter("year"))?null:_req.getParameter("year");
        page.setBegin(begin);
        Map map = new HashMap();
        map.put("page", page);
        map.put("userId",userId);
        map.put("year",year);
        ListRange<UserCashFlow> userCashFlowListRange = userCashFlowService.getUserCashFlowByPage(map);
        modelMap.put("userCashFlows", userCashFlowListRange);
        return "";
    }
    @RequestMapping( value = "/list/{tableNum}", method = RequestMethod.GET )
    private String userList( @PathVariable
    String tableNum, ModelMap modelMap ) {
        modelMap.put("userInfo", userAdminService);

        return "members/user/show";
    }

    @RequestMapping( value = "/edit/{phone}", method = RequestMethod.GET )
    private String userEdit( @PathVariable
    String phone, ModelMap modelMap )
    {
        modelMap.put( "userInfo",
                      userAdminService.user( phone ) );

        return "members/user/edit";
    }

    @RequestMapping( value = "/query", method = RequestMethod.GET )
    private String userQuery( ModelMap modelMap )
    {
        return "members/user/query";
    }


}
