package com.mouchina.web.controller.award;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.award.TaskConfig;
import com.mouchina.moumou_server_interface.award.TaskConfigService;

@Controller
@RequestMapping(value="/signConfig")
public class TaskConfigController {
	
	@Resource
	private TaskConfigService taskConfigService;
	
	@RequestMapping(value="/addTaskConfig")
	public @ResponseBody void addTaskConfig(@RequestBody TaskConfig taskConfig){
		taskConfigService.addTaskConfig(taskConfig);
	}
	
	@RequestMapping(value="/selectByStatus")
	public @ResponseBody void selectByStatus(Integer status){
		taskConfigService.selectByStatus(status);
	}
	
	@RequestMapping(value="/findAll")
	public @ResponseBody List<TaskConfig> findAll(){
		return taskConfigService.findAll();
	}
	@RequestMapping(value="/deleteTaskConfig")
	public @ResponseBody void deleteTaskConfig(Long id){
		taskConfigService.deleteTaskConfig(id);
	}
	public @ResponseBody void updateTaskConfig(@RequestBody TaskConfig taskConfig){
		taskConfigService.updateTaskConfig(taskConfig);
	}

}
