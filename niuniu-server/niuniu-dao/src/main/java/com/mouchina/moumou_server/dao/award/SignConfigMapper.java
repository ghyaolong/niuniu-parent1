package com.mouchina.moumou_server.dao.award;

import java.util.List;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.SignConfig;

public interface SignConfigMapper extends BaseMapper<SignConfig, Long> {
    int deleteByPrimaryKey(Long id);

    int insert(SignConfig record);

    int insertSelective(SignConfig record);

    SignConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignConfig record);

    int updateByPrimaryKey(SignConfig record);
    
    /**
     * 获取签到天数的财富值
     * @param day
     * @return
     */
    public Double selectTreasureValue(Integer day);
    /**
     * 查询所有的命中率
     * @return
     */
    public List<Double> selectHitRate();
    
}