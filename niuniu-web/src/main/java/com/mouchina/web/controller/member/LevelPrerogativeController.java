package com.mouchina.web.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;

@Controller
@RequestMapping("/level")
public class LevelPrerogativeController {
	
	@Resource
	private UserLevelConfigService userLevelConfigService;
	
	@Resource
	private UserWebService userWebService;
	
	
	@RequestMapping(value = "/prerogative", method = { RequestMethod.GET, RequestMethod.POST })
	public String prerogative(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		System.out.println(loginKey);
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user =userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			List<UserLevelConfig> list =userLevelConfigService.selectAllList(new HashMap<>());
			List configList = new ArrayList<>();
			
			if(list != null && list.size() > 0){
				Map configMap;
				for(UserLevelConfig configVo : list){
					configMap = new HashMap<>();
					Integer level = configVo.getLevel();//当前对象对应的等级
					Double levelRatio = configVo.getLevelRatio();//当前等级对应的等级系数
					Integer homePageRedEn = configVo.getHomePageRedEnvelope();
					int redEnve = (int) (configVo.getLevelRatio() * 100 - 100);
					configMap.put("level", level);
					configMap.put("levelRatio", levelRatio);
					configMap.put("homePageRedEnvelope", homePageRedEn); //可领福袋个数
					configMap.put("redEnvelopeAdditionalAward", redEnve); //福袋金额分
					configMap.put("isShopping", 1);  //商城购买
					configList.add(configMap);
				}
			}
			modelMap.put("result", Constants.ERROR_OK);
			modelMap.put("advertList", configList);
			
		}else{
			//用户不存在
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		
		return "";
	}
	
}
