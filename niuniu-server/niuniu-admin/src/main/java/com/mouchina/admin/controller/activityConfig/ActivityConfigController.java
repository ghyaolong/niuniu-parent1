package com.mouchina.admin.controller.activityConfig;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server_interface.activityConfig.ActivityConfigService;
import com.mouchina.moumou_server_interface.view.ActivityConfigResult;

/**
 * Created by zhanglei on 16/11/17.
 */
@Controller
@RequestMapping("/activityConfig")
public class ActivityConfigController {

	@Resource
	private ActivityConfigService activityConfigService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String activityConfigList(@RequestParam(value = "advertId") String advertId, ModelMap modelMap) {
		modelMap.put("advertId", advertId);
		return "activityConfig/list";
	}

	/**
	 * 未查到数据
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/listNot", method = RequestMethod.GET)
	private String advertNotList(ModelMap modelMap) {
		return "activityConfig/not";
	}

	@RequestMapping(value = "/listSuccess", method = RequestMethod.GET)
	private String activityConfigSuccessList(ModelMap modelMap) {
		return "activityConfig/success";
	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String activityConfigQuery(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "rank");

		if (request.getParameter("advertId") != null) {
			try {
				map.put("advertId", request.getParameter("advertId"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ActivityConfigResult<ListRange<ActivityConfig>> activityConfigResult = activityConfigService
				.selectListActivityConfigPage(map);
		modelMap.put("activityConfigs", activityConfigResult.getContent());

		ListRange<ActivityConfig> listRange = activityConfigResult.getContent();
		List<ActivityConfig> listData = listRange.getData();
		int size = listData.size();
		System.out.println("size:" + size);
		for (ActivityConfig activityConfig : listData) {

			System.out.println("activityConfig:" + activityConfig.getNumMax());

		}

		return "activityConfig/list";
	}

	@RequestMapping(value = "/list/queryNot", method = RequestMethod.GET)
	private String activityConfigQueryNot(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map map = new HashMap();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "rank desc");
		map.put("state", 4);
		ActivityConfigResult<ListRange<ActivityConfig>> ActivityConfigResult = activityConfigService
				.selectListActivityConfigPage(map);
		modelMap.put("activityConfigs", ActivityConfigResult.getContent());

		return "";
	}

	@RequestMapping(value = "/list/querySuccess", method = RequestMethod.GET)
	private String activityConfigQuerySuccess(HttpServletRequest request, ModelMap modelMap)
			throws UnsupportedEncodingException {
		Map map = new HashMap();
		Page page = new Page(0, 10);
		int begin = 0;

		if (request.getParameter("begin") != null) {
			begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
		}
		page.setBegin(begin);
		map.put("page", page);
		map.put("order", "rank desc");
		map.put("state", 1);
		ActivityConfigResult<ListRange<ActivityConfig>> activityConfigResult = activityConfigService.selectListActivityConfigPage(map);
		modelMap.put("activityConfigs", activityConfigResult.getContent());

		return "";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	private String activityConfigAdd(@ModelAttribute ActivityConfig activityConfig, ModelMap modelMap) {

		ActivityConfigResult<ActivityConfig> activityConfigResult = activityConfigService.addActivityConfig(activityConfig);

		if (null != activityConfigResult) {

			if (null != activityConfigResult.getContent()) {
				return "activityConfig/list";
			}
		}

		return null;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	private String activityConfigEdit(ModelMap modelMap) {
		return "activityConfig/add";
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	private String activityConfigDel(@RequestParam String id, ModelMap modelMap) {
		ActivityConfigResult<Integer> result = activityConfigService.deleteActivityConfig(Long.parseLong(id));
		if (result.getContent() > 0) {
			return "activityConfig/list";
		}
		return null;
	}

}
