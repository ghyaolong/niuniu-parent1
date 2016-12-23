package com.mouchina.admin.service.api.permission;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.permission.StaticMenu;
import com.mouchina.moumou_server_interface.permission.PermissionView;
import com.mouchina.moumou_server_interface.view.MenuResult;

/**
 * Created by douzy on 15/11/19.
 */
public interface MenuAdminService {
    public MenuResult<List<StaticMenu>> getMenuTreeList(Map map);
    public MenuResult<StaticMenu> insertMenu(StaticMenu staticMenu);
    public MenuResult<Integer> updateMenu(StaticMenu staticMenu);
    MenuResult<StaticMenu> selectModel(Map map);
    MenuResult<List<StaticMenu>> getMenuByLoginUser(Map map);

    MenuResult<List<PermissionView>> selectPermissionMenu();
}
