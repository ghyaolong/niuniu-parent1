package com.mouchina.admin.service.impl.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.permission.PermissionAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server_interface.permission.PermissionService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/26.
 */
@Service
public class PermissionServiceSupport implements PermissionAdminService {
    @Resource
    PermissionService permissionService;

    @Override
    public RoleResult<ListRange<StaticPermission>> selectPagePermission(Map map) {
        return permissionService.selectPagePermission(map);
    }

    @Override
    public RoleResult<StaticPermission> insertPermission(StaticPermission staticPermission) {
        return permissionService.insertPermission(staticPermission);
    }

    @Override
    public RoleResult<Integer> updatePermission(StaticPermission staticPermission) {
        return permissionService.updatePermission(staticPermission);
    }

    @Override
    public RoleResult<Integer> deletePermission(Long id) {
        return permissionService.deletePermission(id);
    }

    @Override
    public RoleResult<StaticPermission> selectPermissionModel(Long id) {
        return permissionService.selectPermissionModel(id);
    }
    @Override
    public RoleResult<List<StaticPermission>> selectPermissionTree(Map map){
        return permissionService.selectPermissionTree(map);
    }
}
