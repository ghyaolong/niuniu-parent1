package com.mouchina.admin.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.member.UserAgentAdminService;
import com.mouchina.admin.utils.ExportExcelUtil;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.member.UserAgentCommission;
import com.mouchina.moumou_server.entity.vo.UserAgentCommissionVo;

/**
 * 代理商成交金额提成controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/agent/commission")
public class UserAgentCommissionController {

	private static final Logger LOGGER = Logger.getLogger(UserAgentCommissionController.class);
	
	
	@Resource
	private UserAgentAdminService userAgentAdminService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String commissionList(ModelMap modelMap) {
		return "/members/agent/commission/list";
	}

	@RequestMapping(value = "/list/query", method = RequestMethod.GET)
	private String commissionQuery(HttpServletRequest request, ModelMap modelMap) throws UnsupportedEncodingException {
		Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
            map.put("userPhone", userPhone);
        }

        page.setBegin( begin );
        map.put("page", page);
        map.put("order","stat_date desc");
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("statDategt", DateUtils.parseDate( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("statDatelt", DateUtils.parseDate( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        ListRange<UserAgentCommission> userAgentCommissions = userAgentAdminService.selectListUserAgentCommissionPage(map);
        modelMap.put( "userAgentCommissions", userAgentCommissions );
        return "";
	}
	
	 /**
     * 代理商成交金额提成结算
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/update/{id}")
    private String updateCommissions(@PathVariable Long id, HttpServletRequest request, ModelMap modelMap) {
        Integer result = 0;
        if (id > 0) {
        	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		String phone = userDetails.getUsername();
        	Map map = new HashMap();
        	map.put("id", id);
        	map.put("modifyTime", new Date());
        	map.put("state", 1);
            result = userAgentAdminService.updateUserAgentCommissionByMap(map);
            LOGGER.info("结算代理商成交金额提成，结算人手机号码:" + phone);
        }
        modelMap.put("result", result);
        return "";
    }
	
	/**
	 * 代理商成交金额提成信息导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/export", method = RequestMethod.GET )
    private String exportuserAgentCommission( HttpServletRequest request, HttpServletResponse response )
    {
        Map map = new HashMap(  );
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
            map.put("userPhone", userPhone);
        }
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("statDategt", DateUtils.parseDate( start ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if ( end != null && !end.isEmpty() ) {
        	try {
				map.put("statDatelt", DateUtils.parseDate( end ));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        map.put("order","stat_date desc");
        List<UserAgentCommissionVo> userAgentCommissions = userAgentAdminService.exportUserAgentCommission(map);
        ExportExcelUtil<UserAgentCommissionVo> exportExcel = new ExportExcelUtil<UserAgentCommissionVo>();
        String fileName = "代理商成交金额提成信息" + DateUtils.getNowDateStringALL();
		String[] title = {"统计日期","用户ID","手机号码","用户昵称","推荐代理类型","被推荐人ID","被推荐人手机号码","被推荐人昵称","提成金额","结算标志"};
		exportExcel.exportExcel(fileName, title, userAgentCommissions, response);
        return "success";
    }
	
}
