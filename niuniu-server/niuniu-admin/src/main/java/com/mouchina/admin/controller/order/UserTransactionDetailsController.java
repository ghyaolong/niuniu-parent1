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
import com.mouchina.moumou_server.entity.order.UserTransactionDetails;
import com.mouchina.moumou_server.entity.vo.UserTransactionDetailsVo;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.UserTransactionDetailsService;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 用户交易明细controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/order/trade")
public class UserTransactionDetailsController {
	
	@Resource
	private UserTransactionDetailsService userTransactionDetailsService;
	
	@Resource
	private UserAssetsService userAssetsService;
	
    @Resource
    UserVerifyService userVerifyService;
    
    private UserAssets userAssets;
    
    private UserResult<User> userResult;
    
    private User user;
    
    private UserTransactionDetailsVo userTransactionDetailsVo;

	@RequestMapping( value = "/list", method = RequestMethod.GET )
    private String payTransactionDetailsList( ModelMap modelMap )
    {
        return "order/usertransactiondetails/list";
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
        String orderNo= StringUtils.isBlank(request.getParameter("orderNo"))?null:request.getParameter("orderNo");
        String userPhone=null;
        if(!StringUtils.isBlank(request.getParameter("userPhone")))
        {
            Long userId = 0L;
            String uPhone = request.getParameter("userPhone");
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setPhone(uPhone);
            User user = userVerifyService.selectUser(userIdentity);
            map.put("userId", user == null ? 0 : user.getId());
        }
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");

        page.setBegin( begin );
        map.put("page", page);
        map.put("tradeNo",orderNo);
        map.put("order","create_time desc");
        map.put("year",year);
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("createTimegt", DateUtils.parse( start ));
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
        ListRange<UserTransactionDetails>  userTransactionDetails = userTransactionDetailsService.selectListUserTransactionDetails(map);
        for(UserTransactionDetails details : userTransactionDetails.getData()){
        	long userId = details.getUserId();
        	if(userId > 0){
        		userResult = userVerifyService.selectUserByUserId(userId);
        		user = userResult.getContent() == null ? null : userResult.getContent();
        		if(user != null){
        			details.setPhoneNum(user.getPhone());
        			details.setUserName(user.getNickName());
        		}
        		userAssets = userAssetsService.selectUserAssets(userId);
        		if(userAssets != null){
        			details.setLuckyBagBalance((double)userAssets.getRedEnvelopeBalance());
                	details.setPurseBalance((double)userAssets.getCashBalance());
        		}
        	}
        }
        modelMap.put( "userTransactionDetails", userTransactionDetails );

        return "";
    }
    
    @RequestMapping( value = "/export", method = RequestMethod.GET )
    private String exportUserTransatctionDetails( HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null ) {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String orderNo= StringUtils.isBlank(request.getParameter("orderNo"))?null:request.getParameter("orderNo");
        String userPhone=null;
        if(!StringUtils.isBlank(request.getParameter("userPhone")))
        {
            Long userId = 0L;
            String uPhone = request.getParameter("userPhone");
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setPhone(uPhone);
            User user = userVerifyService.selectUser(userIdentity);
            map.put("userId", user == null ? 0 : user.getId());
        }
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");

        page.setBegin( begin );
//        map.put("page", page);
        map.put("tradeNo",orderNo);
        map.put("order","create_time desc");
        map.put("year",year);
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        if ( start != null && !start.isEmpty()) {
        	  try {
				map.put("createTimegt", DateUtils.parse( start ));
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
        List<UserTransactionDetailsVo> detailsVos = userTransactionDetailsService.exportListUserTransactionDetails(map);
        for(UserTransactionDetailsVo detailsVo : detailsVos){
        	long userId = detailsVo.getUserId();
        	if(userId > 0){
        		userResult = userVerifyService.selectUserByUserId(userId);
        		user = userResult.getContent() == null ? null : userResult.getContent();
        		if(user != null){
        			detailsVo.setPhoneNum(user.getPhone());
        			detailsVo.setUserName(user.getNickName());
        		}
        		userAssets = userAssetsService.selectUserAssets(userId);
        		if(userAssets != null){
        			detailsVo.setLuckyBagBalance((double)userAssets.getRedEnvelopeBalance() / 100);
        			detailsVo.setPurseBalance((double)userAssets.getCashBalance() / 100);
        			detailsVo.setCurrentuckyBagBalance((double)userAssets.getRedEnvelopeBalance() / 100);
        			detailsVo.setCurrentPurseBalance((double)userAssets.getCashBalance() / 100);
        		}
        		//设置不显示的字段
        		detailsVo.setPurseBalance(0.0);
        		detailsVo.setLuckyBagBalance(0.0);
        	}
        }
        ExportExcelUtil<UserTransactionDetailsVo> exportExcel = new ExportExcelUtil<UserTransactionDetailsVo>();
        String fileName = "用户交易明细" + DateUtils.getNowDateStringALL();
		String[] title = {"用户标识","用户手机号","用户昵称","交易时间","交易类型","交易金额","交易订单号","支付类型","钱袋余额","福袋余额","交易状态","当前钱袋余额","当前福袋余额"};
		exportExcel.exportExcel(fileName, title, detailsVos, response);
        return "success";
    }
	
}
