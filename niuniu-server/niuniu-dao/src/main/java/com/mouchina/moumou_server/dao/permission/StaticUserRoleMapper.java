package com.mouchina.moumou_server.dao.permission;

import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;

public interface StaticUserRoleMapper extends BaseMapper<StaticUserRole,java.lang.Integer> {
    Integer deleteByUserId(Long userId);
    
    int updateStaticUserRoleByMap(Map map);
}


