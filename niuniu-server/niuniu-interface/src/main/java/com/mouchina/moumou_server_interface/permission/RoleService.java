package com.mouchina.moumou_server_interface.permission;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/21.
 */
public interface RoleService {
    RoleResult<ListRange<StaticRole>> selectListRangePager(Map map);
    RoleResult<StaticRole> insertRole(StaticRole staticRole);
    RoleResult<Integer> deleteRole(Long code);
    RoleResult<StaticRole> selectRole(Long code);
    RoleResult<Integer> updateRole(StaticRole staticRole);
    RoleResult<List<StaticRole>> selectAllRole();

    /**
     * 权限分配
     */
    RoleResult<StaticUserRole> insertUserRole(StaticUserRole staticUserRole);

    /**
     * 删除某个用户下所有已分配的角色
     * @param userId
     * @return
     */
    RoleResult<Integer> deleteUserRole(Long userId);

    /**
     * 获取某用户下所有角色
     * @param map
     * @return
     */
    RoleResult<List<StaticUserRole>> selectUserRoleByUserPhone(Map map);
    
    // 查询用户角色
    StaticUserRole selectStaticUserRoleByMap(Map map);
    
}
