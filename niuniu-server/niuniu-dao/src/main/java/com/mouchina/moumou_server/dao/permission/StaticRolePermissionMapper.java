package com.mouchina.moumou_server.dao.permission;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
public interface StaticRolePermissionMapper extends BaseMapper<StaticRolePermission,java.lang.String> {
    int deleteByRoleId(Long roleId);
}
