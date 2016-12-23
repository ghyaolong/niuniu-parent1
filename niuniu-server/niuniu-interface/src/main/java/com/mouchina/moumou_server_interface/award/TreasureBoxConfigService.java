package com.mouchina.moumou_server_interface.award;

import java.util.List;

import com.mouchina.moumou_server.entity.award.TreasureBoxConfig;

/**
 * 宝箱配置
 * @author Administrator
 *
 */
public interface TreasureBoxConfigService {
	/**
	 * 增加宝箱配置
	 * @param treasureBoxConfig
	 */
	public void addTreasureBoxConfig(TreasureBoxConfig treasureBoxConfig);
	/**
	 * 删除宝箱配置
	 * @param id
	 */
	public void deleteTreasureBoxConfig(Long id);
	/**
	 * 更新宝箱配置
	 * @param treasureBoxConfig
	 */
	public void updateTreasureBoxConfig(TreasureBoxConfig treasureBoxConfig);
	/**
	 * 查询所有的宝箱配置
	 * @return
	 */
	public List<TreasureBoxConfig> findAll();

}
