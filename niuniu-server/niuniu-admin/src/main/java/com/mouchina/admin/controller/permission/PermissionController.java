package com.mouchina.admin.controller.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.service.api.permission.PermissionAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/26.
 */
@Controller
@RequestMapping(value = "/permission")
public class PermissionController {
    @Resource
    PermissionAdminService permissionAdminService;

    @RequestMapping(value ="/index",method = RequestMethod.GET)
    public String permissionIndex() {
        return "/permission/list";
    }
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String roleAdd(HttpServletRequest _req,ModelMap modelMap){
        return "/permission/add";
    }
    @RequestMapping(value = "/edit/{code}",method = RequestMethod.GET)
    public String roleEdit(@PathVariable Long code,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<StaticPermission> roleResult = permissionAdminService.selectPermissionModel(code);
        modelMap.put("permission",roleResult.getContent());
        return "/permission/edit";
    }
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String roleAddPost(StaticPermission staticPermission,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<StaticPermission> roleRoleResult= permissionAdminService.insertPermission(staticPermission);
//        modelMap.put("result",roleRoleResult.getState());
        return "redirect:/permission/index.html";
    }
    @RequestMapping(value = "/remove/{code}",method = RequestMethod.POST)
    public String roleRemove(@PathVariable Long code,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<Integer> roleResult = permissionAdminService.deletePermission(code);
        modelMap.put("result",roleResult.getState());
        return "";
    }
    @RequestMapping(value = "/modifly",method = RequestMethod.POST)
    public String roleModifly(StaticPermission staticPermission,HttpServletRequest _req,ModelMap modelMap) {
        RoleResult<Integer> roleResult = permissionAdminService.updatePermission(staticPermission);
        modelMap.put("result",roleResult.getState());
        return "";
    }
    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    public String permissionTree(HttpServletRequest _req,ModelMap modelMap){
        RoleResult<List<StaticPermission>> roleResult= permissionAdminService.selectPermissionTree(null);
        modelMap.put("permissionTree",roleResult.getContent());
        return "";
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String permissionList(HttpServletRequest _req,ModelMap modelMap){
        String pg = _req.getParameter("p");

        Integer Ipg = 0;
        if (!StringUtils.isBlank(pg)) {
            Ipg = Integer.valueOf(pg);
        }
        Page page = new Page(0, 10);
        page.setBegin(Ipg);

        Map map = new HashMap();
        map.put("page", page);
        RoleResult<ListRange<StaticPermission>> roleResult = permissionAdminService.selectPagePermission(map);
        modelMap.put("result", roleResult);

        return "";
    }
}
