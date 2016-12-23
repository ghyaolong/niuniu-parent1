package com.mouchina.admin.controller.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.permission.MenuAdminService;
import com.mouchina.moumou_server.entity.permission.StaticMenu;
import com.mouchina.moumou_server_interface.permission.PermissionView;
import com.mouchina.moumou_server_interface.view.MenuResult;

/**
 * Created by douzy on 15/11/19.
 */
@Controller
@RequestMapping( "/menu" )
public class MenuController {

    @Resource
    MenuAdminService menuAdminService;

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String menuIndex() {
        return "/permission/menu/tree";
    }

    @RequestMapping(value = "/get_menu_tree_list",method = RequestMethod.GET)
    public String getMenuTreeList(HttpServletRequest _req,ModelMap modelMap) {
        MenuResult<List<StaticMenu>> staticMenuMenuResult= menuAdminService.getMenuTreeList(null);
        modelMap.put("menuList",staticMenuMenuResult.getContent());
        return "";
    }

    @RequestMapping(value = "/addMenu",method = RequestMethod.POST)
    public String addMenu( StaticMenu staticMenu,HttpServletRequest _req,ModelMap modelMap) {
        MenuResult<StaticMenu> staticMenuMenuResult= menuAdminService.insertMenu(staticMenu);
        modelMap.put("result",staticMenuMenuResult.getState());
        return "";
    }
    @RequestMapping(value = "/updMenu",method = RequestMethod.POST)
    public String updMenu( StaticMenu staticMenu,HttpServletRequest _req,ModelMap modelMap) {
        MenuResult<Integer> staticMenuMenuResult = menuAdminService.updateMenu(staticMenu);
        modelMap.put("result", staticMenuMenuResult.getState());
        return "";
    }
    @RequestMapping(value = "/sel/{id}",method = RequestMethod.GET)
    public String selectMenu(@PathVariable Integer id,HttpServletRequest _req,ModelMap modelMap){
        Map menuMap=new HashMap();
        menuMap.put("menuCode",id);
        MenuResult<StaticMenu> menuMenuResult=menuAdminService.selectModel(menuMap);
        modelMap.put("result",menuMenuResult.getContent());
        return "";
    }

    @RequestMapping(value = "/getLeftMenu",method = RequestMethod.GET)
    public String selectLeftMenu(HttpServletRequest _req,ModelMap modelMap) {
        MenuResult<List<PermissionView>> staticPermissionRoleResult= menuAdminService.selectPermissionMenu();
        modelMap.put("result",staticPermissionRoleResult.getContent());
        return "";
    }
}
