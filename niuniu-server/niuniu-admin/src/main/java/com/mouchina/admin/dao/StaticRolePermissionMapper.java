package com.mouchina.admin.dao;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;

public interface StaticRolePermissionMapper extends BaseMapper<StaticRolePermission,String> {
    int deleteByRoleId(Long roleId);
}
