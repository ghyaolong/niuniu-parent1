package com.mouchina.moumou_server_interface.award;

import java.util.List;

import com.mouchina.moumou_server.entity.award.TaskConfig;

public interface TaskConfigService {

	/**
	 * 根据任务展示状态查询任务配置
	 * @param status 任务的展示状态 0：可领取 ，1：已领取 ,2:已完成
	 * @return
	 */
	List<TaskConfig> selectByStatus(Integer status);
	
	/**
	 * 查询所有的任务
	 * @return 
	 */
	public List<TaskConfig> findAll();
	
	/**
	 * 增添一条任务配置
	 * @param taskConfig 任务
	 */
	void addTaskConfig(TaskConfig taskConfig);
	
	/**
	 * 根据主键删除一条任务配置
	 * @param id 主键id
	 */
	void deleteTaskConfig(Long id);
	
	
	/**
	 * 更新任务配置
	 * @param taskConfig  任务
	 */
	void updateTaskConfig(TaskConfig taskConfig);
	
	
}
