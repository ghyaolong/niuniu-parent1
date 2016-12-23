package com.mouchina.moumou_server.dao.award;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.TreasureBoxConfig;

/**
 * 宝箱的配置
 * @author Administrator
 *
 */
public interface TreasureBoxConfigMapper extends BaseMapper<TreasureBoxConfig, Long> {

	public int deleteByPrimaryKey(Long id);

	public int insert(TreasureBoxConfig record);

	public int insertSelective(TreasureBoxConfig record);

	public TreasureBoxConfig selectByPrimaryKey(Long id);

	public int updateByPrimaryKeySelective(TreasureBoxConfig record);

	public int updateByPrimaryKey(TreasureBoxConfig record);

	/**
	 * 获取所有宝箱的时间点
	 * 
	 * @return
	 */
	public List<Date> getTreasureBoxTimes();
	/**
	 * 获取所有的命中率
	 * @return
	 */
	public List<Double> selectHitRate();
	/**
	 * 查询用户命中的奖励
	 * @param map
	 * @return
	 */
	public TreasureBoxConfig selectUserHitAward(Map<String,Object> map);
	
}