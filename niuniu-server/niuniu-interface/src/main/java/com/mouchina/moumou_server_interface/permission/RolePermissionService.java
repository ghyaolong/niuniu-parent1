package com.mouchina.moumou_server_interface.permission;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/12/1.
 */
public interface RolePermissionService {
    /**
     * 新增角色 权限关系
     * @param staticRolePermission
     * @return
     */
    RoleResult<StaticRolePermission> insertRolePermission(StaticRolePermission staticRolePermission);

    /**
     * 查询角色权限关系
     * @param map
     * @return
     */
    RoleResult<ListRange<StaticRolePermission>> selectRolePermission(Map map);

    /**
     * 批量删除角色权限关系  根据 角色ID
     * @param roleId
     * @return
     */
    RoleResult<Integer> deleteRolePermissionList(Long roleId);

    /**
     * 查询角色拥有的权限
     * @param map
     * @return
     */
    RoleResult<List<StaticRolePermission>> selectPermissionList(Map map);
}
