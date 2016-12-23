package com.mouchina.admin.service.impl.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.permission.RoleAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.permission.StaticRole;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/21.
 */
@Service
public class RoleServiceSupport implements RoleAdminService {
    @Resource
    RoleService roleService;
    @Resource
    UserVerifyService userVerifyService;


    @Override
    public RoleResult<ListRange<StaticRole>> selectListRangePager(Map map) {
        return roleService.selectListRangePager(map);
    }

    @Override
    public RoleResult<StaticRole> insertRole(StaticRole staticRole) {
        return roleService.insertRole(staticRole);
    }

    @Override
    public RoleResult<Integer> deleteRole(Long code) {
        return roleService.deleteRole(code);
    }

    @Override
    public RoleResult<StaticRole> selectRole(Long code) {
        return roleService.selectRole(code);
    }

    @Override
    public RoleResult<Integer> updateRole(StaticRole staticRole) {
        return roleService.updateRole(staticRole);
    }

    @Override
    public RoleResult<List<StaticRole>> selectAllRole() {
        return roleService.selectAllRole();
    }

    @Override
    public RoleResult<StaticUserRole> insertUserRole(StaticUserRole staticUserRole) {
        return roleService.insertUserRole(staticUserRole);
    }

    /**
     * 角色分配
     * @param userPhone
     * @param roles
     * @return
     */
    @Override
    public RoleResult<Integer>  distributionRole(String userPhone,String roles) {
        RoleResult<Integer> roleResult = new RoleResult<Integer>();
        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setPhone(userPhone);

        User user = userVerifyService.selectUser(userIdentity);
        //用户不存在
        if (user == null) {
            roleResult.setState(1);
            return roleResult;
        }

        RoleResult<Integer> delRoleResult = deleteUserRole(user.getId());

        int errorNum = 0;

//        if (delRoleResult.getState() > 0) {

            String[] roleArr = roles.split(",");

            for (String roleId : roleArr) {

                RoleResult<StaticRole> roleRoleResult = roleService.selectRole(Long.valueOf(roleId));
                StaticRole staticRole = roleRoleResult.getContent();

                StaticUserRole staticUserRole = new StaticUserRole();

                staticUserRole.setRoleId(staticRole.getRoleId());
                staticUserRole.setUserId(user.getId());
                staticUserRole.setUserPhone(user.getPhone());
                staticUserRole.setUserRoleNote(staticRole.getRoleName());

                RoleResult<StaticUserRole> roleUserRoleResult = insertUserRole(staticUserRole);

                if (roleUserRoleResult.getState() <= 0) {
                    errorNum++;
                }
            }

            if (errorNum == 0) {         //分配角色成功
                roleResult.setState(0);
                roleResult.setContent(0);
            } else {
                roleResult.setState(3);//成功  但是有未成功插入的权限,   失败数 在content体现
                roleResult.setContent(errorNum);
            }

//        } else {
//            roleResult.setState(2);//角色 删除失败
//        }
        return roleResult;
    }
    @Override
    public RoleResult<Integer> deleteUserRole(Long userId) {
        return roleService.deleteUserRole(userId);
    }
    @Override
    public RoleResult<List<StaticUserRole>> selectUserRoleByUserPhone(Map map) {
        return roleService.selectUserRoleByUserPhone(map);
    }
}
