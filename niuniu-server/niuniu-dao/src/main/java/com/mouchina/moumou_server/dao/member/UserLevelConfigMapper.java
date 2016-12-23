package com.mouchina.moumou_server.dao.member;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;

public interface UserLevelConfigMapper extends BaseMapper<UserLevelConfig, Long>{

    int deleteByPrimaryKey(Long id);

    int insert(UserLevelConfig record);

    int insertSelective(UserLevelConfig record);

    UserLevelConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLevelConfig record);

    int updateByPrimaryKey(UserLevelConfig record);
}