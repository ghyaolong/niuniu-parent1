package com.mouchina.admin.controller.order;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.util.AlipayNotify;
import com.mouchina.admin.service.api.alipay.AlipayRemiburseService;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.admin.service.api.order.UserRemiburseAdminService;
import com.mouchina.admin.service.api.tencentPay.TencentRemiburseService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.order.UserReimburseView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping( "/order/userremiburse" )
public class UserReimburseOrderController
{
    @Resource
    private UserRemiburseAdminService userRemiburseAdminService;
    @Resource
    private AlipayRemiburseService alipayRemiburseService;
    @Resource
    private TencentRemiburseService tencentRemiburseService;
    @Resource
    private PayOrderAdminService payOrderAdminService;
    static Logger logger = LogManager.getLogger();
    @RequestMapping( value = "/new", method = RequestMethod.GET )
    private String userremiburseNew( ModelMap modelMap )
    {
        return "order/userremiburse/new";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST )
    private String userremiburseAdd( UserReimburse userReimburse, ModelMap modelMap )
    {
        return "redirect:show/" + userReimburse.getReimburseNo(  ) + ".html";
    }

    @RequestMapping( value = "/update", method = RequestMethod.POST )
    private String userremiburseUpdate( UserReimburse userReimburse, ModelMap modelMap )
    {
        userRemiburseAdminService.updateAgreeUserReimbursePrice( userReimburse );

        return "redirect:/order/userremiburse/list.html";
    }

    @RequestMapping( value = "/edit/{reimburseNo}", method = RequestMethod.GET )
    private String userremiburseEdit( @PathVariable
    String reimburseNo, ModelMap modelMap )
    {
        UserReimburse userReimburse = userRemiburseAdminService.selectUserReimburse( reimburseNo );

        modelMap.put( "userReimburse", userReimburse );

        PayOrder payOrder= payOrderAdminService.selectPayOrderByOrderNo(userReimburse.getOrderNo());
        String payAction="";
        if(payOrder.getPayWay()==1)
            payAction="alipay";
        if(payOrder.getPayWay()==2)
            payAction="tencentPay";
        modelMap.put("payAction",payAction);

        return "order/userremiburse/edit";
    }

    @RequestMapping( value = "/show", method = RequestMethod.GET )
    private String userremiburseShow( ModelMap modelMap )
    {
        return "order/userremiburse/show";
    }

    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String userremiburseList( ModelMap modelMap )
    {
//        modelMap.put("orderRemiburseState", stateService.getOrderRemiburseState());
        return "order/userremiburse/list";
    }

    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String userremiburseQuery( HttpServletRequest request, ModelMap modelMap )
    {
        Map map = new HashMap(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null )
        {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }
        String userPhone= StringUtils.isBlank(request.getParameter("userPhone"))?null:request.getParameter("userPhone");
        String orderNo=StringUtils.isBlank(request.getParameter("orderNo"))?null:request.getParameter("orderNo");
        String orderState= StringUtils.isBlank(request.getParameter("orderState"))?null:request.getParameter("orderState");
        String year=StringUtils.isBlank(request.getParameter("year"))?null:request.getParameter("year");

        page.setBegin( begin);
        map.put("page", page);
        map.put("phone",userPhone);
        map.put("orderNo",orderNo);
        map.put("state",orderState);
        map.put("order","create_time desc");
        map.put("year",year);
        ListRange<UserReimburseView> userremiburses = userRemiburseAdminService.selectListUserReimbursePage( map );
        modelMap.put( "userReimburses", userremiburses );

        return "";
    }
    @RequestMapping(value = "/ali_notify",method = RequestMethod.POST)
    private void aliPayNotify(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws IOException {

        logger.info("===================支付宝退款回调==============");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //批次号

        String batch_no = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("batch_no:"+batch_no);
        //批量退款数据中转账成功的笔数

        String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("success_num:"+success_num);
        //批量退款数据中的详细信息
        String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("result_details:"+result_details);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if(AlipayNotify.verify(params)){//验证成功
            logger.info("==============参数校验成功  开始业务处理==============");
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            alipayRemiburseService.alipayNotify(batch_no,result_details);
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            //判断是否在商户网站中已经做过了这次通知返回的处理
            //如果没有做过处理，那么执行商户的业务程序
            //如果有做过处理，那么不执行商户的业务程序

            response.getWriter().println("success");    //请不要修改或删除
            logger.info("success");
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            logger.info("==============参数校验成功  业务处理结束==============");
            //////////////////////////////////////////////////////////////////////////////////////////
        }else {//验证失败
            logger.info("==============参数校验失败  返回fail==============");
            response.getWriter().println("fail");
            logger.info("fail");
        }
        logger.info("===================支付宝退款回调==============");
        //return "order/userremiburse/list";
    }

    /**
     * 支付宝退款
     * @param request
     * @param modelMap
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    @RequestMapping(value = "/alipay/api",method = RequestMethod.POST)
    private String  alipayApi(HttpServletRequest request,ModelMap modelMap) throws UnsupportedEncodingException, ParseException {
        String price = request.getParameter("price");
        String processBak = request.getParameter("processBak");
        String reimburseNo = request.getParameter("reimburseNo");
        String reimburseCause = request.getParameter("reimburseCause");

        Map parmasMap = new HashMap();
        parmasMap.put("price", price);
        parmasMap.put("processBak", processBak);
        parmasMap.put("reimburseNo", reimburseNo);
        parmasMap.put("reimburseCause",reimburseCause);
        String sHtmlText = alipayRemiburseService.buildFormHtml(parmasMap);
        modelMap.put("aliHtml", sHtmlText == null ? "退款失败,请重试!" : sHtmlText);
        return "order/userremiburse/alipayApi";
    }

    /**
     * 微信退款
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(value ="/tencentPay/api",method = RequestMethod.POST)
    private String  tencentApi(HttpServletRequest request,ModelMap modelMap) throws Exception {
        String price = request.getParameter("price");
        String orderNo = request.getParameter("orderNo");
        String reimburseNo = request.getParameter("reimburseNo");

        Map parmasMap = new HashMap();
        parmasMap.put("price", price);
        parmasMap.put("reimburseNo", reimburseNo);
        parmasMap.put("orderNo", orderNo);

        String  result=tencentRemiburseService.tencentTrmiburseApi(parmasMap);
        modelMap.put("result",result);
        return "order/userremiburse/tencentpayApi";
    }
    public static String getOrderNoTableNum(String orderNo) {
        String tableNum = orderNo.substring(0, 4);
        return tableNum;
    }
}
