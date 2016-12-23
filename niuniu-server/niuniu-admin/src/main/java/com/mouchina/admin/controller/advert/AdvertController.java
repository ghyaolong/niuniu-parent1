package com.mouchina.admin.controller.advert;

import java.io.UnsupportedEncodingException;
import java.lang.Thread.State;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.vo.AdvertVo;
import com.mouchina.admin.utils.RandomUtil;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.AdvertContentUtil;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.JsonUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.BannerConfig;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.PublicWelFareAdvertVo;
import com.mouchina.moumou_server_interface.activityConfig.ActivityConfigService;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.view.ActivityConfigResult;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.Result;

/**
 * Created by douzy on 16/2/17.
 */
@Controller
@RequestMapping("/advert")
public class AdvertController {
	private static final Logger LOGGER = Logger.getLogger(AdvertController.class);
	@Resource
	AdvertService advertService;

	@Resource
	ActivityConfigService activityConfigService;

	@Resource
	UserAdminService userAdminService;

	@Resource
	UserCommentService userCommentService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String advertList(ModelMap modelMap) {
		return "advert/list";
	}

	@RequestMapping(value = "/listNot", method = RequestMethod.GET)
	private String advertNotList(ModelMap modelMap) {
		return "advert/not";
	}

	@RequestMapping(value = "/listSuccess", method = RequestMethod.GET)
	private String advertSuccessList(ModelMap modelMap) {
		return "advert/success";
	}
/**
 * 添加幸运魔轮
 * @param request
 * @param response
 * @param modelMap
 * @return
 * @throws UnsupportedEncodingException
 * @throws ParseException
 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	private String insertInfo(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws UnsupportedEncodingException, ParseException {
		// 获取到代表当前用户的信息
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();
		LOGGER.info("我的手机号"+phone);
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		LOGGER.info("userIdentity========"+userIdentity.toString());
		User user = userAdminService.selectUser(userIdentity);
		LOGGER.info("用户========"+user.toString());
		String advertName = request.getParameter("advertName");
		String advertContent = request.getParameter("advertContent");
		String startTime = request.getParameter("startTime");//活动开始时间
		String endTime = request.getParameter("endTime");//活动结束时间
		String showTime = request.getParameter("showTime");//活动显示时间
		String preferredPic = request.getParameter("smallPhotoUrl");//特等奖小图
		Advert advert = new Advert();
		if (startTime == null||startTime=="" ||endTime==""|| endTime == null) {
			Calendar c = Calendar.getInstance();
			// 当前的day_of_month加一就是明天时间
			c.add(Calendar.DAY_OF_MONTH, 1);
			String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			advert.setEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(tomorrow));
			advert.setStartTime(new Date());
		} else {
			advert.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
			advert.setEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(endTime));
		}
		
		String scopeBegin0 = request.getParameter("scopeBegin0");
		String scopeEnd0 = request.getParameter("scopeEnd0");
		String[] numMax = request.getParameterValues("numMax");// 对应区间中奖人数数组
		List<String> list = new ArrayList<>();
		for(String a:numMax){
			if(a != null && a != ""){
				list.add(a);
			}
		}
		numMax= list.toArray(new String[0]);
		String[] prize = request.getParameterValues("prize");// 奖品
		String[] prizePic = request.getParameterValues("photoUrl");// 奖品图片
		List<String> list2 = new ArrayList<>();
		for(String a:prizePic){
			if(a != null && a != ""){
				list2.add(a);
			}
		}
		//转换成json格式
		AdvertContentUtil advertContentUtil = new AdvertContentUtil(list2, advertContent);
		String content = JsonUtils.javaToString(advertContentUtil);
		
		advert.setAdvertName(advertName);
		advert.setAdvertContent(content);
		advert.setAdvertType(11);//幸运转盘
		advert.setCreateTime(new Date());
		advert.setUserId(user.getId());
		advert.setState((byte) 1);
		advert.setRedEnvelopeAmount(0);
		advert.setRedEnvelopeCount(0);
		advert.setRedEnvelopeType(1);
		advert.setPreferredPic(preferredPic);;
		AdvertResult<Advert> advertResult = advertService.addAdvert3(advert, user);
		
		if (advertResult.getState() == 1) {
			ActivityConfig activityConfig;
			List<StringBuffer> proList = new ArrayList<>();// 存放所有区间随机数生成的字符串
			proList = RandomUtil.getRandom(scopeBegin0, scopeEnd0, numMax);
			
			// 循环插入属性
			for (int i = 0; i < numMax.length; i++) {
				Long advertId = advertResult.getContent().getId();
				activityConfig = new ActivityConfig();
				activityConfig.setAdvertId(advertId);
				activityConfig.setPrize(prize[i]);
				activityConfig.setPrizePic(prizePic[i]);
				activityConfig.setNumMax(numMax[i]);
				activityConfig.setShowTime(new SimpleDateFormat("yyyy-MM-dd").parse(showTime));
				activityConfig.setScope(scopeBegin0 + "," + scopeEnd0);
				if(i == 0){
					activityConfig.setRank(100);
				}else{
					activityConfig.setRank(i);
				}
				activityConfig.setProbability(proList.get(i).toString());
				ActivityConfigResult<ActivityConfig> activityConfigResult = activityConfigService.addActivityConfig(activityConfig);
				if(activityConfigResult.getState() != 1){
					break;
				}
			}
		}

		return "advert/listOperatingActivities";

	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String advertQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");

		if (request.getParameter("startTime") != null) {
			try {
				map.put("startTimegt", DateUtils.parseDate(request.getParameter("startTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getParameter("endTime") != null) {
			try {
				map.put("startTimelt", DateUtils.parseDate(request.getParameter("endTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		AdvertResult<ListRange<AdvertVo>> advertResult = new AdvertResult<ListRange<AdvertVo>>();

		AdvertResult<ListRange<Advert>> advertQuerResult = advertService.selectListAdvertPage(map);

		ListRange<AdvertVo> listRange = new ListRange<AdvertVo>();
		List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
		for (Advert advert : advertQuerResult.getContent().getData()) {
			AdvertVo advertVo = new AdvertVo();
			User user = userAdminService.selectUserByUserId(advert.getUserId());
			BeanUtils.copyProperties(advert, advertVo);
			advertVo.setPhone(user.getPhone());
			advertVoList.add(advertVo);
		}
		listRange.setData(advertVoList);
		listRange.setPage(advertQuerResult.getContent().getPage());
		advertResult.setContent(listRange);
		modelMap.put("adverts", advertResult.getContent());

		return "";
	}

	@RequestMapping(value = "/list/queryNot", method = RequestMethod.GET)
	private String advertQueryNot(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		map.put("state", 4);
		AdvertResult<ListRange<Advert>> advertResult = advertService.selectListAdvertPage(map);
		modelMap.put("adverts", advertResult.getContent());

		return "";
	}

	@RequestMapping(value = "/list/querySuccess", method = RequestMethod.GET)
	private String advertQuerySuccess(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time desc");
		map.put("state", 1);
		AdvertResult<ListRange<Advert>> advertResult = advertService.selectListAdvertPage(map);
		modelMap.put("adverts", advertResult.getContent());

		return "";
	}

	@RequestMapping(value = "/listOperatingActivities", method = RequestMethod.GET)
	private String listOperatingActivities(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {

		return "advert/listOperatingActivities";
	}

	@RequestMapping(value = "/list/listOperatingActivitiesSuccess", method = RequestMethod.GET)
	private String listOperatingActivitiesSuccess(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("advertType", 7);
		map.put("page", page);
		map.put("order", "create_time desc");
		map.put("state", 1);
		AdvertResult<ListRange<Advert>> advertResult = advertService.selectListOperateTheActivity(map);
		
		ListRange<Advert> listRange = advertResult.getContent();
		List<Advert> listData = listRange.getData();
		int size = listData.size();
		System.out.println("size:" + size);
		for (Advert advert : listData) {

			System.out.println("advert:" + advert.getAdvertName());

		}
		modelMap.put("adverts", advertResult.getContent());

		return "";
	}

	@RequestMapping(value = "/operatingActivitiesToAdd", method = RequestMethod.GET)
	private String OperatingActivitiesToAdd(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		System.out.println("operatingActivitiesToAdd");
		return "advert/operatingActivitiesAdd";

	}

	@RequestMapping(value = "/OperatingActivitiesAdd", method = RequestMethod.GET)
	private String OperatingActivitiesAdd(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		return "advert/listOperatingActivities";

	}

	/**
	 * 审核通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/adopt/{adId}")
	private String adopt(@PathVariable Long adId, HttpServletRequest request, ModelMap modelMap) {
		Integer result = 0;
		if (adId > 0) {
			AdvertResult<Advert> advertAdvertResult = advertService.selectAdvert(adId);
			if (advertAdvertResult.getState() == 1) {
				Advert advert = advertAdvertResult.getContent();
				if (advert.getState() != (byte) 4) { // 增加广告状态4(未通过)校验，为了屏蔽违法广告信息
														// 2016-09-23
					advert.setState((byte) 1);
				}
				AdvertResult<Integer> advertResult = advertService.updateCheckPassAdvert(advert);
				result = advertResult.getContent();
			}
		}
		modelMap.put("result", result);
		return "";
	}

	/**
	 * 审核未通过
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/noThrough/{adId}")
	private String noThrough(@PathVariable Long adId, HttpServletRequest request, ModelMap modelMap) {
		Integer result = 0;
		if (adId > 0) {
			AdvertResult<Advert> advertAdvertResult = advertService.selectAdvert(adId);
			if (advertAdvertResult.getState() == 1) {
				Advert advert = advertAdvertResult.getContent();
				advert.setState((byte) 4);
				AdvertResult<Integer> advertResult = advertService.updateCheckFailAdvert(advert);
				result = advertResult.getContent();
			}
		}
		modelMap.put("result", result);
		return "";
	}

	@RequestMapping(value = "/banner/init", method = RequestMethod.GET)
	private String bannerInit(ModelMap modelMap) {
		return "advert/banner/list";
	}

	@RequestMapping(value = "/banner/list", method = RequestMethod.GET)
	private String bannerList(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "create_time");

		if (request.getParameter("startTime") != null) {
			try {
				map.put("createTimegt", DateUtils.parseDate(request.getParameter("startTime")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (request.getParameter("endTime") != null) {
			try {
				map.put("createTimelt", DateUtils.parseDate(request.getParameter("endTime")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Result<ListRange<BannerConfig>> result = new Result<ListRange<BannerConfig>>();
		Result<ListRange<BannerConfig>> resultQuery = advertService.selectListBannerPage(map);

		ListRange<BannerConfig> listRange = new ListRange<BannerConfig>();
		List<BannerConfig> bannerConfigs = new ArrayList<>();
		List<BannerConfig> bannerConfigList = resultQuery.getContent().getData();
		if (null != bannerConfigList && bannerConfigList.size() > 0) {
			for (BannerConfig config : bannerConfigList) {
				bannerConfigs.add(config);
			}
		}
		int size = bannerConfigs.size();
		if (size < 6) {
			for (int i = 0; i < 6 - size; i++) {
				BannerConfig bannerConfig = new BannerConfig();
				bannerConfig.setId((long) 0);
				bannerConfigs.add(bannerConfig);
			}
		}
		listRange.setData(bannerConfigs);
		listRange.setPage(resultQuery.getContent().getPage());
		result.setContent(listRange);
		modelMap.put("banners", listRange);
		return "";
	}
	@RequestMapping(value = "/banner/add", method = RequestMethod.GET)
	private String bannerAdd(HttpServletRequest request, ModelMap modelMap, BannerConfig bannerConfig)
			throws UnsupportedEncodingException {
		int result = advertService.saveOrUpdateBanner(bannerConfig);
		modelMap.put("result", result);
		if (result > 0) {
			modelMap.put("msg", "保存成功");
		} else {
			modelMap.put("msg", "保存失败");
		}
		return "";
	}
	/**
	 * 创建公益
	 * @param request
	 * @param modelMap
	 * @param bannerConfig
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/publicWelfare/addInfo", method = RequestMethod.GET)
	private String publicWelfareAddInfo(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		return "advert/publicWelFare/addUI";
	}
	
	@RequestMapping(value = "/publicWelfare/add", method = RequestMethod.POST)
	private String publicWelfareAdd(HttpServletRequest request, ModelMap modelMap, Advert advert)
			throws UnsupportedEncodingException {
		// 获取到代表当前用户的信息
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String phone = userDetails.getUsername();
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setPhone(phone);
		User user = userAdminService.selectUser(userIdentity);
		if(user != null){
			String photoUrl = request.getParameter("photoUrl");
			List<String> photoList = new ArrayList<>();
			photoList.add(photoUrl);
			AdvertContentUtil advertContentUtil = new AdvertContentUtil(photoList, advert.getAdvertContent());
			String content = JsonUtils.javaToString(advertContentUtil);
			advert.setAdvertContent(content);//广告内容json格式保存
			advert.setUserId(user.getId());
			advert.setAdvertType(10);
			advert.setCreateTime(new Date());
			/*增加公益广告信息*/
			int result = advertService.addPublicWelfare(advert);
			modelMap.put("result", result);
			if (result > 0) {
				modelMap.put("msg", "保存成功");
			} else {
				modelMap.put("msg", "保存失败");
			}
		}
		
		return "advert/publicWelFare/list";
	}
	
	/**
	 * 公益广告查询
	 * @param request
	 * @param modelMap
	 * @param bannerConfig
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/publicWelfare/list", method = RequestMethod.GET)
	private String publicWelfareList(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		return "advert/publicWelFare/list";
	}
	
	@RequestMapping(value = "/publicWelfareSuccess/list", method = RequestMethod.GET)
	private String publicWelfareListSuccess(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		
		Map<String, Object> map = new HashMap<>();
		int begin = 0;
		if (StringUtil.isNotEmpty(request.getParameter("begin"))) {
			begin = Integer.valueOf(request.getParameter("begin"));
		}
		Page page = new Page(begin, 10);
		page.setBegin(begin);
		map.put("advertType", new Integer(10));
		map.put("page", page);
		map.put("order", "create_time desc");
		map.put("state", 1);
		ListRange<PublicWelFareAdvertVo> listRange = advertService.selectPublicWelFareVoList(map);
		modelMap.put("publicWelFares", listRange);

		return "";
	}
	
	@RequestMapping("/publicWelfare/updateState")
	private String publicWelfareUpdateState(HttpServletRequest request, ModelMap modelMap) {
		int result = 0;
		String state = request.getParameter("state");
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(state) && StringUtil.isNotEmpty(id)) {
			result = advertService.updateStateById(Long.valueOf(id), Integer.valueOf(state));
		}
		modelMap.put("result", result);
		if (result > 0){
			modelMap.put("msg", "修改成功");
		} else {
			modelMap.put("msg", "修改失败");
		}
		return "";
	}
	
}


