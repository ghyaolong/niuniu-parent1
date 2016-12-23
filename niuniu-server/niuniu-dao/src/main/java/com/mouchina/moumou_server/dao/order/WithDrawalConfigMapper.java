package com.mouchina.moumou_server.dao.order;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.WithDrawalConfig;

public interface WithDrawalConfigMapper extends BaseMapper<WithDrawalConfig, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(WithDrawalConfig record);

    int insertSelective(WithDrawalConfig record);

    WithDrawalConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WithDrawalConfig record);

    int updateByPrimaryKey(WithDrawalConfig record);
}