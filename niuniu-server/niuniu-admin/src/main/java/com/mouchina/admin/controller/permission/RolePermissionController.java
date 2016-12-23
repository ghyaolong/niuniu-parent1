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
import com.mouchina.admin.service.api.permission.RolePermissionAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/12/1.
 */
@Controller
@RequestMapping(value = "/rolePermission")
public class RolePermissionController {
    @Resource
    RolePermissionAdminService rolePermissionAdminService;

    /**
     * 查询角色权限关系
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String selectRolePermission(HttpServletRequest _req, ModelMap modelMap) {
        String pg = _req.getParameter("p");

        Integer Ipg = 0;
        if (!StringUtils.isBlank(pg)) {
            Ipg = Integer.valueOf(pg);
        }
        Page page = new Page(0, 10);
        page.setBegin(Ipg);

        Map map = new HashMap();
        map.put("page", page);
        RoleResult<ListRange<StaticRolePermission>> roleResult = rolePermissionAdminService.selectRolePermission(map);
        modelMap.put("result", roleResult);

        return "";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addRolePermission(HttpServletRequest _req, ModelMap modelMap) {
        String roleId = _req.getParameter("roleId");
        String permission = _req.getParameter("permission");
        Long rId = 0L;
        if (!StringUtils.isBlank(roleId))
            rId = Long.valueOf(roleId);

        RoleResult<StaticRolePermission> rolePermissionRoleResult = rolePermissionAdminService.insertRolePermission(rId, permission);
        modelMap.put("result", rolePermissionRoleResult.getContent());
        return "";
    }

    /**
     * 查询角色所拥有的权限
     *
     * @param roleId
     * @param _req
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/getPermission/{roleId}", method = RequestMethod.GET)
    public String selecrPermissionByRoleId(@PathVariable Long roleId, HttpServletRequest _req, ModelMap modelMap) {
        Map map = new HashMap();
        map.put("roleId", roleId);
        RoleResult<List<StaticRolePermission>> roleResult = rolePermissionAdminService.selectPermissionList(map);
        modelMap.put("result", roleResult.getContent());
        return "";
    }

    @RequestMapping(value = "/getRole/{permissionId}", method = RequestMethod.GET)
    public String selectRoleByPermission(@PathVariable Long permissionId, HttpServletRequest _req, ModelMap modelMap) {
        Map map = new HashMap();
        map.put("permissionId", permissionId);
        RoleResult<List<StaticRole>> roleResult = rolePermissionAdminService.selectRoleByPermission(map);
        modelMap.put("result", roleResult.getContent());
        return "";
    }
}
