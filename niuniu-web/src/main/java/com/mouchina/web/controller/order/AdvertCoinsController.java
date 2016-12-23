package com.mouchina.web.controller.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server.entity.order.RechargeConfig;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;
import com.mouchina.moumou_server_interface.order.RechargeConfigService;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SystemResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;

@Controller
@RequestMapping("/recharge")
public class AdvertCoinsController {
	
	@Resource
	private UserWebService userWebService;
	@Resource
	private RechargeConfigService rechargeConfigService;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	private UserLevelConfigService userLevelConfigService;
	@Resource
	private UserAgentService userAgentService;
	@Resource
	SystemService systemService;
	
	@RequestMapping(value = "/coinsSelect", method = { RequestMethod.GET, RequestMethod.POST })
	public String CoinsSelect(HttpServletRequest request, ModelMap modelMap){
		String loginKey = request.getParameter("loginKey");
		if(StringUtil.isEmpty(loginKey)){
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			Map map =new HashMap();
			List<RechargeConfig> listConfig =rechargeConfigService.selectListRecharge(map);
			List listRecharge =new ArrayList();
			if(listConfig !=null && listConfig.size()>0){
				Map<String,Object>  mapRecharge;
				for(RechargeConfig rechargeVo :listConfig){
					mapRecharge =new HashMap<String,Object>();
					Integer exchangeConis =rechargeVo.getExchangeCoins(); //可兑换广告币
					Integer additionalCoins =rechargeVo.getAdditionalCoins(); //赠送的广告币
					Byte hotFlag =rechargeVo.getHotFlag();  //热销标志,1代表热销，0非热销
					Integer rechargeMoney =rechargeVo.getRechargeMoney(); //充值金额(分)
					mapRecharge.put("exchangeCoins", exchangeConis);
					mapRecharge.put("additionalCoins", additionalCoins);
					mapRecharge.put("hotFlag", hotFlag);
					mapRecharge.put("rechargeMoney", rechargeMoney);
					mapRecharge.put("id", rechargeVo.getId());
					listRecharge.add(mapRecharge);
				}
				Map cashBalanceMap =new HashMap();
				UserAssets assets = userAssetsService.selectUserAssets(user.getId());
				Integer balance = assets.getCashBalance();//返回广告币
				cashBalanceMap.put("cashBalance", balance);
				cashBalanceMap.put("listConins", listRecharge);
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("withdrawalList", cashBalanceMap);
			}
			//获取充值配置参数
			Map<String,Object> sqlMap =new HashMap<String,Object>();
			sqlMap.put("configKey","order.recharge.config.content");
			sqlMap.put("configGroup", "order");
			sqlMap.put("order", "create_time desc");
			SystemResult<List<SystemGlobalConfig>> systemGlobalConfigResult =systemService.selectListSystemGlobalConfig(sqlMap);
			if(systemGlobalConfigResult != null){
				List<SystemGlobalConfig> systemGlobConfigList =systemGlobalConfigResult.getContent();
				if(systemGlobConfigList != null && systemGlobConfigList.size() >0){
					SystemGlobalConfig systemGlobalConfig =systemGlobConfigList.get(0);
					String configContent =systemGlobalConfig.getConfigContent();
					modelMap.put("configContent",configContent);
				}else{
					modelMap.put("configContent","");
				}
			}
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

}
