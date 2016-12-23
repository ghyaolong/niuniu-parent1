package com.mouchina.moumou_server.dao.award;

import java.util.Date;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;

public interface TreasureAwardHistoryMapper extends BaseMapper<TreasureAwardHistory, Long> {
	
	public int deleteByPrimaryKey(Long id);

    public int insert(TreasureAwardHistory record);

    public int insertSelective(TreasureAwardHistory record);

    public TreasureAwardHistory selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(TreasureAwardHistory record);

    public int updateByPrimaryKey(TreasureAwardHistory record);
    /**
     * 根据用户的user id去查询数据
     * @param userId	用户id
     * @return
     */
    public TreasureAwardHistory selectByUserId(Long userId);
    /**
     * 查询用户最后领取奖励的时间
     * @param userId
     * @return
     */
    public Date selectUserLastAwardHistory(Long userId);
    /**
     * 查询宝箱是否已经开启
     * @param map
     * @return
     */
    public TreasureAwardHistory selectTreasureBoxIsOpen(Map<String,Object> map);
    
}