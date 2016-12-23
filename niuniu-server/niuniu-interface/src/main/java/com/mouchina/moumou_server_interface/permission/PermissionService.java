package com.mouchina.moumou_server_interface.permission;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/26.
 */
public interface PermissionService {
    /**
     * 查询权限
     * @param map
     * @return
     */
    RoleResult<ListRange<StaticPermission>> selectPagePermission(Map map);

    /**
     * 新增权限
     * @param staticPermission
     * @return
     */
    RoleResult<StaticPermission> insertPermission(StaticPermission staticPermission);

    /**
     * 更新权限
     * @param staticPermission
     * @return
     */
    RoleResult<Integer> updatePermission(StaticPermission staticPermission);

    /**
     * 删除权限
     * @param id
     * @return
     */
    RoleResult<Integer> deletePermission(Long id);

    /**
     *  根据主键查询权限
     * @param id
     * @return
     */
    RoleResult<StaticPermission> selectPermissionModel(Long id);

    /**
     * 查询权限树
     * @param map
     * @return
     */
    RoleResult<List<StaticPermission>> selectPermissionTree(Map map);
}
