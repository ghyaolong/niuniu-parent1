package com.mouchina.web.controller.award;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.moumou_server.entity.award.SignHistory;
import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.award.SignHistoryService;
import com.mouchina.moumou_server_interface.award.TreasureAwardHistoryServiceNew;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.TreasureBoxSignVo;

/**
 * 宝箱签到接口的合并
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/treasureBoxSignHistory")
public class TreasureBoxSignHistoryController {
	
	@Resource
	private SignHistoryService signHistoryService;
	@Resource
	private UserWebService userWebService;
	@Resource
	private TreasureAwardHistoryServiceNew treasureAwardHistoryServiceNew;
	
	/**
	 * 开启宝箱和用户签到
	 * @param loginKey		
	 * @param isSign		是否签到
	 * @param isQuery		是否查询
	 * @param tag			标记调用什么接口
	 * @return
	 */
	@RequestMapping(value="/openTreasureBoxUserSign")
	public @ResponseBody Object openTreasureBoxUserSign(String loginKey,String isSign,String isQuery) {
		TreasureBoxSignVo treasureBoxSignVo = new TreasureBoxSignVo();
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			treasureBoxSignVo.setResult("0");
			treasureBoxSignVo.setErrorMsg("用户为空!");
			return treasureBoxSignVo;
		}
		
		Long userId = user.getId();
		SignHistory signHistory = signHistoryService.insertUserSign(userId,isSign);
		TreasureAwardHistory treasureAwardHistory = treasureAwardHistoryServiceNew.addOpenUserTreasureAward(userId,isQuery);
		
		treasureBoxSignVo.setSignHistory(signHistory);
		treasureBoxSignVo.setTreasureAwardHistory(treasureAwardHistory);
		treasureBoxSignVo.setResult("1");
		return treasureBoxSignVo;
		
	}
	
}
