package com.mouchina.admin.controller.order;

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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.utils.ExportExcelUtil;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.UserLuckyBagDetails;
import com.mouchina.moumou_server.entity.vo.UserLuckyBagDetailsVo;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.UserLuckyBagDetailsService;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 统计用户福袋明细controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("order/luckybag")
public class UserLuckyBagDetailsController {

	@Resource
	private UserLuckyBagDetailsService userLuckyBagDetailsService;
	
	@Resource
	private UserVerifyService userVerifyService;
	
	@Resource
	private UserAssetsService userAssetsService;
	
	private UserAssets userAssets;
	
    private UserResult<User> userResult;
    
    private User user;
	
	@RequestMapping( value = "/list", method = RequestMethod.GET )
    private String payTransactionDetailsList( ModelMap modelMap )
    {
        return "order/userluckybagdetails/list";
    }

    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String payOrderQuery( HttpServletRequest request, ModelMap modelMap )
    {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
            Long userId = 0L;
            String uPhone = request.getParameter("userPhone");
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setPhone(userPhone);
            User user = userVerifyService.selectUser(userIdentity);
            map.put("userId", user == null ? 0 : user.getId());
        }
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");

        page.setBegin( begin );
        map.put("page", page);
        map.put("order","create_time desc");
        map.put("year",year);
        map.put("stateType", 1); // 1:退款类型
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
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
        ListRange<UserLuckyBagDetails>  userLuckyBagDetails = userLuckyBagDetailsService.selectListUserLuckyBagDetails(map);
        for(UserLuckyBagDetails details : userLuckyBagDetails.getData()){
        	long userId = details.getUserId();
        	if(userId > 0){
        		userResult = userVerifyService.selectUserByUserId(userId);
        		user = userResult.getContent() == null ? null : userResult.getContent();
        		if(user != null){
        			details.setPhoneNum(user.getPhone());
        		}
        		userAssets = userAssetsService.selectUserAssets(userId);
        		if(userAssets != null){
                	details.setPurseBalance((double)userAssets.getCashBalance());
        		}
        	}
        }
        modelMap.put( "userLuckyBagDetails", userLuckyBagDetails );

        return "";
    }
    
    @RequestMapping( value = "/export", method = RequestMethod.GET )
    private String exportUserLuckyBagDetails( HttpServletRequest request, HttpServletResponse response )
    {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String userPhone = request.getParameter("userPhone");
        if(userPhone != null && !userPhone.isEmpty())
        {
            Long userId = 0L;
            String uPhone = request.getParameter("userPhone");
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setPhone(userPhone);
            User user = userVerifyService.selectUser(userIdentity);
            map.put("userId", user == null ? 0 : user.getId());
        }
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");

        page.setBegin( begin );
        map.put("page", page);
        map.put("order","create_time desc");
        map.put("year",year);
        map.put("stateType", 1); // 1:退款类型
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
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
        List<UserLuckyBagDetailsVo>  detailsVos = userLuckyBagDetailsService.exportListUserLuckyBagDetails(map);
        for(UserLuckyBagDetailsVo details : detailsVos){
        	long userId = details.getUserId();
        	if(userId > 0){
        		userResult = userVerifyService.selectUserByUserId(userId);
        		user = userResult.getContent() == null ? null : userResult.getContent();
        		if(user != null){
        			details.setPhoneNum(user.getPhone());
        		}
        		userAssets = userAssetsService.selectUserAssets(userId);
        		if(userAssets != null){
                	details.setPurseBalance((double)userAssets.getCashBalance() / 100);
                	details.setCurrentuckyPurseBalance((double)userAssets.getCashBalance() / 100);
        		}
        		// 设置不显示字段
        		details.setPurseBalance(0.0);
        	}
        }
        ExportExcelUtil<UserLuckyBagDetailsVo> exportExcel = new ExportExcelUtil<UserLuckyBagDetailsVo>();
        String fileName = "用户福袋明细" + DateUtils.getNowDateStringALL();
		String[] title = {"用户标识","用户手机号","发布时间","福袋总额","福袋数量","钱袋余额","退还金额","是否领完","当前钱袋余额"};
		exportExcel.exportExcel(fileName, title, detailsVos, response);
        return "success";
    }
}
