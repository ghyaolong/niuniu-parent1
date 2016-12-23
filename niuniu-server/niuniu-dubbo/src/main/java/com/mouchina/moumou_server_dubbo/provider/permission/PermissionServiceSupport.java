package com.mouchina.moumou_server_dubbo.provider.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.permission.StaticPermissionMapper;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server_interface.permission.PermissionService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/26.
 */
public class PermissionServiceSupport implements PermissionService {
    @Resource
    StaticPermissionMapper staticPermissionMapper;

    @Override
    public RoleResult<ListRange<StaticPermission>> selectPagePermission(Map map) {
        RoleResult<ListRange<StaticPermission>> roleResult = new RoleResult<>();
        ListRange<StaticPermission> listRangePermission = new ListRange<>();
        List<StaticPermission> staticPermissions = staticPermissionMapper.selectPageList(map);
        Page page = (Page) map.get("page");
        page.setCount(staticPermissionMapper.selectCount(map));
        listRangePermission.setData(staticPermissions);
        listRangePermission.setPage(page);
        roleResult.setContent(listRangePermission);
        roleResult.setState(1);
        return roleResult;
    }

    @Override
    public RoleResult<StaticPermission> insertPermission(StaticPermission staticPermission) {
        RoleResult<StaticPermission> staticPermissionRoleResult=new RoleResult<>();
        int result=staticPermissionMapper.insertSelective(staticPermission);
        staticPermissionRoleResult.setState(result);
        staticPermissionRoleResult.setContent(staticPermission);
        return staticPermissionRoleResult;
    }

    @Override
    public RoleResult<Integer> updatePermission(StaticPermission staticPermission) {
        RoleResult<Integer> roleResult = new RoleResult<>();

        int result = staticPermissionMapper.updateByPrimaryKeySelective(staticPermission);

        roleResult.setState(result);
        roleResult.setContent(result);
        return roleResult;
    }

    @Override
    public RoleResult<Integer> deletePermission(Long id) {
        RoleResult<Integer> roleResult = new RoleResult<>();

        int result = staticPermissionMapper.deleteByPrimaryKey(id);
        roleResult.setState(result);
        roleResult.setContent(result);
        return roleResult;
    }

    @Override
    public RoleResult<StaticPermission> selectPermissionModel(Long id) {
        RoleResult<StaticPermission> staticPermissionRoleResult = new RoleResult<>();

        StaticPermission staticPermission = staticPermissionMapper.selectByPrimaryKey(id);

        staticPermissionRoleResult.setContent(staticPermission);
        staticPermissionRoleResult.setState(1);
        return staticPermissionRoleResult;
    }

    @Override
    public RoleResult<List<StaticPermission>> selectPermissionTree(Map map) {
        RoleResult<List<StaticPermission>> roleResult = new RoleResult<>();
        List<StaticPermission> staticPermissionList = staticPermissionMapper.selectList(map);
        roleResult.setContent(staticPermissionList);
        roleResult.setState(1);

        return roleResult;
    }
}
