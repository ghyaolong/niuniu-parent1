package com.mouchina.admin.service.api.permission;


import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.permission.StaticPermission;

/**
 * Created by douzy on 15/12/8.
 */
public interface LoginService {
    /**
     * 根据登录用户查询 所拥有的权限
     * @param map
     * @return
     */
    List<StaticPermission> getLoginUserPermissionByPhone(Map map);

    /**
     * 所有权限
     * @return
     */
    List<StaticPermission> getAllPermission();
}
