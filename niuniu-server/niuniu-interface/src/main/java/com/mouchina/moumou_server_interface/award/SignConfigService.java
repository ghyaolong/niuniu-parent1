package com.mouchina.moumou_server_interface.award;

import java.util.List;

import com.mouchina.moumou_server.entity.award.SignConfig;

/**
 * 签到
 * @author Administrator
 *
 */
public interface SignConfigService {

	/**
	 * 增加签到配置
	 * @param signConfig
	 */
	public void addSignConfig(SignConfig signConfig);
	/**
	 * 删除配置
	 * @param signConfigId
	 */
	public void deleteSignConfig(Long signConfigId);
	/**
	 * 更新配置
	 * @param signConfig
	 */
	public void updateSignConfig(SignConfig signConfig);
	/**
	 * 查询所有的配置
	 * @return
	 */
	public List<SignConfig> findAll();
	
}
