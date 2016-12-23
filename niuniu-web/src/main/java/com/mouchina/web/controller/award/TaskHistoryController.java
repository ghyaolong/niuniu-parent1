package com.mouchina.web.controller.award;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.web.controller.member.UserController;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.BaseResultVo;
import com.mouchina.web.service.api.vo.TaskHistoryVo;

@Controller
@RequestMapping(value="/taskHistory")
public class TaskHistoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Resource
	private TaskHistoryService taskHistoryService;
	@Resource
	private UserWebService userWebService;

	@RequestMapping(value="/task")
	public @ResponseBody Object task(String loginKey,Long taskConfigId,String isQuery) {
		TaskHistoryVo taskHistoryVo = new TaskHistoryVo();

		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			logger.error("用户不存在!");
			taskHistoryVo.setObj("用户不存在!");
			taskHistoryVo.setResult("0");
			return taskHistoryVo;
		}
		
		Long userId = user.getId();
		Object obj = taskHistoryService.insertTask(userId,taskConfigId,isQuery);
		taskHistoryVo.setObj(obj);
		taskHistoryVo.setResult("1");
		return taskHistoryVo;
	}
	
	@RequestMapping(value="/shareApp")
	public @ResponseBody Object shareApp(String loginKey) {
		BaseResultVo<Object> baseResultVo = new BaseResultVo<Object>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在");
			return baseResultVo;
		}
		
		Long userId = user.getId();
		logger.info("分享app已经完成，需要完成任务----------用户名：[{}]",userId);
		
		Object obj = taskHistoryService.insertTask(userId,(long)4,"1");
		baseResultVo.setResult("1");
		baseResultVo.setData(obj);
		
		return baseResultVo;
	}
	
}
