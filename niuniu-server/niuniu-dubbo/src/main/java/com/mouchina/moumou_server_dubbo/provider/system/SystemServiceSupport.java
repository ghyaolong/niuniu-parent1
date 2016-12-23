package com.mouchina.moumou_server_dubbo.provider.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.cache.CacheManager;
import com.mouchina.moumou_server.dao.system.SystemDataMapper;
import com.mouchina.moumou_server.dao.system.SystemGlobalConfigMapper;
import com.mouchina.moumou_server.entity.system.SystemData;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SystemDataVo;
import com.mouchina.moumou_server_interface.view.SystemResult;

/**
 * 类说明
 *
 * @author larry 2015年7月12日下午5:52:35
 */

public class SystemServiceSupport implements SystemService {

	@Resource
	private CacheManager cacheManager;
	@Resource
	private SystemGlobalConfigMapper systemGlobalConfigMapper;
	@Resource
	private SystemDataMapper systemDataMapper;

	@Override
	public SystemResult<SystemGlobalConfig> addSystemGlobalConfig(
			SystemGlobalConfig systemGlobalConfig) {
		// TODO Auto-generated method stub
		deleteCache();
		SystemResult<SystemGlobalConfig> systemResult = new SystemResult<SystemGlobalConfig>();
		systemResult.setState(systemGlobalConfigMapper
				.insertSelective(systemGlobalConfig));
		systemResult.setContent(systemGlobalConfig);
		
		return systemResult;

	}
	private void deleteCache(){
		Map map = new HashMap();
		map.put("state", 1);
		cacheManager.deleteMapByMap(String.class, SystemGlobalConfig.class,
				map);
		
	}
	@Override
	public SystemResult<Integer> updateSystemGlobalConfig(
			SystemGlobalConfig systemGlobalConfig) {
		// TODO Auto-generated method stub
		deleteCache();
		SystemResult<Integer> systemResult = new SystemResult<Integer>();
		systemResult.setContent(systemGlobalConfigMapper
				.updateByPrimaryKeySelective(systemGlobalConfig));

		deleteCache();

		return systemResult;

	}

	@Override
	public SystemResult<SystemGlobalConfig> selectSystemGlobalConfig(
			Integer systemGlobalConfigId) {
		// TODO Auto-generated method stub
		SystemResult<SystemGlobalConfig> systemResult = new SystemResult<SystemGlobalConfig>();
		SystemGlobalConfig systemGlobalConfig = systemGlobalConfigMapper
				.selectByPrimaryKey(systemGlobalConfigId);
		if (systemGlobalConfig != null) {
			systemResult.setContent(systemGlobalConfig);
			systemResult.setState(1);
		}
		return systemResult;

	}

	@Override
	public SystemResult<List<SystemGlobalConfig>> selectListSystemGlobalConfig(
			Map map) {
		// TODO Auto-generated method stub
		SystemResult<List<SystemGlobalConfig>> systemResult = new SystemResult<List<SystemGlobalConfig>>();
		systemResult.setContent(systemGlobalConfigMapper.selectList(map));
		systemResult.setState(1);
		return systemResult;

	}

	@Override
	public SystemResult<Map<String, SystemGlobalConfig>> selectSystemGlobalConfigMap() {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("state", 1);
		SystemResult<Map<String, SystemGlobalConfig>> systemResult = new SystemResult<Map<String, SystemGlobalConfig>>();
		Map<String, SystemGlobalConfig> systemGlobalConfigMap = new HashMap<String, SystemGlobalConfig>();

		systemGlobalConfigMap = cacheManager.getMapByMap(String.class,
				SystemGlobalConfig.class, map);

		if (systemGlobalConfigMap == null) {
			SystemResult<List<SystemGlobalConfig>> systemListResult = selectListSystemGlobalConfig(map);
			if (systemListResult.getContent() != null) {
				systemGlobalConfigMap =new HashMap<String, SystemGlobalConfig>();
				systemResult.setState(1);
				for (SystemGlobalConfig systemGlobalConfig : systemListResult
						.getContent()) {

					systemGlobalConfigMap.put(
							systemGlobalConfig.getConfigKey(),
							systemGlobalConfig);
				}
			}

			if (systemGlobalConfigMap.size() > 0) {
				systemResult.setContent(systemGlobalConfigMap);
				cacheManager.addMapByMap(String.class,
						SystemGlobalConfig.class, map,
						systemGlobalConfigMap);
			}
		}else{
			systemResult.setState(1);
			systemResult.setContent(systemGlobalConfigMap);
		}
		
		return systemResult;

	}

	@Override
	public SystemResult<SystemGlobalConfig> selectSystemGlobalConfigByKey(
			String systemGlobalConfigKey) {
		SystemResult<SystemGlobalConfig> systemResult = new SystemResult<SystemGlobalConfig>();
		SystemResult<Map<String, SystemGlobalConfig>> systemMapResult = selectSystemGlobalConfigMap();
		if (systemMapResult.getState() == 1) {
			Map<String, SystemGlobalConfig> systemGlobalConfigMap = systemMapResult
					.getContent();
			SystemGlobalConfig  systemGlobalConfig=systemGlobalConfigMap.get(systemGlobalConfigKey);
			if(systemGlobalConfig!=null){
				systemResult.setContent(systemGlobalConfig);
				systemResult.setState(1);
			}
			
		}
		return systemResult;

	}
	@Override
	public List<SystemDataVo> selectListSystemData(Map<String,Object> map) {

		List<SystemData> systemDatas= systemDataMapper.selectList(map);
		List<SystemDataVo> systemDataVos=new ArrayList<>();
		for(SystemData systemData:systemDatas)
		{
			SystemDataVo systemDataVo=new SystemDataVo();
			systemDataVo.setId(systemData.getId());
			systemDataVo.setName(systemData.getKeyName());
			systemDataVo.setValue(systemData.getSysValue());
			systemDataVos.add(systemDataVo);
		}
		return systemDataVos;
	}
	@Override
	public int updateSystemData(SystemData sysData) {
		return systemDataMapper.updateByPrimaryKeySelective(sysData);
	}

}
