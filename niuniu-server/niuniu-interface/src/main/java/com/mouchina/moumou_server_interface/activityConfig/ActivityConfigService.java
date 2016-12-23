package com.mouchina.moumou_server_interface.activityConfig;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server_interface.view.ActivityConfigResult;

public interface ActivityConfigService {

	/***
	 * 活动配置
	 * 
	 * @param activityConfig
	 * @return
	 */
	public ActivityConfigResult<ActivityConfig> addActivityConfig(ActivityConfig activityConfig);

	public ActivityConfigResult<Integer> updateActivityConfig(ActivityConfig activityConfig);

	public ActivityConfigResult<ListRange<ActivityConfig>> selectListActivityConfigPage(Map<String, Object> map);

	public ActivityConfigResult<ActivityConfig> selectActivityConfig(Long activityConfigId);

	public ActivityConfigResult<Integer> deleteActivityConfig(Long activityConfigId);
	
	/**
	 * 查询奖品信息
	 * @param map
	 * @return
	 */
	public List<ActivityConfig> selectListActivityConfigs(Map<String, Object> map);

}
