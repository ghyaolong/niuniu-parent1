package com.mouchina.moumou_server_dubbo.provider.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.permission.StaticRoleMapper;
import com.mouchina.moumou_server.dao.permission.StaticUserRoleMapper;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/21.
 */
public class RoleServiceSupport implements RoleService {
    @Resource
    StaticRoleMapper staticRoleMapper;
    @Resource
    StaticUserRoleMapper staticUserRoleMapper;
    @Override
    public RoleResult<ListRange<StaticRole>> selectListRangePager(Map map) {
        RoleResult<ListRange<StaticRole>> roleResult = new RoleResult<>();
        ListRange<StaticRole> listRange = new ListRange<>();
        listRange.setData(staticRoleMapper.selectPageList(map));
        Page page = (Page) map.get("page");
        page.setCount(staticRoleMapper.selectCount(map));
        listRange.setPage(page);
        roleResult.setState(1);
        roleResult.setContent(listRange);
        return roleResult;
    }
    @Override
    public RoleResult<StaticRole> insertRole(StaticRole staticRole) {
        RoleResult<StaticRole> roleRoleResult = new RoleResult<>();
        int Result = staticRoleMapper.insertSelective(staticRole);
        roleRoleResult.setState(Result);
        roleRoleResult.setContent(staticRole);
        return roleRoleResult;
    }
    @Override
    public RoleResult<Integer> deleteRole(Long code) {
        RoleResult<Integer> roleResult = new RoleResult<>();

        int result = staticRoleMapper.deleteByPrimaryKey(code);
        roleResult.setState(result);
        roleResult.setContent(result);
        return roleResult;
    }
    @Override
    public RoleResult<StaticRole> selectRole(Long code) {
        RoleResult<StaticRole> roleRoleResult = new RoleResult<>();

        StaticRole staticRole = staticRoleMapper.selectByPrimaryKey(code);

        roleRoleResult.setState(1);

        roleRoleResult.setContent(staticRole);

        return roleRoleResult;
    }
    @Override
    public RoleResult<Integer> updateRole(StaticRole staticRole) {
        RoleResult<Integer> roleResult = new RoleResult<>();

        int result = staticRoleMapper.updateByPrimaryKeySelective(staticRole);

        roleResult.setState(result);

        roleResult.setContent(result);

        return roleResult;
    }

    @Override
    public RoleResult<List<StaticRole>> selectAllRole() {
        RoleResult<List<StaticRole>> roleResult = new RoleResult<>();
        List<StaticRole> staticRoles = staticRoleMapper.selectList(null);
        roleResult.setState(1);
        roleResult.setContent(staticRoles);
        return roleResult;
    }

    @Override
    public RoleResult<StaticUserRole> insertUserRole(StaticUserRole staticUserRole) {
        RoleResult<StaticUserRole> roleRoleResult = new RoleResult<>();
        int Result = staticUserRoleMapper.insertSelective(staticUserRole);
        roleRoleResult.setState(Result);
        roleRoleResult.setContent(staticUserRole);

        return roleRoleResult;
    }
    @Override
    public RoleResult<Integer> deleteUserRole(Long userId) {
        RoleResult<Integer> roleResult = new RoleResult<>();
        int Result = staticUserRoleMapper.deleteByUserId(userId);
        roleResult.setState(Result);
        roleResult.setContent(Result);
        return roleResult;
    }

    @Override
    public RoleResult<List<StaticUserRole>> selectUserRoleByUserPhone(Map map) {
        RoleResult<List<StaticUserRole>> roleResult = new RoleResult<>();
        List<StaticUserRole> staticUserRoles = staticUserRoleMapper.selectList(map);
        roleResult.setState(1);
        roleResult.setContent(staticUserRoles);
        return roleResult;
    }
	@Override
	public StaticUserRole selectStaticUserRoleByMap(Map map) {
		return staticUserRoleMapper.selectModel(map);
	}
}
