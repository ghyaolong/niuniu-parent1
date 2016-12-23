package com.mouchina.web.controller.particular;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.user.particular.ConsumeParticularVo;
import com.mouchina.moumou_server_interface.particular.UserParticularService;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.BaseResultVo;

@Controller
@RequestMapping(value = "/userParticular")
public class UserParticularController {
	
	@Autowired
	private UserParticularService userParticularService;
	@Autowired
	private UserWebService userWebService;
	
	/**
	 * 收入详情【3.0】
	 * @param loginKey
	 * @param begin
	 * @return
	 */
	@RequestMapping(value = "/selectEarnParticular")
	public @ResponseBody BaseResultVo<List<RedEnvelope>> selectEarnParticular(String loginKey, Integer begin) {
		BaseResultVo<List<RedEnvelope>> baseResultVo = new BaseResultVo<List<RedEnvelope>>();
		
		if (StringUtil.isEmpty(loginKey)) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(Constants.ERROR_CODE_100001);
			return baseResultVo;
		}

		if (begin == null) begin = 0;

		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(Constants.ERROR_CODE_100001);
			return baseResultVo;
		}
		
		Map<String, Object> userParticularMap = this.setPage(begin);
		userParticularMap.put("userId", user.getId());
		userParticularMap.put("order", "create_time desc");
		userParticularMap.put("page", new Page(begin, 10));
		userParticularMap.put("state", 1);
		List<RedEnvelope> redEnvelopes = userParticularService.selectEarnParticular(userParticularMap);
		
		baseResultVo.setResult("1");
		baseResultVo.setData(redEnvelopes);
		return baseResultVo;
	}
	
	/**
	 * 查询消费明细
	 * 
	 * @param userId
	 * @param begin
	 * @return
	 */
	@RequestMapping(value = "/selectConsumeParticular")
	public @ResponseBody BaseResultVo<List<ConsumeParticularVo>> selectConsumeParticular(String loginKey, Integer begin) {
		Long userId = userWebService.getUserByLoginKey(loginKey).getId();
		Map<String, Object> consumeParticularMap = this.setPage(begin);
		consumeParticularMap.put("userId", userId);
		List<ConsumeParticularVo> consumeParticularVos = userParticularService.selectConsumeParticular(consumeParticularMap);
		
		BaseResultVo<List<ConsumeParticularVo>> baseResultVo = new BaseResultVo<List<ConsumeParticularVo>>();
		baseResultVo.setResult("1");
		baseResultVo.setData(consumeParticularVos);
		return baseResultVo;
	}
	
	/**
	 * 设置分页
	 * @param offset
	 * @return
	 */
	private Map<String,Object> setPage(Integer offset) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("offset", offset);
		map.put("pagesize", 10);
		return map;
	}
	
}
