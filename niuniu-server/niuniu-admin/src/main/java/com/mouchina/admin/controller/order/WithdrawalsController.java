package com.mouchina.admin.controller.order;

import com.alipay.util.AlipayNotify;
import com.mouchina.admin.service.api.alipay.AlipayWithdrawalService;
import com.mouchina.admin.service.api.tencentPay.TencentWithdrawalsService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.view.WelfareResult;
import com.mouchina.moumou_server_interface.view.WithdrawalView;
import org.apache.commons.lang.StringUtils;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by douzy on 16/2/19.
 */
@Controller
@RequestMapping("/withdrawals")
public class WithdrawalsController {
    @Resource
    WithdrawalsService withdrawalsService;
    @Resource
    AlipayWithdrawalService alipayWithdrawalService;

    @Resource
    TencentWithdrawalsService tencentWithdrawalsService;

    static Logger logger = LogManager.getLogger();
    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String cashcouponList( ModelMap modelMap ) {


        return "welfare/withdrawal/list";
    }
    @RequestMapping( value = "/update", method = RequestMethod.POST )
    private String userremiburseUpdate( WithdrawalHistoryOrder withdrawalHistoryOrder, ModelMap modelMap )
    {
        withdrawalsService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);

        return "welfare/withdrawal/list";
    }
    @RequestMapping( value = "/edit/{id}", method = RequestMethod.GET )
    private String userremiburseEdit( @PathVariable
                                      Integer id, ModelMap modelMap ) {
        Map map = new HashMap();
        map.put("id", id);
        WelfareResult<WithdrawalHistoryOrder> withdrawalHistoryOrderWelfareResult = withdrawalsService.selectWithdrawalOrderModel(map);

        WithdrawalHistoryOrder withdrawalHistoryOrder = withdrawalHistoryOrderWelfareResult.getContent();

        modelMap.put("withdrawals", withdrawalHistoryOrder);

        String payUrl = "";
        if (withdrawalHistoryOrder.getWithdrawalChannel() == 1)
            payUrl = "tencentPay";
        if (withdrawalHistoryOrder.getWithdrawalChannel() == 2)
            payUrl = "alipay";
        modelMap.put("payUrl", payUrl);
        return "welfare/withdrawal/edit";
    }
    /**
     * 提现审核列表
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String cashcouponQuery( HttpServletRequest request, ModelMap modelMap ) {
        Map map = new HashMap();
        Page page = new Page(0, 10);
        int begin = 0;
        if (request.getParameter("begin") != null) {
            begin = Integer.valueOf(request.getParameter("begin").toString()).intValue();
        }
        page.setBegin(begin);
        map.put("page", page);
        WelfareResult<ListRange<WithdrawalView>> withdrawalHistoryOrderListRange = withdrawalsService.selectListRangeWithdrawalHistory(map);
        modelMap.put("WithdrawalHistoryOrders", withdrawalHistoryOrderListRange.getContent());
        return "";
    }
    /**
     * 批量付款回调
     * @param request
     * @param response
     * @param modelMap
     * @throws IOException
     */
    @RequestMapping(value = "/ali_notify",method = RequestMethod.POST)
    private void ali_notify(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) throws IOException, ParseException {
        logger.info("===================支付宝批量付款回调==============");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
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
        //批量付款数据中转账成功的详细信息
        String success_details = "", fail_details = "";

        String batchNo = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"), "UTF-8");
        logger.info("批次号:" + batchNo);
        if (!StringUtils.isBlank(request.getParameter("success_details")))
            success_details = new String(request.getParameter("success_details").getBytes("ISO-8859-1"), "UTF-8");
        if (!StringUtils.isBlank(request.getParameter("fail_details")))
            fail_details = new String(request.getParameter("fail_details").getBytes("ISO-8859-1"), "UTF-8");

        logger.info("成功付款详细:" + success_details);
        //批量付款数据中转账失败的详细信息
        logger.info("失败详细:" + fail_details);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if (AlipayNotify.verify(params)) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            if (alipayWithdrawalService.aliNotify(batchNo, success_details, fail_details)) {
                response.getWriter().println("success");
                logger.info("::::::::::::::::::SUCCESS::::::::::::::::::");
            } else {
                response.getWriter().println("fail");
                logger.info("::::::::::::::::::FAIL 批次号异常::::::::::::::::::");
            }
            //判断是否在商户网站中已经做过了这次通知返回的处理
            //如果没有做过处理，那么执行商户的业务程序
            //如果有做过处理，那么不执行商户的业务程序

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            //////////////////////////////////////////////////////////////////////////////////////////
        } else {//验证失败
            response.getWriter().println("fail");
            logger.info("::::::::::::::::::FAIL::::::::::::::::::");
        }
        logger.info("===================支付宝批量付款回调==============");
    }
    @RequestMapping(value = "/alipay/api",method = RequestMethod.GET)
    private String  alipayApi(HttpServletRequest request,ModelMap modelMap) throws Exception {
        String wid = request.getParameter("wid");
        Map parmasMap = new HashMap();
        parmasMap.put("wid", wid);
        //建立请求
        String sHtmlText =alipayWithdrawalService.buildForm(parmasMap);
        System.out.print(sHtmlText);
        modelMap.put("aliHtml", sHtmlText);
        return "welfare/withdrawal/alipayApi";
    }
    /**
     * 微信提现
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(value ="/tencentPay/api",method = RequestMethod.POST)
    private String  tencentApi(HttpServletRequest request,ModelMap modelMap) throws Exception {
        String price = request.getParameter("price");
        String withdrawalsNo = request.getParameter("withdrawalsNo");

        Map parmasMap = new HashMap();
        parmasMap.put("price", price);
        parmasMap.put("withdrawalsNo", withdrawalsNo);

        String  result=tencentWithdrawalsService.tencentWithdrawalsApi(parmasMap);
        modelMap.put("result",result);
        return "order/userremiburse/tencentpayApi";
    }
}
