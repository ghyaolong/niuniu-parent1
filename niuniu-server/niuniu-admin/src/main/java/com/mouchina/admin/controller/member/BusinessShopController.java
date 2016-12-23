package com.mouchina.admin.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.member.BusinessShop;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.member.BusinessShopService;
import com.mouchina.moumou_server_interface.member.UserIdentity;

/**
 * 商户店铺controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/businessShop")
public class BusinessShopController {

	@Resource
	BusinessShopService businessShopService;
	
	@Resource
	UserAdminService userAdminService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String init(ModelMap modelMap) {
		return "members/businessshop/list";
	}
	
	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String businessShopQuery(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map<String,Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;
		String beginStr = request.getParameter("begin");
		if (beginStr != null && !beginStr.isEmpty()) {
			begin = Integer.valueOf(beginStr);
		}
		String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        String userPhone = request.getParameter("userPhone");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("createTimegt", DateUtils.parse( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("createTimelt", DateUtils.parse( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if(userPhone != null && !userPhone.isEmpty())
        {
        	UserIdentity userIdentity = new UserIdentity();
			userIdentity.setPhone(userPhone);
			User user = userAdminService.selectUser(userIdentity);
			if (user != null) {
				map.put("userId", user.getId());
			}
        }
        page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		ListRange<BusinessShop> listRange = businessShopService.queryPageListBusinessShopByMap(map);
		if(listRange != null && listRange.getData() != null){
			List<BusinessShop> businessShops = listRange.getData();
			for(BusinessShop shop : businessShops){
				Long userId = shop.getUserId();
				/*查询用户昵称、手机号*/
				User user = userAdminService.selectUserByUserId(userId);
				if(user != null ){
					shop.setNickName(user.getNickName());
					shop.setPhone(user.getPhone());
				}
			}
		}
		modelMap.put("businessShops", listRange);
		
		return "";
	}
	
	/**
	 * 审核通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.GET)
	private String pass(HttpServletRequest request, ModelMap modelMap, BusinessShop businessShop) {
		Integer result = 0;
		if (businessShop.getId() > 0) {
			result = businessShopService.updatePassBusinessShop(businessShop);
		}
		modelMap.put("result", result);
		if(result > 0){
			modelMap.put("msg", "审核成功。");
		}else{
			modelMap.put("msg", "审核失败。");
		}
		return "";
	}

	/**
	 * 审核未通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/noPass",  method = RequestMethod.GET)
	private String noPass(HttpServletRequest request, ModelMap modelMap, BusinessShop businessShop) {
		Integer result = 0;
		if (businessShop.getId() > 0) {
			result = businessShopService.updateNoPassBusinessShop(businessShop);
		}
		modelMap.put("result", result);
		if(result > 0){
			modelMap.put("msg", "审核成功。");
		}else{
			modelMap.put("msg", "审核失败。");
		}
		modelMap.put("result", result);
		return "";
	}
	
}
