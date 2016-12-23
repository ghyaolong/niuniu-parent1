package com.mouchina.moumou_server_dubbo.provider.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.permission.StaticRolePermissionMapper;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server_interface.permission.RolePermissionService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/12/1.
 */
public class RolePermissionServiceSupport implements RolePermissionService {
    @Resource
    StaticRolePermissionMapper staticRolePermissionMapper;

    @Override
    public RoleResult<StaticRolePermission> insertRolePermission(StaticRolePermission staticRolePermission) {
        RoleResult<StaticRolePermission> rolePermissionRoleResult=new RoleResult<>();
        int result=staticRolePermissionMapper.insertSelective(staticRolePermission);
        rolePermissionRoleResult.setContent(staticRolePermission);
        rolePermissionRoleResult.setState(result);
        return rolePermissionRoleResult;
    }

    @Override
    public RoleResult<ListRange<StaticRolePermission>> selectRolePermission(Map map) {
        RoleResult<ListRange<StaticRolePermission>> roleResult = new RoleResult<>();
        ListRange<StaticRolePermission> listRange = new ListRange<>();
        List<StaticRolePermission> staticRolePermissions = staticRolePermissionMapper.selectPageList(map);
        Page page = (Page) map.get("Page");
        page.setCount(staticRolePermissionMapper.selectCount(map));
        listRange.setData(staticRolePermissions);
        listRange.setPage(page);

        roleResult.setContent(listRange);
        roleResult.setState(1);
        return roleResult;
    }

    @Override
    public RoleResult<Integer> deleteRolePermissionList(Long roleId) {
        RoleResult<Integer> roleResult = new RoleResult<>();
        int result = staticRolePermissionMapper.deleteByRoleId(roleId);
        roleResult.setContent(result);
        roleResult.setState(result);
        return roleResult;
    }
    @Override
    public RoleResult<List<StaticRolePermission>> selectPermissionList(Map map)
    {
        RoleResult<List<StaticRolePermission>> roleResult=new RoleResult<>();
        List<StaticRolePermission> staticRolePermissions=staticRolePermissionMapper.selectList(map);
        roleResult.setContent(staticRolePermissions);
        roleResult.setState(1);
        return roleResult;
    }
}
