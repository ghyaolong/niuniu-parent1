package com.mouchina.moumou_server_interface.permission;

import java.io.Serializable;
import java.util.List;

import com.mouchina.moumou_server.entity.permission.StaticPermission;

/**
 * Created by douzy on 15/12/3.
 */
public class PermissionView implements Serializable {
    private StaticPermission staticPermission;
    private List<PermissionView> staticPermissionList;

    public StaticPermission getStaticPermission() {
        return staticPermission;
    }

    public void setStaticPermission(StaticPermission staticPermission) {
        this.staticPermission = staticPermission;
    }

    public List<PermissionView> getStaticPermissionList() {
        return staticPermissionList;
    }

    public void setStaticPermissionList(List<PermissionView> staticPermissionList) {
        this.staticPermissionList = staticPermissionList;
    }
}
