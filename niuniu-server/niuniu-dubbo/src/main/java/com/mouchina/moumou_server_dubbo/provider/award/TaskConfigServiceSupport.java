package com.mouchina.moumou_server_dubbo.provider.award;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.dao.award.TaskConfigMapper;
import com.mouchina.moumou_server.entity.award.TaskConfig;
import com.mouchina.moumou_server_interface.award.TaskConfigService;

@Service("taskConfigServiceSupport")
public class TaskConfigServiceSupport implements TaskConfigService {
	
	@Resource
	private TaskConfigMapper taskConfigMapper;

	@Override
	public List<TaskConfig> selectByStatus(Integer status) {
		return taskConfigMapper.selectByStatus(status);
	}

	@Override
	public List<TaskConfig> findAll() {
		return taskConfigMapper.findAll();
	}

	@Override
	public void addTaskConfig(TaskConfig taskConfig) {
		taskConfigMapper.insertSelective(taskConfig);

	}

	@Override
	public void deleteTaskConfig(Long id) {

		taskConfigMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateTaskConfig(TaskConfig taskConfig) {
		taskConfigMapper.updateByPrimaryKeySelective(taskConfig);
	}

}
