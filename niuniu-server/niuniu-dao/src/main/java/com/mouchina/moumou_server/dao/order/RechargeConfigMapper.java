package com.mouchina.moumou_server.dao.order;


import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.RechargeConfig;

public interface RechargeConfigMapper extends BaseMapper<RechargeConfig, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(RechargeConfig record);

    int insertSelective(RechargeConfig record);

    RechargeConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RechargeConfig record);

    int updateByPrimaryKey(RechargeConfig record);
    
    
}