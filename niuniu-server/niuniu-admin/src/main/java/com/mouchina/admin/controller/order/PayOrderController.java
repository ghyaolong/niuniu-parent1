package com.mouchina.admin.controller.order;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping( "/order/pay" )
public class PayOrderController
{
    @Resource
    private PayOrderAdminService payOrderAdminService;
    @Resource
    UserVerifyService userVerifyService;

    @RequestMapping( value = "/new", method = RequestMethod.GET )
    private String payOrderNew( ModelMap modelMap )
    {
        return "order/payorder/new";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST )
    private String payOrderAdd( PayOrder payOrder, ModelMap modelMap )
    {
        return "redirect:show/" + payOrder.getId(  ) + ".html";
    }

    @RequestMapping( value = "/show", method = RequestMethod.GET )
    private String payOrderShow( ModelMap modelMap )
    {
        return "order/payorder/show";
    }

    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String payOrderList( ModelMap modelMap )
    {
        return "order/payorder/list";
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
        map.put("orderNo",orderNo);
        map.put("order","create_time desc");
        map.put("year",year);
        
        if ( request.getParameter( "startTime" ) != null ) {
        	  try {
				map.put("createTimegt", DateUtils.parseDate( request.getParameter( "startTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if ( request.getParameter( "endTime" ) != null ) {
        	try {
				map.put("createTimelt", DateUtils.parseDate( request.getParameter( "endTime")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        ListRange<PayOrder> payOrders = payOrderAdminService.selectListPayOrderByOrderNo( map );
        modelMap.put( "payOrders", payOrders );

        return "";
    }
}
