package com.mouchina.web.controller.award;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.award.SignConfig;
import com.mouchina.moumou_server_interface.award.SignConfigService;

@Controller
@RequestMapping(value="/signConfig")
public class SignConfigController {

	@Resource
	private SignConfigService signConfigService;
	
	@RequestMapping(value="/addSignConfig")
	public @ResponseBody void addSignConfig(@RequestBody SignConfig signConfig) {
		signConfigService.addSignConfig(signConfig);
	}
	
}
