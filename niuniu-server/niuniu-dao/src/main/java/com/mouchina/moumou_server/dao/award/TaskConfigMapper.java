package com.mouchina.moumou_server.dao.award;

import java.util.List;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.TaskConfig;

public interface TaskConfigMapper extends BaseMapper<TaskConfig, Long> {
	int deleteByPrimaryKey(Long id);

	int insert(TaskConfig record);

	int insertSelective(TaskConfig record);

	TaskConfig selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TaskConfig record);

	int updateByPrimaryKey(TaskConfig record);

	List<TaskConfig> selectByStatus(Integer status);

	List<TaskConfig> findAll();
}