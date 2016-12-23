package com.mouchina.moumou_server_interface.permission;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.permission.StaticMenu;
import com.mouchina.moumou_server_interface.view.MenuResult;

/**
 * Created by douzy on 15/11/19.
 */
public interface MenuService {
    /**
     * 获取菜单树结构
     *
     * @return
     */
    MenuResult<List<StaticMenu>> getMenuTreeList(Map map);

    /**
     * 新增菜单
     * @param staticMenu
     * @return
     */
    MenuResult<StaticMenu> insertMenu(StaticMenu staticMenu);

    /**
     * 更新菜单
     * @param staticMenu
     * @return
     */
     MenuResult<Integer> updateMenu(StaticMenu staticMenu);

    /**
     * 查找某个菜单对象
     * @param map
     * @return
     */
    MenuResult<StaticMenu> selectModel(Map map);

}
