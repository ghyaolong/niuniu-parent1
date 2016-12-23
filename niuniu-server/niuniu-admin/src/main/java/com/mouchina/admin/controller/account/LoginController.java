package com.mouchina.admin.controller.account;

import com.mouchina.admin.service.api.member.UserAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by douzy on 16/2/19.
 */
@Controller
@RequestMapping(value = "/account")
public class LoginController {
    @Resource
    UserAdminService userAdminService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest _req,ModelMap modelMap) {
        String msg=_req.getParameter("msg");
        modelMap.put("result",msg);
        return "account/login";
    }
}
