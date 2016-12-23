package com.mouchina.admin.dao;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;

public interface StaticUserRoleMapper extends BaseMapper<StaticUserRole,Integer> {
    Integer deleteByUserId(Long userId);
}


