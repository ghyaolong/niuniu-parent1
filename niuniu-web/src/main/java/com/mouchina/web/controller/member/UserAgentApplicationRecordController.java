package com.mouchina.web.controller.member;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.luckybag.LuckyBagHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAgentApplicationRecord;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server_interface.member.UserAgentApplicationRecordService;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.Result;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;

/**
 * 用户申请代理商信息controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/agent")
public class UserAgentApplicationRecordController extends BaseController {

	@Resource
	private UserAgentApplicationRecordService userAgentApplicationRecordService;
	
	@Resource
	private UserWebService userWebService;
	
	@Resource
	private UserAgentService userAgentService;
	
	@Resource
	private UserVerifyService userVerifyService;
	
	/**维护用户申请代理商记录信息**/
	@RequestMapping(value = "/apply", method = RequestMethod.GET )
	public String preserveAgentApplicationRecord(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, UserAgentApplicationRecord userAgentApplicationRecord)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)
				|| userAgentApplicationRecord.getAgentLevel() == null
				|| StringUtil.isEmpty(userAgentApplicationRecord.getApplicationName())
				|| StringUtil.isEmpty(userAgentApplicationRecord.getPhoneNumber())) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.remove("userAgentApplicationRecord");
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			userAgentApplicationRecord.setUserId(user.getId());
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			modelMap.remove("userAgentApplicationRecord");
			return "";
		}
		Result<UserAgentApplicationRecord> result = userAgentApplicationRecordService.saveOrUpdateAgentApplicationRecord(userAgentApplicationRecord);
		if (result.getState() == Constants.ERROR_OK) {
			modelMap.put("result", Constants.ERROR_OK);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_400002);
		}
		modelMap.remove("userAgentApplicationRecord");
		return "";
	}
	
	/**线上用零钱支付成为星级代理商**/
	@RequestMapping(value = "/onLineStarLevel", method = RequestMethod.GET )
	public String onLineStarLevelUserAgent(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, UserAgent userAgent)
			throws IOException {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)
				|| userAgent.getAgentLevel() == null
				|| userAgent.getAgentPiont() == null
				|| userAgent.getApplyAmount() == null) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			modelMap.remove("userAgent");
			return "";
		}
		if (userAgent.getAgentLevel().intValue() != 4
				|| userAgent.getAgentPiont().intValue() != 0
				|| userAgent.getApplyAmount().intValue() != 98000) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_600000);
			modelMap.remove("userAgent");
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user != null){
			/*查询个人账户信息*/
			UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
			if(userAssets.getRedEnvelopeBalance().intValue() < userAgent.getApplyAmount().intValue()){
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500006);
				modelMap.remove("userAgent");
				return "";
			}
			userAgent.setUserId(user.getId());
			Result<UserAgent> result = userAgentService.addOnLineUserAgent(userAgent);
			if (result.getState() == Constants.ERROR_OK) {
				/*更新个人账户信息*/
				userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() - userAgent.getApplyAmount());
				userAssets.setTableNum(user.getTableNum());
				userVerifyService.updateUserAssets(userAssets);
				modelMap.put("result", Constants.ERROR_OK);
			} else {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500007);
			}
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		modelMap.remove("userAgent");
		return "";
	}
}
