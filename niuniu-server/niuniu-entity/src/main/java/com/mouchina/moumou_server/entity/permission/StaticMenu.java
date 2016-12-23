package com.mouchina.moumou_server.entity.permission;

import java.io.Serializable;

/**
 * Created by douzy on 15/11/19.
 */
public class StaticMenu implements Serializable {

    private long menuCode;
    private String menuName;
    private String menuDesc;
    private String menuParentCode;
    private Integer menuOrder;
    private String menuUrl;

    public StaticMenu() {
    }

    public long getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(long menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuParentCode() {
        return menuParentCode;
    }

    public void setMenuParentCode(String menuParentCode) {
        this.menuParentCode = menuParentCode;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }
}
