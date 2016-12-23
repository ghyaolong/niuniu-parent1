package com.mouchina.web.controller.member;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.user.particular.UserIncomeDo;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.income.UserIncomeService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.IncomeResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.income.UserIncomeWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.BaseResultVo;
import com.mouchina.web.service.api.vo.UserIncomeVo;

@Controller
@RequestMapping("/income")
public class IncomeController extends BaseController {

	@Resource
	private UserWebService userWebService;
	@Resource
	private UserIncomeWebService userIncomeWebService;
	@Resource
	private AdvertService advertService;
	@Resource
	private UserVerifyService userVerifyService;
	@Autowired
	private UserIncomeService userIncomeService;
	
	/**
	 * 用户每日收益列表【3.0版本】
	 * @param loginKey
	 * @param begin
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/userIncomeList3")
	public @ResponseBody BaseResultVo<Map<String,Object>> userIncomeList3(String loginKey,Integer begin) {
		Long userId = userWebService.getUserByLoginKey(loginKey).getId();
		Map<String,Object> userIncomeMap = this.setPage(begin);
		userIncomeMap.put("userId", userId);
		List<UserIncomeSum> userIncomeSums = userIncomeService.selectIncomeList3(userIncomeMap);
		Integer incomeCount = userIncomeService.selectIncomeCount(userId);
		
		Map<String,Object> userIncomeSumMap = new HashMap<String,Object>();
		userIncomeSumMap.put("incomeCount", incomeCount);
		userIncomeSumMap.put("userIncomeSums", userIncomeSums);
		
		BaseResultVo<Map<String,Object>> baseResultVo = new BaseResultVo<Map<String,Object>>();
		baseResultVo.setOffset(begin);
		baseResultVo.setResult("1");
		baseResultVo.setData(userIncomeSumMap);
		return baseResultVo;
	}

	@RequestMapping(value = "/detail/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String userIncomeDetailList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			long incomeId) throws IOException {

		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			IncomeResult<UserIncomeSum> incomeResult = userIncomeWebService.selectUserIncomeSum(incomeId);

			if (incomeResult.getState() == 1) {
				modelMap.put("result", "1");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", new Page(begin, 10));
				map.put("order", "create_time desc");
				map.put("userId", incomeResult.getContent().getUserId());
				map.put("incomeDate", incomeResult.getContent().getIncomeDate());
				map.put("userId", user.getId());
				ListRange<UserIncome> listRange = userIncomeWebService.selectUserIncome(map).getContent();
				ListRange<UserIncomeVo> userIncomeListVo = new ListRange<UserIncomeVo>();
				userIncomeListVo.setPage(listRange.getPage());
				List<UserIncomeVo> listVo = new ArrayList<UserIncomeVo>();
				for (UserIncome userIncome : listRange.getData()) {
					UserIncomeVo userIncomeVo = new UserIncomeVo();
					BeanUtils.copyProperties(userIncome, userIncomeVo);
					AdvertResult<RedEnvelope> advertRedResult = advertService.selectRedEnvelope(userIncome.getEnventId());
					AdvertResult<Advert> advertResult = advertService.selectAdvert(advertRedResult.getContent().getAdvertId());
					UserResult<User> userResult = userVerifyService.selectUserByUserId(userIncome.getEventUserId());
					userIncomeVo.setUserName(userResult.getContent().getName());
					userIncomeVo.setEnventName(advertResult.getContent().getAdvertName());
					listVo.add(userIncomeVo);
				}
				userIncomeListVo.setData(listVo);

				modelMap.put("userIncomeList", userIncomeListVo.getData());
				modelMap.put("page", userIncomeListVo.getPage());
			} else {

				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			}

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}
	
	/**
	 * 帮赚明细【3.0版本】
	 * @param loginKey	用户唯一标识
	 * @param begin		分页的页数
	 * @param incomeId	
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/userIncomeDetailList3")
	public @ResponseBody BaseResultVo<Map<String,Object>> userIncomeDetailList3(String loginKey,Integer begin, String queryDate) {
		Long userId = userWebService.getUserByLoginKey(loginKey).getId();
		
		Map<String,Object> userIncomeDetailListMap = this.setPage(begin);
		userIncomeDetailListMap.put("userId", userId);
		userIncomeDetailListMap.put("createTime", queryDate);
		List<UserIncomeDo> userIncomeDos = userIncomeService.userIncomeDetailList3(userIncomeDetailListMap);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("date",new SimpleDateFormat("MM月dd日").format(userIncomeDos.get(0).getDate()));
		data.put("userIncome", userIncomeDos);
		
		BaseResultVo<Map<String,Object>> baseResultVo = new BaseResultVo<Map<String,Object>>();
		baseResultVo.setResult("1");
		baseResultVo.setErrorMsg("请求成功!");
		baseResultVo.setData(data);
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
