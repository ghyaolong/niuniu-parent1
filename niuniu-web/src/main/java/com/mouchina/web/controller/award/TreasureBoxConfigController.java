package com.mouchina.web.controller.award;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.award.TreasureBoxConfig;
import com.mouchina.moumou_server_interface.award.TreasureBoxConfigService;

@Controller
@RequestMapping(value="/treasureBoxConfig")
public class TreasureBoxConfigController {
	
	@Resource
	private TreasureBoxConfigService treasureBoxConfigService;

	@RequestMapping(value="/addTreasureBoxConfig")
	public @ResponseBody void addTreasureBoxConfig(@RequestBody TreasureBoxConfig treasureBoxConfig) {
		treasureBoxConfigService.addTreasureBoxConfig(treasureBoxConfig);
	}
	
}
