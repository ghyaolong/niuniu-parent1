package com.mouchina.admin.service.impl.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.permission.RolePermissionAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server_interface.permission.RolePermissionService;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/12/1.
 */
@Service
public class RolePermissionServiceSupport implements RolePermissionAdminService {
    @Resource
    RolePermissionService rolePermissionService;
    @Resource
    RoleService roleService;

    @Override
    public RoleResult<StaticRolePermission> insertRolePermission(Long rId,String permission) {
        RoleResult<StaticRolePermission> rolePermissionRoleResult = new RoleResult<StaticRolePermission>();

        rolePermissionRoleResult.setState(0);
        rolePermissionRoleResult.setContent(null);

        Map map=new HashMap();
        map.put("roleId",rId);

        RoleResult<List<StaticRolePermission>> roleResultList=selectPermissionList(map);
        boolean isFlag=false;
        if(roleResultList.getContent().size()>0) {
            RoleResult<Integer> roleResult = deleteRolePermissionList(rId);
            isFlag = roleResult.getState() > 0;
        }else
            isFlag=true;
        if (isFlag) {
            String[] perAdd=permission.split(",");
            for(String permis:perAdd) {
                StaticRolePermission staticRolePermission=new StaticRolePermission();
                staticRolePermission.setPermissionId(Long.valueOf(permis));
                staticRolePermission.setRoleId(rId);
                staticRolePermission.setRolePermissionNote("角色权限分配");
                rolePermissionRoleResult = rolePermissionService.insertRolePermission(staticRolePermission);
            }
        }

        return rolePermissionRoleResult;
    }

    @Override
    public RoleResult<ListRange<StaticRolePermission>> selectRolePermission(Map map) {
        return rolePermissionService.selectRolePermission(map);
    }

    @Override
    public RoleResult<Integer> deleteRolePermissionList(Long roleId) {
        return rolePermissionService.deleteRolePermissionList(roleId);
    }

    @Override
    public RoleResult<List<StaticRolePermission>> selectPermissionList(Map map){
        return rolePermissionService.selectPermissionList(map);
    }

    @Override
    public RoleResult<List<StaticRole>> selectRoleByPermission(Map map) {
        RoleResult<List<StaticRole>> staticRoles = new RoleResult<List<StaticRole>>();

        RoleResult<List<StaticRolePermission>> roleResult = rolePermissionService.selectPermissionList(map);

        if (roleResult.getState() > 0) {
            List<StaticRolePermission> staticRolePermissions = roleResult.getContent();
            List<StaticRole> staticRoleList = new ArrayList<StaticRole>();

            for (StaticRolePermission staticRolePermission : staticRolePermissions) {
                Long roleId = staticRolePermission.getRoleId();
                RoleResult<StaticRole> roleRoleResult = roleService.selectRole(roleId);
                staticRoleList.add(roleRoleResult.getContent());
            }
            staticRoles.setContent(staticRoleList);
            staticRoles.setState(1);
        }

        return staticRoles;
    }
}
