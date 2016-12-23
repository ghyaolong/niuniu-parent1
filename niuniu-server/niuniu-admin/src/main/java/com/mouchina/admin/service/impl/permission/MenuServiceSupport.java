package com.mouchina.admin.service.impl.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.base.common.LoginInfo;
import com.mouchina.admin.service.api.permission.MenuAdminService;
import com.mouchina.base.cache.CacheManager;
import com.mouchina.moumou_server.entity.permission.StaticMenu;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;
import com.mouchina.moumou_server_interface.permission.MenuService;
import com.mouchina.moumou_server_interface.permission.PermissionService;
import com.mouchina.moumou_server_interface.permission.PermissionView;
import com.mouchina.moumou_server_interface.permission.RolePermissionService;
import com.mouchina.moumou_server_interface.permission.RoleService;
import com.mouchina.moumou_server_interface.view.MenuResult;
import com.mouchina.moumou_server_interface.view.RoleResult;

/**
 * Created by douzy on 15/11/19.
 */
@Service
public class MenuServiceSupport implements MenuAdminService {

    @Resource
    MenuService menuService;
    @Resource
    RoleService roleService;
    @Resource
    PermissionService permissionService;
    @Resource
    RolePermissionService rolePermissionService;
    @Resource
    CacheManager cacheManager;

    @Override
    public MenuResult<List<StaticMenu>> getMenuTreeList(Map map) {
        return menuService.getMenuTreeList(map);
    }

    @Override
    public MenuResult<StaticMenu> insertMenu(StaticMenu staticMenu) {
        return menuService.insertMenu(staticMenu);
    }

    @Override
    public MenuResult<Integer> updateMenu(StaticMenu staticMenu) {
        return menuService.updateMenu(staticMenu);
    }

    @Override
    public MenuResult<StaticMenu> selectModel(Map map) {
        return menuService.selectModel(map);
    }

    @Override
    public MenuResult<List<StaticMenu>> getMenuByLoginUser(Map map) {
        MenuResult<List<StaticMenu>> menuResult = new MenuResult<List<StaticMenu>>();

        List<StaticMenu> staticMenus = new ArrayList<StaticMenu>();


        return menuResult;
    }

    @Override
    public MenuResult<List<PermissionView>> selectPermissionMenu() {
        MenuResult<List<PermissionView>> menuResult = new MenuResult<List<PermissionView>>();

        String loginPhone= LoginInfo.getLoginUserPhone();

        Map map = new HashMap();
        map.put("userPhone", loginPhone);
        List<PermissionView> permissionViewsCache=cacheManager.getListByMap(MenuServiceSupport.class, map);

        permissionViewsCache = null;
        if(permissionViewsCache==null) {
            RoleResult<List<StaticUserRole>> roleResult = roleService.selectUserRoleByUserPhone(map);
            if (roleResult.getState() > 0 && roleResult.getContent().size() > 0) {

                List<String> staticRolePermissions = new ArrayList<String>();

                for (StaticUserRole staticUserRole : roleResult.getContent()) {
                    Map rolePermissionMap = new HashMap();
                    rolePermissionMap.put("roleId", staticUserRole.getRoleId());
                    RoleResult<List<StaticRolePermission>> listRoleResult = rolePermissionService.selectPermissionList(rolePermissionMap);
                    if (listRoleResult.getState() > 0 && listRoleResult.getContent().size() > 0) {
                        staticRolePermissions = fullRolePermissionList(staticRolePermissions, listRoleResult.getContent());
                    }
                }
                List<StaticPermission> staticPermissionList = removeDistinct(staticRolePermissions);
                List<PermissionView> permissionViews = getPermissionViews(staticPermissionList);
                
                cacheManager.addListByMap(MenuServiceSupport.class, map, permissionViews);
                menuResult.setContent(permissionViews);
                menuResult.setState(1);
            }
        }else
        {
            menuResult.setContent(permissionViewsCache);
            menuResult.setState(1);
        }

        return menuResult;
    }

    private List<String> fullRolePermissionList(List<String> resultStaticRolePermissions, List<StaticRolePermission> staticRolePermissions) {
        for (StaticRolePermission staticRolePermission : staticRolePermissions) {
            resultStaticRolePermissions.add(staticRolePermission.getPermissionId().toString());
        }
        return resultStaticRolePermissions;
    }

    /**
     * 去除重复权限
     *
     * @param staticRolePermissions
     * @return
     */
    private List<StaticPermission> removeDistinct(List<String> staticRolePermissions) {
        List<StaticPermission> result = new ArrayList<StaticPermission>();
        for (String permissionId : staticRolePermissions) {
            int resu = Collections.frequency(staticRolePermissions, permissionId);
            if (Collections.frequency(staticRolePermissions, permissionId) == 1) {
                RoleResult<StaticPermission> roleResult = permissionService.selectPermissionModel(Long.valueOf(permissionId));
                if (roleResult.getState() > 0) {
                    result.add(roleResult.getContent());
                }
            }
        }
        return result;
    }

    /**
     * 树结构
     *
     * @param staticPermissions
     * @return
     */
    private List<PermissionView> getPermissionViews(List<StaticPermission> staticPermissions) {

        List<PermissionView> permissionViews = new ArrayList<PermissionView>();

        for (StaticPermission staticPermission : staticPermissions) {
            if (staticPermission.getPermissionParent()==0) {//
                PermissionView permissionView = new PermissionView();
                permissionView.setStaticPermission(staticPermission);
                permissionViews.add(permissionView);
            }else {
                getChildrenPermission(permissionViews,staticPermission);
            }
        }
        return permissionViews;
    }

    /**
     * 子树
     * @param permissionViewList
     * @param staticPermission
     * @return
     */
    private List<PermissionView> getChildrenPermission(List<PermissionView> permissionViewList,StaticPermission staticPermission) {
        List<PermissionView> permissionViews=new ArrayList<PermissionView>();
        for(PermissionView permissionView:permissionViewList){
           if(permissionView.getStaticPermission().getId()==staticPermission.getPermissionParent()){
               List<PermissionView> childrPermissionViewsList=permissionView.getStaticPermissionList();

               if(childrPermissionViewsList==null)
                   childrPermissionViewsList=new ArrayList<PermissionView>();
               PermissionView childrPermissionView=new PermissionView();
               childrPermissionView.setStaticPermission(staticPermission);
               childrPermissionViewsList.add(childrPermissionView);
               permissionView.setStaticPermissionList(childrPermissionViewsList);
           }else
           {
               if(permissionView.getStaticPermissionList()!=null){
                   getChildrenPermission(permissionView.getStaticPermissionList(),staticPermission);
               }
           }
        }
        return permissionViews;
    }
}
