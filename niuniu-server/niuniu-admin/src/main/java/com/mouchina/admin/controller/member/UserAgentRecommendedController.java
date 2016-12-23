package com.mouchina.admin.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.member.UserAgentAdminService;
import com.mouchina.admin.utils.ExportExcelUtil;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.vo.AgentRecommendedVo;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 代理商推荐提成收益controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/agent/recommended")
public class UserAgentRecommendedController {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(UserAgentRecommendedController.class);
	
	@Resource
	UserAdminService userAdminService;
	
	@Resource
	private UserAgentAdminService userAgentAdminService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String recommendedList(ModelMap modelMap) {
		return "/agent/recommended/list";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String recommendedQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
        	User user = userAgentAdminService.selectUserInfo(userPhone);
			if (user != null) {
				map.put("incomeUserId", user.getId());
			}
        }
        page.setBegin( begin );
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("incomeDategt", DateUtils.parseDate( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("incomeDatelt", DateUtils.parseDate( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        map.put("page", page);
        map.put("group","income_user_id, income_date");
        map.put("order","income_date desc");
        ListRange<AgentRecommendedVo> agentRecommendeds = userAgentAdminService.selectListUserAgentRecommendedPage(map);
        modelMap.put( "agentRecommendeds", agentRecommendeds );
        return "";
	}
	
	/**
	 * 代理商推荐提成收益信息导出
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping( value = "/export", method = RequestMethod.GET )
    private String exportAgentRecommendedDetails( HttpServletRequest request, HttpServletResponse response )
    {
        Map map = new HashMap(  );
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
        	User user = userAgentAdminService.selectUserInfo(userPhone);
			if (user != null) {
				map.put("incomeUserId", user.getId());
			}
        }
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("incomeDategt", DateUtils.parseDate( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("incomeDatelt", DateUtils.parseDate( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        map.put("group","income_user_id, income_date");
        map.put("order","income_date desc");
        List<AgentRecommendedVo> agentRecommendedVos = userAgentAdminService.exportUserAgentRecommended(map);
        ExportExcelUtil<AgentRecommendedVo> exportExcel = new ExportExcelUtil<AgentRecommendedVo>();
        String fileName = "代理商推荐提成收益信息" + DateUtils.getNowDateStringALL();
		String[] title = {"统计日期","用户ID","手机号码","用户昵称","推荐星级代理数","推荐星级代理收益","推荐中心代理数","推荐中心代理收益","推荐区县代理数","推荐区县代理收益","推荐提成总收益"};
		exportExcel.exportExcel(fileName, title, agentRecommendedVos, response);
        return "success";
    }
	
}
