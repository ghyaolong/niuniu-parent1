package com.mouchina.moumou_server.dao.activityConfig;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;

public interface ActivityConfigMapper extends BaseMapper<ActivityConfig, Long> {
	
	public int deleteByPrimaryKey(Long id);

	public int insert(ActivityConfig record);

	public int insertSelective(ActivityConfig record);

	public ActivityConfig selectByPrimaryKey(Long id);

	public int updateByPrimaryKeySelective(ActivityConfig record);

	public int updateByPrimaryKey(ActivityConfig record);

	public List<ActivityConfig> selectListPageActivityConfig(Map<String, Object> map);

}