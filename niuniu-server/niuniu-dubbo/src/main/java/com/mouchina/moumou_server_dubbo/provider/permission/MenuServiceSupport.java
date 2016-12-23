package com.mouchina.moumou_server_dubbo.provider.permission;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.permission.StaticMenuMapper;
import com.mouchina.moumou_server.entity.permission.StaticMenu;
import com.mouchina.moumou_server_interface.permission.MenuService;
import com.mouchina.moumou_server_interface.view.MenuResult;

/**
 * Created by douzy on 15/11/19.
 */
public class MenuServiceSupport implements MenuService {
    @Resource
    StaticMenuMapper staticMenuMapper;

    @Override
    public MenuResult<List<StaticMenu>> getMenuTreeList(Map map) {
        MenuResult<List<StaticMenu>> menuResult = new MenuResult<>();
        List<StaticMenu> staticMenus = staticMenuMapper.selectList(map);
        menuResult.setState(1);
        menuResult.setContent(staticMenus);
        return menuResult;
    }
    @Override
    public MenuResult<StaticMenu> insertMenu(StaticMenu staticMenu) {
        MenuResult<StaticMenu> menuResult = new MenuResult<>();
        int result = staticMenuMapper.insertSelective(staticMenu);
        menuResult.setState(result);
        menuResult.setContent(staticMenu);
        return menuResult;
    }
    @Override
    public MenuResult<Integer> updateMenu(StaticMenu staticMenu) {
        MenuResult<Integer> menuResult = new MenuResult<>();
        int result = staticMenuMapper.updateByPrimaryKeySelective(staticMenu);
        menuResult.setState(result);
        return menuResult;
    }
    @Override
    public MenuResult<StaticMenu> selectModel(Map map) {
        MenuResult<StaticMenu> menuMenuResult=new MenuResult<>();
        StaticMenu staticMenu=staticMenuMapper.selectModel(map);
        menuMenuResult.setContent(staticMenu);
        menuMenuResult.setState((staticMenu==null?0:1));
        return menuMenuResult;
    }

}
