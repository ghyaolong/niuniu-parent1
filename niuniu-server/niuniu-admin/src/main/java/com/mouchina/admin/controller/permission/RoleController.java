package com.mouchina.admin.controller.permission;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.service.api.permission.RoleAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/21.
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    RoleAdminService roleAdminService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String roleIndex(HttpServletRequest _req, ModelMap modelMap) {
        return "/permission/role/list";
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String roleAdd(HttpServletRequest _req,ModelMap modelMap){
        return "/permission/role/add";
    }
    @RequestMapping(value = "/edit/{code}",method = RequestMethod.GET)
    public String roleEdit(@PathVariable Long code,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<StaticRole> roleResult = roleAdminService.selectRole(code);
        modelMap.put("role",roleResult.getContent());
        return "/permission/role/edit";
    }
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String roleAddPost(StaticRole staticRole,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<StaticRole> roleRoleResult= roleAdminService.insertRole(staticRole);
//        modelMap.put("result",roleRoleResult.getState());
        return "redirect:/role/index.html";
    }
    @RequestMapping(value = "/remove/{code}",method = RequestMethod.POST)
    public String roleRemove(@PathVariable Long code,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<Integer> roleResult = roleAdminService.deleteRole(code);
        modelMap.put("result",roleResult.getState());
        return "";
    }
    @RequestMapping(value = "/modifly",method = RequestMethod.POST)
    public String roleModifly(StaticRole staticRole,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<Integer> roleResult = roleAdminService.updateRole(staticRole);
        return "redirect:/role/index.html";
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String roleList(HttpServletRequest _req, ModelMap modelMap) {
        String pg = _req.getParameter("p");

        Integer Ipg = 0;
        if (!StringUtils.isBlank(pg)) {
            Ipg = Integer.valueOf(pg);
        }
        Page page = new Page(0, 10);
        page.setBegin(Ipg);

        Map map = new HashMap();
        map.put("page", page);
        RoleResult<ListRange<StaticRole>> roleResult = roleAdminService.selectListRangePager(map);
        modelMap.put("result", roleResult);

        return "";
    }
    @RequestMapping(value = "/save/userRole",method = RequestMethod.POST)
    public String saveUserRole(HttpServletRequest _req,ModelMap modelMap){
         String userPhone=_req.getParameter("userPhone");
         String roleArr=_req.getParameter("roles");
         String uPhone="",roles="";
         if(!StringUtils.isBlank(userPhone))
             uPhone=userPhone;
         if(!StringUtils.isBlank(roleArr))
             roles=roleArr;

        RoleResult<Integer> roleResult= roleAdminService.distributionRole(uPhone, roles);
        modelMap.put("result",roleResult.getContent());
        return "";
    }
}
