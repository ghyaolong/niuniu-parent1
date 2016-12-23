package com.mouchina.web.controller.award;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;
import com.mouchina.moumou_server_interface.award.TreasureAwardHistoryServiceNew;
import com.mouchina.web.service.api.member.UserWebService;

@Controller
@RequestMapping(value="/treasureAwardHistory")
public class TreasureAwardHistoryController {
	
	@Autowired
	private TreasureAwardHistoryServiceNew treasureAwardHistoryServiceNew;
	@Resource
	private UserWebService userWebService;
	
	@RequestMapping(value="/openTreasureBox")
	public @ResponseBody TreasureAwardHistory openTreasureBox(String loginKey,String isQuery) {
		Long userId = userWebService.getUserByLoginKey(loginKey).getId();
		TreasureAwardHistory treasureAwardHistory = treasureAwardHistoryServiceNew.addOpenUserTreasureAward(userId,isQuery);
		treasureAwardHistory.setResult("1");
		return treasureAwardHistory;
	}
	
}
