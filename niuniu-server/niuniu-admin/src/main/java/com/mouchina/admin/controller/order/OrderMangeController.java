package com.mouchina.admin.controller.order;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.admin.service.api.order.OrderAdminService;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.view.PayResult;

@Controller
@RequestMapping( "/order" )
public class OrderMangeController
{
    @Resource
    private OrderAdminService orderAdminService;
    @Resource
    private UserAdminService userAdminService;
    @Resource
    private PayOrderAdminService payOrderAdminService;


    @RequestMapping( value = "/new", method = RequestMethod.GET )
    private String orderNew( ModelMap modelMap )
    {
        return "order/order/new";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST )
    private String orderAdd( Order order, ModelMap modelMap )
    {
        return "redirect:show/" + order.getId(  ) + ".html";
    }

    @RequestMapping( value = "/show", method = RequestMethod.GET )
    private String orderShow( ModelMap modelMap )
    {
        return "order/order/show";
    }

    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String orderList( ModelMap modelMap )
    {

//        modelMap.put("orderStateMap", stateService.getOrderState());
        return "order/order/list";

    }

    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String orderQuery( HttpServletRequest request, ModelMap modelMap ) throws UnsupportedEncodingException {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null )
        {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String orderNo= StringUtils.isBlank(request.getParameter("orderNo"))?null:request.getParameter("orderNo");
        String title=StringUtils.isBlank(request.getParameter("title"))?null: URLDecoder.decode(request.getParameter("title"), "utf-8");
        String userName=StringUtils.isBlank(request.getParameter("userName"))?null:URLDecoder.decode(request.getParameter("userName"),"utf-8");
        String orderState= StringUtils.isBlank(request.getParameter("orderState"))?null:request.getParameter("orderState");
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");



        page.setBegin( begin );
        map.put("page", page);
        map.put("orderNo",orderNo);
        map.put("userName",userName);
//        map.put("orderState",orderState);
        map.put("order","create_time desc");
        map.put("title",title);
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
      
        ListRange<OrderView> orderViews = orderAdminService.selectListRangeOrdersView( map );
        modelMap.put( "orderViews", orderViews );

        return "";
    }
    @RequestMapping(value = "/refund/detail",method = RequestMethod.GET)
    private String  refundDetail(HttpServletRequest _req,ModelMap modelMap) {
        String orderNo= StringUtils.isBlank(_req.getParameter("orderNo"))?null:_req.getParameter("orderNo");
        String detailNos= StringUtils.isBlank(_req.getParameter("detailNos"))?null:_req.getParameter("detailNos");
        PayResult<Integer> payResult= payOrderAdminService.reimburseQueryPayOrder(orderNo, detailNos);
        modelMap.put("result",payResult);
        return "";
    }
    @RequestMapping(value = "/refund/exec",method = RequestMethod.POST)
    private String refundExec(HttpServletRequest _req,ModelMap modelMap) {
        String orderNo= StringUtils.isBlank(_req.getParameter("orderNo"))?null:_req.getParameter("orderNo");
        String detailNos= StringUtils.isBlank(_req.getParameter("detailNos"))?null:_req.getParameter("detailNos");
        Integer pri= StringUtils.isBlank(_req.getParameter("pri"))?0:new BigDecimal(_req.getParameter("pri")).multiply(new BigDecimal(100)).intValue();
        PayResult<Integer> payResult= payOrderAdminService.updateReimburseRequestPayOrder(orderNo, detailNos,pri);
        modelMap.put("result",payResult);
        return "";
    }
	@RequestMapping(value = "/detail/{orderId}", method = RequestMethod.GET)
	private String orderdetail(@PathVariable String orderId, ModelMap modelMap) {
        OrderView orderView = orderAdminService.selectOrderView(orderId);
        modelMap.put("orderView", orderView);
        Long uid=0L;
        if(orderView!=null)
            uid=orderView.getOrder().getUserId();
        modelMap.put("users",userAdminService.selectUserByUserId(uid));
//        modelMap.put("orderStateMap", stateService.getOrderState());
//        modelMap.put("orderDetailState",stateService.getOrderDetailState());
        return "order/order/detail";
    }
}
