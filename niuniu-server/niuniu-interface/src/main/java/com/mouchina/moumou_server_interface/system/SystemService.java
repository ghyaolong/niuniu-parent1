package com.mouchina.moumou_server_interface.system;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.SystemResult;

/**
 * 系统数据接口
 * 
 * @author Administrator
 *
 */
public interface SystemService {

	/**
	 * 添加系统全局配置
	 * 
	 * @param systemGlobalConfig
	 * @return
	 */
	SystemResult<SystemGlobalConfig> addSystemGlobalConfig(SystemGlobalConfig systemGlobalConfig);

	/**
	 * 更新系统全局配置
	 * 
	 * @param systemGlobalConfig
	 * @return
	 */
	SystemResult<Integer> updateSystemGlobalConfig(SystemGlobalConfig systemGlobalConfig);

	/**
	 * 获取系统全局配置(仅供系统管理后台使用)
	 * 
	 * @param systemGlobalConfigId
	 * @return
	 */
	SystemResult<SystemGlobalConfig> selectSystemGlobalConfig(Integer systemGlobalConfigId);

	/**
	 * 获取系统配置列表
	 */
	SystemResult<List<SystemGlobalConfig>> selectListSystemGlobalConfig(Map<String,Object> map);

	/**
	 * 获取系统配置map结果
	 * 
	 * @param map
	 * @return
	 */
	SystemResult<Map<String, SystemGlobalConfig>> selectSystemGlobalConfigMap();

	/**
	 * 获取系统全局配置
	 * 
	 * @param systemGlobalConfigKey
	 * @return
	 */
	SystemResult<SystemGlobalConfig> selectSystemGlobalConfigByKey(String systemGlobalConfigKey);

	public List<SystemDataVo> selectListSystemData(Map<String,Object> map);

	public int updateSystemData(SystemData sysData);
}
